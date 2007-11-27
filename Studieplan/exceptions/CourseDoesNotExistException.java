/**
 * 
 */
package exceptions;

/**
 * Thrown whenever the applications attempts to look up a course that does not exist in the database.
 * This is likely due to a malformatted course number or the database is not up to date.
 * @author Niels Thykier
 */
public class CourseDoesNotExistException extends CourseException {

	private static final long serialVersionUID = 2980651101109614877L;
	
	/**
	 * @param courseID the ID of the course that did not exist
	 */
	public CourseDoesNotExistException(String courseDescription) {
		super("*** Kursus", courseDescription, "blev ikke fundet ***");
	}
	
}
