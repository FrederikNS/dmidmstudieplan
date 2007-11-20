/**
 * 
 */
package databaseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.MatchResult;

import dataClass.Course;
import dataClass.CourseDoesNotExistException;
import dataClass.CritalCourseDataMissingException;

/**
 * @author Niels Thykier
 *
 */
public class DatabaseReader implements DatabaseHandler {
	
	File database[] = new File[3];
	Scanner scan[] = new Scanner[3];
	
	public DatabaseReader() throws FileNotFoundException {
		database[DatabaseFiles.KRAV.ordinal()]  = openFile(DatabaseFiles.KRAV.toString());
		database[DatabaseFiles.SKEMA.ordinal()] = openFile(DatabaseFiles.SKEMA.toString());
		database[DatabaseFiles.NAVN.ordinal()]  = openFile(DatabaseFiles.NAVN.toString());
	}
	
	private File openFile(String filename) throws FileNotFoundException {
		return new File(filename);
	}
	
	private void openFileScan() {
		try {
		scan[DatabaseFiles.KRAV.ordinal()] = new Scanner(database[DatabaseFiles.KRAV.ordinal()]);
		scan[DatabaseFiles.SKEMA.ordinal()] = new Scanner(database[DatabaseFiles.SKEMA.ordinal()]);
			scan[DatabaseFiles.NAVN.ordinal()] = new Scanner(database[DatabaseFiles.NAVN.ordinal()]);
		} catch (FileNotFoundException e) {
		}
	}
	
    /**
     * Looks up a course using a Course ID and returns it with all the related data about the course.
     * @param courseID The ID to look up.
     * @return the Course (and related data).
     * @throws CourseDoesNotExistException If the course does not exist
     * @throws CritalCourseDataMissingException 
     */
    public Course findCourse(String courseID) throws CourseDoesNotExistException, CritalCourseDataMissingException {
        openFileScan();
        try {
        	while(!scan[DatabaseFiles.NAVN.ordinal()].hasNext(courseID)) {
        		scan[DatabaseFiles.NAVN.ordinal()].nextLine();
        	}
        } catch(NoSuchElementException e) {
        	throw new CourseDoesNotExistException(courseID);
        }

        
        String courseName, dependencies[], skema[], year = "";
        
        scan[DatabaseFiles.NAVN.ordinal()].useDelimiter("\n");
        scan[DatabaseFiles.NAVN.ordinal()].next("\\d{5} (.*)");
        courseName = scan[DatabaseFiles.NAVN.ordinal()].match().group(1);
        
        try {
        	while(!scan[DatabaseFiles.KRAV.ordinal()].hasNext(courseID)) {
        		scan[DatabaseFiles.KRAV.ordinal()].nextLine();
        	}
        	
        	scan[DatabaseFiles.KRAV.ordinal()].useDelimiter("\n");
        	scan[DatabaseFiles.KRAV.ordinal()].next("\\d{5} (.*)");
            String depends = scan[DatabaseFiles.KRAV.ordinal()].match().group(1);
            dependencies = depends.split(" ");
        } catch(NoSuchElementException e) {
            dependencies = new String[1];
            dependencies[0] = null;       
        }
        
        try {
        	while(!scan[DatabaseFiles.SKEMA.ordinal()].hasNext(courseID+"(-.)?")) {
        		scan[DatabaseFiles.SKEMA.ordinal()].nextLine();
        	}

        	scan[DatabaseFiles.SKEMA.ordinal()].useDelimiter("\n");
        	scan[DatabaseFiles.SKEMA.ordinal()].next("\\d{5}(-\\w)?(.*)");
            MatchResult result = scan[DatabaseFiles.SKEMA.ordinal()].match();
            year = result.group(1);
            if(year == null) year = "b";
            skema = result.group(2).trim().split(" ");
            
        } catch(NoSuchElementException e) {
        	throw new CritalCourseDataMissingException("Skema");
        }
        
        Course course = new Course(courseID);
        course.setCourseName(courseName);
        if(dependencies[0] != null) 
            course.setDependencies(dependencies);
        if(skema[0] != null)
        	course.setSkemagruppe(skema);
        course.setYear(year);
        return course;
    } 
		
}
