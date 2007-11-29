package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import ui.Core;

import dataClass.ProgramCore;
import dataClass.StudyPlan;
import databases.CourseBase;
import exceptions.CannotSaveStudyPlanException;
import exceptions.ConflictingCourseInStudyPlanException;
import exceptions.CorruptStudyPlanFileException;
import exceptions.CourseAlreadyExistsException;
import exceptions.CourseDoesNotExistException;
import exceptions.FilePermissionException;
import exceptions.StudyPlanDoesNotExistException;
import junit.framework.TestCase;

public class CoreTest extends TestCase {

	Core core;
	
	protected void setUp() throws Exception {
		super.setUp();
		String testSetup[] = {"--no-ui"};
		core = new ProgramCore(testSetup); 
	}
	
	public void testCourseBase() {
		CourseBase base = core.getCourseBase();
		assertNotNull(base);
	}
	
	public void testCreateStudyPlan() {
		core.newStudyPlan("testSubject");
		try {
			StudyPlan testPlan = core.getStudyPlan("testSubject");
			assertNotNull(testPlan);
		} catch (StudyPlanDoesNotExistException e) {
			fail("Exception: StudyPlanDoesNotExistException");
		}
	}
	
	public void testFillStudyPlanPositive() {
		try {
			core.getStudyPlan("testSubject", true);
		} catch (StudyPlanDoesNotExistException e1) {
			fail("Not gonna happen");
		}
		try {
			core.addCourseToStudyPlan("testSubject", "01005", 1);
			core.addCourseToStudyPlan("testSubject", "01715", 2);
		} catch (ConflictingCourseInStudyPlanException e) {
			fail(e.toString());
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			StudyPlan testPlan = core.getStudyPlan("testSubject");
			assertTrue(testPlan.contains("01005") && testPlan.contains("01715"));
		} catch (StudyPlanDoesNotExistException e) {
			fail("Not gonna happen2");
		}
	}
	
	public void testFillStudyPlanNegative() {
		try {
			core.getStudyPlan("testSubject", true);
		} catch (StudyPlanDoesNotExistException e2) {
			fail("Not gonna happen3");
		}
		try {
			core.addCourseToStudyPlan("testSubject", "01005", 1);
		} catch (Exception e1) {
			fail(e1.toString());
		}
		try {
			core.addCourseToStudyPlan("testSubject", "01005", 1);
			fail("Course does already exists");
		} catch (CourseAlreadyExistsException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			core.addCourseToStudyPlan("testSubject", "01715", 1);
			fail("No conflicting Exception");
		} catch (ConflictingCourseInStudyPlanException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	public void testSavePositive() {
		try {
			testFillStudyPlanPositive();
			StudyPlan testPlan = core.getStudyPlan("testSubject");
			System.out.println(testPlan.printSemester(1));
			try {
				core.saveStudyPlanAs(testPlan, "testSavePositive");
			} catch (CannotSaveStudyPlanException e) {
				fail("Exception: CannotSaveStudyPlanException");
			} catch (FilePermissionException e) {
				fail("Exception: FilePermissionException");
			}
		} catch (StudyPlanDoesNotExistException e) {
			fail("Exception: StudyPlanDoesNotExistsException");
		}
	}

	public void testSaveNegative() {
		testFillStudyPlanPositive();
		try {
			core.saveStudyPlanAs("testPlanFail", "testSaveNegative");
			fail("No Exception");
		} catch (CannotSaveStudyPlanException e) {
			fail(e.toString());
		} catch (FilePermissionException e) {
			fail(e.toString());
		} catch (StudyPlanDoesNotExistException e) {
			
		}
		try {
			core.saveStudyPlanAs("testSubject", "PermissionDenied");
			fail("No Exception");
		} catch (FilePermissionException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	public void testLoadPositive() {
		//TODO
		try {
			core.loadStudyPlan("testSavePositive");
		} catch (FilePermissionException e) {
			fail("Exception: FilePermission");
		} catch (FileNotFoundException e) {
			fail("Exception: FailNotFound");
		} catch (CorruptStudyPlanFileException e) {
			fail("Exception: CorruptStudyPlan");
		} catch (IOException e) {
			fail("Exception: IOException");
		}
	}
	
	public void testLoadNegative() {
		try {
			core.loadStudyPlan("FileDoesNotExist");
			fail("No Exception");
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			fail("Exception: IOException");
		}
		try {
			core.loadStudyPlan("PermissionDenied");
			fail("No Exception");
		} catch (FilePermissionException e) {
			
		} catch (IOException e) {
			fail("Exception: IOException");
		}
	}
	
	public void testRemoveCoursePositive() {
		testFillStudyPlanPositive();
		try {
			core.removeCourseFromStudyPlan("testSubject", "01005");
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	public void testRemoveCourseNegative() {
		testFillStudyPlanPositive();
		try {
			core.removeCourseFromStudyPlan("testSubject", "01006");
			fail("No Exception");
		} catch (CourseDoesNotExistException e) {
			
		} catch (StudyPlanDoesNotExistException e) {
			fail(e.toString());
		}
		try {
			core.removeCourseFromStudyPlan("testFail", "01005");
			fail("No Exception");
		} catch (CourseDoesNotExistException e) {
			fail(e.toString());
		} catch (StudyPlanDoesNotExistException e) {
			
		}
	}

	public void testCourseInStudyPlanPositive() {
		testFillStudyPlanPositive();
		try {
			assertTrue(core.isCourseInStudyPlan("testSubject", "01005"));
		} catch (StudyPlanDoesNotExistException e) {
			fail(e.toString());
		}
	}
	
	public void testCourseInStudyPlanNegative() {
		testFillStudyPlanPositive();
		try {
			core.isCourseInStudyPlan("testFail", "01005");
			fail("No Exception");
		} catch (StudyPlanDoesNotExistException e) {
			
		}
	}
	
	public void testValidCoursePositive() {
		assertTrue(core.isValidCourse("01005"));
	}
	
	public void testValidCourseNegative() {
		assertFalse(core.isValidCourse("99999") || core.isValidCourse("gk2opt"));
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		core = null;
	}

}