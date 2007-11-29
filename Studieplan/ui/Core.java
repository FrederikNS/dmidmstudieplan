package ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import dataClass.Course;
import dataClass.SelectedCourse;
import exceptions.CannotSaveStudyPlanException;
import exceptions.ConflictingCourseInStudyPlanException;
import exceptions.CorruptStudyPlanFileException;
import exceptions.CourseAlreadyExistsException;
import exceptions.CourseDoesNotExistException;
import exceptions.FilePermissionException;
import dataClass.StudyPlan;
import databases.CourseBase;
import exceptions.StudyPlanDoesNotExistException;


/**
 * This is an interface which acts as a middleman for the outer parts of the program and the core of the program
 * @author
 */
public interface Core {
	
	/**
	 * Look up a course by a course ID in the database.
	 * @param courseID the ID of the course to find
	 * @return the Course with that ID.
	 * @throws CourseDoesNotExistException Thrown if no course has that ID.
	 */
	public Course findCourse(String courseID) throws CourseDoesNotExistException;
	
	/**
	 * Get the CourseBase, which contains a list of all courses. 
	 * @return The CourseBase
	 */
	public CourseBase getCourseBase();
	
	/**
	 * Generate a new StudyPlan to work on.
	 * @param studentID the studentID of that Student.
	 * @return the new StudyPlan.
	 */
	public StudyPlan newStudyPlan(String studentID);

	/**
	 * Adds the Course to the current StudyPlan
	 * @param courseID The ID of the course to be added.
	 * @param semester The semester the course should be added too.
	 * @throws CourseDoesNotExistException Thrown if no course had that course ID.
	 * @throws IllegalArgumentException Thrown if semester is less than 0 or greater than 20
	 * @throws CourseAlreadyExistsException Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException Thrown if the course to be added have at least one matching skema data with a course on the same semester as the course to be added. 
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan related to studentID does not exist.
	 * @see ui.Core#addCourseToStudyPlan(java.lang.String, dataClass.SelectedCourse)
	 */	
	public void addCourseToStudyPlan(String courseID, int semester) throws ConflictingCourseInStudyPlanException, CourseDoesNotExistException, IllegalArgumentException, StudyPlanDoesNotExistException, CourseAlreadyExistsException;
	/**
	 * Adds the Course to the current StudyPlan
	 * @param course The course to be added.
	 * @param semester The semester the course should be added too.
	 * @throws IllegalArgumentException Thrown if semester is less than 0 or greater than 20
	 * @throws CourseAlreadyExistsException Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException Thrown if the course to be added have at least one matching skema data with a course on the same semester as the course to be added. 
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan related to studentID does not exist.
	 * @see ui.Core#addCourseToStudyPlan(java.lang.String, dataClass.SelectedCourse)
	 */	
	public void addCourseToStudyPlan(Course course, int semester) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException, IllegalArgumentException, StudyPlanDoesNotExistException;

	/**
	 * Adds the course to the current StudyPlan.
	 * @param course The course to be added.
	 * @throws CourseAlreadyExistsException Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException Thrown if the course to be added have at least one matching skema data with a course on the same semester as the course to be added. 
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan related to studentID does not exist.
	 */
	public void addCourseToStudyPlan(SelectedCourse course) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException, StudyPlanDoesNotExistException;
	
	/**
	 * Adds a Course to a StudyPlan on a given semester
	 * @see ui.Core#addCourseToStudyPlan(java.lang.String, dataClass.SelectedCourse)
	 * @param studentID the student ID of the student, who's plan it should be added to.
	 * @param courseID The ID of the course to be added.
	 * @param semester The semester the course should be added too.
	 * @throws CourseDoesNotExistException Thrown if no course had that course ID.
	 * @throws IllegalArgumentException Thrown if semester is less than 0 or greater than 20
	 * @throws CourseAlreadyExistsException Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException Thrown if the course to be added have at least one matching skema data with a course on the same semester as the course to be added.  
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan related to studentID does not exist.
	 */
	public void addCourseToStudyPlan(String studentID, String courseID, int semester) throws ConflictingCourseInStudyPlanException, CourseDoesNotExistException, IllegalArgumentException, StudyPlanDoesNotExistException, CourseAlreadyExistsException;

