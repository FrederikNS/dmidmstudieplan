package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import dataClass.Course;
import dataClass.SelectedCourse;
import exceptions.CannotSaveStudyPlanException;
import exceptions.CorruptStudyPlanFileException;
import exceptions.CourseDoesNotExistException;
import exceptions.CritalCourseDataMissingException;
import exceptions.FilePermissionException;
import dataClass.StudyPlan;
import exceptions.StudyPlanDoesNotExistException;

public interface Core {
	
	public Course findCourse(String courseID) throws CourseDoesNotExistException, CritalCourseDataMissingException;
	public Course[] getAllCourses();
	
	public StudyPlan newStudyPlan(String studentID);
	public void addCourseToStudyPlan(String studentID, Course course, int semester) throws Exception;
	public void addCourseToStudyPlan(String studentID, String courseID, int semester) throws Exception;
	public void addCourseToStudyPlan(String studentID, SelectedCourse course) throws Exception;
	public StudyPlan getStudyPlan(String studentID) throws StudyPlanDoesNotExistException;
	public StudyPlan getStudyPlan(String studentID, boolean createNewIfNotExists) throws StudyPlanDoesNotExistException;
	public void removeCourseFromStudyPlan(String studentID, String courseID) throws Exception;
	public void removeCourseFromStudyPlan(String studentID, Course course) throws Exception;

	public void saveStudyPlanAs(String studentID, String newName) throws  CannotSaveStudyPlanException, FilePermissionException, StudyPlanDoesNotExistException;
	public void saveStudyPlanAs(StudyPlan plan, String newName)  throws CannotSaveStudyPlanException, FilePermissionException ;
	
	public void saveStudyPlan(String studentID) throws  CannotSaveStudyPlanException, FilePermissionException, StudyPlanDoesNotExistException;
	public void saveStudyPlan(StudyPlan plan)  throws CannotSaveStudyPlanException, FilePermissionException ;
	public StudyPlan loadStudyPlan(String studentID) throws StudyPlanDoesNotExistException, FilePermissionException, FileNotFoundException, IOException, CorruptStudyPlanFileException;	
	
	
	public Iterator<Course> getDatabaseReaderIterator();
	
	public boolean isValidCourse(String courseID);
	 
	
	/**
	 * Search through the Course data files for a pattern (this can be a regular expression).
	 * @param pattern searches for a pattern.
	 * @return returns an array of courses which (somehow) matches this pattern.
	 * @throws CourseDoesNotExistException thrown if no courses match the pattern.
	 */
	public Course[] search(String pattern) throws CourseDoesNotExistException;
	
}
