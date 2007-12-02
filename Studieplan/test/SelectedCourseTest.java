package test;

import dataClass.Course;
import dataClass.SelectedCourse;
import databases.CourseBase;
import junit.framework.TestCase;

/**
 * Tests the class SelectedCourses by usint jUnit
 * @author Morten Sørensen
 */
public class SelectedCourseTest extends TestCase {

	/**
	 * Sets up the class variable
	 */
	SelectedCourse sc;
	
	/**
	 * Sets up some data needed for the tests
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		CourseBase cb = new CourseBase();
		sc = new SelectedCourse(cb.findCourse("01715"), 2);
	}

	/**
	 * A positive test of the comparing function
	 */
	public void testCompareToPositive() {
		SelectedCourse sc2 = new SelectedCourse("01715", " ", 1);
		int test = 0;
		if(sc.compareTo(sc2) == 1) {
			test++;
		}
		sc2 = new SelectedCourse(sc2, 2);
		if(sc.compareTo(sc2) == 0){
			test++;
		}
		sc2 = new SelectedCourse(sc2, 3);
		if(sc.compareTo(sc2) == -1){
			test++;
		}
		sc2 = new SelectedCourse("01005", " ", 1);
		if(sc.compareTo(sc2) == 1) {
			test++;
		}
		sc2 = new SelectedCourse(sc2, 2);
		if(sc.compareTo(sc2) == 1){
			test++;
		}
		sc2 = new SelectedCourse(sc2, 3);
		if(sc.compareTo(sc2) == -1){
			test++;
		}
		sc2 = new SelectedCourse("02101", " ", 1);
		if(sc.compareTo(sc2) == 1) {
			test++;
		}
		sc2 = new SelectedCourse(sc2, 2);
		if(sc.compareTo(sc2) == -1){
			test++;
		}
		sc2 = new SelectedCourse(sc2, 3);
		if(sc.compareTo(sc2) == -1){
			test++;
		}
		assertTrue(test == 9);
		
	}

	/**
	 * A negative test of the comparing function
	 */
	public void testCompareToNegative() {
		try {
			//Tries to create an object that gets typecasted
			sc.compareTo((SelectedCourse) new Object());
			fail("Should not allow us to do that");
		} catch (Exception e) {
			
		}
		try {
			//Creates a Course, that gets typecasted to SelectedCourse 
			SelectedCourse sc2 = (SelectedCourse) new Course("01005", " ");
			sc.compareTo(sc2);
			//These two types are uncompareable
			fail("uncompareble objects");
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * Testing if it can get the right semester
	 */
	public void testGetSemester() {
		assertTrue(sc.getStartingSemester() == 2);
	}

	/**
	 * A positive test to see if two courses are equal
	 */
	public void testEqualsSelectedCoursePositive() {
		SelectedCourse sc2 = new SelectedCourse("01715", " ", 2);
		assertTrue(sc.equals(sc2));
	}

	/**
	 * A negative test to see if two courses are equal
	 */
	public void testEqualsSelectedCourseNegative() {
		SelectedCourse sc2 = new SelectedCourse("01005", " ", 2);
		assertFalse(sc.equals(sc2));
	}

	/**
	 * Checks if a semester is valid
	 */
	public void testIsValidSemesterPositive() {
		assertTrue(SelectedCourse.isValidSemester(2));
	}

	/**
	 * Checks if a semester is invalid
	 */
	public void testIsValidSemesterNegative() {
		boolean test1;
		boolean test2;
		test1 = SelectedCourse.isValidSemester(-1);
		test2 = SelectedCourse.isValidSemester(21);
		//If just one passes, the test will fail
		assertFalse(test1 || test2);
	}

	/**
	 * Tears down everything the test has created, including resetting the class variable
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		sc = null;
	}


}
