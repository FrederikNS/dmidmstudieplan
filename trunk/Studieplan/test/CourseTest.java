package test;

import junit.framework.TestCase;
import dataClass.Course;
import dataClass.SelectedCourse;
import databases.DatabaseReader;

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

	public void testSkemaToString() {
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

	public void testGetCourseName() {
		assertTrue(cc.getCourseName().equals("Funktionalanalyse"));
	}
	
	public void testGetDependenciesPositive() {
		String[] skema = cc.getDependencies();
		for(int i = 0; i < skema.length ; i++) {
			if(skema[i].contains("01035")) {
				assertTrue(true);
			}
		}
	}

	public void testConflictingSkemaPositive() {
		Course cc2;
		try {
			DatabaseReader db = new DatabaseReader();
			cc2 = db.findCourse("01005");
		} catch (Exception e) {
			fail("Course not found");
			return;
		}

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

	public void testEqualsObject() {
		boolean test1 = cc.equals(new Course(cc.getCourseID(),cc.getCourseName()));
		boolean test2 = cc.equals(new SelectedCourse(cc.getCourseID(), cc.getCourseID(),1 ));
		assertTrue(test1 && test2);
	}

	public void testIsSameCourseIDPositive() {
		assertTrue(cc.isSameCourseID(cc.getCourseID()));
	}
	
	public void testIsSameCourseIDNegative() {
		assertFalse(cc.isSameCourseID("01005") || cc.isSameCourseID("0"));
	}
	
	protected void tearDown() throws Exception  {
		super.tearDown();
		cc = null;
	}
}
