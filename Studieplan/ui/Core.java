package ui;

import java.util.Iterator;

import dataClass.Course;
import dataClass.SelectedCourse;
import exceptions.CourseDoesNotExistException;
import dataClass.StudyPlan;
import exceptions.StudyPlanDoesNotExistException;

public interface Core {
	
	public Course findCourse(String courseID) throws CourseDoesNotExistException;
	public Course[] getAllCourses();
	public void addCourseToStudyPlan(String studentID, Course course, int semester) throws Exception;
	public void addCourseToStudyPlan(String studentID, String courseID, int semester) throws Exception;
	public void addCourseToStudyPlan(String studentID, SelectedCourse course) throws Exception;
	public StudyPlan getStudyPlan(String studentID) throws StudyPlanDoesNotExistException;
	public void removeCourseFromStudyPlan(String studentID, String courseID) throws Exception;
	public void removeCourseFromStudyPlan(String studentID, Course course) throws Exception;
	
	public void saveStudyPlan(String studentID) throws Exception;
	public void saveStudyPlan(StudyPlan plan) throws Exception;
	
	public Iterator<Course> getDatabaseReaderIterator();
	
	public boolean isValidCourse(String courseID);
	
	public StudyPlan loadStudyPlan(String studentID) throws StudyPlanDoesNotExistException; 
	
	/**
	 * Search through the Course data files for a pattern (this can be a regular expression).
	 * @param pattern searches for a pattern.
	 * @return returns an array of courses which (somehow) matches this pattern.
	 * @throws CourseDoesNotExistException thrown if no courses match the pattern.
	 */
	public Course[] search(String pattern) throws CourseDoesNotExistException;
	
}
