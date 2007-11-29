package test;

import java.io.FileNotFoundException;

import dataClass.Course;
import dataClass.SelectedCourse;
import dataClass.StudyPlan;
import databases.UserDatabase;
import exceptions.CannotSaveStudyPlanException;
import exceptions.ConflictingCourseInStudyPlanException;
import exceptions.CourseAlreadyExistsException;
import exceptions.FilePermissionException;
import junit.framework.TestCase;

/**
 * This test class runs a series of tests on the class UserDatabase using jUnit
 * @author Morten Sørensen
 */
public class UserDatabaseTest extends TestCase {
	
	/**
	 * Sets up the class variable usr
	 */
	UserDatabase usr;

	/**
	 * Initialises the UserDatabase
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		usr = new UserDatabase();
	}

	/**
	 * A positive test to see if a saved study plan exists
	 */
	public void testExistsPositive() {
		try {
			usr.exists("UserDBTest");
		} catch (FileNotFoundException e) {
			fail(e.toString());
		}
	}
	
	/**
	 * A negative test to see if a saved study plan exists
	 */
	public void testExistsNegative() {
		try {
			//User added the file extension by himself
			usr.exists("UserDBTest.plan");
			fail("File ought not exist");
		} catch (FileNotFoundException e) {
			
		}
		try {
			usr.exists("testFail");
			fail("File ought not exists");
		} catch (FileNotFoundException e) {
			
		}
	}

	/**
	 * A positive test of the save function
	 */
	public void testSavePositive() {
		StudyPlan testPlan = new StudyPlan("testSubject2");
		try {
			testPlan.add(new SelectedCourse("01005", "Matematik 1", 1));
		} catch (Exception e) {
			fail(e.toString());		
		}
		try {
			usr.saveStudyPlan(testPlan);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	/**
	 * A negative test of the save function
	 */
	public void testSaveNegative() {
		//User types in an empty name
		StudyPlan testPlan = new StudyPlan("");
		try {
			usr.saveStudyPlan(testPlan);
			fail("No name");
		} catch (CannotSaveStudyPlanException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
		StudyPlan testPlan2 = new StudyPlan("PermissionDenied");
		try {
			//User tries to save to a file he doesn't have permissions to write in
			usr.saveStudyPlan(testPlan2);
			fail("Permission Denied");
		} catch (FilePermissionException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	/**
	 * A positive test of the load function
	 */
	public void testLoadPositive() {
		testSavePositive();
		StudyPlan plan = null;
		try {
			plan = usr.loadStudyPlan("testSubject2");			
		} catch (Exception e) {
			fail(e.toString());
			return;
		}
		
		assertTrue(plan.contains("01005"));
	}
	
	/**
	 * A negative test of the load function
	 */
	public void testLoadNegative() {
		testSavePositive();
		try {
			//User types in a plan that doesn't exists
			usr.loadStudyPlan("testFail");
			fail("File ought not to exists");
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			//User tries to load a plan without the required read permission
			usr.loadStudyPlan("PermissionDenied");
			fail("Permission denied");
		} catch (FilePermissionException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	/**
	 * A positive test of deleting a study plan
	 */
	public void testDeletePositive() {
		//Sets up some test data
		testSavePositive();
		try {
			usr.deleteFile("testSubject2");
		} catch (Exception e) {
			fail("File should exists");
		}
	}
	
	/**
	 * A negative test of deleting a study plan
	 */
	public void testDeleteNegative() {
		try {
			//User forgot to type in a name
			usr.deleteFile("");
			fail("no name");
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			//Usere typed in an non-existing study plan
			usr.deleteFile("testFail");
			fail("File ought not to exist");
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			//User tries to delete a studyplan without the required permission
			usr.deleteFile("PermissionDenied");
			fail("Permission Denied");
		} catch (FilePermissionException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	/**
	 * Tears down everything the test has created, including resetting the class variable
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		usr = null;
	}

}
