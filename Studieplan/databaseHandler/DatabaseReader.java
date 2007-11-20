/**
 * 
 */
package databaseHandler;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.regex.MatchResult;

import dataClass.Course;
import dataClass.CourseDoesNotExistException;

/**
 * @author Niels Thykier
 *
 */
public class DatabaseReader implements DatabaseHandler {
	
	RandomAccessFile database[] = new RandomAccessFile[3];
	Scanner scan[] = new Scanner[3];
	
	public DatabaseReader() throws FileNotFoundException {
		database[DatabaseFiles.KRAV.ordinal()]  = openFile(DatabaseFiles.KRAV.toString());
		database[DatabaseFiles.SKEMA.ordinal()] = openFile(DatabaseFiles.SKEMA.toString());
		database[DatabaseFiles.NAVN.ordinal()]  = openFile(DatabaseFiles.NAVN.toString());
	}
	
	private RandomAccessFile openFile(String filename) throws FileNotFoundException {
		return new RandomAccessFile(filename, "r");
	}
	
	private void openFileScan() {
		scan[DatabaseFiles.KRAV.ordinal()] = new Scanner((Readable) database[DatabaseFiles.KRAV.ordinal()]);
		scan[DatabaseFiles.SKEMA.ordinal()] = new Scanner((Readable) database[DatabaseFiles.SKEMA.ordinal()]);
		scan[DatabaseFiles.NAVN.ordinal()] = new Scanner((Readable) database[DatabaseFiles.NAVN.ordinal()]);
	}
	
	/**
	 * Looks up a course using a Course ID and returns it with all the related data about the course.
	 * @param courseID The ID to look up.
	 * @return the Course (and related data).
	 * @throws CourseDoesNotExistException If the course does not exist
	 */
	public Course findCourse(String courseID) throws CourseDoesNotExistException {
		openFileScan();
		if(!scan[DatabaseFiles.NAVN.ordinal()].hasNext(courseID) ) {
			throw new CourseDoesNotExistException(courseID);
		}
		
		String courseName, dependencies[] = null;
		
		scan[DatabaseFiles.NAVN.ordinal()].next(courseID + " (.*)");
	    courseName = scan[DatabaseFiles.NAVN.ordinal()].match().group(1);
		
	    if(!scan[DatabaseFiles.KRAV.ordinal()].hasNext(courseID) ) {
	    	int Max;
	    	scan[DatabaseFiles.KRAV.ordinal()].next(courseID + " (.*)+");
	    	MatchResult result = scan[DatabaseFiles.KRAV.ordinal()].match();
	    	Max = result.groupCount();
	    	dependencies = new String[Max];
	    	for(int i=1 ; i <= Max ; i ++ ) {
	    		dependencies[i] = scan[DatabaseFiles.KRAV.ordinal()].match().group(i);
	    	}
	    }
	    
		Course course = new Course(courseID);
		course.setCourseName(courseName);
		if(dependencies != null) 
			course.setDependencies(dependencies);
		return course;
	}
		
}
