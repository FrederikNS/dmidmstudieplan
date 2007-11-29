package test;

import dataClass.Course;
import dataClass.SelectedCourse;
import databases.DatabaseReader;
import junit.framework.TestCase;

/**
 * Tests the class SelectedCourses by usint jUnit
 * @author Morten Sørensen
 */
public class SelectedCourseTest extends TestCase {

	SelectedCourse sc;
	
	protected void setUp() throws Exception {
		super.setUp();
		DatabaseReader db = new DatabaseReader();
		sc = new SelectedCourse(db.findCourse("01715"), 2);
	}

	public void testCompareToPositive() {
		SelectedCourse sc2 = new SelectedCourse("01715", " ", 1);
		int test = 0;
		if(sc.compareTo(sc2) == 1) {
			test++;
		}
		sc2.setSemester(2);
		if(sc.compareTo(sc2) == 0){
			test++;
		}
		sc2.setSemester(3);
		if(sc.compareTo(sc2) == -1){
			test++;
		}
		sc2 = new SelectedCourse("01005", " ", 1);
		if(sc.compareTo(sc2) == 1) {
			test++;
		}
		sc2.setSemester(2);
		if(sc.compareTo(sc2) == 1){
			test++;
		}
		sc2.setSemester(3);
		if(sc.compareTo(sc2) == -1){
			test++;
		}
		sc2 = new SelectedCourse("02101", " ", 1);
		if(sc.compareTo(sc2) == 1) {
			test++;
		}
		sc2.setSemester(2);
		if(sc.compareTo(sc2) == -1){
			test++;
		}
		sc2.setSemester(3);
		if(sc.compareTo(sc2) == -1){
			test++;
		}
		assertTrue(test == 9);
		
	}

	public void testCompareToNegative() {
		try {
			sc.compareTo((SelectedCourse) new Object());
			fail("Should not allow us to do that");
		} catch (Exception e) {
			
		}
		try {
			SelectedCourse sc2 = (SelectedCourse) new Course("01005", " ");
			sc.compareTo(sc2);
			fail("uncompareble objects");
		} catch(Exception e) {
			
		}
	}
	
	public void testGetSemesterPositive() {
		assertTrue(sc.getSemester() == 2);
	}

	public void testGetSemesterNegative() {
		assertFalse(sc.getSemester() != 2);
	}

	public void testSetSemesterPositive() {
		sc.setSemester(5);
		assertTrue(sc.getSemester() == 5);
	}

	public void testSetSemesterNegative() {
		try {
			sc.setSemester(-1);
			fail("illegal argument");
		} catch (IllegalArgumentException e) {
			
		}
		try {
			sc.setSemester(21);
			fail("illegal argument");
		} catch (IllegalArgumentException e) {
			
		}
	}

	public void testEqualsSelectedCoursePositive() {
		SelectedCourse sc2 = new SelectedCourse("01715", " ", 2);
		assertTrue(sc.equals(sc2));
	}

	public void testEqualsSelectedCourseNegative() {
		SelectedCourse sc2 = new SelectedCourse("01005", " ", 2);
		assertFalse(sc.equals(sc2));
	}

	public void testIsValidSemesterPositive() {
		assertTrue(sc.isValidSemester(2));
	}

	public void testIsValidSemesterNegative() {
		boolean test1;
		boolean test2;
		test1 = sc.isValidSemester(-1);
		test2 = sc.isValidSemester(21);
		assertFalse(test1 || test2);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		sc = null;
	}


}
