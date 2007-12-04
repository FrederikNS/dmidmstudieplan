/**
 * 
 */
package cores;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

import dataClass.Course;
import dataClass.SelectedCourse;
import dataClass.StudyPlan;
import databases.CourseBase;
import databases.UserDatabase;
import ui.Dialog;
import ui.UI;
import exceptions.AnotherCourseDependsOnThisCourseException;
import exceptions.CannotSaveStudyPlanException;
import exceptions.ConflictingCourseInStudyPlanException;
import exceptions.CorruptStudyPlanFileException;
import exceptions.CourseAlreadyExistsException;
import exceptions.CourseCannotStartInThisSemesterException;
import exceptions.CourseDoesNotExistException;
import exceptions.CourseIsMissingDependenciesException;
import exceptions.FilePermissionException;
import exceptions.StudyPlanDoesNotExistException;

/**
 * This is our main class and links all the classes together.
 * 
 * The ProgramCore will start everything at Construction.
 * 
 * @author Niels Thykier
 * @author Frederik Nordahl Sabroe
 * @author Morten Sørensen
 */
public class ProgramCore implements Core {

	/**
	 * Field for holding the UI.
	 */
	private UI ui;

	/**
	 * Field for the CourseBase
	 */
	private CourseBase courseDB;

	/**
	 * Field for the UserDatabase, used to save and load StudyPlans to and from
	 * the HDD.
	 */
	private UserDatabase userDB;

	/**
	 * A list of StudyPlans loaded/created.
	 */
	private ArrayList<StudyPlan> planList;

	/**
	 * The "current" plan.
	 */
	private StudyPlan currentPlan;

	/**
	 * A safety switch to avoid starting multiple Cores.
	 */
	private boolean onlyOneCore = false;

	/**
	 * This will init the other classes and have the UI start its interaction.
	 * There can only be contructed one ProgramCore. Attempts to construct a
	 * second will cause the constructor to throw an exception.
	 * 
	 * This constructor will catch all possible Runtime Exceptions and any such
	 * will cause it to do System.exit(1);
	 * 
	 * If no Runtime Exception is thrown, the Constructor will call
	 * System.exit(0);
	 * 
	 * @param cmdLineArgs
	 *            the Commandline arguments (if any)
	 * @throws Exception
	 * @throws Exception
	 *             If a ProgramCore has already been started.
	 */
	public ProgramCore(String cmdLineArgs[]) throws Exception {
		if (onlyOneCore)
			throw new Exception();
		onlyOneCore = true;

		boolean makeUI = true, silent = false;
		PrintStream stdin = System.out;
		if (cmdLineArgs != null) {
			for (int i = 0; i < cmdLineArgs.length; i++) {
				if (cmdLineArgs[i].equalsIgnoreCase("--no-ui")) {
					makeUI = false;
				} else if (cmdLineArgs[i].equalsIgnoreCase("--silent")) {
					try {
						System.setOut(new PrintStream("/dev/null"));
						silent = true;
					} catch (Throwable e) {
						System.err
								.println("Cannot enter silent mode: Unable to redirect stdout");
					}
				} else if (cmdLineArgs[i].equalsIgnoreCase("--file")) {
					if (cmdLineArgs.length > i) {
						i++;
						InputStream in;
						try {
							in = new FileInputStream(cmdLineArgs[i]);
						} catch (Exception e) {
							System.err
									.println("Could not open the file for reading.");
							System.err.println(e);
							System.exit(2);
							return;
						}
						try {
							System.setIn(in);
						} catch (Throwable e) {
							System.err.println("Could not re-direct stdin.");
							System.err.println(e);
							System.exit(2);
						}
					}
				}
			}
		}

		try {
			try {
				courseDB = new CourseBase();
			} catch (FileNotFoundException e) {
				System.err
						.println("Failed to initialize CourseBase due to missing files.");
				System.err.println(e);
				e.printStackTrace(System.err);
				System.exit(1);
			} catch (FilePermissionException e) {
				System.err
						.println("Failed to initialize CourseBase due to file permissions.");
				System.err.println(e);
				e.printStackTrace(System.err);
				System.exit(1);
			}
			userDB = new UserDatabase();

			planList = new ArrayList<StudyPlan>();

			currentPlan = new StudyPlan("temp");
			try {
				planList.add(currentPlan);
			} catch (Exception e) {

			}

			System.out.println(courseDB.getStatisticalData());

			int returnCode = 0;
			if (makeUI) {
				ui = new Dialog(this);
				try {
					if (silent) {
						System.setOut(stdin);
					}
					ui.start();
				} catch (RuntimeException e) {
					System.err.println("Runtime Exception happened in the UI: "
							+ ui.getClass().getName());
					System.err.println(e);
					System.err.println("\n");
					e.printStackTrace(System.err);
					returnCode = 1;
					System.exit(returnCode);
				}
				ui = null;
				userDB = null;
				planList = null;
				currentPlan = null;
				courseDB = null;

				System.exit(returnCode);
			}

		} catch (RuntimeException e) {
			System.err
					.println("Runtime Exception happened in outside ui.start().");
			System.err.println(e);
			System.err.println("\n");
			e.printStackTrace(System.err);

			System.exit(1);
		}
		System.out
				.println("ProgramCore: Entering \"Daemon\" (for testing) mode");
		System.out
				.println("If only the core is the running, the program will terminate with an exit code 0.");
	}

