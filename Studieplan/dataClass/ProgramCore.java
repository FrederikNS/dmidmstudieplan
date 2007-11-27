/**
 * 
 */
package dataClass;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import databases.CourseBase;
import databases.UserDatabase;
import ui.Core;
import ui.Dialog;
import ui.UI;
import exceptions.CannotSaveStudyPlanException;
import exceptions.ConflictingCourseInStudyPlanException;
import exceptions.CorruptStudyPlanFileException;
import exceptions.CourseAlreadyExistsException;
import exceptions.CourseDoesNotExistException;
import exceptions.CritalCourseDataMissingException;
import exceptions.FilePermissionException;
import exceptions.StudyPlanDoesNotExistException;


/**
 * @author Niels Thykier
 * This is our main class and links all the classes together.
 * 
 * The ProgramCore will start everything at Construction. 
 */
public class ProgramCore implements Core {

	private UI ui;
	private CourseBase courseDB;
	private UserDatabase userDB;
	private ArrayList<StudyPlan> planList;
	private StudyPlan currentPlan;
	private boolean onlyOneCore = false;
	
	
	/**
	 * This will init the other classes and have the UI start its interaction.
	 * There can only be contructed one ProgramCore. Attempts to construct a second will cause
	 * the constructor to throw an exception.
	 * 
	 * This constructor will catch all possible Runtime Exceptions and any such will cause it to
	 * do System.exit(1);
	 * 
	 *  If no Runtime Exception is thrown, the Constructor will call System.exit(0); 
	 * 
	 * @param cmdLineArgs the Commandline arguments (if any)
	 * @throws Exception If a ProgramCore has already been started. 
	 */
	public ProgramCore(String cmdLineArgs[]) throws Exception  {
		if(onlyOneCore)
			throw new Exception();
		onlyOneCore = true;
		
		try {
			try {
				courseDB = new CourseBase();
				courseDB.reloadDatabase();
			} catch (Exception e) {
				System.err.println("Failed to initialize CourseBase.");
				System.err.println(e);
				e.printStackTrace(System.err);
				System.exit(1);
			}
			userDB = new UserDatabase();
			
			planList = new ArrayList<StudyPlan>();
			
			currentPlan = new StudyPlan("temp");
			
			planList.add(currentPlan);
			
			
			ui = new Dialog(this);
			int returnCode = 0;
			try {
				ui.start();
			} catch(RuntimeException e) {
				System.err.println("Runtime Exception happened in the UI: " + ui.getClass().getName() );
				System.err.println(e);
				System.err.println("\n");
				e.printStackTrace(System.err);
				returnCode = 1;
				System.exit(1);
			}
			
			ui = null;
			userDB = null;
			planList = null;
			currentPlan = null;
			courseDB = null;
			
			System.exit(returnCode);
			
		}catch(RuntimeException e) {
			System.err.println("Runtime Exception happened in outside ui.start().");
			System.err.println(e);
			System.err.println("\n");
			e.printStackTrace(System.err);
			
			System.exit(1);
		}
	}

	/* (non-Javadoc)
	 * @see ui.Core#findCourse(java.lang.String)
	 */
	public Course findCourse(String courseID) throws CourseDoesNotExistException {
		return courseDB.findCourse(courseID);
	}

	/* (non-Javadoc)
	 * @see ui.Core#getAllCourses()
	 */
	public Course[] getAllCourses() {
		return courseDB.getAllCourses();
	}

	/* (non-Javadoc)
	 * @see ui.Core#loadStudyPlan(java.lang.String)
	 */
	public StudyPlan loadStudyPlan(String studentID) throws FilePermissionException, FileNotFoundException, IOException, CorruptStudyPlanFileException {
		return userDB.loadStudyPlan(studentID);
	}

	/* (non-Javadoc)
	 * @see ui.Core#saveStudyPlan(java.lang.String)
	 */
	public void saveStudyPlan(String studentID) throws CannotSaveStudyPlanException, FilePermissionException, StudyPlanDoesNotExistException {
		saveStudyPlan(getStudyPlan(studentID));
	}

	/* (non-Javadoc)
	 * @see ui.Core#saveStudyPlan(dataClass.StudyPlan)
	 */
	public void saveStudyPlan(StudyPlan plan) throws CannotSaveStudyPlanException, FilePermissionException {
		userDB.saveStudyPlan(plan);
	}

	/* (non-Javadoc)
	 * @see ui.Core#isValidCourse(java.lang.String)
	 */
	public boolean isValidCourse(String courseID) {
		boolean valid = false;
		try {
			findCourse(courseID);
			valid = true;
		}catch(Exception e) {
			//Course does not exist
		}
		return valid;
	}
	
	/* (non-Javadoc)
	 * @see ui.Core#getDatabaseReaderIterator()
	 */
	/*public Iterator<Course> getCourseDatabaseIterator() {
		return dbRead.iterator();
	}*/

	/* (non-Javadoc)
	 * @see ui.Core#addCourseToStudyPlan(java.lang.String, java.lang.String, int)
	 */
	public void addCourseToStudyPlan(String studentID, String courseID, int semester) throws ConflictingCourseInStudyPlanException, CourseDoesNotExistException, CritalCourseDataMissingException, IllegalArgumentException, StudyPlanDoesNotExistException {
		addCourseToStudyPlan(studentID, findCourse(courseID), semester);
	}
	
