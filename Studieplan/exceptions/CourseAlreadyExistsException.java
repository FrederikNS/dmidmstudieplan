/**
 * 
 */
package exceptions;

/**
 * Thrown if the course does already exist
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class CourseAlreadyExistsException extends CourseException {

	/**
	 * This is a serialized id used for the input/output streams
	 */
	private static final long serialVersionUID = -1366474386574273339L;
	
	/**
	 * 
	 * @param courseID
	 */
	public CourseAlreadyExistsException(String courseID) {
		super("*** Kurset",courseID, "findes allerede i studieplanen. ***");
	}

}
