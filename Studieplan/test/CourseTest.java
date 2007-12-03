package test;

import junit.framework.TestCase;
import dataClass.Course;
import dataClass.SelectedCourse;
import databases.CourseBase;

/**
 * This test class tests the class Course
 * @author Morten Sørensen
 * @author Niels Thykier
 */
public class CourseTest extends TestCase {

	/**
	 * A class variable the test uses
	 */
	Course cc;
	
	/**
	 * Sets up some data the test needs and associates it with the class variable
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		CourseBase cb = new CourseBase();
		cc = cb.findCourse("01715");
	}

	/**
	 * Tests if it can find the correst courseID
	 */
	public void testGetCourseID() {
		assertTrue(cc.getCourseID().equals("01715"));
	}
	
	/**
	 * Tests if it can find the correct schema groups for the selected course
	 */
	public void testSkemaToString() {
		try {
			//Uses course 01005 because it got more schema groups
			CourseBase cb = new CourseBase();
			cc = new SelectedCourse(cb.findCourse("01005"), 1);
		} catch (Exception e) {
			fail(e.toString());
			return;
		}
		//Sets up a string array with the correct schema groups
		String test[] = {"E5A","E5B","E3B","F5A","F5B","F3B"};
		String skema = cc.skemaToString();
		for(int i = 0; i < test.length ; i++) {
			//Tests if it finds any schema groups that does not belong to course 01005
			if(!skema.contains(test[i])) {
				fail("did not contain " + test[i]);
			}
		}
	}

	/**
	 * Tests if it can find the correct name of the course
	 */
	public void testGetCourseName() {
		assertTrue(cc.getCourseName().equals("Funktionalanalyse"));
	}
	
	/**
	 * A positive test to see if it can find the correct dependency
	 */
	public void testGetDependenciesPositive() {
		String skema = cc.getDependencies();
		assertTrue(skema.contains("01035"));
	}

	/**
	 * A negative test of finding the correct dependency
	 */
	public void testConflictingSkemaPositive() {
		Course cc2;
		try {
			CourseBase cb = new CourseBase();
			cc2 = cb.findCourse("01005");
		} catch (Exception e) {
			fail("Course not found");
			return;
		}

		assertTrue(cc.conflictingSkema(cc2));
	}

	/**
	 * Testing if it can set new dependencies for a course
	 */
	public void testSetDependencies() {
		String depends = "01005 02101";
		cc.setDependencies(depends);
		String test = cc.getDependencies();
		//Tests to see if it actually worked
		assertTrue(test.equals(depends));
	}

	/**
	 * Testing if it can get the skema data of the course (and that is matches the expected values from the database converted into the internal data pattern)
	 */
	public void testGetFullSkemaData() {
		assertTrue( cc.getFullSkemaData() == ((Course.INTERNAL_WEDNESDAY_MORNING<<Course.INTERNAL_SHIFT_AUTUMN_LONG) | Course.INTERNAL_SEASON_AUTUMN_LONG) );
	}

	/**
	 * Testing if the setFullSkemaData overwrites the skema data in the course as expected.
	 */
	public void testSetFullSkemaDataPositive() {
		long newSkema = Course.INTERNAL_WEDNESDAY_MORNING | Course.INTERNAL_THURSDAY_MORNING;
		cc.setFullSkemaData(newSkema);
		assertTrue( cc.getFullSkemaData() == newSkema);
	}
	
	/**
	 * Testing if the setFullSkemaData obeys the "ignore 0" as it says it will.
	 */
	public void testSetFullSkemaDataNegative() {
		long currentSkema = cc.getFullSkemaData();
		cc.setFullSkemaData(0);
		assertTrue( cc.getFullSkemaData() == currentSkema);
	}

	/**
	 * Test if the setSkemagruppe method accurately parse the data required.
	 */
	public void testSetSkemagruppePositive() {
		fail("Not yet implemented");
	}

	/**
	 * Test if the setSkemagruppe method ignore mal-formatted data as it claims it will
	 */
	public void testSetSkemagruppeNegative() {
		fail("Not yet implemented");
	}
	
	/**
	 * Tests if the data it gets from the database, is the same as the class variable got specified
	 */
	public void testEqualsObject() {
		boolean test1 = cc.equals(new Course(cc.getCourseID(),cc.getCourseName()));
		boolean test2;
		try {
			test2 = cc.equals(new SelectedCourse(cc,1 ));
		}  catch (Exception e) {
			fail("Could not create course");
			return;
		}
		assertTrue(test1 && test2);
	}

	/**
	 * A positive test of comparing two courses to see if they are the same
	 */
	public void testIsSameCourseIDPositive() {
		assertTrue(cc.isSameCourseID(cc.getCourseID()));
	}
	
	/**
	 * A negative test of comparing two courses to see if they are the same
	 */
	public void testIsSameCourseIDNegative() {
		assertFalse(cc.isSameCourseID("01005") || cc.isSameCourseID("0"));
	}
	
	/**
	 * Tears down everything the test used, including resetting the class variable
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception  {
		super.tearDown();
		cc = null;
	}
}