	/**
	 * Look up a course by a course ID in the database.
	 * 
	 * @param courseID
	 *            the ID of the course to find
	 * @return the Course with that ID.
	 * @throws CourseDoesNotExistException
	 *             Thrown if no course has that ID.
	 */
	public Course findCourse(String courseID)
			throws CourseDoesNotExistException {
		return courseDB.findCourse(courseID);
	}

	/**
	 * Get the CourseBase, which contains a list of all courses.
	 * 
	 * @return The CourseBase
	 */
	public CourseBase getCourseBase() {
		return courseDB;
	}

	/**
	 * Load a saved StudyPlan
	 * 
	 * @param studentID
	 *            the unique Identifier of the of the StudyPlan (or the filename
	 *            of it without a .plan extension)
	 * @return the StudyPlan.
	 * @throws FilePermissionException
	 *             Thrown if required File Permissions for the file was missing.
	 * @throws FileNotFoundException
	 *             Thrown if no file could be found (determined from the
	 *             studentID).
	 * @throws IOException
	 *             If internal read errors happened.
	 * @throws CorruptStudyPlanFileException
	 *             Thrown if the file could be found, but the data was not
	 *             understandable.
	 */
	public StudyPlan loadStudyPlan(String studentID)
			throws FilePermissionException, FileNotFoundException, IOException,
			CorruptStudyPlanFileException {
		currentPlan = userDB.loadStudyPlan(studentID);
		return currentPlan;
	}

	/**
	 * Saves a StudyPlan
	 * 
	 * @param studentID
	 *            This is the students ID
	 * @throws CannotSaveStudyPlanException
	 *             Thrown if the studyplan could not be saved
	 * @throws FilePermissionException
	 *             Thrown if the user does not have the permissions required to
	 *             write to the file
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if the studyplan does not exist
	 */
	public void saveStudyPlan(String studentID)
			throws CannotSaveStudyPlanException, FilePermissionException,
			StudyPlanDoesNotExistException {
		saveStudyPlan(getStudyPlan(studentID));
	}

	/**
	 * Saves a StudyPlan under a different name
	 * 
	 * @param plan
	 *            This is the studyplan
	 * @throws CannotSaveStudyPlanException
	 *             Thrown if the studyplan could not be saved
	 * @throws FilePermissionException
	 *             Thrown if the user does not have the permissions required to
	 *             write to the file
	 */
	public void saveStudyPlan(StudyPlan plan)
			throws CannotSaveStudyPlanException, FilePermissionException {
		userDB.saveStudyPlan(plan);
	}

	/**
	 * Test if a course ID is a valid course.
	 * 
	 * @param courseID
	 *            the Course ID to look up
	 * @return true if such a course exists and all mandatory data for it can be
	 *         found.
	 */
	public boolean isValidCourse(String courseID) {
		boolean valid = false;
		try {
			findCourse(courseID);
			valid = true;
		} catch (Exception e) {
			// Course does not exist
		}
		return valid;
	}

