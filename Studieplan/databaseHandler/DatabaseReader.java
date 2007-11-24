/**
 * 
 */
package databaseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.MatchResult;

import dataClass.Course;
import exceptions.CourseDoesNotExistException;
import exceptions.CritalCourseDataMissingException;

/**
 * @author Niels Thykier
 *
 */
public class DatabaseReader implements DatabaseHandler, Iterable<Course> {
	
	/**
	 * Files objects, the databases loaded into memory.
	 */
	private File database[] = new File[3];
	/**
	 * Scanner objects, used to parse the database files
	 */
	private Scanner scan[] = new Scanner[3];
	
	/**
	 * Opens the three database files for reading.
	 * @throws FileNotFoundException If one of the database files are missing.
	 */
	public DatabaseReader() throws FileNotFoundException {
		database[DatabaseFiles.KRAV.ordinal()]  = openFile(DatabaseFiles.KRAV.toString());
		database[DatabaseFiles.SKEMA.ordinal()] = openFile(DatabaseFiles.SKEMA.toString());
		database[DatabaseFiles.NAVN.ordinal()]  = openFile(DatabaseFiles.NAVN.toString());
	}
	
	/**
	 * This is called by the constructor to open the database files.
	 * @param filename The name of the file to open.
	 * @return The file opened.
	 * @throws FileNotFoundException if the file is not found.
	 */
	private File openFile(String filename) throws FileNotFoundException {
		return new File(filename);
	}
	
