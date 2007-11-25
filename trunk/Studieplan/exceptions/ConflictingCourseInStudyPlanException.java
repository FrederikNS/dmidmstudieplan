/**
 * 
 */
package exceptions;

/**
 * @author Niels Thykier
 *
 */
public class ConflictingCourseInStudyPlanException extends Exception {
	
	private static final long serialVersionUID = 6027116905595428749L;
	private String course1;
	private String course2;
	
	public ConflictingCourseInStudyPlanException(String course1, String course2) {
		this.course1 = course1;
		this.course2 = course2;
	}

	public String getCourse1() {
		return course1;
	}
	
	public String getCourse2() {
		return course2;
	}
	
	public String toString() {
		return "The course " + course1 + " shared the same skema on the same semester with " + course2;
	}
}
