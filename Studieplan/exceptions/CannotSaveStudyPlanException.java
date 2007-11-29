package exceptions;

import java.io.IOException;

import dataClass.StudyPlan;

/**
 * Thrown if the studyplan cannot be saved
 * @author Niels Thykier
 */
public class CannotSaveStudyPlanException extends IOException {

	/**
	 * This is a serialized id used for the input/output streams
	 */
	private static final long serialVersionUID = 2781269550038892353L;
	
	/**
	 * This is the studyplan
	 */
	private StudyPlan studyPlan;
	/**
	 * This is the reason why the studyplan could not be saved
	 */
	private String reason;
	
	/**
	 * Prints the error
	 * @param studyPlan
	 * @param reason
	 */
	public CannotSaveStudyPlanException(StudyPlan studyPlan, String reason) {
		super("Could not save study plan: " + studyPlan + ": " + reason);
		this.studyPlan = studyPlan;
		this.reason = reason;
	}
	
	/**
	 * Gets the reason
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	
	/**
	 * Gets the studyplan
	 * @return the studyPlan
	 */
	public StudyPlan getStudyPlan() {
		return studyPlan;
	}

}
