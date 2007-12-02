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
	 * Sets up the class variable used for SelectedCourse
	 */
	SelectedCourse sc;
	/**
	 * Sets up the class variable used for CourseBase
	 */
	CourseBase cb;
	
	/**
	 * Sets up some data needed for the tests
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		cb = new CourseBase();
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
	 * Testing if it can get the right starting semester for a course
	 */
	public void testGetStartingSemester() {
		SelectedCourse sc2 = null;
		try {
			sc2 = new SelectedCourse(cb.findCourse("01005"), 1);
		} catch (Exception e) {
			//Should not happen, else something is terrible wrong
			System.out.println(e);
		}
		assertTrue(sc2.getStartingSemester() == 1);
	}

	/**
	 * Testing if it can get the right starting period for a course
	 */
	public void testGetStartingPeriod() {
		SelectedCourse sc2 = null;
		try {
			sc2 = new SelectedCourse(cb.findCourse("01005"), 1);
		} catch (Exception e) {
			//Should not happen, else something is terrible wrong
			System.out.println(e);
		}
		assertTrue(sc2.getStartingPeriod()==1);
	}

	/**
	 * Testing if it can get the right finishing period
	 */
	public void testGetFinishingPeriod() {
		assertTrue(sc.getFinishingPeriod() == 3);
	}

	/**
	 * Testing if a course is in the same semester as another course
	 */
	public void testGetIsInSameSemester() {
		SelectedCourse sc2 = new SelectedCourse("02101", " ", 1);
		int test = 0;
		if(sc.getIsInSameSemester(sc2) == 1) {
			test++;
		}
		sc2 = new SelectedCourse(sc2, 2);
		if(sc.getIsInSameSemester(sc2) == 0){
			test++;
		}
		sc2 = new SelectedCourse(sc2, 3);
		if(sc.getIsInSameSemester(sc2) == -1){
			test++;
		}
		sc2 = new SelectedCourse("01005", " ", 1);
		if(sc.getIsInSameSemester(sc2) == 1) {
			test++;
		}
		sc2 = new SelectedCourse(sc2, 2);
		if(sc.getIsInSameSemester(sc2) == 0){
			test++;
		}
		sc2 = new SelectedCourse(sc2, 3);
		if(sc.getIsInSameSemester(sc2) == -1){
			test++;
		}
		sc2 = new SelectedCourse("02101", " ", 1);
		if(sc.getIsInSameSemester(sc2) == 1) {
			test++;
		}
		sc2 = new SelectedCourse(sc2, 2);
		if(sc.getIsInSameSemester(sc2) == 0){
			test++;
		}
		sc2 = new SelectedCourse(sc2, 3);
		if(sc.getIsInSameSemester(sc2) == -1){
			test++;
		}
		assertTrue(test == 9);
	}

	/**
	 * Testing which period a course is in
	 */
	public void testGetIsInPeriod() {
		int test = 0;
		if(sc.getIsInPeriod(1, 2) == 1) {
			test++;
		}
		if(sc.getIsInPeriod(3, 4) == 0) {
			test++;
		}
		if(sc.getIsInPeriod(5, 6) == -1) {
			test++;
		}
		sc = new SelectedCourse(sc, 1);
		if(sc.getIsInPeriod(1, 2) == 0) {
			test++;
		}
		if(sc.getIsInPeriod(3, 4) == -1) {
			test++;
		}
		if(sc.getIsInPeriod(3, 4) == -1) {
			test++;
		}
		sc = new SelectedCourse(sc, 3);
		if(sc.getIsInPeriod(1, 2) == 1) {
			test++;
		}
		if(sc.getIsInPeriod(3, 4) == 1) {
			test++;
		}
		if(sc.getIsInPeriod(5, 6) == 0) {
			test++;
		}
		assertTrue(test == 9);
	}

	/**
	 * Testing which semester a course is in
	 */
	public void testGetIsInSemester() {
		int test = 0;
		SelectedCourse sc2 = new SelectedCourse("02238", " ", 1);		
		if(sc2.getIsInSemester(true, 1, 2) == 1) {
			test++;
			System.out.println("test½");
		}
		if(sc2.getIsInSemester(false, 1, 2) == 1) {
			test++;
			System.out.println("test1");
		}
		if(sc2.getIsInSemester(3) == 1) {
			test++;
		}
		sc2 = new SelectedCourse(sc2, 1);
		if(sc2.getIsInSemester(1, 2) == 1) {
			test++;
		}
		if(sc2.getIsInSemester(1, 2) == 1) {
			test++;
		}
		if(sc2.getIsInSemester(1, 2) == 1) {
			test++;
		}
		sc2 = new SelectedCourse(sc2, 3);
		if(sc2.getIsInSemester(1, 2) == 1) {
			test++;
		}
		if(sc2.getIsInSemester(1, 2) == 1) {
			test++;
		}
		if(sc2.getIsInSemester(1, 2) == 1) {
			test++;
		}
		assertTrue(test == 9);
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