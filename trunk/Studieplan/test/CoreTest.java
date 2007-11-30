package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;
import ui.Core;
import dataClass.Course;
import dataClass.ProgramCore;
import dataClass.SelectedCourse;
import dataClass.StudyPlan;
import databases.CourseBase;
import databases.DatabaseReader;
import exceptions.AnotherCourseDependsOnThisCourseException;
import exceptions.CannotSaveStudyPlanException;
import exceptions.ConflictingCourseInStudyPlanException;
import exceptions.CorruptStudyPlanFileException;
import exceptions.CourseAlreadyExistsException;
import exceptions.CourseDoesNotExistException;
import exceptions.FilePermissionException;
import exceptions.StudyPlanDoesNotExistException;

/**
 * Tests the class Core by using jUnit
 * @author Morten Sørensen
 */
public class CoreTest extends TestCase {

	/**
	 * Creates the global variable core
	 */
	Core core;
	
	/**
	 * Sets up the data the test class uses throughout testing
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		//Forces the core to not load ui
		String testSetup[] = {"--no-ui"};
		core = new ProgramCore(testSetup); 
	}
	
	/**
	 * Tests if Core is able to create a new CourseBase that isn't null
	 */
	public void testCourseBase() {
		CourseBase base = core.getCourseBase();
		assertNotNull(base);
	}
	
	/**
	 * Tests if core is able to create a new studyplan
	 */
	public void testCreateStudyPlan() {
		core.newStudyPlan("testSubject");
		try {
			StudyPlan testPlan = core.getStudyPlan("testSubject");
			assertNotNull(testPlan);
		} catch (StudyPlanDoesNotExistException e) {
			//Fails if it can't find the study plan (something failed during created it)
			fail("Exception: StudyPlanDoesNotExistException");
		}
	}
	
	/**
	 * Makes an positive test, to see if it can fill a study plan correct
	 */
	public void testFillStudyPlanPositive() {
		try {
			//Creates a new studyplan if it doesn't exist
			core.getStudyPlan("testSubject", true);
		} catch (StudyPlanDoesNotExistException e1) {
			//We forced it to be created, therefore it is not going to happen
			fail(e1.toString());
		}
		try {
			//Adds two different courses that normally would conflict (same schema groups) in two different semesters
			core.addCourseToStudyPlan("testSubject", "01005", 1);
			core.addCourseToStudyPlan("testSubject", "01035", 2);
		} catch (Exception e) {
			//If any other kind of error occures, something is wrong
			fail("Exception " + e.toString());
		}
		try {
			//Finds a studyplan
			StudyPlan testPlan = core.getStudyPlan("testSubject");
			//Checks if both courses are in the study plan
			assertTrue( testPlan.contains("01005") && testPlan.contains("01035"));
		} catch (StudyPlanDoesNotExistException e) {
			//If this exception is thrown, something had gone wrong in adding the courses to the study plan 
			fail(e.toString());
		} 
	}
	
