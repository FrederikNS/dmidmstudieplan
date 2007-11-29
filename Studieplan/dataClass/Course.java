package dataClass;


/**
 * @author Kaffe
 *
 */
public class Course implements CourseSkemaData {
	private String courseID;
	private String dependencies[];
	private String courseName;
	private int internalSkema;
	
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
	
	public String skemaToString() {		
		return InternalSkema.internalSkemaToExternString(internalSkema);
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
	 * @param compareTo
	 * @return true if
	 */
	public boolean conflictingSkema(Course compareTo) {		
		return 0 != (compareTo.getInternalSkemaRepresentation() & internalSkema);
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
	public int getInternalSkemaRepresentation() {
		return internalSkema & INTERNAL_ALL_DAYS;
	}
	
	/**
	 * @return
	 */
	public int getFullSkemaData() {
		return internalSkema;
	}

	
	/**
	 * @param skema
	 */
	public void setFullSkemaData(int skema) {
		this.internalSkema = skema;
	}
	
	/**
	 * 
	 */
	public void setInternalSkemaRepresentation(int newInteralSkema) {
		internalSkema = newInteralSkema & INTERNAL_ALL_DAYS;
	}

	/**
	 * @param skemagruppe the skemagruppe to set
	 */
	public void setSkemagruppe(String[] skemagruppe, String[] periodData ) {
		internalSkema |= InternalSkema.parseDTUskema(skemagruppe, periodData);
	}

	/**
	 * @return the season
	 */
	public int getSeason() {
		return internalSkema & INTERNAL_SEASON_ALL;
	}


	public boolean equals(Object obj) {
		if(obj instanceof Course) {
			Course course = (Course) obj;
			return isSameCourseID(course.getCourseID());
		}
				
		return false;
	}
	
	/**
	 * Test if the course have the same ID as this.
	 * @param courseID The ID of the course.
	 * @return true if the courseID match.
	 */
	public boolean isSameCourseID(String courseID) {
		return courseID.equals(this.courseID);
	}
	
	public String toString() {
		String s = courseID  + " " + courseName + ", skemagruppe: " + skemaToString();
		if(dependencies != null){
			s += ", forudgående kurser: ";
			for(int i = 0;dependencies.length > i;i++){
				s += dependencies[i];
			}
		}
		return s;
	}
}