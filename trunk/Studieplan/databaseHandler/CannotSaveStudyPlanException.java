package databaseHandler;

import java.io.IOException;

import dataClass.StudyPlan;

/**
 * @author Niels Thykier
 *
 */
public class CannotSaveStudyPlanException extends IOException {

	private static final long serialVersionUID = 0L;

	private StudyPlan studyPlan;
	private String reason;
	
	public CannotSaveStudyPlanException(StudyPlan studyPlan, String reason) {
		super();
		this.studyPlan = studyPlan;
		this.reason = reason;
	}
	
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	
	/**
	 * @return the studyPlan
	 */
	public StudyPlan getStudyPlan() {
		return studyPlan;
	}

}