	/**
	 * Initialize the Scanner-objects, so we can parse the databases.
	 * Used by any method that parses files. (e.g. findCourse )
	 */
	private void openFileScan() {
		try {
		scan[DatabaseFiles.KRAV.ordinal()] = new Scanner(database[DatabaseFiles.KRAV.ordinal()]);
		scan[DatabaseFiles.SKEMA.ordinal()] = new Scanner(database[DatabaseFiles.SKEMA.ordinal()]);
		scan[DatabaseFiles.NAVN.ordinal()] = new Scanner(database[DatabaseFiles.NAVN.ordinal()]);
        scan[DatabaseFiles.NAVN.ordinal()].useDelimiter("\n");
        scan[DatabaseFiles.KRAV.ordinal()].useDelimiter("\n");
        scan[DatabaseFiles.SKEMA.ordinal()].useDelimiter("\n");
		} catch (FileNotFoundException e) {
		}
	}
    /**
     * Looks up a course using a Course ID and returns it with all the related data about the course.
     * @param courseID The ID to look up.
     * @return the Course (and related data).
     * @throws CourseDoesNotExistException If the course does not exist
     * @throws CritalCourseDataMissingException If a critial part of the course data is missing (e.g. the "skema" data is missing)
     */
    public Course findCourse(String courseID) throws CourseDoesNotExistException, CritalCourseDataMissingException {
    	
    	//load the parses.
        openFileScan();
        
        try {
        	//parse through the Name database and see if the courseID appears.
        	//if it does not appear, the scanner will run out of lines and throw the
        	// NoSuchElementException.
        	//
        	//else if it exists, the condition for the while loop will be false and it will end
        	// before we run out of lines.
        	while(!scan[DatabaseFiles.NAVN.ordinal()].hasNext(courseID + ".*") ) {
        		scan[DatabaseFiles.NAVN.ordinal()].nextLine();
        	}
        } catch(NoSuchElementException e) {
        	throw new CourseDoesNotExistException(courseID);
        }

        
        String courseName, dependencies[], skema[], season = "";
            
        /*The data in the Name database is formatted like this.
         ddddd c*
         * where d is a (decimal) digit and c* is one or more characters (incl. whitespace).
         * Now, due to the while loop above, we are now on the exact line, where the course entry is,
         *  so we do not have to check that the courseID match again.
         * We now use a "regular expression" to sort out the data we want, which is (in this case)
         * the name of the course.
         *
       	 * The regular expression "\d{5} (.*)" (note that Java requires the back slash to be escaped, 
       	 * which explains the \\ rather than \ in the code below) searches for exactly 5 (decimal) 
       	 * digits, a white-space and then any number of characters, symbols and whitespace. The brackets
       	 * specify that we are interested in whatever matches the pattern inside them, so we are 
       	 * interested in that last bundle of characters, symbols and whitespaces after exactly 5 digits
       	 * and one whitespace.
       	 * 
       	 * scan[enum.NAVN.ordinal()].match.group(1) returns the match of that pattern we specified as 
       	 * "interesting". (It returns the first of these patterns, but in this case there is only one.)
         */
        scan[DatabaseFiles.NAVN.ordinal()].next("\\d{5} (.*)");
        courseName = scan[DatabaseFiles.NAVN.ordinal()].match().group(1);
        
    	try {
        	//look through the Skema Database - an entry here is mandantory!
        	//Note that there has been added an optional "dash single character" to the course ID
        	// The reason is the Skema database is structed slightly different than the rest and
        	// for this pattern to properly read every line, we have to include it.
        	// the formatting is explained in greater detail below.
        	while(!scan[DatabaseFiles.SKEMA.ordinal()].hasNext(courseID+"(-\\w)?.*")) {
        		scan[DatabaseFiles.SKEMA.ordinal()].nextLine();
        	}
        	/*In this database, the course number is optionally followed by a -f or -e
        	 ddddd(-c)? SSS( SSS)*
        	 * Where ddddd are the digits in the course ID, (-c)? means the optional occurance of
        	 * a -f or a -e followed by whitespace and then the three lettered skema data. This
        	 * optional parameter character determines, which part of the year this course is 
        	 * available.
        	 * 
        	 * Note: if the optional -c part is left out, it may or may not be replaced by whitespace 
        	 * and we assume it to mean "both the winter and the summer season" in such cases, specified
        	 * with a "-b" or simply "b"
        	 * 
        	 * To handle this we have (once again) found a suitable pattern. Asside from the first
        	 * five digits (which we already know what are), we specify that we are interested in
        	 * in a dash followed by a single non-whitespace character. We declare (with the ?-mark) 
        	 * that it is optional. Anything (including whitespaces) beyond that is bundled up in the
        	 * (.*) pattern.
        	 * 
        	 * Due to the structure of this database file, we know that anything beyond the five first 
        	 * digits and the optional season specifier, will be whitespace delimited "Skema" data, so
        	 * if trimmed for trailing and (more importantly) leading whitespaces and then split with 
        	 * the " " delimited, it will now be an array of "Skema" details. 
        	 */
        	scan[DatabaseFiles.SKEMA.ordinal()].next("\\d{5}(-\\w)?(.*)");
            MatchResult result = scan[DatabaseFiles.SKEMA.ordinal()].match();
            season = result.group(1);
            // if the optional season specifier is missing, it will be assumed to "both" or "b"
            if(season == null || season.equals("") ) season = "-b";
            skema = result.group(2).trim().split(" ");
            
        } catch(NoSuchElementException e) {
        	//Mandantory entry in this database - Critial data missing
        	//The course is either invalid or either the Name or the Skema Database is corrupted.
        	// Can't fix that.
        	throw new CritalCourseDataMissingException("Skema");
        }
        
        
        try {
        	//scan through the Dependency/Demand database and see if an entry appears.
        	//an entry in this database is not mandantory.
        	while(!scan[DatabaseFiles.KRAV.ordinal()].hasNext(courseID + "(.*)+") ) {
        		scan[DatabaseFiles.KRAV.ordinal()].nextLine();
        	}
        	
        	/*This time we search for the dependency courses and this database is formatted like this:
             ddddd rrrrr( rrrrr)* 
        	 * Where ddddd is the five digits in the course we wish to look up and rrrrr is(/are) the 
        	 * course number of the required course(s). A course appearing in this database 
        	 * (as the first part of an entry) must have at least one dependency course. 
        	 */ 
        	//Possible re-write with for loop and .group
        	scan[DatabaseFiles.KRAV.ordinal()].next("\\d{5} (\\d{5})+");
            String depends = scan[DatabaseFiles.KRAV.ordinal()].match().group(1);
            //Since the regular expression simply fetches everything but the original course number
            // and the first whitespace after, the match will contain the dependency courses in a 
            // String, where they are delimited by a single whitespace. Hench we split it, using " " 
            // as delimiter to get it in an array.
            dependencies = depends.split(" ");
        } catch(NoSuchElementException e) {
        	//no entry - but not mandantory either.
            dependencies = new String[1];
            dependencies[0] = null;       
        }
        
        //All the (mandantory) data was successfully retrieved
        //Create a Course object and fill it up with data
        Course course = new Course(courseID);
        course.setCourseName(courseName);
        
        //dependencies are optional, so we check if there are any dependencies before adding them.
        if(dependencies[0] != null) 
            course.setDependencies(dependencies);
        
        course.setSkemagruppe(skema);
        course.setSeason(season);
        return course;
    } 
    
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Course> iterator() {
		return new DatabaseReaderIterator(this.new Iter());
	}

