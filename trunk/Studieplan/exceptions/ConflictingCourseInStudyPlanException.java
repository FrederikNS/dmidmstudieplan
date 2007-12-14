package exceptions;

/**
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class ConflictingCourseInStudyPlanException extends Exception {
	
	/**
	 * This is a serialized id used for the input/output streams
	 */
	private static final long serialVersionUID = 6027116905595428749L;
	
	/**
	 * The first of the conflicting courses
	 */
	private String course1;
	
	/**
	 * The second of the conflicting courses
	 */
	private String course2;
	
	/**
	 * Prints the error to the terminal
	 * @param course1 The first of the conflicting courses
	 * @param course2 The second of the conflicting courses
	 */
	public ConflictingCourseInStudyPlanException(String course1, String course2) {
		super("*** Kurset " + course1 + " ligger i samme skemaperiode som " + course2 + " ***");
		this.course1 = course1;
		this.course2 = course2;
	}

	/**
	 * Gets the first course
	 * @return The first of the conflicting courses
	 */
	public String getCourse1() {
		return course1;
	}
	
	/**
	 * Gets the second course
	 * @return The second of the conflicting courses
	 */
	public String getCourse2() {
		return course2;
	}
}