	/**
	 * Adds a Course to a StudyPlan on a given semester
	 * 
	 * @see cores.Core#addCourseToStudyPlan(java.lang.String,
	 *      dataClass.SelectedCourse)
	 * @param studentID
	 *            the student ID of the student, who's plan it should be added
	 *            to.
	 * @param courseID
	 *            The ID of the course to be added.
	 * @param semester
	 *            The semester the course should be added too.
	 * @throws CourseDoesNotExistException
	 *             Thrown if no course had that course ID.
	 * @throws IllegalArgumentException
	 *             Thrown if semester is less than 0 or greater than 20
	 * @throws CourseAlreadyExistsException
	 *             Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException
	 *             Thrown if the course to be added have at least one matching
	 *             skema data with a course on the same semester as the course
	 *             to be added.
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if the StudyPlan related to studentID does not exist.
	 * @throws CourseIsMissingDependenciesException
	 *             if the course being added has unmet dependencies. Thrown if
	 *             the course being added has unmet dependencies.
	 * @throws CourseCannotStartInThisSemesterException
	 *             Thrown if the course being added cannot start in that
	 *             semester.
	 */
	public void addCourseToStudyPlan(String studentID, String courseID,
			int semester) throws ConflictingCourseInStudyPlanException,
			CourseDoesNotExistException, IllegalArgumentException,
			StudyPlanDoesNotExistException, CourseAlreadyExistsException,
			CourseIsMissingDependenciesException,
			CourseCannotStartInThisSemesterException {
		addCourseToStudyPlan(studentID, findCourse(courseID), semester);
	}

	/**
	 * Adds a Course to a StudyPlan on a given semester
	 * 
	 * @see cores.Core#addCourseToStudyPlan(java.lang.String,
	 *      dataClass.SelectedCourse)
	 * @param studentID
	 *            the student ID of the student, who's plan it should be added
	 *            to.
	 * @param course
	 *            The course to be added.
	 * @param semester
	 *            The semester the course should be added too.
	 * @throws IllegalArgumentException
	 *             Thrown if semester is less than 0 or greater than 20
	 * @throws CourseAlreadyExistsException
	 *             Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException
	 *             Thrown if the course to be added have at least one matching
	 *             skema data with a course on the same semester as the course
	 *             to be added.
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if the StudyPlan related to studentID does not exist.
	 * @throws CourseIsMissingDependenciesException
	 *             if the course being added has unmet dependencies. Thrown if
	 *             the course being added has unmet dependencies.
	 * @throws CourseCannotStartInThisSemesterException
	 *             Thrown if the course being added cannot start in that
	 *             semester.
	 */
	public void addCourseToStudyPlan(String studentID, Course course,
			int semester) throws CourseAlreadyExistsException,
			IllegalArgumentException, ConflictingCourseInStudyPlanException,
			StudyPlanDoesNotExistException,
			CourseIsMissingDependenciesException,
			CourseCannotStartInThisSemesterException {
		addCourseToStudyPlan(studentID, new SelectedCourse(course, semester));
	}

	/**
	 * Adds a SelectedCourse to a StudyPlan.
	 * 
	 * @param studentID
	 *            the student ID of the student, who's plan it should be added
	 *            to.
	 * @param course
	 *            The course to be added.
	 * @throws CourseAlreadyExistsException
	 *             Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException
	 *             Thrown if the course to be added have at least one matching
	 *             skema data with a course on the same semester as the course
	 *             to be added.
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if the StudyPlan related to studentID does not exist.
	 * @throws CourseIsMissingDependenciesException
	 *             Thrown if the course being added has unmet dependencies.
	 *             Thrown if the course being added has unmet dependencies.
	 * @throws CourseCannotStartInThisSemesterException
	 *             Thrown if the course being added cannot start in the semester
	 *             it wants to.
	 */
	public void addCourseToStudyPlan(String studentID, SelectedCourse course)
			throws CourseAlreadyExistsException,
			ConflictingCourseInStudyPlanException,
			StudyPlanDoesNotExistException,
			CourseIsMissingDependenciesException,
			CourseCannotStartInThisSemesterException {
		StudyPlan sp = getStudyPlan(studentID);
		sp.add(course);
	}

