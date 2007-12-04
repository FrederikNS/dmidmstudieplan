/**
 * 
 */
package dataClass;

import java.io.Serializable;

import exceptions.CourseCannotStartInThisSemesterException;

/**
 * This class is a course selected by a student. On top of all the information
 * in the Course class, this also contains the data on which semester this course is 
 * taken.
 * @author Niels Thykier
 * @author Frederik Nordahl Sabroe
 * @author Morten Sørensen
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
	 * @throws IllegalArgumentException Thrown if semester is invalid (e.g. less than 1).
	 * @throws CourseCannotStartInThisSemesterException Thrown if the course cannot start in that semester.
	 */
	public SelectedCourse(Course course, int semester) throws IllegalArgumentException, CourseCannotStartInThisSemesterException  {
		super(course.getCourseID(), course.getCourseName() );
		if(!SelectedCourse.isValidSemester(semester))
			throw new IllegalArgumentException();
		
		if(!course.canStartInSemester(semester)) {
			throw new CourseCannotStartInThisSemesterException(course.getCourseID(), semester);
		}
		
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
	 * Check if this course and the compareTo course are share at least one period. Used as a part of the test for determining conflicts between courses.
	 * 
	 * This, however, cannot tell if they have courses on the same days!
	 * 
	 * @param compareTo The course to test against.
	 * @return -1,0 or 1 if this course is ends before, shares at least one period with or comes after the compareTo course.
	 */
	public int getIsInSameSemester(SelectedCourse compareTo) {
		return getIsInPeriod(compareTo.getStartingPeriod(), compareTo.getFinishingPeriod());
	}
	
	/**
	 * Check if the course is within a given period. Used as a part of the test for determining conflicts between courses.
	 *
	 * This, however, cannot tell if they have courses on the same days!
	 * 
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
		System.err.println(getCourseID() + " " + courseStart + " " + courseEnd + " vs " + start + " " + finish);
		if(courseStart > finish) {
			return 1;
		}
		if(courseEnd < start) {
			return -1;
		}
		return 0; 
	}
	
	/**
	 * Test if this course has lectures in this single period.
	 * @param period the period to test.
	 * @return true if the course has something in that period.
	 */
	public boolean getHasSkemaInPeriod(int period) {
		if(period < 1 || period > 41) {
			throw new IllegalArgumentException();
		}
		if(period < this.getStartingPeriod() || period > this.getFinishingPeriod() ) {
			return false;
		}
		int periodSkema = 0;
		switch( period % 4) {
		case 0:
			periodSkema = INTERNAL_SEASON_SPRING_SHORT; 
			break;
		case 1:
			periodSkema = INTERNAL_DAYS_AUTUMN;
			break;
		case 2:
			periodSkema = INTERNAL_SEASON_AUTUMN_SHORT;
			break;
		case 3:
			periodSkema = INTERNAL_DAYS_SPRING;
			break;
		}

		return 0 != (getFullSkemaData() & periodSkema);
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