	/**
	 * Adds a Course to a StudyPlan on a given semester
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
	 * Adds a SelectedCourse to a StudyPlan.
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
	 * Remove a course from a StudyPlan
	 * @param studentID the unique identifier of the StudyPlan
	 * @param courseID the ID of the course to look for.
	 * @throws CourseDoesNotExistException Thrown if no such course exists (either in general or in the StudyPlan)
	 * @throws StudyPlanDoesNotExistException Thrown if no StudyPlan with that identifier existed.
	 */
	public void removeCourseFromStudyPlan(String studentID, String courseID) throws CourseDoesNotExistException, StudyPlanDoesNotExistException;
	
	/**
	 * Remove a course from a StudyPlan
	 * @param studentID the unique identifier of the StudyPlan
	 * @param course to look for.
	 * @throws CourseDoesNotExistException Thrown if no such course existed in the StudyPlan
	 * @throws StudyPlanDoesNotExistException Thrown if no StudyPlan with that identifier existed.
	 */
	public void removeCourseFromStudyPlan(String studentID, Course course) throws CourseDoesNotExistException, StudyPlanDoesNotExistException;

	/**
	 * Saves a StudyPlan under a different name
	 * @param studentID this is the student ID
	 * @param newName this is the filename the user wants to save as
	 * @throws CannotSaveStudyPlanException Thrown if the studyplan could not be saved
	 * @throws FilePermissionException Thrown if the user does not have the permissions required to write to the file
	 * @throws StudyPlanDoesNotExistException Thrown if the studyplan does not exist
	 */
	public void saveStudyPlanAs(String studentID, String newName) throws  CannotSaveStudyPlanException, FilePermissionException, StudyPlanDoesNotExistException;
	
	/**
	 * Saves a StudyPlan under a different name
	 * @param plan This is the studyplan
	 * @param newName This is the name of the file the use wants to save the studyplan to
	 * @throws CannotSaveStudyPlanException Thrown if the studyplan could not be saved
	 * @throws FilePermissionException Thrown if the user does not have the permissions required to write to the file
	 */
	public void saveStudyPlanAs(StudyPlan plan, String newName)  throws CannotSaveStudyPlanException, FilePermissionException ;
	
	/**
	 * Saves a StudyPlan
	 * @param studentID This is the students ID
	 * @throws CannotSaveStudyPlanException Thrown if the studyplan could not be saved
	 * @throws FilePermissionException Thrown if the user does not have the permissions required to write to the file
	 * @throws StudyPlanDoesNotExistException Thrown if the studyplan does not exist
	 */
	public void saveStudyPlan(String studentID) throws  CannotSaveStudyPlanException, FilePermissionException, StudyPlanDoesNotExistException;
	
	/**
	 * Saves a StudyPlan
	 * @param plan This is the Studyplan
	 * @throws CannotSaveStudyPlanException Thrown if the studyplan could not be saved
	 * @throws FilePermissionException Thrown if the user does not have the permissions required to write to the file
	 */
	public void saveStudyPlan(StudyPlan plan)  throws CannotSaveStudyPlanException, FilePermissionException ;
	
	
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
	 * Check if a course has already been added to a StudyPlan
	 * @param studentID the student ID (or unique identifier) of the StudyPlan
	 * @param course the Course to check for.
	 * @return true if the Course is already in the plan.
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan did not exist
	 */
	public boolean isCourseInStudyPlan(String studentID, Course course) throws StudyPlanDoesNotExistException;
	
	/**
	 * Check if a course has already been added to a StudyPlan
	 * @param studentID the student ID (or unique identifier) of the StudyPlan
	 * @param courseID the ID number of the Course to check for.
	 * @return true if the Course is already in the plan.
	 * @throws StudyPlanDoesNotExistException Thrown if the StudyPlan did not exist
	 */
	public boolean isCourseInStudyPlan(String studentID, String courseID) throws StudyPlanDoesNotExistException;
	
	/**
	 * Test if a course ID is a valid course.
	 * @param courseID the Course ID to look up
	 * @return true if such a course exists and all mandatory data for it can be found.
	 */
	public boolean isValidCourse(String courseID);
	 
	
	/**
	 * Search through the Course data files for a pattern (this can be a regular expression).
	 * @param pattern searches for a pattern.
	 * @return returns an array of courses which (somehow) matches this pattern.
	 * @throws CourseDoesNotExistException thrown if no courses match the pattern.
	 * @deprecated It is slow, not required and does not use the CourseBase.
	 */
	public Course[] search(String pattern) throws CourseDoesNotExistException;
	
}