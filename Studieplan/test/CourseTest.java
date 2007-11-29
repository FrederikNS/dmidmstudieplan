package test;

import dataClass.Course;
import dataClass.SelectedCourse;
import databases.DatabaseReader;
import junit.framework.TestCase;

public class CourseTest extends TestCase {

	Course cc;
	
	protected void setUp() throws Exception {
		super.setUp();
		DatabaseReader db = new DatabaseReader();
		cc = new SelectedCourse(db.findCourse("01715"), 2);
	}

	public void testGetCourseIDPositive() {
		assertTrue(cc.getCourseID() == "01715");
	}

	public void testGetCourseIDNegative() {
		assertFalse(cc.getCourseID() != "01715");
	}

	public void testSkemaToStringPositive() {
		try {
			DatabaseReader db = new DatabaseReader();
			cc = new SelectedCourse(db.findCourse("01005"), 1);
		} catch (Exception e) {
			fail(e.toString());
			return;
		}
		String test[] = {"E5A","E5B","E3B","F5A","F5B","F3B"};
		String skema = cc.skemaToString();
		for(int i = 0; i < test.length ; i++) {
			if(!skema.contains(test[i])) {
				fail("did not contain " + test[i]);
			}
		}
	}

	public void testSkemaToStringNegative() {
		fail("Not yet implemented");
	}

	public void testGetCourseName() {
		assertTrue(cc.getCourseName().equals("Funktionalanalyse"));
	}
	
	public void testGetDependenciesPositive() {
		String test[] = {"01035"};
		String[] skema = cc.getDependencies();
		for(int i = 0; i < skema.length ; i++) {
			if(skema[i].contains("01035")) {
				assertTrue(true);
			}
		}
	}

	public void testConflictingSkema() {
		Course cc2 = new Course("01715", " ");
		assertTrue(cc.conflictingSkema(cc2));
	}

	public void testSetDependencies() {
		String depends[] = {"01005", "02101"};
		cc.setDependencies(depends);
		String test[] = cc.getDependencies();
		assertTrue(test.equals(depends));
	}

	public void testGetFullSkemaData() {
		//TODO
		cc.getFullSkemaData();
	}

	public void testSetFullSkemaData() {
		fail("Not yet implemented");
	}

	public void testSetSkemagruppe() {
		fail("Not yet implemented");
	}

	public void testGetSeason() {
		fail("Not yet implemented");
	}

	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	public void testIsSameCourseID() {
		fail("Not yet implemented");
	}
	
	protected void tearDown() throws Exception  {
		super.tearDown();
		cc = null;
	}
}
