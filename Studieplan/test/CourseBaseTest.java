package test;

import dataClass.Course;
import databases.CourseBase;
import exceptions.CourseDoesNotExistException;
import junit.framework.TestCase;

/**
 * This test class runs a series of tests on the class CourseBase
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class CourseBaseTest extends TestCase {

	/**
	 * Sets up a class variable
	 */
	private CourseBase base;
	
	/**
	 * Initialises the CourseBase
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		base = new CourseBase();
	}
	
	/**
	 * Tests to see how many courses the database can find
	 */
	public void testAmountOfCourses(){
		//More than 100, just to set something
		assertTrue(base.getAmountOfCourses()>100);
	}
	
	/**
	 * Tests if it can find a specific course
	 */
	public void testFindCourse() {
		try {
			assertNotNull(base.findCourse("01005"));
		} catch (CourseDoesNotExistException e) {
			fail("Course Not Found");
		}
	}
	
	/**
	 * Tests if it can find any course containing the specified string
	 */
	public void testSearch() {
		Course[] match;
		try {
			match = base.search("mat");
		} catch (CourseDoesNotExistException e) {
			fail("Nothing Found");
			return;
		}
		assertTrue(match.length > 0);
	}
	
	/**
	 * Tests if it can find all courses
	 */
	public void testGetAllCourses() {
		assertTrue(base.getAllCourses().length==base.getAmountOfCourses());
	}
	
	/**
	 * Tests if it can do toString()
	 */
	public void testToString() {
		assertNotNull(base.toString());
	}
	
	/**
	 * Test that the getAllCourses does not allow methods outside CourseBase to alter in the 
	 * list inside CourseBase. 
	 */
	public void testOverwriteProtection() {
		Course[] temp = base.getAllCourses();		
		temp[0] = null;
		assertNotNull(base.getAllCourses()[0]);
	}
	
	/**
	 * Tears everything down, including resetting the class variable
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		base = null;
	}

}
