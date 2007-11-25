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
	private String season;
	
	public Course(String CourseID) {
		this.courseID = CourseID;
	}
	
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

	public boolean compareSkema(Course compareTo) {
		
		String compareSeason = compareTo.getSeason();
		if(season == compareSeason) {
			//TODO
		}
		
		String compareSkema[] = compareTo.getSkemagruppe();
		for(int i = 0 ; i < skemagruppe.length ; i++ ) {
			for(int j = 0 ; j < compareSkema.length ; j++) {
				if(compareSkema[j].equalsIgnoreCase(skemagruppe[i]) ) 
					return true;
			}
		}
		
		return false;
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

	/**
	 * @return the season
	 */
	public String getSeason() {
		return season;
	}

	/**
	 * @param season the season to set
	 */
	public void setSeason(String season) {
		this.season = season;
	}

	/**
	 * Test if the courses are the same.
	 * @param course the course you wish to compare it to.
	 * @return true if the courseID of the two courses match.
	 */
	public boolean equals(Course course) {
		return equals(course.getCourseID());
	}
	
	/**
	 * Test if the course have the same ID as this.
	 * @param courseID The ID of the course.
	 * @return true if the courseID match.
	 */
	public boolean equals(String courseID) {
		return courseID.equals(this.courseID);
	}
	
	public String toString() {
		String s = courseID + season + " " + courseName + " ";
		
		
		for(int i = 0 ; i< skemagruppe.length ; i++) {
			s += skemagruppe[i];
		}
		
		return s;
	}
}