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
	
	
	public void reloadDatabase() {
		allCourses = new ArrayList<Course>();
	
		Iterator<Course> ilt = dbRead.iterator();
		
		while(ilt.hasNext())
			allCourses.add(ilt.next());
	}
	
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
	
	public Course[] search(String pattern) throws CourseDoesNotExistException {
		return dbRead.search(pattern);
	}
	
	public Course[] getAllCourses() {
		return allCourses.toArray(new Course[1]);
	}
	
	public String toString() {
		
		String toReturn = "Kursus list: \n";
		Iterator<Course> ilt = allCourses.iterator();
		while(ilt.hasNext()) {
			toReturn += ilt.next().toString() + "\n"; 
		}
		
		return toReturn;
	}
	
}
