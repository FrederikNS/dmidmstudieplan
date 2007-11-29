/**
 * 
 */
package databases;

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
import exceptions.FilePermissionException;

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
	private int lineNumber[] = {0,0,0};

	/**
	 * Opens the three database files for reading.
	 * @throws FileNotFoundException If one of the database files are missing.
	 * @throws FilePermissionException if permissions is missing to read the file
	 */
	public DatabaseReader() throws FileNotFoundException, FilePermissionException {
		database[DatabaseFiles.KRAV.ordinal()]  = openFile(DatabaseFiles.KRAV.toString());
		database[DatabaseFiles.SKEMA.ordinal()] = openFile(DatabaseFiles.SKEMA.toString());
		database[DatabaseFiles.NAVN.ordinal()]  = openFile(DatabaseFiles.NAVN.toString());
		openFileScan(DatabaseFiles.KRAV);
		openFileScan(DatabaseFiles.SKEMA);
		openFileScan(DatabaseFiles.NAVN);
	}

	/**
	 * This is called by the constructor to open the database files.
	 * @param filename The name of the file to open.
	 * @return The file opened.
	 * @throws FileNotFoundException if the file is not found.
	 * @throws FilePermissionException if permissions is missing to read the file
	 */
	private File openFile(String filename) throws FileNotFoundException, FilePermissionException {
		File f = new File(filename);
		if(!f.exists() )
			throw new FileNotFoundException(f.getAbsolutePath());
		if(!f.canRead() )
			throw new FilePermissionException("read");
		return f;
	}

	/**
	 * Initialize the Scanner-objects, so we can parse the databases.
	 * Used by any method that parses files. (e.g. findCourse )
	 */
	private void openFileScan(DatabaseFiles db) {
		try {
			scan[db.ordinal()] = new Scanner(database[db.ordinal()]);
			scan[db.ordinal()].useDelimiter("\n");
			lineNumber[db.ordinal()] = 0;
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

		String courseName;
		final int id = DatabaseFiles.NAVN.ordinal();
		final int runStart = lineNumber[id]; 
		do {

			if(null != scan[id].findInLine(courseID + " (.*)") ){
				courseName = scan[id].match().group(1).trim();
				//All the (mandantory) data was successfully retrieved
				//Create a Course object and fill it up with data
				Course course = new Course(courseID);
				course.setCourseName(courseName);

				course = findCourseSkema(course);
				getNextLine(DatabaseFiles.NAVN);
				return findCourseDependencies(course);			
			}
			getNextLine(DatabaseFiles.NAVN);
		} while(lineNumber[id] != runStart);
		
		throw new CourseDoesNotExistException(courseID);
	}

	private void getNextLine(DatabaseFiles db) {
		
		if(scan[db.ordinal()].hasNextLine()) {
			lineNumber[db.ordinal()]++;
			try {
				scan[db.ordinal()].nextLine();} 
			catch(NoSuchElementException e) {
			}
		} else {
			openFileScan(db);
			lineNumber[db.ordinal()]=0;
		}
	}

	private Course findCourseSkema(Course course) throws CritalCourseDataMissingException {
		String courseID = course.getCourseID();
		String[] period, skema;
		final int id = DatabaseFiles.SKEMA.ordinal();
		final int runStart = lineNumber[id];


		//look through the Skema Database - an entry here is mandantory!
		//Note that there has been added an optional "dash single character" to the course ID
		// The reason is the Skema database is structed slightly different than the rest and
		// for this pattern to properly read every line, we have to include it.
		/*In this database, the course number is optionally followed by a -f or -e
        	 ddddd  SSS( SSS)*
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
		do {

			if(null != scan[id].findInLine(courseID+"  (( [EF][12345][AB])*)(( (januar|juni)){0,2})") ){
				MatchResult result = scan[id].match();
				skema = result.group(1).trim().split(" ");
				period = result.group(3).trim().split(" ");
				course.setSkemagruppe(skema, period);
				getNextLine(DatabaseFiles.SKEMA);
				return course;
			}
			getNextLine(DatabaseFiles.SKEMA);
		} while(lineNumber[id] != runStart);

		throw new CritalCourseDataMissingException("Skema");        
	}

	private Course findCourseDependencies(Course course) {
		String courseID = course.getCourseID();
		String dependencies[];
		final int id = DatabaseFiles.KRAV.ordinal();
		final int runStart = lineNumber[id];
		do {

			if(null != scan[id].findInLine("^" + courseID + "( (\\d{5})+)") ) {
				/*This time we search for the dependency courses and this database is formatted like this:
	             ddddd rrrrr( rrrrr)* 
				 * Where ddddd is the five digits in the course we wish to look up and rrrrr is(/are) the 
				 * course number of the required course(s). A course appearing in this database 
				 * (as the first part of an entry) must have at least one dependency course. 
				 */ 
				String depends = scan[id].match().group(1);
				//Since the regular expression simply fetches everything but the original course number
				// and the first whitespace after, the match will contain the dependency courses in a 
				// String, where they are delimited by a single whitespace. Hench we split it, using " " 
				// as delimiter to get it in an array.
				dependencies = depends.split(" ");
				getNextLine(DatabaseFiles.KRAV);
				course.setDependencies(dependencies);

				return course;
			}
			getNextLine(DatabaseFiles.KRAV);
		} while(lineNumber[id] != runStart);
		
		return course;
	} 

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Course> iterator() {
		return this.new Iter();
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
		/*openFileScan(DatabaseFiles.KRAV);
		openFileScan(DatabaseFiles.SKEMA);
		openFileScan(DatabaseFiles.NAVN);*/
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
		Course[] list = match.toArray(new Course[1]);
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
	public class Iter implements Iterator<Course> {
		Scanner s;
		boolean cached = false;
		Course courseCache;

		public Iter() {
			try {
				s = new Scanner(database[DatabaseFiles.NAVN.ordinal()]);
				s.useDelimiter("\n");
			} catch(FileNotFoundException e){

			}

		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {
			//We already got an unread result cached. That is still the next entry. 
			if(cached) return true;
			boolean next = false;
			while(null != s.findInLine("(\\d{5}) ((.*))")  ) {
				try {
					//Cache all the possible "nexts".
					//Allows us to catch the exceptions and garantuee 
					// that the getNextCourse* methods do not throw them.
					MatchResult match = s.match();
					Course course = new Course(match.group(1));
					course.setCourseName(match.group(2));
					course = findCourseSkema(course);
					courseCache = findCourseDependencies(course);
					cached = true;
					next = true;
					break;
				} catch (CritalCourseDataMissingException e) {
				} 
				if(!s.hasNextLine())
					break;
				s.nextLine();	    
			}
			// if no next exists, clean up (just in case)
			if(!next) {
				cached = false;
				courseCache = null;
			}
			return next;
		}

		public Course next() {
			Course toReturn;
			if(hasNext()) {
				toReturn = courseCache;
				courseCache = null;
				cached = false;
				try {
					s.nextLine();
				} catch(NoSuchElementException e) {
				}
			} else {
				throw new NoSuchElementException(); 
			}
			return toReturn;
		}

		public void remove() {
			//Not supported... Follow the standard
			throw new UnsupportedOperationException();
		}

	}

}
