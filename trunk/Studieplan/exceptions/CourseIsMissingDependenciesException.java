/**
 * 
 */
package exceptions;

/**
 * @author Niels Thykier
 */
public class CourseIsMissingDependenciesException extends Exception {

	/**
	 * This is a serialized id used for the input/output streams
	 */
	private static final long serialVersionUID = 1264886153105068668L;

	/**
	 * The list of missing dependencies.
	 */
	private final String missing;
	
	/**
	 * @param missing The missing courses. 
	 */
	public CourseIsMissingDependenciesException(String missing) {
		super("*** Kurset kunne ikke tilføjes, følgende kurser kræves " + missing + " ***");
		this.missing = missing;
	}
	
	/**
	 * Get a list of the missing courses.
	 * @return The missing courses as a string.
	 */
	public String getMissingCourses() {
		return missing;
	}
	
}
