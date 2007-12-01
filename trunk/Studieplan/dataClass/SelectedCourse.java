/**
 * 
 */
package dataClass;

import java.io.Serializable;

/**
 * @author Niels Thykier
 * This class is a course selected by a student. On top of all the information
 * in the Course class, this also contains the data on which semester this course is 
 * taken.
 */
public class SelectedCourse extends Course implements Comparable<SelectedCourse>, Serializable {

	/**
	 * serialVersionUID needed so that this class can be Serializable. 
	 */
	private static final long serialVersionUID = -2357834380866036780L;
	
	/**
	 * The semester this course is taken on.
	 */
	private int semester = 0;

	/**
	 * Create a SelectedCourse from a courseID, courseName and a semester.
	 * NB: This will NOT look up all the data from the databases.
	 * 
	 * Use the findCourse from Core or DatabaseReader and the {@link #SelectedCourse(Course, int)} Constructor
	 * for that.
	 * 
	 * @param courseID The ID number of the course.
	 * @param courseName The name of the Course.
	 * @param semester the semester it is taken on.
	 * @throws IllegalArgumentException thrown if semester is invalid (e.g. less than 1). or in case the base class Constructor would throw an exception
	 * @see dataClass.Course#Course(String, String)
	 */
	public SelectedCourse(String courseID, String courseName, int semester) throws IllegalArgumentException{
		super(courseID, courseName);
		setSemester(semester);
		}
	
	/**
	 * Contructor used when loading from this class from classes via the ObjectInputStream class.
	 * @see databases.UserDatabase#loadStudyPlan(String, String)
	 */
	protected SelectedCourse() {
	}
	
	/**
	 * Create a SelectedCourse from a Course and a semester.
	 * @param course An object of the Course type.
	 * @param semester the semester it is taken on.
	 * @throws IllegalArgumentException thrown if semester is invalid (e.g. less than 1).
	 */
	public SelectedCourse(Course course, int semester) throws IllegalArgumentException {
		super(course.getCourseID(), course.getCourseName() );
		setDependencies(course.getDependencies());
		setFullSkemaData(course.getFullSkemaData());
		setSemester(semester);
	}
	
	
	
	/**
	 * Follows the standards of the compareTo method.
	 * 
	 * This class is sorted first by the semester it appears in and secondarily the number of the 
	 * course if the semesters are the same.
	 * 
	 * @param compareTo the object to compare to this one.
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(SelectedCourse compareTo) {
		int order = 0;
		
		if(semester < compareTo.getSemester() ) {
			order = -1;
		} else if (semester > compareTo.getSemester() ) {
			order = 1;
		} else {
			int ID = Integer.parseInt( super.getCourseID() );
			int compareToID = Integer.parseInt(compareTo.getCourseID());
			if(ID < compareToID ) {
				order = -1;
			} else if(ID > compareToID) {
				order = 1;
			} 
		}
		
		return order;
	}
	
	/**
	 * Get the semester of this course.
	 * @return The semester 
	 */
	public int getSemester() {
		return semester ;
	}
	
	/**
	 * Tests if the inputted semester is valid
	 * @param semester The value to test
	 * @return true of the semester is valid. (0 < semester < 21)
	 */
	public static boolean isValidSemester(int semester) {
		return semester < 21 && semester > 0;
	}
	
	/**
	 * Update the semester of this course.
	 * @param semester the semester to set
	 * @throws IllegalArgumentException if the semester is less than 1 or greater than 20
	 */
	public void setSemester(int semester) throws IllegalArgumentException {
		if(! isValidSemester(semester) )
			throw new IllegalArgumentException();
		this.semester = semester;

	}

}
