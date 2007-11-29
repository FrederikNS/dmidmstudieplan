package exceptions;

/**
 * Exception in case the studyplan does not exist
 * @author frederikns
 */
public class StudyPlanDoesNotExistException extends Exception {

	/**
	 * This is a serialized id used for the input/output streams
	 */
	private static final long serialVersionUID = 374198298635963990L;
	
	/**
	 * The student ID
	 */
	private String studentID;
	
	/**
	 * Prints the error message
	 * @param studentID The student ID
	 */
	public StudyPlanDoesNotExistException(String studentID) {
		super("*** Studieplanen for " + studentID + " eksisterer ikke ***");
		this.studentID = studentID;
	}
	
	/**
	 * Gets the student ID
	 * @return the studentID
	 */
	public String getStudentID() {
		return studentID;
	}

}