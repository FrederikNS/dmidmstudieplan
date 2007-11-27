package exceptions;

public abstract class CourseException extends Exception {
	
	private String courseID;
	
	protected CourseException(String startOfMessage, String courseID, String endOfMessage) {
		super(startOfMessage +" " + courseID + " " + endOfMessage); 
		this.courseID = courseID; 
	}
	
	public String getCourseID() {
		return courseID;
	}

}