	/**
	 * Runs a negative test to see if it is possible to add conflicting courses
	 */
	public void testFillStudyPlanNegative() {
		try {
			//Creates a new study plan
			core.getStudyPlan("testSubject", true);
		} catch (StudyPlanDoesNotExistException e2) {
			//We just forced it to be created, this exception is not going to happen
			fail(e2.toString());
		}
		try {
			//Adds a course to the study plan
			core.addCourseToStudyPlan("testSubject", "01005", 1);
		} catch (Exception e1) {
			fail(e1.toString());
		}
		try {
			//Adds the same course to the study plan
			core.addCourseToStudyPlan("testSubject", "01005", 1);
			fail("Course does already exists");
		} catch (CourseAlreadyExistsException e) {
			//It threw this error, just as expected
			
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			//Adds a course that has the same schema group as course 01005
			core.addCourseToStudyPlan("testSubject", "01715", 1);
			fail("No conflicting Exception");
		} catch (ConflictingCourseInStudyPlanException e) {
			//Throws an exception, just as expected
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	/**
	 * A positive test to see if the save function works
	 */
	public void testSavePositive() {
		try {
			//Sets up a study plan with data (reuses the data inserted from a previous test)
			testFillStudyPlanPositive();
			StudyPlan testPlan = core.getStudyPlan("testSubject");
			try {
				//Saves the study plan
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

	/**
	 * A negative test to see if the save function works as expected
	 */
	public void testSaveNegative() {
		//Loads in the data from a previous test (to avoid making the same thing several times)
		testFillStudyPlanPositive();
		try {
			//Tries to save an non-existing study plan
			core.saveStudyPlanAs("testPlanFail", "testSaveNegative");
			fail("No Exception");
		} catch (CannotSaveStudyPlanException e) {
			fail(e.toString());
		} catch (FilePermissionException e) {
			fail(e.toString());
		} catch (StudyPlanDoesNotExistException e) {
			//It is expected it not to be able to find that study plan
			
		}
		try {
			//Saves the study plan to a file the user have no permission to write in
			core.saveStudyPlanAs("testSubject", "PermissionDenied");
			fail("No Exception");
		} catch (FilePermissionException e) {
			//Permission denied to the file, as expected
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	/**
	 * Do a positive test of the load function to see if it works
	 */
	public void testLoadPositive() {
		//Sets up test data
		testSavePositive();
		try {
			//Loads an existing study plan
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
	
	/**
	 * Do a negative test of the load function to see if it reacts as expected
	 */
	public void testLoadNegative() {
		try {
			//Tries to load a study plan that does not exists
			core.loadStudyPlan("FileDoesNotExist");
			fail("No Exception");
		} catch (FileNotFoundException e) {
			//An exception, as expected
			
		} catch (IOException e) {
			fail("Exception: IOException");
		}
		try {
			//Tries to load a study plan the user got no permission to read
			core.loadStudyPlan("PermissionDenied");
			fail("No Exception");
		} catch (FilePermissionException e) {
			//An exception, as expected
			
		} catch (IOException e) {
			fail("Exception: IOException");
		}
	}
	
	/**
	 * A positive test of removing courses from an existing study plan
	 */
	public void testRemoveCoursePositive() {
		//Sets up test data
		testFillStudyPlanPositive();
		try {
			//Remove a course from the study plan
			core.removeCourseFromStudyPlan("testSubject", "01005");
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	/**
	 * A negative test of removing courses from a study plan
	 */
	public void testRemoveCourseNegative() {
		//Sets up test data
		testFillStudyPlanPositive();
		try {
			//Tries to remove a course thats not in the study plan
			if(core.removeCourseFromStudyPlan("testSubject", "01006")) {  
				fail("Course ought not to have been in the plan");
			}
		} catch (StudyPlanDoesNotExistException e) {
			fail(e.toString());
		} catch (CourseDoesNotExistException Success) {
			
		}  catch (Exception e) {
			fail("Wrong exception: " + e.toString());
			return;
		}
		try {
			//Tries to remove a course from a non-existing study plan
			if(core.removeCourseFromStudyPlan("testFail", "01005")) {
				fail("No Exception");
			}
		} catch (CourseDoesNotExistException e) {
			fail(e.toString());
		} catch (StudyPlanDoesNotExistException e) {
			
		} catch (Exception e) {
			fail("Wrong exception: " + e.toString());
			return;
		}
		
		DatabaseReader db;
		try {
			db = new DatabaseReader();
		} catch (Exception e) {
			fail("DatabaseReader failed");
			return;
		}

		try {
			core.addCourseToStudyPlan(new SelectedCourse(db.findCourse("01035"), 2));
			core.addCourseToStudyPlan(new SelectedCourse(db.findCourse("01250"), 3));
		} catch (Exception e) {
			System.err.println(e);
			fail("Adding course?");
		}
		try {
			core.removeCourseFromStudyPlan("01035");
		} catch (AnotherCourseDependsOnThisCourseException e) {

		} catch(Exception e) {
			fail("Wrong Exception: " + e.toString());
			return;
		}
	}

	/**
	 * A positive test to see if a course is in a study plan
	 */
	public void testCourseInStudyPlanPositive() {
		//Sets up test data
		testFillStudyPlanPositive();
		try {
			//Checks if a course is in the study plan
			assertTrue(core.isCourseInStudyPlan("testSubject", "01005"));
		} catch (StudyPlanDoesNotExistException e) {
			fail(e.toString());
		}
	}
	
	/**
	 * A negative test to see if a course is in a study plan
	 */
	public void testCourseInStudyPlanNegative() {
		//Sets up test data
		testFillStudyPlanPositive();
		try {
			//Searching for a course in a non-existing study plan
			core.isCourseInStudyPlan("testFail", "01005");
			fail("No Exception");
		} catch (StudyPlanDoesNotExistException e) {
			
		}
	}
	
	/**
	 * A positive test to see if a course ID is valid
	 */
	public void testValidCoursePositive() {
		assertTrue(core.isValidCourse("01005"));
	}
	
	/**
	 * A negative test to see if a course ID is valid
	 */
	public void testValidCourseNegative() {
		assertFalse(core.isValidCourse("99999") || core.isValidCourse("gk2opt"));
	}
	
	/**
	 * Tears down everything the test has created, including setting the class variable to null
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		core = null;
	}

}