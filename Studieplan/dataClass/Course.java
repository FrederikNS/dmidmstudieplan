package dataClass;

import java.io.Serializable;


/**
 * 
 * @author Niels Thykier
 */
public class Course implements Serializable{



	/**
	 * serialVersionUID needed so that this class can be Serializable. 
	 */
	private static final long serialVersionUID = 4491280720903309211L;
	
	/**
	 * The bit-flag for the short part of the spring semester
	 */
	public static final int INTERNAL_SEASON_SPRING_SHORT = 0x00000800;
	/**
	 * The bit-flag for the short part of the autumn semester
	 */
	public static final int INTERNAL_SEASON_AUTUMN_SHORT = 0x00800000;
	/**
	 * The bit-flag for the long part of the spring semester
	 */
	public static final int INTERNAL_SEASON_SPRING       = 0x00000400;
	/**
	 * The bit-flag for the long part of the autum semester
	 */
	public static final int INTERNAL_SEASON_AUTUMN       = 0x00400000;
	/**
	 * Filter bit-flags for isolating all the season bit-flags.
	 */
	public static final int INTERNAL_SEASON_ALL          = 0x00C00C00;

	/**
	 * Filter for isolating all the day bit-flags
	 */
	public static final int INTERNAL_DAYS         = 0x003ff3ff;


	/**
	 * The Monday morning class bit-flag. 
	 * DTU alias: A1
	 */
	public static final int INTERNAL_MONDAY_MORNING       = 0x00000001;
	/**
	 * The Monday afternoon class bit-flag. 
	 * DTU alias: A2
	 */
	public static final int INTERNAL_MONDAY_AFTERNOON     = 0x00000002;
	/**
	 * The Tuesday morning class bit-flag. 
	 * DTU alias: A3
	 */
	public static final int INTERNAL_TUESDAY_MORNING      = 0x00000004;
	/**
	 * The Tuesday afternoon class bit-flag. 
	 * DTU alias: 4A
	 */
	public static final int INTERNAL_TUESDAY_AFTERNOON    = 0x00000008; 
	/**
	 * The Wedneysday morning class bit-flag. 
	 * DTU alias: 5A
	 */
	public static final int INTERNAL_WEDNEYSDAY_MORNING   = 0x00000010;

	/**
	 * The Thursday afternoon class bit-flag. 
	 * DTU alias: 1B
	 */
	public static final int INTERNAL_THURSDAY_AFTERNOON   = 0x00000020; 
	/**
	 * The Thursdag morning class bit-flag. 
	 * DTU alias: B2
	 */
	public static final int INTERNAL_THURSDAY_MORNING     = 0x00000040; 
	/**
	 * The Friday afternoon class bit-flag. 
	 * DTU alias: 3B
	 */
	public static final int INTERNAL_FRIDAY_AFTERNOON     = 0x00000080;
	/**
	 * The Friday morning class bit-flag. 
	 * DTU alias: 4B
	 */
	public static final int INTERNAL_FRIDAY_MORNING       = 0x00000100;
	/**
	 * The Wedneysday afternoon class bit-flag. 
	 * DTU alias: 5B
	 */ 
	public static final int INTERNAL_WEDNEYSDAY_AFTERNOON = 0x00000200;