	/**
	 * Remove a course from a StudyPlan
	 * 
	 * @param studentID
	 *            the unique identifier of the StudyPlan
	 * @param course
	 *            to look for.
	 * @return true if the course was in the list.
	 * @throws CourseDoesNotExistException
	 *             Thrown if no such course existed in the StudyPlan
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if no StudyPlan with that identifier existed.
	 * @throws AnotherCourseDependsOnThisCourseException
	 *             Thrown if another course depends on the course that was to be
	 *             removed.
	 */
	public boolean removeCourseFromStudyPlan(String studentID, Course course)
			throws CourseDoesNotExistException, StudyPlanDoesNotExistException,
			AnotherCourseDependsOnThisCourseException {
		StudyPlan plan = getStudyPlan(studentID, false);
		if (!plan.remove(course)) {
			throw new CourseDoesNotExistException(course.getCourseID());
		}
		return true;
	}

	/**
	 * Remove a course from a StudyPlan
	 * 
	 * @param studentID
	 *            the unique identifier of the StudyPlan
	 * @param courseID
	 *            the ID of the course to look for.
	 * @return true if the course was in the list.
	 * @throws CourseDoesNotExistException
	 *             Thrown if no such course exists (either in general or in the
	 *             StudyPlan)
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if a StudyPlan with that studentID did not exist
	 * @throws AnotherCourseDependsOnThisCourseException
	 *             Thrown if another course depends on the course that was to be
	 *             removed.
	 * @throws IllegalArgumentException
	 *             Thrown if the ID of the course would cause the Course(String,
	 *             String) constructor to fail.
	 * @see dataClass.Course#Course(String, String)
	 */
	public boolean removeCourseFromStudyPlan(String studentID, String courseID)
			throws CourseDoesNotExistException, StudyPlanDoesNotExistException,
			IllegalArgumentException, AnotherCourseDependsOnThisCourseException {
		return removeCourseFromStudyPlan(studentID, new Course(courseID, " "));
	}

	/**
	 * Remove a course from the current StudyPlan
	 * 
	 * @param courseID
	 *            the ID of the course to look for.
	 * @return true if the course was in the list.
	 * @throws CourseDoesNotExistException
	 *             Thrown if no such course exists (either in general or in the
	 *             StudyPlan)
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if a StudyPlan with that studentID did not exist
	 * @throws AnotherCourseDependsOnThisCourseException
	 *             Thrown if another course depends on the course that was to be
	 *             removed.
	 * @throws IllegalArgumentException
	 *             Thrown if the ID of the course would cause the Course(String,
	 *             String) constructor to fail.
	 * @see dataClass.Course#Course(String, String)
	 */
	public boolean removeCourseFromStudyPlan(String courseID)
			throws CourseDoesNotExistException, StudyPlanDoesNotExistException,
			IllegalArgumentException, AnotherCourseDependsOnThisCourseException {
		return removeCourseFromStudyPlan(currentPlan.getStudent(), new Course(
				courseID, " "));
	}

	/**
	 * Remove a course from the current StudyPlan
	 * 
	 * @param course
	 *            to look for.
	 * @return true if the course was in the list.
	 * @throws CourseDoesNotExistException
	 *             Thrown if no such course existed in the StudyPlan
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if no StudyPlan with that identifier existed.
	 * @throws AnotherCourseDependsOnThisCourseException
	 *             Thrown if another course depends on the course that was to be
	 *             removed.
	 */
	public boolean removeCourseFromStudyPlan(Course course)
			throws CourseDoesNotExistException, StudyPlanDoesNotExistException,
			AnotherCourseDependsOnThisCourseException {
		return removeCourseFromStudyPlan(currentPlan.getStudent(), course);
	}

	/**
	 * Get the StudyPlan related to the studentID
	 * 
	 * @param studentID
	 *            the studentID related to the plan
	 * @return the StudyPlan
	 * @throws StudyPlanDoesNotExistException
	 *             thrown if the StudyPlan does not exist.
	 */
	public StudyPlan getStudyPlan(String studentID)
			throws StudyPlanDoesNotExistException {
		return getStudyPlan(studentID, false);
	}

