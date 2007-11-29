package test;

import dataClass.Course;
import dataClass.SelectedCourse;
import dataClass.StudyPlan;
import exceptions.ConflictingCourseInStudyPlanException;
import exceptions.CourseAlreadyExistsException;
import exceptions.CourseDoesNotExistException;
import junit.framework.TestCase;

/**
 * This test class tests the class StudyPlan by usint jUnit
 * @author Morten Sørensen
 */
public class StudyPlanTest extends TestCase {
	
	/**
	 * Sets up an class variable
	 */
	StudyPlan sp;

	/**
	 * Sets up a study plan the test will use
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception{
		super.setUp();
		sp = new StudyPlan("testSubject3");
	}

	/**
	 * A positive test of comparing two objects with each other
	 */
	public void testEqualsObjectPositive() {
		Object obj = new StudyPlan("testSubject3");
		assertTrue(sp.equals(obj));
	}
	
	/**
	 * A negative of comparing to objects
	 */
	public void testEqualsObjectNegative() {
		assertFalse(sp.equals("testFail") || sp.equals(""));
	}

	/**
	 * A positive test of adding a course to the study plan
	 */
	public void testAddPositive() {
		//Sets up a course we want to add
		SelectedCourse sc;
		sc = new SelectedCourse("01005", " ", 1);
		try {
			sp.add(sc);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	/**
	 * A negative test of adding a course
	 */
	public void testAddNegative() {
		//Sets up test data
		testAddPositive();
		SelectedCourse sc;
		sc = new SelectedCourse("01005", " ", 1);
		try {
			//Adds a course that already exists in the study plan
			sp.add(sc);
			fail("Should conflict with the already added course");
		} catch (CourseAlreadyExistsException e) {
			
		} catch (ConflictingCourseInStudyPlanException e) {
			fail(e.toString());
		}
		SelectedCourse sc2;
		sc2 = new SelectedCourse("01715", " ", 1);
		try {
			//Adds a course where there is an comflict in schema group
			sp.add(sc2);
			fail("course conflict expected");
		} catch (ConflictingCourseInStudyPlanException e) {
			
		} catch (CourseAlreadyExistsException e) {
			//Should not happen, the course should not alreay exist in the study plan
			fail(e.toString());
		} 
	}

	/**
	 * A positive test to see if a study plan contains a specific string
	 */
	public void testContainsStringPositive() {
		try {
			sp.add(new SelectedCourse("01005", " ", 1));
		} catch (Exception e) {
			fail(e.toString());
		}
		assertTrue(sp.contains("01005"));
	}

	/**
	 * A negative test to see if a study plan contains a a specific sting
	 */
	public void testContainsStringNegative() {
		assertFalse(sp.contains("01005"));
	}

	/**
	 * A positive test to see if a study plan contains a specific course
	 */
	public void testContainsCoursePositive() {
		SelectedCourse sc;
		try {
			sc = new SelectedCourse("01005", " ", 1);
			sp.add(sc);
		} catch (Exception e) {
			fail(e.toString());
			sc = null;
		}
		boolean test1 = sp.contains(sc);
		boolean test2 = sp.contains(new Course("01005", " "));
		assertTrue(test1 && test2);
	}

	/**
	 * A negative test to see if a study plan contains a specific course
	 */
	public void testContainsCourseNegative() {
		assertFalse(sp.contains("01005"));
	}

	/**
	 * A positive test of removing a specific string from a study plan 
	 */
	public void testRemoveStringPositive() {
		//Sets up test data
		testAddPositive();
		try {
			assertTrue(sp.remove("01005"));
		} catch (CourseDoesNotExistException e) {
			fail(e.toString());
		}
	}

	/**
	 * A negative test of removing a specific string from a study plan
	 */
	public void testRemoveStringNegative() {
		try {
			sp.remove("01005");
			fail("course ought not to exist");
		} catch (CourseDoesNotExistException e) {
			
		}
		try {
			sp.remove("");
			fail("no string given");
		} catch (CourseDoesNotExistException e) {
			
		} catch (IllegalArgumentException e) {
			
		}
	}

	/**
	 * A positive test of removing a specific course from a study plan
	 */
	public void testRemoveCoursePositive() {
		//Sets up test data
		testAddPositive();
		try {
			assertTrue(sp.remove(new Course("01005", " ")));
		} catch (CourseDoesNotExistException e) {
			fail(e.toString());
		}
	}

	/**
	 * A negative test of removing a specific course from a study plan
	 */
	public void testRemoveCourseNegative() {
		try {
			sp.remove(new Course("01005", " "));
			fail("course ought not to exist");
		} catch (CourseDoesNotExistException e) {
			
		}
		try {
			sp.remove(new Course("", " "));
			fail("no name");
		} catch (CourseDoesNotExistException e) {
			
		} catch (IllegalArgumentException e) {
			
		}
	}

	/**
	 * A positive test of printing study plan for a semester
	 */
	public void testPrintSemesterPositive() {
		//Sets up test data
		testAddPositive();
		try {
			sp.printSemester(1);
		} catch (IllegalArgumentException e) {
			fail(e.toString());
		}
	}

	/**
	 * A negative test of printing study plan for a semester
	 */
	public void testPrintSemesterNegative() {
		try {
			//Tries to print study plan from a non-existing semester
			sp.printSemester(21);
			fail("Exception expected");
		} catch (IllegalArgumentException e) {
			
		}
	}

	/**
	 * Tears everything the test class has created, including resetting the class variable
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		sp = null;
	}
}
