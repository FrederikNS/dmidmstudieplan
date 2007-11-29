package test;

import dataClass.Course;
import databases.DatabaseReader;
import junit.framework.TestCase;
import dataClass.CourseSkemaData.InternalSkema;

public class TestDatabaseReader extends TestCase{

	private DatabaseReader reader;
	

	protected void setUp() throws Exception {
		super.setUp();
		try{
		reader = new DatabaseReader();
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	public void testFindCourse() {
		Course course = null;
		try {
			course = reader.findCourse("01005");
		} catch (Exception e) {
			fail(e.toString());
		}
		boolean navn = course.getCourseName().equals("Matematik 1");
		String[] dtuSkema={"E5A","E5B","E3B","F5A","F5B","F3B"};
		String[] length={null};
		int i = InternalSkema.parseDTUskema(dtuSkema,length);
		boolean skema = course.getFullSkemaData()==i;
		boolean deps = course.getDependencies() == null;
		assertTrue(navn && skema && deps);
	}
	
	public void testFindCourseNegative() {
		try {
			reader.findCourse("0100051");
			fail("Fail Expected");
		} catch (Exception e) {
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		reader = null;
	}

}