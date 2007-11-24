/**
 * 
 */
package exceptions;

/**
 * @author Niels Thykier
 *
 */
public class CourseAlreadyExistsException extends CourseDoesNotExistException {

	private static final long serialVersionUID = -1366474386574273339L;
	
	public CourseAlreadyExistsException(String courseID) {
		super(courseID);
	}

	public String toString() {
		return "The Course " + getCourseDescription() + " already exists";
	}
	
}
