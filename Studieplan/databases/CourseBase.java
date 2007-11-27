/**
 * 
 */
package databases;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import dataClass.Course;
import databases.DatabaseReader;
import exceptions.CourseDoesNotExistException;
import exceptions.FilePermissionException;

/**
 * @author Niels Thykier
 *
 */
public class CourseBase {
	private ArrayList<Course> allCourses;
	private DatabaseReader dbRead;
	
	public CourseBase() throws FileNotFoundException, FilePermissionException {
		
		dbRead = new DatabaseReader();
		
		reloadDatabase();
	}
	
	
	/**
	 * This method reloads the entire database.
	 */
	public void reloadDatabase() {
		allCourses = new ArrayList<Course>();
	
		Iterator<Course> ilt = dbRead.iterator();
		
		while(ilt.hasNext())
			allCourses.add(ilt.next());
	}
	
	/**
	 * This method loops through the entire database, in order to find a specific course ID
	 * @param courseID is the ID it is searching for
	 * @return all data for the selected course
	 * @throws CourseDoesNotExistException if the courseID does not exists
	 */
	public Course findCourse(String courseID) throws CourseDoesNotExistException {
		Course course;
		Iterator<Course> ilt = allCourses.iterator();
		while(ilt.hasNext()) {
			 course = ilt.next();
			 if(course.getCourseID().equalsIgnoreCase(courseID)) {
				 return course;
			 }
		}
		throw new CourseDoesNotExistException(courseID);
	}
	
	/**
	 * This methods search the course database for a pattern
	 * @param pattern is what you are searching for
	 * @return the course that fits to the pattern
	 * @throws CourseDoesNotExistException if the pattern doesn't fit to any courses
	 */
	public Course[] search(String pattern) throws CourseDoesNotExistException {
		return dbRead.search(pattern);
	}
	
	/**
	 * This method lists every course in the database
	 * @return a list containing every course in the database
	 */
	public Course[] getAllCourses() {
		return allCourses.toArray(new Course[1]);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		String toReturn = "Kursus list: \n";
		Iterator<Course> ilt = allCourses.iterator();
		while(ilt.hasNext()) {
			toReturn += ilt.next().toString() + "\n"; 
		}
		
		return toReturn;
	}
	
}