	/* (non-Javadoc)
	 * @see ui.Core#addCourseToStudyPlan(java.lang.String, dataClass.Course, int)
	 */
	public void addCourseToStudyPlan(String studentID, Course course, int semester) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException, IllegalArgumentException, StudyPlanDoesNotExistException {
		addCourseToStudyPlan(studentID, new SelectedCourse(course, semester));		
	}

	/* (non-Javadoc)
	 * @see ui.Core#addCourseToStudyPlan(java.lang.String, dataClass.SelectedCourse)
	 */
	public void addCourseToStudyPlan(String studentID, SelectedCourse course) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException, StudyPlanDoesNotExistException {
		StudyPlan sp = getStudyPlan(studentID);
		sp.add(course);
	}

	/* (non-Javadoc)
	 * @see ui.Core#removeCourseFromStudyPlan(java.lang.String, dataClass.Course)
	 */
	public void removeCourseFromStudyPlan(String studentID, Course course) throws CourseDoesNotExistException, StudyPlanDoesNotExistException  {
		removeCourseFromStudyPlan(studentID, course.getCourseID());		
	}
	/* (non-Javadoc)
	 * @see ui.Core#removeCourseFromStudyPlan(java.lang.String, java.lang.String)
	 */
	public void removeCourseFromStudyPlan(String studentID, String courseID) throws CourseDoesNotExistException, StudyPlanDoesNotExistException {
		StudyPlan plan = getStudyPlan(studentID, false);
		plan.remove(courseID);
	}

	/* (non-Javadoc)
	 * @see ui.Core#getStudyPlan(java.lang.String)
	 */
	public StudyPlan getStudyPlan(String studentID) throws StudyPlanDoesNotExistException {
		return getStudyPlan(studentID, false);
	}
	
	/* (non-Javadoc)
	 * @see ui.Core#getStudyPlan(java.lang.String, boolean)
	 */
	public StudyPlan getStudyPlan(String studentID, boolean createNewIfNotExists) throws StudyPlanDoesNotExistException {
		StudyPlan sp;
		Iterator<StudyPlan> ilt = planList.iterator();
		while(ilt.hasNext()) {
			sp = ilt.next();
			if(sp.equals(studentID) ) {
				return sp;
			}
		}
		if(!createNewIfNotExists)
			throw new StudyPlanDoesNotExistException(studentID);
		
		sp = new StudyPlan(studentID);
		planList.add(sp);
		return sp;
	}

	/* (non-Javadoc)
	 * @see ui.Core#search(java.lang.String)
	 */
	public Course[] search(String pattern) throws CourseDoesNotExistException { 
		return courseDB.search(pattern);
	}

	/* (non-Javadoc)
	 * @see ui.Core#saveStudyPlanAs(java.lang.String, java.lang.String)
	 */
	public void saveStudyPlanAs(String studentID, String newName) throws CannotSaveStudyPlanException, FilePermissionException, StudyPlanDoesNotExistException {
		saveStudyPlanAs(getStudyPlan(studentID), newName);		
	}

	/* (non-Javadoc)
	 * @see ui.Core#saveStudyPlanAs(dataClass.StudyPlan, java.lang.String)
	 */
	public void saveStudyPlanAs(StudyPlan plan, String newName) throws CannotSaveStudyPlanException, FilePermissionException {
		plan.setStudent(newName);
		userDB.saveStudyPlan(plan);
	}

	/* (non-Javadoc)
	 * @see ui.Core#newStudyPlan(java.lang.String)
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
	
	/* (non-Javadoc)
	 * @see ui.Core#isCourseInStudyPlan(java.lang.String, java.lang.String)
	 */
	public boolean isCourseInStudyPlan(String studentID, String courseID) throws StudyPlanDoesNotExistException {
		StudyPlan sp = getStudyPlan(studentID);
		return sp.contains(courseID);
	}
	
	/* (non-Javadoc)
	 * @see ui.Core#isCourseInStudyPlan(java.lang.String, dataClass.SelectedCourse)
	 */
	public boolean isCourseInStudyPlan(String studentID, Course course) throws StudyPlanDoesNotExistException {
		return this.isCourseInStudyPlan(studentID, course.getCourseID());
	}

	public void addCourseToStudyPlan(String courseID, int semester) throws ConflictingCourseInStudyPlanException, CourseDoesNotExistException, CritalCourseDataMissingException, IllegalArgumentException, StudyPlanDoesNotExistException {
		addCourseToStudyPlan("temp", courseID, semester);
	}

	public void addCourseToStudyPlan(Course course, int semester) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException, IllegalArgumentException, StudyPlanDoesNotExistException {
		addCourseToStudyPlan("temp", course, semester);
	}

	public void addCourseToStudyPlan(SelectedCourse course) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException, StudyPlanDoesNotExistException {
		addCourseToStudyPlan("temp", course);
	}

	public StudyPlan getStudyPlan() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