	/**
	 * Search through the Course data files for a pattern.
	 * @param pattern searches for a pattern.
	 * @return returns an array of courses which (somehow) matches this pattern.
	 */
	public Course[] search(String pattern) throws CourseDoesNotExistException {
		ArrayList<Course> match = new ArrayList<Course>();
		Course course;
    	//load the parses.
        openFileScan();
        for(int i = 0 ; i < scan.length ; i++ ) {
	        try {
	        	for ( ; ; ) {
	        		if(scan[i].hasNext(".*" + pattern + ".*") ) {
	        			scan[i].next("(\\d{5}).*");
	        			try {
							course = findCourse(scan[i].match().group(1));
							match.add(course);
						} catch (Exception e) {}
	        		}
	        		scan[i].nextLine();
	        	}
	        }catch(NoSuchElementException e) {
	        }
        }
		Course[] list = (Course[]) match.toArray();
		if(list.length < 1) {
			throw new CourseDoesNotExistException("containing " + pattern);
		}
		return  list;
	}
	
	
    /**
     * @author Niels Thykier
     * This inner class handles all interaction with the DatabaseReaderIterator.
     * Care should be used when using this class directly. It does not follow the 
     * Iterator standards (nor does it implements said interface).
     * 
     * If you need an Iterator for the Database readers, see the 
     * DatabaseReaderIterator class or the Iterator<Course> iterator() method of DatabaseReader.
     */
    public class Iter {
    	Scanner s;
    	boolean cached = false;
    	String idCache;
    	Course courseCache;
    	
    	private Iter() {
    		try {
    		s = new Scanner(database[DatabaseFiles.NAVN.ordinal()]);
    		s.useDelimiter("\n");
    		} catch(FileNotFoundException e){
    			//since the file is already loaded into memory, it cannot be missing
    		}

    	}
    	
    	/**
    	 * Scans the database and checks for the next valid entry.
    	 * 
    	 * Note: this method caches the CourseID and the Course. 
    	 * @return true if another valid Course could be found.
    	 */
    	public boolean hasNext() {
    		//We already got an unread result cached. That is still the next entry. 
    		if(cached) return true;

    		boolean next = false;
    		
    		while(s.hasNext("(\\d{5}).*")) {
	        	try {
	        		//Cache all the possible "nexts".
	        		//Allows us to catch the exceptions and garantuee 
	        		// that the getNextCourse* methods do not throw them.
	    			s.next("(\\d{5}).*");
	    			//TODO test that this reg.ex. works properly
	    			idCache = s.match().group(1); 
	        		courseCache = findCourse(idCache);
	        		cached = true;
	        		next = true;
	        		break;
	        	} catch(Exception e) {
	        	}
	        	
	        }
    		// if no next exists, clean up (just in case)
    		if(!next) {
    			cached = false;
    			courseCache = null;
    			idCache = null;
    		}
    		return next;
    	}
    	
    	/**
    	 * Fetches the next course ID in the database. 
    	 * @return the course ID of the next entry.
    	 * @throws NoSuchElementException if there is no more entries could be found
    	 */
    	public String getNextCourseID() {
    		String toReturn;
    		if(hasNext()) {
    			toReturn = idCache;
    			courseCache = null;
    			idCache = null;
    			cached = false;
    		} else {
    			throw new NoSuchElementException();
    		}
    		return toReturn;
    	}

    	/**
    	 * Fetches the next course in the database. 
    	 * @return the course of the next entry.
    	 * @throws NoSuchElementException if there is no more entries could be found
    	 */
    	public Course getNextCourse() {
    		Course toReturn;
    		if(hasNext()) {
    			toReturn = courseCache;
    			courseCache = null;
    			idCache = null;
    			cached = false;
    		} else {
    			throw new NoSuchElementException(); 
    		}
			return toReturn;
    	}

    }
    
}
