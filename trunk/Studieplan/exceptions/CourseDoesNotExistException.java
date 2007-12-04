/**
 * 
 */
package exceptions;

/**
 * Thrown whenever the applications attempts to look up a course that does not exist in the database.
 * This is likely due to a malformatted course number or the database is not up to date.
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class CourseDoesNotExistException extends CourseException {

	/**
	 * This is a serialized id used for the input/output streams
	 */
	private static final long serialVersionUID = 2980651101109614877L;
	
	/**
	 * Prints the error message
	 * @param courseDescription the description or the ID of the course that could not be found 
	 */
	public CourseDoesNotExistException(String courseDescription) {
		super("*** Kursus", courseDescription, "blev ikke fundet ***");
	}
}
