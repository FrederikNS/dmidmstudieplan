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
	
	public int getFullSkemaData() {
		return internalSkema;
	}

	
	public void setFullSkemaData(int skema) {
		this.internalSkema = skema;
	}
	
	/**
	 * 
	 */
	public void setInternalSkemaRepresentation(int newInteralSkema) {
		internalSkema =  newInteralSkema & INTERNAL_ALL_DAYS;
	}

	/**
	 * @param skemagruppe the skemagruppe to set
	 */
	public void setSkemagruppe(String[] skemagruppe) {
		Skema[] dat  = Skema.values();
		internalSkema &= ~(INTERNAL_ALL_DAYS);
		
		
		for(int i = 0 ; i < skemagruppe.length ; i++ ) {
			for(int j = 0 ; j < dat.length ; j++ ) {
				if(dat[j].isSameDTUSkema(skemagruppe[i])) {
					internalSkema |= dat[j].getInteralRepresentation();
					j = dat.length;
				}
			}
		}
	}

	/**
	 * @return the season
	 */
	public int getSeason() {
		return internalSkema & INTERNAL_SEASON_ALL;
	}

	/**
	 * @param season the season to set
	 */
	public void setSeason(String season, String period) {
		int temp;
		if(season.equalsIgnoreCase("-f")) {
			temp = INTERNAL_SEASON_SPRING_LONG;
		} else if(season.equalsIgnoreCase("-e")) {
			temp = INTERNAL_SEASON_AUTUMN_LONG;
		} else  {
			temp = INTERNAL_SEASON_SPRING_LONG | INTERNAL_SEASON_AUTUMN_LONG;
		}
		
		if(!(period == null || period.equals("") ) ) {
			if(period.equalsIgnoreCase("")) {
				if(0 != (temp & INTERNAL_SEASON_AUTUMN_LONG) ) {
					temp &= (~INTERNAL_SEASON_AUTUMN_LONG);
				}
				temp |= INTERNAL_SEASON_AUTUMN_SHORT;
			} else if(period.equalsIgnoreCase("juni")) {
				if(0 != (temp & INTERNAL_SEASON_SPRING_SHORT) ) {
					temp &= (~ INTERNAL_SEASON_SPRING_SHORT);
				}				
				temp |= INTERNAL_SEASON_SPRING_SHORT;				
			}

		}
		this.internalSkema |= temp;
	}
	
	/**
	 * Test if the courses are the same.
	 * @param course the course you wish to compare it to.
	 * @return true if the courseID of the two courses match.
	 */
	public boolean equals(Course course) {
		return isSameCourseID(course.getCourseID());
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
		String s = courseID  + " " + courseName + " ";
		return s;
	}
}