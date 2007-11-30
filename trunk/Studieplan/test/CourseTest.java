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

	public void testGetCourseID() {
		assertTrue(cc.getCourseID().equals("01715"));
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
		String skema = cc.getDependencies();
		assertTrue(skema.contains("01035"));
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
		String depends = "01005 02101";
		cc.setDependencies(depends);
		String test = cc.getDependencies();
		assertTrue(test.equals(depends));
	}

	public void testGetFullSkemaData() {
		assertTrue( cc.getFullSkemaData() == Course.INTERNAL_WEDNEYSDAY_MORNING);
	}

	public void testSetFullSkemaData() {
		int newSkema = Course.INTERNAL_WEDNEYSDAY_MORNING | Course.INTERNAL_THURSDAY_MORNING;
		cc.setFullSkemaData(newSkema);
		assertTrue( cc.getFullSkemaData() == newSkema);
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
