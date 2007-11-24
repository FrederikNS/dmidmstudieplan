/**
 * 
 */
package dataClass;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import databaseHandler.DatabaseReader;
import databaseHandler.UserDatabase;
import ui.Core;
import ui.Dialog;
import ui.UI;
import exceptions.CourseDoesNotExistException;
import exceptions.StudyPlanDoesNotExistException;


/**
 * @author Niels Thykier
 *
 */
public class ProgramCore implements Core {

	private UI ui;
	private DatabaseReader dbRead;
	private UserDatabase userDB;
	private ArrayList<StudyPlan> planList;
	
	
	public ProgramCore(String cmdLineArgs[]) {
		try {
			dbRead = new DatabaseReader();
		} catch (FileNotFoundException e) {
			System.err.println("Failed to initialize DatabaseReader.");
			System.err.println(e);
		}
		userDB = new UserDatabase();
		
		planList = new ArrayList<StudyPlan>();
		
		planList.add(new StudyPlan("temp"));
		
		ui = new Dialog(this);
		
		
		ui.start();
	}

	/* (non-Javadoc)
	 * @see ui.Core#findCourse(java.lang.String)
	 */
	public Course findCourse(String courseID) throws CourseDoesNotExistException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ui.Core#getAllCourses()
	 */
	public Course[] getAllCourses() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ui.Core#loadStudyPlan(java.lang.String)
	 */
	public StudyPlan loadStudyPlan(String studentID) throws StudyPlanDoesNotExistException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ui.Core#saveStudyPlan(java.lang.String)
	 */
	public void saveStudyPlan(String studentID) throws Exception {
		
	}

	/* (non-Javadoc)
	 * @see ui.Core#saveStudyPlan(dataClass.StudyPlan)
	 */
	public void saveStudyPlan(StudyPlan plan) throws Exception{
		userDB.saveStudyPlan(plan);
	}

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
	public Iterator<Course> getDatabaseReaderIterator() {
		return dbRead.iterator();
	}

	public void addCourseToStudyPlan(String studentID, String courseID, int semester) throws Exception {
		addCourseToStudyPlan(studentID, findCourse(courseID), semester);
	}
	
	public void addCourseToStudyPlan(String studentID, Course course, int semester) throws Exception {
		addCourseToStudyPlan(studentID, new SelectedCourse(course, semester));
		
	}

	public void addCourseToStudyPlan(String studentID, SelectedCourse course) throws Exception {
		StudyPlan sp = getStudyPlan(studentID);
		sp.add(course);
	}

	public void removeCourseFromStudyPlan(String studentID, Course course) throws Exception {
		removeCourseFromStudyPlan(studentID, course.getCourseID());		
	}
	public void removeCourseFromStudyPlan(String studentID, String courseID) throws Exception {
		
	}
	
	public StudyPlan getStudyPlan(String studentID) throws StudyPlanDoesNotExistException {
		StudyPlan sp;
		Iterator<StudyPlan> ilt = planList.iterator();
		while(ilt.hasNext()) {
			sp = ilt.next();
			if(sp.equals(studentID) ) {
				return sp;
			}
		}
		throw new StudyPlanDoesNotExistException(studentID);
	}

	public Course[] search(String pattern) throws CourseDoesNotExistException { 
		return dbRead.search(pattern);
	}
}
