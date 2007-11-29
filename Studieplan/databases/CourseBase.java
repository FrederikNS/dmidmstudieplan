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
 * CourseBase contains all valid read from databases.
 * It uses the DatabaseReader to read the course from the databases.
 * @author Niels Thykier
 */
public class CourseBase {
	/**
	 * An ArrayList containing all the courses from the databases 
	 */
	private ArrayList<Course> allCourses;
	/**
	 * DatabaseReader, used whenever it is reloading all the courses or searching in the 
	 * databases.
	 */
	private DatabaseReader dbRead;
	/**
	 * Statistic data: amount of courses loaded during the last reload.
	 */
	private int amountOfCourses;
	/**
	 * Statistic data: time (in milliseconds) it took to load the courses doing the last reload.
	 */
	private long loadTime;
	
	/**
	 * Constructs a new CourseBase that contains all valid courses.
	 * @throws FileNotFoundException if the database files could not be found.
	 * @throws FilePermissionException if the files could not be read due to missing permissions
	 */
	public CourseBase() throws FileNotFoundException, FilePermissionException {
		
		dbRead = new DatabaseReader();
		
		reloadDatabase();
	}
	
	
	/**
	 * Used to (re-)load all the courses from the databases.
	 * The previous list of the Courses are voided, so only the courses in the databases
	 * at the time of reloading will appear in the CourseBase, when it is done.
	 */
	public void reloadDatabase() {
		allCourses = new ArrayList<Course>();
		int i = 0;
		System.out.print("Loading courses from files...");
		Iterator<Course> ilt = dbRead.iterator();
		long start = System.currentTimeMillis();
		while(ilt.hasNext()) {
			allCourses.add(ilt.next());
			i++;
		}
		loadTime = System.currentTimeMillis() - start;
		amountOfCourses = i;
		System.out.println("Done");
		System.out.println("Loaded " + amountOfCourses +" courses in " + loadTime + " milliseconds");
		System.out.println("Average: " + getLoadAverage() + " courses per second");
		
	}
	
	/**
	 * Get the amount of courses loaded (statistical data) 
	 * @return The amount of courses that was loaded during the last reload.
	 */
	public int getAmountOfCourses() {
		return amountOfCourses;
	}
	
	/**
	 * Get the load average for Courses per second (statistical data)
	 * @return The load average meassured in courses per second. 
	 */
	public long getLoadAverage() {
		return (amountOfCourses*1000)/loadTime;
	}
	
	/**
	 * Get the time it took to load the courses in the last load/reload. (statistical data) 
	 * @return the time in milliseconds it took to do the last load/reload.
	 */
	public long getLoadTime() {
		return loadTime;
	}
	
	/**
	 * Look up a Course in the CourseBase by its ID.
	 * @param courseID The ID of the course to find.
	 * @return The Course with that given ID.
	 * @throws CourseDoesNotExistException Thrown if the course does not exist.
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
	 * Search through the database files for a course containing a pattern.
	 * @param pattern The pattern to search for. 
	 * @return The Courses that match the pattern in some way.
	 * @throws CourseDoesNotExistException
	 * @deprecated It is slow and does not search through the courses in the CourseBase, 
	 * but sends it on to the DatabaseReader's search.
	 */
	public Course[] search(String pattern) throws CourseDoesNotExistException {
		return dbRead.search(pattern);
	}
	
	/**
	 * Get an array of all the courses in the CourseBase.
	 * @return All the courses in the CourseBase as a Array.
	 */
	public Course[] getAllCourses() {
		return allCourses.toArray(new Course[1]);
	}
	
	/**
	 * Prints a list of all the Courses in the CourseBase.
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
