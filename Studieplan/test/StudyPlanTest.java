package test;

import junit.framework.TestCase;
import dataClass.Course;
import dataClass.SelectedCourse;
import dataClass.StudyPlan;
import databases.DatabaseReader;
import exceptions.ConflictingCourseInStudyPlanException;

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
		DatabaseReader db;
		SelectedCourse sc;
		try {
			db = new DatabaseReader();
			sc = new SelectedCourse(db.findCourse("01005"), 1);
		} catch (Exception e) {
			fail(e.toString());
			return;
		}
		
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
		
		DatabaseReader db;
		SelectedCourse sc1;
		try {
			db = new DatabaseReader();
			sc1 = new SelectedCourse(db.findCourse("01715"), 1);
			sp.add(sc1);
		} catch (ConflictingCourseInStudyPlanException e) {
			
		} catch (Exception e) {
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
		assertTrue(sp.remove("01005"));
	}

	/**
	 * A negative test of removing a specific string from a study plan
	 */
	public void testRemoveStringNegative() {
	
	    if(sp.remove("01005")) {
			fail("course ought not to exist");
	    }
		try {
			if(sp.remove("")) {
				fail("no string given");
			}
		} catch (IllegalArgumentException e) {
			
		}
	}

	/**
	 * A positive test of removing a specific course from a study plan
	 */
	public void testRemoveCoursePositive() {
		//Sets up test data
		testAddPositive();
		assertTrue(sp.remove(new Course("01005", " ")));
	}

	/**
	 * A negative test of removing a specific course from a study plan
	 */
	public void testRemoveCourseNegative() {

		if(sp.remove(new Course("01005", " "))) {
			fail("course ought not to exist");
		}

		try {
			if(sp.remove(new Course("", " "))) {
				fail("no name");
			}
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
