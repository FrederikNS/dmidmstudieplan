package test;

import java.io.File;
import java.io.FileNotFoundException;

import junit.framework.TestCase;
import dataClass.SelectedCourse;
import dataClass.StudyPlan;
import databases.CourseBase;
import databases.UserDatabase;
import exceptions.CannotSaveStudyPlanException;
import exceptions.FilePermissionException;

/**
 * This test class runs a series of tests on the class UserDatabase using jUnit
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class UserDatabaseTest extends TestCase {

	/**
	 * Sets up the class variable usr
	 */
	UserDatabase usr;
	/**
	 * Set to true if the Permission cannot be run.
	 */
	boolean disablePermissionTest = false;

	/**
	 * Initialises the UserDatabase
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		usr = new UserDatabase();
		File f = new File("PermissionDenied.plan");
		if(!f.exists()) {
			f.createNewFile();
		}
		if(f.canRead()) {
			if(!f.setReadable(false)) {
				disablePermissionTest = true;
				return;
			}
		}
		if(f.canWrite()) {
			if(!f.setWritable(false)){
				disablePermissionTest = true;
				return;
			}
		}
	}

	/**
	 * A positive test to see if a saved study plan exists
	 */
	public void testExistsPositive() {
		assertTrue(usr.exists("UserDBTest"));
	}

	/**
	 * A negative test to see if a saved study plan exists
	 */
	public void testExistsNegative() {
		if(usr.exists("UserDBTest.plan"))
			fail("File ought not exist");

		if(usr.exists("testFail"))
			fail("File ought not exists");
	}

	/**
	 * A positive test of the save function
	 */
	public void testSavePositive() {
		StudyPlan testPlan = new StudyPlan("testSubject2");
		CourseBase cb;
		try {
			cb = new CourseBase();
			testPlan.add(new SelectedCourse(cb.findCourse("01005"), 1));
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
		//For this test works, the file PermissionDenied.plan needs to be off-limit for the user.
		//It is easy for a linux user to make such kind of file, all they need is to hand it over to the root user
		//and set up permissions for it (only root can read and write).
		//For Windows users, this is harder. Usually they always log in as some sort of administrator, and therefore
		//always got full access to the system.
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
		if(disablePermissionTest) {
			System.out.println("Warning: Permission test skipped");
			assertTrue(true);
			return;
		}
		try {
			//User tries to load a plan without the required read permission
			usr.loadStudyPlan("PermissionDenied");
			//For this test works, the file PermissionDenied.plan needs to be off-limit for the user.
			//It is easy for a linux user to make such kind of file, all they need is to hand it over to the root user
			//and set up permissions for it (only root can read and write).
			//For Windows users, this is harder. Usually they always log in as some sort of administrator, and therefore
			//always got full access to the system.
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
		if(disablePermissionTest) {
			System.out.println("Warning: Permission test skipped");
			return;
		}
		try {
			//User tries to delete a studyplan without the required permission
			usr.deleteFile("PermissionDenied");
			//For this test works, the file PermissionDenied.plan needs to be off-limit for the user.
			//It is easy for a linux user to make such kind of file, all they need is to hand it over to the root user
			//and set up permissions for it (only root can read and write).
			//For Windows users, this is harder. Usually they always log in as some sort of administrator, and therefore
			//always got full access to the system.
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
