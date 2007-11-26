package exceptions;

public class StudyPlanDoesNotExistException extends Exception {

	private static final long serialVersionUID = 374198298635963990L;
	private String studentID;
	
	public StudyPlanDoesNotExistException(String studentID) {
		super("The plan for " + studentID + " did not exist");
		this.studentID = studentID;
	}
	
	public String getStudentID() {
		return studentID;
	}

}