	/**
	 * Get the StudyPlan related to the studentID
	 * 
	 * @param studentID
	 *            the studentID related to the plan
	 * @param createNewIfNotExists
	 *            if true, the core will generate the StudyPlan rather than
	 *            throwing an exception, if it does not exist.
	 * @return the StudyPlan
	 * @throws StudyPlanDoesNotExistException
	 *             thrown if the createNewIfNotExists is false and the StudyPlan
	 *             does not exist.
	 */
	public StudyPlan getStudyPlan(String studentID, boolean createNewIfNotExists)
			throws StudyPlanDoesNotExistException {
		StudyPlan sp;
		Iterator<StudyPlan> ilt = planList.iterator();
		while (ilt.hasNext()) {
			sp = ilt.next();
			if (sp.equals(new StudyPlan(studentID))) {
				return sp;
			}
		}
		if (!createNewIfNotExists)
			throw new StudyPlanDoesNotExistException(studentID);

		sp = new StudyPlan(studentID);
		planList.add(sp);
		return sp;
	}

	/**
	 * Search through the Course data files for a pattern (this can be a regular
	 * expression).
	 * 
	 * @param pattern
	 *            searches for a pattern.
	 * @return returns an array of courses which (somehow) matches this pattern.
	 * @throws CourseDoesNotExistException
	 *             thrown if no courses match the pattern.
	 */
	public Course[] search(String pattern) throws CourseDoesNotExistException {
		return courseDB.search(pattern);
	}

	/**
	 * Saves a StudyPlan under a different name
	 * 
	 * @param studentID
	 *            this is the student ID
	 * @param newName
	 *            this is the filename the user wants to save as
	 * @throws CannotSaveStudyPlanException
	 *             Thrown if the studyplan could not be saved
	 * @throws FilePermissionException
	 *             Thrown if the user does not have the permissions required to
	 *             write to the file
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if the studyplan does not exist
	 */
	public void saveStudyPlanAs(String studentID, String newName)
			throws CannotSaveStudyPlanException, FilePermissionException,
			StudyPlanDoesNotExistException {
		saveStudyPlanAs(getStudyPlan(studentID), newName);
	}

	/**
	 * Saves a StudyPlan under a different name
	 * 
	 * @param plan
	 *            This is the studyplan
	 * @param newName
	 *            This is the name of the file the use wants to save the
	 *            studyplan to
	 * @throws CannotSaveStudyPlanException
	 *             Thrown if the studyplan could not be saved
	 * @throws FilePermissionException
	 *             Thrown if the user does not have the permissions required to
	 *             write to the file
	 */
	public void saveStudyPlanAs(StudyPlan plan, String newName)
			throws CannotSaveStudyPlanException, FilePermissionException {
		plan.setStudent(newName);
		userDB.saveStudyPlan(plan);
	}

	/**
	 * Generate a new StudyPlan to work on.
	 * 
	 * @param studentID
	 *            the studentID of that Student.
	 * @return the new StudyPlan.
	 */
	public StudyPlan newStudyPlan(String studentID) {
		StudyPlan plan;
		try {
			plan = getStudyPlan(studentID, true);
		} catch (StudyPlanDoesNotExistException e) {
			plan = new StudyPlan(studentID);
			planList.add(plan);
		}
		return plan;
	}

	/**
	 * Check if a course has already been added to a StudyPlan
	 * 
	 * @param studentID
	 *            the student ID (or unique identifier) of the StudyPlan
	 * @param courseID
	 *            the ID number of the Course to check for.
	 * @return true if the Course is already in the plan.
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if the StudyPlan did not exist
	 */
	public boolean isCourseInStudyPlan(String studentID, String courseID)
			throws StudyPlanDoesNotExistException {
		StudyPlan sp = getStudyPlan(studentID);
		return sp.contains(courseID);
	}

	/**
	 * Check if a course has already been added to a StudyPlan
	 * 
	 * @param studentID
	 *            the student ID (or unique identifier) of the StudyPlan
	 * @param course
	 *            the Course to check for.
	 * @return true if the Course is already in the plan.
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if the StudyPlan did not exist
	 */
	public boolean isCourseInStudyPlan(String studentID, Course course)
			throws StudyPlanDoesNotExistException {
		return this.isCourseInStudyPlan(studentID, course.getCourseID());
	}

