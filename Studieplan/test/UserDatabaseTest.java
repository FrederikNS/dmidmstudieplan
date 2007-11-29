package test;

import java.io.FileNotFoundException;
import dataClass.StudyPlan;
import databases.UserDatabase;
import exceptions.CannotSaveStudyPlanException;
import exceptions.FilePermissionException;
import junit.framework.TestCase;

public class UserDatabaseTest extends TestCase {
	
	UserDatabase usr;

	protected void setUp() throws Exception {
		super.setUp();
		usr = new UserDatabase();
	}

	public void testExistsPositive() {
		try {
			usr.exists("UserDBTest");
		} catch (FileNotFoundException e) {
			fail(e.toString());
		}
	}
	
	public void testExistsNegative() {
		try {
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

	public void testSavePositive() {
		StudyPlan testPlan = new StudyPlan("testSubject2");
		try {
			usr.saveStudyPlan(testPlan);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	public void testSaveNegative() {
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
			usr.saveStudyPlan(testPlan2);
			fail("Permission Denied");
		} catch (FilePermissionException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	public void testLoadPositive() {
		testSavePositive();
		try {
			usr.loadStudyPlan("testSubject2");
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	public void testLoadNegative() {
		testSavePositive();
		try {
			usr.loadStudyPlan("testFail");
			fail("File ought not to exists");
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			usr.loadStudyPlan("PermissionDenied");
			fail("Permission denied");
		} catch (FilePermissionException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	public void testDeletePositive() {
		testSavePositive();
		try {
			usr.deleteFile("testSubject2");
		} catch (Exception e) {
			fail("File should exists");
		}
	}
	
	public void testDeleteNegative() {
		testSavePositive();
		try {
			usr.deleteFile("");
			fail("no name");
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			usr.deleteFile("testFail");
			fail("File ought not to exist");
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			usr.deleteFile("PermissionDenied");
			fail("Permission Denied");
		} catch (FilePermissionException e) {
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		usr = null;
	}

}
