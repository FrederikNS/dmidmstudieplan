/**
 * 
 */
package exceptions;

/**
 * @author Niels Thykier
 * Thrown whenever the applications attempts to look up a course that does not exist in the database.
 * This is likely due to a malformatted course number or the database is not up to date.
 */
public class CourseDoesNotExistException extends Exception {

	private static final long serialVersionUID = 2980651101109614877L;

	/**
	 * The course ID that did not exist
	 */
	private String courseDesciption;
	
	/**
	 * @param courseID the ID of the course that did not exist
	 */
	public CourseDoesNotExistException(String courseDesciption) {
		this.courseDesciption = courseDesciption;
	}
	
	public String toString() {
		return "Course " + courseDesciption + " could not be found";
	}
}
