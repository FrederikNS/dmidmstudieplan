package exceptions;

/**
 * Exception thrown when a Course cannot be selected in a given semester.
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class CourseCannotStartInThisSemesterException extends Exception {
	
	/**
	 * This is a serialized id used for the input/output streams
	 */
	private static final long serialVersionUID = 6482213418111231627L;
	/**
	 * The ID of the course 
	 */
	private final String courseID;
	
	/**
	 * The semester that it could not start in.
	 */
	private final int semester;
	
	/**
	 * Create new instance of this exception.
	 * @param courseID The course in question.
	 * @param semester The semester related to this exception.
	 */
	public CourseCannotStartInThisSemesterException(String courseID, int semester) {
		super("*** Kursus " + courseID + " kan ikke starte i semesteret " + semester + "***");
		this.courseID = courseID;
		this.semester = semester;
	}

	/**
	 * Get the ID of the course that caused this exception.
	 * @return the courseID
	 */
	public String getCourseID() {
		return courseID;
	}

	/**
	 * Get the semester that the course could not start in.
	 * @return the semester
	 */
	public int getSemester() {
		return semester;
	}
}
