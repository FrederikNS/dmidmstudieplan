package test;

import junit.framework.TestCase;
import dataClass.Course;
import databases.CourseBase;
import databases.CourseBase.DatabaseReader;

/**
 * This test class performs a series of tests on the class DatabaseReader
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class DatabaseReaderTest extends TestCase{

	/**
	 * Sets up a class variable
	 */
	private DatabaseReader reader;
	

	/**
	 * Initialises the DatabaseReader
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		try{
			CourseBase cb = new CourseBase();
			reader = cb.new DatabaseReader();
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	
	/**
	 * 
	 */
	public void testLoadAllCourses() {
		Course[] course = reader.loadAllCourses().toArray(new Course[1]);
		assertTrue(course.length > 100);
	}
	
	
	/**
	 * Tears everything down the class needed for the tests, including resetting the class variable
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		reader = null;
	}

}