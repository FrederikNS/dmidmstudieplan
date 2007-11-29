package test;

import dataClass.Course;
import dataClass.SelectedCourse;
import dataClass.StudyPlan;
import exceptions.ConflictingCourseInStudyPlanException;
import exceptions.CourseAlreadyExistsException;
import exceptions.CourseDoesNotExistException;
import junit.framework.TestCase;

public class StudyPlanTest extends TestCase {
	
	StudyPlan sp;

	protected void setUp()  throws Exception{
		super.setUp();
		sp = new StudyPlan("testSubject3");
	}

	public void testEqualsObjectPositive() {
		Object obj = new StudyPlan("testSubject3");
		assertTrue(sp.equals(obj));
	}
	
	public void testEqualsObjectNegative() {
		assertFalse(sp.equals("testFail") || sp.equals(""));
	}

	public void testAddPositive() {
		SelectedCourse sc;
		sc = new SelectedCourse("01005", 1);
		try {
			sp.add(sc);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	public void testAddNegative() {
		testAddPositive();
		SelectedCourse sc;
		sc = new SelectedCourse("01005", 1);
		try {
			sp.add(sc);
			fail("Should conflict with the already added course");
		} catch (CourseAlreadyExistsException e) {
			
		} catch (ConflictingCourseInStudyPlanException e) {
			fail(e.toString());
		}
		SelectedCourse sc2;
		sc2 = new SelectedCourse("01715", 1);
		try {
			sp.add(sc2);
			fail("course conflict expected");
		} catch (ConflictingCourseInStudyPlanException e) {
			
		} catch (CourseAlreadyExistsException e) {
			fail("Should not already exist in studyplan");
		} 
	}

	public void testContainsStringPositive() {
		try {
			sp.add(new SelectedCourse("01005", 1));
		} catch (Exception e) {
			fail(e.toString());
		}
		assertTrue(sp.contains("01005"));
	}

	public void testContainsStringNegative() {
		assertFalse(sp.contains("01005"));
	}

	public void testContainsCoursePositive() {
		SelectedCourse sc;
		try {
			sc = new SelectedCourse("01005", 1);
			sp.add(sc);
		} catch (Exception e) {
			fail(e.toString());
			sc = null;
		}
		boolean test1 = sp.contains(sc);
		boolean test2 = sp.contains(new Course("01005"));
		assertTrue(test1 && test2);
	}

	public void testContainsCourseNegative() {
		assertFalse(sp.contains("01005"));
	}

	public void testRemoveStringPositive() {
		testAddPositive();
		try {
			assertTrue(sp.remove("01005"));
		} catch (CourseDoesNotExistException e) {
			fail(e.toString());
		}
	}

	public void testRemoveStringNegative() {
		try {
			sp.remove("01005");
			fail("course ought not to exist");
		} catch (CourseDoesNotExistException e) {
			
		}
		try {
			sp.remove("");
			fail("no name");
		} catch (CourseDoesNotExistException e) {
			
		}
	}

	public void testRemoveCoursePositive() {
		testAddPositive();
		try {
			assertTrue(sp.remove(new Course("01005")));
		} catch (CourseDoesNotExistException e) {
			fail(e.toString());
		}
	}

	public void testRemoveCourseNegative() {
		try {
			sp.remove(new Course("01005"));
			fail("course ought not to exist");
		} catch (CourseDoesNotExistException e) {
			
		}
		try {
			sp.remove(new Course(""));
			fail("no name");
		} catch (CourseDoesNotExistException e) {
			
		}
	}

	public void testPrintSemesterPositive() {
		testAddPositive();
		try {
			sp.printSemester(1);
		} catch (IllegalArgumentException e) {
			fail(e.toString());
		}
	}

	public void testPrintSemesterNegative() {
		try {
			sp.printSemester(21);
			fail("Exception expected");
		} catch (IllegalArgumentException e) {
			
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		sp = null;
	}
}
