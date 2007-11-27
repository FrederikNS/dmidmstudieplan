/**
 * 
 */
package exceptions;

/**
 * @author Niels Thykier
 *
 */
public class CourseAlreadyExistsException extends CourseException {

	private static final long serialVersionUID = -1366474386574273339L;
	
	public CourseAlreadyExistsException(String courseID) {
		super("*** Kurset",courseID, "findes allerede i studieplanen. ***");
	}

}
