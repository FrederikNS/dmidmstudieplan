/**
 * 
 */
package dataClass;

import ui.Core;
import exceptions.CourseDoesNotExistException;
import exceptions.StudyPlanDoesNotExistException;


/**
 * @author Niels Thykier
 *
 */
public class ProgramCore implements Core {

	/* (non-Javadoc)
	 * @see ui.Core#addCourseToPlan(java.lang.String, int)
	 */
	public void addCourseToPlan(String courseID, int semester) throws Exception {
		// TODO Auto-generated method stub
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
	 * @see ui.Core#removeCourseFromPlan(java.lang.String, int)
	 */
	public void removeCourseFromPlan(String courseID, int semester) throws Exception {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see ui.Core#saveStudyPlan(java.lang.String)
	 */
	public void saveStudyPlan(String studentID) throws Exception {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see ui.Core#saveStudyPlan(dataClass.StudyPlan)
	 */
	public void saveStudyPlan(StudyPlan plan) throws Exception{
		// TODO Auto-generated method stub
	}

}
