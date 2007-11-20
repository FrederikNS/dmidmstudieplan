/**
 * 
 */
package dataClass;

/**
 * @author Niels Thykier
 * This class specified a course selected by a student. On top of all the information
 * in the Course class, this also contains the data on which semester this course is 
 * taken.
 */
public class SelectedCourse extends Course implements Comparable<SelectedCourse> {

	private int semester = 0;

	/**
	 * @param CourseID the ID number of the course.
	 * @param semester the semester it is taken on.
	 * @throws IllegalArgumentException thrown if semester is invalid (e.g. less than 1).
	 */
	public SelectedCourse(String CourseID, int semester) throws IllegalArgumentException{
		super(CourseID);
		if(semester < 1 || semester > 20 )
			throw new IllegalArgumentException();
		this.semester = semester;
		}
	
	/*(non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * 
	 * Follows the standards of the compareTo method.
	 * 
	 * This class is sorted first by the semester it appears in and secondarily the number of the 
	 * course if the semesters are the same.
	 * 
	 * @param compareTo the object to compare to this one.
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 */
	public int compareTo(SelectedCourse compareTo) {
		//TODO check if the return values have not been reversed.
		int order = 0;
		
		if(semester < compareTo.getSemester() ) {
			order = 1;
		} else if (semester > compareTo.getSemester() ) {
			order = -1;
		} else {
			int ID = Integer.parseInt( super.getCourseID() );
			int compareToID = Integer.parseInt(compareTo.getCourseID());
			if(ID < compareToID ) {
				order = 1;
			} else if(ID > compareToID) {
				order = -1;
			} 
		}
		
		return order;
	}
	
	/**
	 * @return the semester
	 */
	public int getSemester() {
		return semester ;
	}

	/**
	 * @param semester the semester to set
	 */
	public void setSemester(int semester) {
		this.semester = semester;
	}

}
