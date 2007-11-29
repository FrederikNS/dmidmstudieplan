package exceptions;

import java.io.IOException;

import dataClass.StudyPlan;

/**
 * @author Niels Thykier
 *
 */
public class CannotSaveStudyPlanException extends IOException {

	private static final long serialVersionUID = 2781269550038892353L;
	
	/**
	 * this is the studyplan
	 */
	private StudyPlan studyPlan;
	/**
	 * this is the 
	 */
	private String reason;
	
	public CannotSaveStudyPlanException(StudyPlan studyPlan, String reason) {
		super("Could not save study plan: " + studyPlan + ": " + reason);
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
