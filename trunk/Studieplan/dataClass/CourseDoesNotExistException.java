/**
 * 
 */
package dataClass;

/**
 * @author Niels Thykier
 *
 */
public class CourseDoesNotExistException extends Exception {

	private static final long serialVersionUID = 2980651101109614877L;

	private String courseID;
	
	public CourseDoesNotExistException(String ID) {
		courseID = ID;
	}
	
	public String toString() {
		return "Error: Course with ID " + courseID + " did not exist";
	}
}