	/**
	 * Adds the Course to the current StudyPlan
	 * 
	 * @param courseID
	 *            The ID of the course to be added.
	 * @param semester
	 *            The semester the course should be added too.
	 * @throws CourseDoesNotExistException
	 *             Thrown if no course had that course ID.
	 * @throws IllegalArgumentException
	 *             Thrown if semester is less than 0 or greater than 20
	 * @throws CourseAlreadyExistsException
	 *             Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException
	 *             Thrown if the course to be added have at least one matching
	 *             skema data with a course on the same semester as the course
	 *             to be added.
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if the StudyPlan related to studentID does not exist.
	 * @throws CourseIsMissingDependenciesException
	 *             Thrown if the course being added has unmet dependencies.
	 * @throws CourseCannotStartInThisSemesterException
	 *             Thrown if the course being added cannot start in that
	 *             semester.
	 * @see cores.Core#addCourseToStudyPlan(java.lang.String,
	 *      dataClass.SelectedCourse)
	 */
	public void addCourseToStudyPlan(String courseID, int semester)
			throws ConflictingCourseInStudyPlanException,
			CourseDoesNotExistException, IllegalArgumentException,
			StudyPlanDoesNotExistException, CourseAlreadyExistsException,
			CourseIsMissingDependenciesException,
			CourseCannotStartInThisSemesterException {
		addCourseToStudyPlan(currentPlan.getStudent(), courseID, semester);
	}

	/**
	 * Adds the Course to the current StudyPlan
	 * 
	 * @param course
	 *            The course to be added.
	 * @param semester
	 *            The semester the course should be added too.
	 * @throws IllegalArgumentException
	 *             Thrown if semester is less than 0 or greater than 20
	 * @throws CourseAlreadyExistsException
	 *             Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException
	 *             Thrown if the course to be added have at least one matching
	 *             skema data with a course on the same semester as the course
	 *             to be added.
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if the StudyPlan related to studentID does not exist.
	 * @throws CourseIsMissingDependenciesException
	 *             Thrown if the course being added has unmet dependencies.
	 * @throws CourseCannotStartInThisSemesterException
	 *             Thrown if the course being added cannot start in that
	 *             semester.
	 * @see cores.Core#addCourseToStudyPlan(java.lang.String,
	 *      dataClass.SelectedCourse)
	 */
	public void addCourseToStudyPlan(Course course, int semester)
			throws CourseAlreadyExistsException,
			ConflictingCourseInStudyPlanException, IllegalArgumentException,
			StudyPlanDoesNotExistException,
			CourseIsMissingDependenciesException,
			CourseCannotStartInThisSemesterException {
		addCourseToStudyPlan(currentPlan.getStudent(), course, semester);
	}

	/**
	 * Adds the course to the current StudyPlan.
	 * 
	 * @param course
	 *            The course to be added.
	 * @throws CourseAlreadyExistsException
	 *             Thrown if the course is already in the StudyPlan.
	 * @throws ConflictingCourseInStudyPlanException
	 *             Thrown if the course to be added have at least one matching
	 *             skema data with a course on the same semester as the course
	 *             to be added.
	 * @throws StudyPlanDoesNotExistException
	 *             Thrown if the StudyPlan related to studentID does not exist.
	 * @throws CourseIsMissingDependenciesException
	 * @throws CourseCannotStartInThisSemesterException
	 *             Thrown if the course being added cannot start in the semester
	 *             it wants to.
	 */
	public void addCourseToStudyPlan(SelectedCourse course)
			throws CourseAlreadyExistsException,
			ConflictingCourseInStudyPlanException,
			StudyPlanDoesNotExistException,
			CourseIsMissingDependenciesException,
			CourseCannotStartInThisSemesterException {
		addCourseToStudyPlan(currentPlan.getStudent(), course);
	}

	/**
	 * Get the current StudyPlan. There will always be a "current" StudyPlan,
	 * though it may be unnamed.
	 * 
	 * @return the StudyPlan
	 */
	public StudyPlan getStudyPlan() {
		return currentPlan;
	}

	/**
	 * Saves the current StudyPlan under a different name
	 * 
	 * @param newName
	 *            This is the name of the file the use wants to save the
	 *            studyplan to
	 * @throws CannotSaveStudyPlanException
	 *             Thrown if the studyplan could not be saved
	 * @throws FilePermissionException
	 *             Thrown if the user does not have the permissions required to
	 *             write to the file
	 */
	public void saveStudyPlanAs(String newName)
			throws CannotSaveStudyPlanException, FilePermissionException {
		saveStudyPlanAs(currentPlan, newName);
	}
}