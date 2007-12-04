package exceptions;

/**
 * This is a general exception extended by all the other exceptions to save code
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public abstract class CourseException extends Exception {
	
	/**
	 * This is the course ID
	 */
	private String courseID;
	
	/**
	 * This is the exception
	 * @param startOfMessage This is the first part of the error message
	 * @param courseID This is the Course ID
	 * @param endOfMessage This is the end of the error message
	 */
	protected CourseException(String startOfMessage, String courseID, String endOfMessage) {
		super(startOfMessage +" " + courseID + " " + endOfMessage); 
		this.courseID = courseID; 
	}
	
	/**
	 * Gets the course ID
	 * @return the course ID
	 */
	public String getCourseID() {
		return courseID;
	}

}
