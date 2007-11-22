package ui;

import dataClass.Course;
import dataClass.CourseDoesNotExistException;
import dataClass.StudyPlan;
import dataClass.StudyPlanDoesNotExistException;

public interface Core {
	
	//public boolean isValidCourse(String courseID);
	
	public Course findCourse(String courseID) throws CourseDoesNotExistException;
	public Course[] getAllCourses();
	public int addCourseToPlan(String courseID, int semester);
	public int removeCourseFromPlan(String courseID, int semester);
	
	public boolean saveStudyPlan(String studentID);
	public boolean saveStudyPlan(StudyPlan plan);
	
	public StudyPlan loadStudyPlan(String studentID) throws StudyPlanDoesNotExistException;
	
}
