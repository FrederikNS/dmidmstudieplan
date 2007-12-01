package test;

import dataClass.Course;
import databases.DatabaseReader;
import junit.framework.TestCase;

/**
 * This test class performs a series of tests on the class DatabaseReader
 * @author Frederik Nordahl Sabroe
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
			reader = new DatabaseReader();
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