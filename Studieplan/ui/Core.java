package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
//import java.util.Iterator;

import dataClass.Course;
import dataClass.SelectedCourse;
import exceptions.CannotSaveStudyPlanException;
import exceptions.ConflictingCourseInStudyPlanException;
import exceptions.CorruptStudyPlanFileException;
import exceptions.CourseAlreadyExistsException;
import exceptions.CourseDoesNotExistException;
import exceptions.CritalCourseDataMissingException;
import exceptions.FilePermissionException;
import dataClass.StudyPlan;
import databases.CourseBase;
import exceptions.StudyPlanDoesNotExistException;

public interface Core {
	
	/**
	 * Look up a course by a course ID in the database.
	 * @param courseID the ID of the course to find
	 * @return the Course with that ID.
	 * @throws CourseDoesNotExistException Thrown if no course has that ID.
	 * @throws CritalCourseDataMissingException Thrown if a course have that ID, the data about it is incomplete.
	 */
	public Course findCourse(String courseID) throws CourseDoesNotExistException;
	/**
	 * Get a List of all the valid courses in the database. 
	 * @return an array of all Valid courses.
	 */
	public CourseBase getCourseBase();
	
	/**
	 * Generate a new StudyPlan to work on.
	 * @param studentID the studentID of that Student.
	 * @return the new StudyPlan.
	 */
	public StudyPlan newStudyPlan(String studentID);

	/**
	 * @see ui.Core#addCourseToStudyPlan(java.lang.String, dataClass.SelectedCourse)
	 * Adds the Course to the current StudyPlan
	 * @param course The course to be added.
	 * @param semester The semester the course should be added too.
	 * @throws CourseDoesNotExistException Thrown if no course had that course ID.
	 * @throws IllegalArgumentException Thrown if semester is less than 0 or greater than 20
	 * @throws CourseAlreadyExistsException Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException Thrown if the course to be added have at least one matching skema data with a course on the same semester as the course to be added. 
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan related to studentID does not exist.
	 * @throws CourseAlreadyExistsException 
	 */	
	public void addCourseToStudyPlan(String courseID, int semester) throws ConflictingCourseInStudyPlanException, CourseDoesNotExistException, CritalCourseDataMissingException, IllegalArgumentException, StudyPlanDoesNotExistException, CourseAlreadyExistsException;
	/**
	 * @see ui.Core#addCourseToStudyPlan(java.lang.String, dataClass.SelectedCourse)
	 * Adds the Course to the current StudyPlan
	 * @param studentID the student ID of the student, who's plan it should be added to.
	 * @param course The course to be added.
	 * @param semester The semester the course should be added too.
	 * @throws IllegalArgumentException Thrown if semester is less than 0 or greater than 20
	 * @throws CourseAlreadyExistsException Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException Thrown if the course to be added have at least one matching skema data with a course on the same semester as the course to be added. 
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan related to studentID does not exist.
	 */	
	public void addCourseToStudyPlan(Course course, int semester) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException, IllegalArgumentException, StudyPlanDoesNotExistException;

	/**
	 * Adds the course to the selected StudyPlan.
	 * @param studentID the student ID of the student, who's plan it should be added to.
	 * @param course The course to be added.
	 * @throws CourseAlreadyExistsException Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException Thrown if the course to be added have at least one matching skema data with a course on the same semester as the course to be added. 
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan related to studentID does not exist.
	 */
	public void addCourseToStudyPlan(SelectedCourse course) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException, StudyPlanDoesNotExistException;
	
	/**
	 * @see ui.Core#addCourseToStudyPlan(java.lang.String, dataClass.SelectedCourse)
	 * @param studentID the student ID of the student, who's plan it should be added to.
	 * @param course The course to be added.
	 * @param semester The semester the course should be added too.
	 * @throws CourseDoesNotExistException Thrown if no course had that course ID.
	 * @throws IllegalArgumentException Thrown if semester is less than 0 or greater than 20
	 * @throws CourseAlreadyExistsException Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException Thrown if the course to be added have at least one matching skema data with a course on the same semester as the course to be added. 
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan related to studentID does not exist.
	 * @throws CourseAlreadyExistsException 
	 */
	public void addCourseToStudyPlan(String studentID, String courseID, int semester) throws ConflictingCourseInStudyPlanException, CourseDoesNotExistException, CritalCourseDataMissingException, IllegalArgumentException, StudyPlanDoesNotExistException, CourseAlreadyExistsException;
	/**
	 * @see ui.Core#addCourseToStudyPlan(java.lang.String, dataClass.SelectedCourse)
	 * @param studentID the student ID of the student, who's plan it should be added to.
	 * @param course The course to be added.
	 * @param semester The semester the course should be added too.
	 * @throws IllegalArgumentException Thrown if semester is less than 0 or greater than 20
	 * @throws CourseAlreadyExistsException Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException Thrown if the course to be added have at least one matching skema data with a course on the same semester as the course to be added. 
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan related to studentID does not exist.
	 */
	public void addCourseToStudyPlan(String studentID, Course course, int semester) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException, IllegalArgumentException, StudyPlanDoesNotExistException;
	
