/**
 * 
 */
package dataClass;

import ui.Core;

/**
 * @author Niels Thykier
 *
 */
public class ProgramCore implements Core {

	/* (non-Javadoc)
	 * @see ui.Core#addCourseToPlan(java.lang.String, int)
	 */
	public int addCourseToPlan(String courseID, int semester) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see ui.Core#findCourse(java.lang.String)
	 */
	public Course findCourse(String courseID)
			throws CourseDoesNotExistException {
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
	public StudyPlan loadStudyPlan(String studentID)
			throws StudyPlanDoesNotExistException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ui.Core#removeCourseFromPlan(java.lang.String, int)
	 */
	public int removeCourseFromPlan(String courseID, int semester) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see ui.Core#saveStudyPlan(java.lang.String)
	 */
	public boolean saveStudyPlan(String studentID) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see ui.Core#saveStudyPlan(dataClass.StudyPlan)
	 */
	public boolean saveStudyPlan(StudyPlan plan) {
		// TODO Auto-generated method stub
		return false;
	}

}
