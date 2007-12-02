/**
 * 
 */
package dataClass;

import java.io.Serializable;

/**
 * This class is a course selected by a student. On top of all the information
 * in the Course class, this also contains the data on which semester this course is 
 * taken.
 * @author Niels Thykier
 */
public class SelectedCourse extends Course implements Comparable<SelectedCourse>, Serializable {

	/**
	 * serialVersionUID needed so that this class can be Serializable. 
	 */
	private static final long serialVersionUID = -2357834380866036780L;
	
	/**
	 * The semester this course is taken on.
	 */
	private final int period;

	/**
	 * Create a SelectedCourse from a courseID, courseName and a semester.
	 * NB: This will NOT look up all the data from the databases.
	 * 
	 * Use the findCourse from Core or CourseBase and the {@link #SelectedCourse(Course, int)} Constructor
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
		if(!SelectedCourse.isValidSemester(semester))
			throw new IllegalArgumentException();
		period = (semester<<1)-1;
		}
	
	/**
	 * Contructor used when loading from this class from classes via the ObjectInputStream class.
	 * @see databases.UserDatabase#loadStudyPlan(String, String)
	 */
	protected SelectedCourse() {
		period = 0;
	}
	
	/**
	 * Create a SelectedCourse from a Course and a semester.
	 * @param course An object of the Course type.
	 * @param semester the semester it is taken on.
	 * @throws IllegalArgumentException thrown if semester is invalid (e.g. less than 1).
	 */
	public SelectedCourse(Course course, int semester) throws IllegalArgumentException {
		super(course.getCourseID(), course.getCourseName() );
		if(!SelectedCourse.isValidSemester(semester))
			throw new IllegalArgumentException();
		period = (semester<<1)-1;
		setDependencies(course.getDependencies());
		setFullSkemaData(course.getFullSkemaData());
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
		
		if(this.getStartingSemester() < compareTo.getStartingSemester() ) {
			order = -1;
		} else if (this.getStartingSemester() > compareTo.getStartingSemester() ) {
			order = 1;
		} else {
			int ID = Integer.parseInt( getCourseID() );
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
	 * Get the semester in which this course starts.
	 * @return The semester 
	 */
	public int getStartingSemester() {
		return (this.period>>1)+1;
	}
	
	/**
	 * Returns the starting period for this course.
	 * @return The period this course starts in.
	 */
	public int getStartingPeriod() {
		return this.period;
	}
	
	/**
	 * Returns the period in which this course finishes.
	 * (For 1 period courses, this will be the same period as the starting period, 
	 * for 2 period courses it will be starting period + 1 and so on...)
	 * 
	 * @return The period this course end in.
	 */
	public int getFinishingPeriod() {
		return this.period + this.getAmountOfPeriods() - 1;
	}
	
	/**
	 * Check if this course and the compareTo course are share at least one period.
	 * @param compareTo The course to test against.
	 * @return -1,0 or 1 if this course is ends before, shares at least one period with or comes after the compareTo course.
	 */
	public int getIsInSameSemester(SelectedCourse compareTo) {
		return getIsInPeriod(compareTo.getStartingPeriod(), compareTo.getFinishingPeriod());
	}
	
	/**
	 * Check if the course is within a given period. Used to test for conflicts between courses.
	 * @param start The first period of interest.
	 * @param finish The last period of interest. 
	 * @return -1,0 or 1 if this course is ends before, shares at least one period with or comes after the period-range. 
	 * @throws IllegalArgumentException If the "start" is greater than "finish", "start" is less than 1 or "finish" greater than 41
	 */
	public int getIsInPeriod(int start, int finish) throws IllegalArgumentException {
		if(finish < start || start < 1 || finish > 41) {
			throw new IllegalArgumentException();
		}
		int courseStart = this.getStartingPeriod();
		int courseEnd = this.getFinishingPeriod();
		if(courseStart > finish) {
			return 1;
		}
		if(courseEnd < start) {
			return -1;
		}
		return 0; 
	}
	
	/**
	 * Check if the course is within a given period. Used to test for conflicts between courses.
	 * 
	 * The boolean parameter "shortPeriod" is first to avoid accidental use of {@link #getIsInPeriod(int, int)}  
	 * 
	 * @param shortPeriod Whether or not the test should start at the short-period part (second-half) of the semester.
	 * @param start The semester to test for.  
	 * @param finish The amount of periods after the initial periods that should be considered "reserved"
	 * @return -1,0 or 1 if this course is ends before, shares at least one semester with or comes after the semester-range.
	 * @throws IllegalArgumentException
	 */
	public int getIsInSemester(boolean shortPeriod, int start, int finish) throws IllegalArgumentException {
		return getIsInPeriod(start<<1 + (shortPeriod?0:-1), finish<<1 + (shortPeriod?0:-1));
	}
	
	/**
	 * Tests if the inputted semester is valid
	 * @param semester The value to test
	 * @return true of the semester is valid. (0 < semester < 21)
	 */
	public static boolean isValidSemester(int semester) {
		return semester < 21 && semester > 0;
	}
	
}
