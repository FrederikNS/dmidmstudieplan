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
	 * A positive test of finding a course
	 */
	public void testFindCoursePositive() {
		Course course = null;
		try {
			course = reader.findCourse("01005");
		} catch (Exception e) {
			fail(e.toString());
		}
		boolean navn = course.getCourseName().equals("Matematik 1");
		String[] dtuSkema={"E5A","E5B","E3B","F5A","F5B","F3B"};
		String[] length=null;
		int i = Course.parseDTUSkema(dtuSkema,length);
		boolean skema = course.getFullSkemaData()==i;
		boolean deps = course.hasDependencies() == false;
		//If it finds the correct name, schema groups and dependencies for the selected course
		//the test will be correct
		assertTrue(navn && skema && deps);
	}
	
	/**
	 * A Negative test of finding a course
	 */
	public void testFindCourseNegative() {
		try {
			reader.findCourse("0100051");
			fail("Fail Expected");
		} catch (Exception e) {
		}
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