	/**
	 * Parses a DTU based skema and turns it into the internal skema (Bit-flags).
	 * 
	 * If either the DTUdata or the courseLength input is missing, that check will be skipped!
	 * 
	 * @param DTUdata The skema in the DTU format (e.g. E1A is Mondag morning)
	 * @param courseLength The length of the course. (3-week and 13-week)
	 * @return A bit-flagged integer containing all the flags that matched the input or 0 if the input was not DTU skema Data.
	 */
	public static int parseDTUSkema(String[] DTUdata, String courseLength[]) {
		int data = 0;
		if(DTUdata != null) {
			for(int i = 0; i < DTUdata.length ; i++ ){
				data |= parseSingleDTUSkema(DTUdata[i]);
			}
		}
		
		if( !(courseLength == null) && !courseLength[0].equals("")) {
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


	/**
	 * Transforms an internal skema (bit-flags) to the DTU skema standard.
	 * 
	 * This will always output the skema in the same order (Spring before Autumn courses) and
	 * thus cannot garantuee to them in the same order as they were converted from.
	 * 
	 * This method heavily exploits the Bit-flags of the INTERNAL_*DAG_* constants.
	 * The flags have been added in such an order that if one loops through them they will
	 * appear in the order: A1 to A5 and then B1 to B5
	 * 
	 * This has been exploited so that two simple math operations generate the digit and the trailing letter,
	 * using the following: 
	 * 		
	 * 		((i%5)+1) + (1 == (i/5)?"B":"A")
	 * 
	 * @param internalRepresentation An internal representation (bit-flags) of the skema.
	 * @return The DTU skema in a single string.
	 */
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

	/**
	 * Converts a single DTU skema group into an internal skema bit-flag.
	 * 
	 * If the input is longer that 3 characters long (after being trimmed), it will read the
	 * three last characters of the string and disregard the rest!
	 * 
	 * The DTU skema is parsed by exploiting the fact that all parts of such a skema 
	 * are valid hexadecimals and thus the Integer.parseInt is used on it, attempting to 
	 * parse it as a hexadecimal.
	 *  
	 * 
	 * @param DTUplan A single DTU skema group (e.g. E1A for monday morning)
	 * @return A set of internal skema bit-flags or 0, if the input was not a DTU skema group. 
	 */
	public static int parseSingleDTUSkema(String DTUplan) {
		//Reading only the three last characters
		DTUplan = DTUplan.trim();
		int length = DTUplan.length();
		if(length > 3) {
			DTUplan = DTUplan.substring(length-3, length);
		}
		int skema;
		try {
			skema = Integer.parseInt(DTUplan, 16);
		} catch(Exception e) {
			return 0;
		}
		int toReturn = 0;
		switch(skema & 0xff) {
		case 0x1A:
			toReturn = INTERNAL_MONDAY_MORNING;
			break;
		case 0x2A:
			toReturn = INTERNAL_MONDAY_AFTERNOON;
			break;
		case 0x3A:
			toReturn = INTERNAL_TUESDAY_MORNING;
			break;
		case 0x4A:
			toReturn = INTERNAL_TUESDAY_AFTERNOON;
			break;
		case 0x5A:
			toReturn = INTERNAL_WEDNEYSDAY_MORNING;
			break;				
		case 0x1B:
			toReturn = INTERNAL_THURSDAY_AFTERNOON;
			break;
		case 0x2B:
			toReturn = INTERNAL_THURSDAY_MORNING;
			break;
		case 0x3B:
			toReturn = INTERNAL_FRIDAY_AFTERNOON;
			break;
		case 0x4B:
			toReturn = INTERNAL_FRIDAY_MORNING;
			break;
		case 0x5B:
			toReturn = INTERNAL_WEDNEYSDAY_AFTERNOON;
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

	/**
	 * The ID of the course.
	 * This is final as no course ought ever to change the ID.
	 */
	final private String courseID;
	/**
	 * The name of the Course. 
	 * Since a course name is not likely to change, it is final.
	 */
	final private String courseName;
	/**
	 * This field contains the ID of all the dependency courses. 
	 */
	private String dependencies[];
	/**
	 * The internal skema representation bit-flag field.
	 */
	private int internalSkema;

	/**
	 * Creates a new Course. This is used by the DatabaseReader, when loading the 
	 * courses into memory.
	 * 
	 * NB: This will NOT look up all the data from the databases.
	 * 
	 * Use the findCourse from Core or DatabaseReader.
	 * 
	 * @param courseID the ID of the course.
	 * @param courseName the Name of the course.
	 * @throws IllegalArgumentException Thrown if the ID is null, not exactly 5 characters long, is a negative number, or if the name is null or "".
	 */
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

	/**
	 * Contructor used when loading from this class from classes via the ObjectInputStream class.
	 * @see databases.UserDatabase#loadStudyPlan(String, String)
	 */
	protected Course() {
		courseName = "";
		courseID = "";
	}
	
	/**
	 * Get the course ID of the Course.
	 * @return the courseID
	 */
	public String getCourseID() {
		return courseID;
	}

	/**
	 * Turn the internal skema representation into the DTU format.
	 * 
	 * This method uses the Course.internalSkemaToExternString(int) method.
	 * 
	 * @return The skema in the DTU format.
	 * @see dataClass.Course#internalSkemaToExternString(int)
	 */
	public String skemaToString() {		
		return internalSkemaToExternString(internalSkema);
	}

	/**
	 * Get the name of the course.
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * Get the course ID of the dependency courses.
	 * @return A string array with the course IDs. This MAY return null, if no courses are loaded!
	 */
	public String[] getDependencies() {
		return dependencies;
	}

	/**
	 * Test if two courses share at least one lession during the week.
	 * @param compareTo the course to compare with.
	 * @return true, if this course and compareTo have at least one lession at the same time during a week. 
	 */
	public boolean conflictingSkema(Course compareTo) {
		int compare = compareTo.getFullSkemaData();
		compare &= ~(INTERNAL_DAYS);
		return 0 != (compare & internalSkema);
	}
	
	/**
	 * Update the dependencies with a new set. 
	 * This is used by the DatabaseReader when loading the the courses into memory. 
	 * @param dependencies A string array containing all the dependencies.
	 */
	public void setDependencies(String[] dependencies) {
		this.dependencies = dependencies;
	}

	/**
	 * Get the skema as represented by bit-flags.
	 * 
	 * For a textual representation use skemaToString().
	 * 
	 * @return internalSkema The internal skema data of the course.
	 */
	public int getFullSkemaData() {
		return internalSkema;
	}

	/**
	 * Update the internal skema bit-flags with the input.
	 * @param skema The new bit-flags.
	 */
	public void setFullSkemaData(int skema) {
		this.internalSkema = skema;
	}

	/**
	 * This method is mainly used by the DatabaseReader to update the Skema data 
	 * of a course using the data from the files.
	 * 
	 * It uses the Course.parseDTUSkema method.
	 * 
	 * @param skemagruppe The DTU based skema format with one entry per array-slot (e.g {"E1A", "E2A"} )
	 * @param periodData The length of the period (3 or 13 weeks) as formatted in the files.
	 */
	public void setSkemagruppe(String[] skemagruppe, String[] periodData) {
		internalSkema = parseDTUSkema(skemagruppe, periodData);
	}

	/**
	 * Overloading of the Object.equals method. 
	 * This specifies that two courses are the same if they courseID is the same.
	 * 
	 * This overloading couses the ArrayList<Course>.contains(course) to return true 
	 * if a course with the same ID is already in that ArrayList.
	 * 
	 * @see dataClass.StudyPlan#contains(Course)
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

	/**
	 * Turns the Course into a string in the following format:
	 * 
	 * ID Name, skemagruppe: [the DTU skema] (, forudgående kurser: [the ID of dependency courses]) 
	 * 
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