package test;

import databases.CourseBase;
import exceptions.CourseDoesNotExistException;
import junit.framework.TestCase;

public class CourseBaseTest extends TestCase {

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
			fail("Course Not Found");
		}
	}
	
	public void testSearch() {
		try {
			assertTrue(base.search("mat").length>0);
		} catch (CourseDoesNotExistException e) {
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