package dataClass;

/**
 * @author Kaffe
 *
 */
public class Course {
	private String courseID;
	private String dependencies[];
	private String courseName;
	private String skemagruppe[];
	private String year;
		
	/**
	 * @return the courseID
	 */
	public String getCourseID() {
		return courseID;
	}

	/**
	 * @param courseID the courseID to set
	 */
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	
	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/**
	 * @return the dependencies
	 */
	public String[] getDependencies() {
		return dependencies;
	}

	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(String[] dependencies) {
		this.dependencies = dependencies;
	}

	/**
	 * @return the skemagruppe
	 */
	public String[] getSkemagruppe() {
		return skemagruppe;
	}

	/**
	 * @param skemagruppe the skemagruppe to set
	 */
	public void setSkemagruppe(String[] skemagruppe) {
		this.skemagruppe = skemagruppe;
	}

	public Course(String CourseID) {
		this.courseID=CourseID;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
}