package test;

import databases.CourseBase;
import exceptions.CourseDoesNotExistException;
import junit.framework.TestCase;

public class TestCourseBase extends TestCase {

	private CourseBase base;
	
	protected void setUp() throws Exception {
		super.setUp();
		base = new CourseBase();
	}
	
	public void testAmountOfCourses(){
		assertTrue(base.getAmountOfCourses()>100);
	}
	
	public void testFindCourse() {
		try {
			assertNotNull(base.findCourse("01005"));
		} catch (CourseDoesNotExistException e) {
			// TODO Auto-generated catch block
			fail("Course Not Found");
		}
	}
	
	public void testSearch() {
		try {
			System.out.println(base.search("mat").length);
		} catch (CourseDoesNotExistException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			assertTrue(base.search("mat").length>0);
		} catch (CourseDoesNotExistException e) {
			// TODO Auto-generated catch block
			fail("Nothing Found");
		}
		
	}
	
	public void testGetAllCourses() {
		assertTrue(base.getAllCourses().length==base.getAmountOfCourses());
	}
	
	public void testToString() {
		assertNotNull(base.toString());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		base = null;
	}

}
