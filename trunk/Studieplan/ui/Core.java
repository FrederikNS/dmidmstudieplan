package ui;

import java.util.Iterator;

import dataClass.Course;
import exceptions.CourseDoesNotExistException;
import dataClass.StudyPlan;
import exceptions.StudyPlanDoesNotExistException;

public interface Core {
	
	public Course findCourse(String courseID) throws CourseDoesNotExistException;
	public Course[] getAllCourses();
	public void addCourseToPlan(String courseID, int semester) throws Exception;
	public void removeCourseFromPlan(String courseID, int semester) throws Exception;
	
	public void saveStudyPlan(String studentID) throws Exception;
	public void saveStudyPlan(StudyPlan plan) throws Exception;
	
	public Iterator<Course> getDatabaseReaderIterator();
	
	public boolean isValidCourse(String courseID);
	
	public StudyPlan loadStudyPlan(String studentID) throws StudyPlanDoesNotExistException;
	
}