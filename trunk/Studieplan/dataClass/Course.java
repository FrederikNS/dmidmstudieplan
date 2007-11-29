package dataClass;

import java.io.Serializable;


/**
 * 
 * @author Niels Thykier
 */
public class Course implements Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = 4491280720903309211L;
	
	public static final int INTERNAL_SEASON_SPRING_SHORT = 0x00000800;
	public static final int INTERNAL_SEASON_AUTUMN_SHORT = 0x00800000;
	public static final int INTERNAL_SEASON_SPRING = 0x00000400;
	public static final int INTERNAL_SEASON_AUTUMN = 0x00400000;
	public static final int INTERNAL_SEASON_ALL = 0x00C00C00;

	public static final int INTERNAL_SEASON_SPRING_DAYS = 0x000003ff;
	public static final int INTERNAL_SEASON_AUTUMN_DAYS = 0x003ff000;

	public static final int INTERNAL_MANDAG_FORMIDDAG    = 0x00000001; //A1
	public static final int INTERNAL_MANDAG_EFTERMIDDAG  = 0x00000002; //A2
	public static final int INTERNAL_TIRSDAG_FORMIDDAG   = 0x00000004; //A3
	public static final int INTERNAL_TIRSDAG_EFTERMIDDAG = 0x00000008; //A4
	public static final int INTERNAL_ONSDAG_FORMIDDAG    = 0x00000010; //A5

	public static final int INTERNAL_TORSDAG_EFTERMIDDAG = 0x00000020; //1B
	public static final int INTERNAL_TORSDAG_FORMIDDAG   = 0x00000040; //2B
	public static final int INTERNAL_FREDAG_EFTERMIDDAG  = 0x00000080; //3B
	public static final int INTERNAL_FREDAG_FORMIDDAG    = 0x00000100; //4B
	public static final int INTERNAL_ONSDAG_EFTERMIDDAG  = 0x00000200; //5B

	public static final int INTERNAL_ALL_DAYS = 0x000002ff;
	public static final int INTERNAL_ALL_SEASONS = 0x30000000;


	public static int parseDTUSkema(String[] DTUdata, String courseLength[]) {
		int data = 0;

		for(int i = 0; i < DTUdata.length ; i++ ){
			data |= parseSingleDTUSkema(DTUdata[i]);
		}

		if( courseLength[0] != null) {
			for(int i = 0 ; i < courseLength.length ; i++ ) {
				if(courseLength[i].equalsIgnoreCase("januar")) {
					data |= INTERNAL_SEASON_AUTUMN_SHORT;
				}else if(courseLength[i].equalsIgnoreCase("juni")) {
					data |= INTERNAL_SEASON_SPRING_SHORT;
				}
			}
		}

		return data;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public static String internalSkemaToExternString(int internalRepresentation) {
		int flag;
		String toReturn = "";
		for(int i = 0 ; i < 10 ; i++) {
			flag = internalRepresentation >> i;
		if(0 != (flag & 1)) {
			toReturn += "F" + ((i%5)+1) + (1 == (i/5)?"B":"A") + " ";

		}
		}
		internalRepresentation >>= 12;
		for(int i = 0 ; i < 10 ; i++) {
			flag = internalRepresentation >> i;
		if(0 != (flag & 1)) {
			toReturn += "E" + ((i%5)+1) + (1 == (i/5)?"B":"A") + " ";
		}
		}

		return toReturn.trim();
	}

	public static int parseSingleDTUSkema(String DTUplan) {
		//Reading only the three last characters
		DTUplan = DTUplan.trim();
		int length = DTUplan.length();
		if(length > 3) {
			DTUplan = DTUplan.substring(length-3, length);
		}
		int skema = Integer.parseInt(DTUplan, 16);
		int toReturn = 0;
		switch(skema & 0xff) {
		case 0x1A:
			toReturn = INTERNAL_MANDAG_FORMIDDAG;
			break;
		case 0x2A:
			toReturn = INTERNAL_MANDAG_EFTERMIDDAG;
			break;
		case 0x3A:
			toReturn = INTERNAL_TIRSDAG_FORMIDDAG;
			break;
		case 0x4A:
			toReturn = INTERNAL_TIRSDAG_EFTERMIDDAG;
			break;
		case 0x5A:
			toReturn = INTERNAL_ONSDAG_FORMIDDAG;
			break;				
		case 0x1B:
			toReturn = INTERNAL_TORSDAG_EFTERMIDDAG;
			break;
		case 0x2B:
			toReturn = INTERNAL_TORSDAG_FORMIDDAG;
			break;
		case 0x3B:
			toReturn = INTERNAL_FREDAG_EFTERMIDDAG;
			break;
		case 0x4B:
			toReturn = INTERNAL_FREDAG_FORMIDDAG;
			break;
		case 0x5B:
			toReturn = INTERNAL_ONSDAG_EFTERMIDDAG;
			break;				
		}

		if((skema & 0xf00) == 0xf00) {
			toReturn |= INTERNAL_SEASON_SPRING;
		} else if((skema & 0xf00) == 0xe00) {
			toReturn <<= 12;
			toReturn |= INTERNAL_SEASON_AUTUMN;
		}

		return toReturn;
	}

	private String courseID;
	private String dependencies[];
	private String courseName;
	private int internalSkema;

	public Course(String courseID, String courseName) throws IllegalArgumentException {
		if(courseID == null || courseID.length() != 5 || Integer.parseInt(courseID) < 0) {
			throw new IllegalArgumentException("Course ID is invalid");
		}
		if(courseName == null || courseName.equals("")) {
			throw new IllegalArgumentException("Name is blank");
		}
		this.courseID = courseID;
		this.courseName = courseName;
	}


	protected Course() {
	}
	
	/**
	 * @return the courseID
	 */
	public String getCourseID() {
		return courseID;
	}

	/**
	 * @return
	 */
	public String skemaToString() {		
		return internalSkemaToExternString(internalSkema);
	}

	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * @return the dependencies
	 */
	public String[] getDependencies() {
		return dependencies;
	}

	/*
	 * @param compareTo
	 * @return true
	 */
	public boolean conflictingSkema(Course compareTo) {
		if(compareTo == null) 
			return false;
		int compare = compareTo.getFullSkemaData();
		compare &= (INTERNAL_SEASON_SPRING_DAYS | INTERNAL_SEASON_AUTUMN_DAYS);
		return 0 == (compare & internalSkema);
	}
	
	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(String[] dependencies) {
		this.dependencies = dependencies;
	}

	/**
	 * @return internalSkema
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
	 * @param skemagruppe the skemagruppe to set
	 */
	public void setSkemagruppe(String[] skemagruppe, String[] periodData ) {
		int i = parseDTUSkema(skemagruppe, periodData);
		internalSkema = i;
	}

	/**
	 * @return the season
	 */
	public int getSeason() {
		return internalSkema & INTERNAL_SEASON_ALL;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		if(courseID == null)
			return false;
		return courseID.equals(this.courseID);
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s = courseID  + " " + courseName + ", skemagruppe: " + skemaToString();
		if(dependencies != null){
			s += ", forudgående kurser: ";
			for(int i = 0 ; i < dependencies.length ; i++){
				s += dependencies[i];
			}
		}
		return s;
	}
}