	/**
	 * @param studentID the student ID of the student, who's plan it should be added to.
	 * @param course The course to be added.
	 * @throws CourseAlreadyExistsException Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException Thrown if the course to be added have at least one matching skema data with a course on the same semester as the course to be added. 
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan related to studentID does not exist.
	 */
	public void addCourseToStudyPlan(String studentID, SelectedCourse course) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException, StudyPlanDoesNotExistException;
	
	/**
	 * Get the current StudyPlan. There will always be a "current" StudyPlan, though it may be unnamed.
	 * @return the StudyPlan
	 */
	public StudyPlan getStudyPlan();
	/**
	 * Get the StudyPlan related to the studentID
	 * @param studentID the studentID related to the plan
	 * @return the StudyPlan
	 * @throws StudyPlanDoesNotExistException thrown if the StudyPlan does not exist.
	 */
	public StudyPlan getStudyPlan(String studentID) throws StudyPlanDoesNotExistException;

	/**	
	 * Get the StudyPlan related to the studentID
	 * @param studentID the studentID related to the plan
	 * @param createNewIfNotExists if true, the core will generate the StudyPlan rather than throwing an exception, if it does not exist.
	 * @return the StudyPlan
	 * @throws StudyPlanDoesNotExistException thrown if the createNewIfNotExists is false and the StudyPlan does not exist.
	 */
	public StudyPlan getStudyPlan(String studentID, boolean createNewIfNotExists) throws StudyPlanDoesNotExistException;
	
	/**
	 * @param studentID the unique identifier of the StudyPlan
	 * @param courseID the ID of the course to look for.
	 * @throws CourseDoesNotExistException Thrown if no such course exists (either in general or in the StudyPlan)
	 * @throws StudyPlanDoesNotExistException Thrown if no StudyPlan with that identifier existed.
	 */
	public void removeCourseFromStudyPlan(String studentID, String courseID) throws CourseDoesNotExistException, StudyPlanDoesNotExistException;
	/**
	 * @param studentID the unique identifier of the StudyPlan
	 * @param course to look for.
	 * @throws CourseDoesNotExistException Thrown if no such course existed in the StudyPlan
	 * @throws StudyPlanDoesNotExistException Thrown if no StudyPlan with that identifier existed.
	 */
	public void removeCourseFromStudyPlan(String studentID, Course course) throws CourseDoesNotExistException, StudyPlanDoesNotExistException;

	public void saveStudyPlanAs(String studentID, String newName) throws  CannotSaveStudyPlanException, FilePermissionException, StudyPlanDoesNotExistException;
	public void saveStudyPlanAs(StudyPlan plan, String newName)  throws CannotSaveStudyPlanException, FilePermissionException ;
	
	public void saveStudyPlan(String studentID) throws  CannotSaveStudyPlanException, FilePermissionException, StudyPlanDoesNotExistException;
	public void saveStudyPlan(StudyPlan plan)  throws CannotSaveStudyPlanException, FilePermissionException ;
	
	//public void saveAllOpenStudyPlans();
	
	/**
	 * Load a saved StudyPlan
	 * @param studentID the unique Identifier of the of the StudyPlan (or the filename of it without a .plan extension)
	 * @return the StudyPlan.
	 * @throws FilePermissionException Thrown if required File Permissions for the file was missing.
	 * @throws FileNotFoundException Thrown if no file could be found (determined from the studentID).
	 * @throws IOException If internal read errors happened.
	 * @throws CorruptStudyPlanFileException Thrown if the file could be found, but the data was not understandable.
	 */
	public StudyPlan loadStudyPlan(String studentID) throws FilePermissionException, FileNotFoundException, IOException, CorruptStudyPlanFileException;	
	
	
	/**
	 * @param studentID the student ID (or unique identifier) of the StudyPlan
	 * @param course the Course to check for.
	 * @return true if the Course is already in the plan.
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan did not exist
	 */
	public boolean isCourseInStudyPlan(String studentID, Course course) throws StudyPlanDoesNotExistException;
	/**
	 * @param studentID the student ID (or unique identifier) of the StudyPlan
	 * @param courseID the ID number of the Course to check for.
	 * @return true if the Course is already in the plan.
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan did not exist
	 */
	public boolean isCourseInStudyPlan(String studentID, String courseID) throws StudyPlanDoesNotExistException;
	
	/**
	 * @param courseID the Course ID to look up
	 * @return true if such a course exists and all mandatory data for it can be found.
	 */
	public boolean isValidCourse(String courseID);
	 
	
	/**
	 * Search through the Course data files for a pattern (this can be a regular expression).
	 * @param pattern searches for a pattern.
	 * @return returns an array of courses which (somehow) matches this pattern.
	 * @throws CourseDoesNotExistException thrown if no courses match the pattern.
	 */
	public Course[] search(String pattern) throws CourseDoesNotExistException;
	
}
