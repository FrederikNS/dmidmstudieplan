package exceptions;

/**
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class AnotherCourseDependsOnThisCourseException extends Exception {
	/**
	 * This is a serialized id used for the input/output streams
	 */
	private static final long serialVersionUID = 1264886153105068668L;

	/**
	 * The list of missing dependencies.
	 */
	private final String depends;
	/**
	 * The ID of the course in question
	 */
	private final String courseID;
	
	/**
	 * @param courseID The ID of the course
	 * @param depends The depending courses. 
	 */
	public AnotherCourseDependsOnThisCourseException(String courseID, String depends) {
		super("*** Kurset, "+ courseID +" kunne ikke fjernes, følgende kurser kræver dette " + depends + " ***");
		this.depends = depends;
		this.courseID = courseID;
	}
	
	/**
	 * Get a list of the missing courses.
	 * @return The missing courses as a string.
	 */
	public String getDependingCourses() {
		return depends;
	}
	
	/**
	 * Get the ID of the course, that was the reason for this exception.
	 * @return The ID of the course that could not be removed.
	 */
	public String getCourseID() {
		return courseID;
	}	
}