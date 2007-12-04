package dataClass;

import java.io.Serializable;


/**
 * This class describes a course that a student could take during his/her stay at the university.
 * 
 * Asside from the ID of the course and its name, it also contains information about what periods
 * it can be taken, whether it is single or a multi-period course and what courses, that it depends on.
 * 
 * @author Niels Thykier
 * @author Frederik Nordahl Sabroe
 * @author Morten Sørensen
 */
public class Course implements Serializable{

	/**
	 * serialVersionUID needed so that this class can be Serializable. 
	 */
	private static final long serialVersionUID = 4491280720903309211L;

	/**
	 * The bit-flag for the short part of the autumn semester
	 */
	public static final int INTERNAL_SEASON_AUTUMN_SHORT  = 0x80000000;
	/**
	 * The bit-flag for the long part of the autum semester
	 */
	public static final int INTERNAL_SEASON_AUTUMN_LONG   = 0x20000000;
	/**
	 * The bit-flag for the short part of the spring semester
	 */
	public static final int INTERNAL_SEASON_SPRING_SHORT  = 0x40000000;
	/**
	 * The bit-flag for the long part of the spring semester
	 */
	public static final int INTERNAL_SEASON_SPRING_LONG   = 0x10000000;
	/**
	 * Filter bit-flags for isolating all the season bit-flags.
	 */
	public static final int INTERNAL_SEASON_ALL           = 0xf0000000;

	/**
	 * Filter for determining the starting season. 
	 * If applying this filter on a internal skema is non-zero, the skema is for a multi-period course.
	 */
	public static final long INTERNAL_STARTING_PERIOD    = 0x0f000000;

	/**
	 * The bit-flag for courses that must start in the short part of the autumn semester
	 */
	public static final int INTERNAL_STARTING_PERIOD_AUTUMN_SHORT  = 0x08000000;
	/**
	 * The bit-flag for courses that must start in the long part of the autum semester
	 */
	public static final int INTERNAL_STARTING_PERIOD_AUTUMN_LONG   = 0x02000000;
	/**
	 * The bit-flag for courses that must start in the short part of the spring semester
	 */
	public static final int INTERNAL_STARTING_PERIOD_SPRING_SHORT  = 0x04000000;
	/**
	 * The bit-flag for courses that must start in the long part of the spring semester
	 */
	public static final int INTERNAL_STARTING_PERIOD_SPRING_LONG   = 0x01000000;
	
	
	/**
	 * Used to bit-shift a INTERNAL_*DAY_* into or out of the long Spring part of the semester 
	 */
	public static final int INTERNAL_SHIFT_SPRING     = 0;
	/**
	 * Used to bit-shift a INTERNAL_*DAY_* into or out of the long Autumn part of the semester 
	 */
	public static final int INTERNAL_SHIFT_AUTUMN     = 12;

	/**
	 * Filter for isolating the days (post season to day shifts)
	 * Use INTERNAL_DAYS_ALL to filter all days regardless of seasons.
	 */
	public static final int INTERNAL_DAYS             = 0x000003ff;

	/**
	 * Filter for isolating all the days bit-flags for the long autumn.
	 */
	public static final int INTERNAL_DAYS_AUTUMN     = INTERNAL_DAYS<<INTERNAL_SHIFT_AUTUMN;
	/**
	 * Filter for isolating all the days bit-flags for the long spring. 
	 */
	public static final int INTERNAL_DAYS_SPRING     = INTERNAL_DAYS<<INTERNAL_SHIFT_SPRING;
	/**
	 * Filter for isolating all the day bit-flags
	 */
	public static final int INTERNAL_DAYS_ALL        = INTERNAL_DAYS_SPRING | INTERNAL_DAYS_AUTUMN;

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
	 * The Wednesday morning class bit-flag. 
	 * DTU alias: 5A
	 */
	public static final int INTERNAL_WEDNESDAY_MORNING   = 0x00000010;

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
	 * The Wednesday afternoon class bit-flag. 
	 * DTU alias: 5B
	 */ 
	public static final int INTERNAL_WEDNESDAY_AFTERNOON  = 0x00000200;

		

	/**
	 * The ID of the course.
	 * This is final as no course ought ever to change the ID.
	 */
	private final String courseID;
	/**
	 * The name of the Course. 
	 * Since a course name is not likely to change, it is final.
	 */
	private final String courseName;
	/**
	 * This field contains the ID of all the dependency courses. 
	 */
	private String dependencies;
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
	 * Use the findCourse from Core or CourseBase.
	 * 
	 * @param courseID the ID of the course.
	 * @param courseName the Name of the course.
	 * @throws IllegalArgumentException Thrown if the ID is null, not exactly 5 characters long, is a negative number, or if the name is null or "".
	 */
	public Course(String courseID, String courseName) throws IllegalArgumentException {
		try {
			if(courseID == null || courseID.length() != 5 || Integer.parseInt(courseID) < 0) {
				throw new IllegalArgumentException("Course ID is invalid");
			}
		}catch(NumberFormatException e) {
			throw new IllegalArgumentException("CourseID is invalid");
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
	 * Parses a DTU based skema and turns it into the internal skema (Bit-flags).
	 * 
	 * If either the DTUdata or the courseLength input is missing, that check will be skipped!
	 * 
	 * @param DTUdata The skema in the DTU format (e.g. E1A is Mondag morning)
	 * @return A bit-flagged integer containing all the flags that matched the input or 0 if the input was not DTU skema Data.
	 * @throws IllegalArgumentException If the DTU plan was malformatted.
	 */
	private int parseDTUSkema(String DTUdata) throws IllegalArgumentException {
		int data = 0;
		if(DTUdata != null) {
			String[] DTUarray = DTUdata.split(" ");
			for(int i = 0; i < DTUarray.length ; i++ ){
				data |= parseSingleDTUSkema(DTUarray[i]);
			}
		}
		return data;
	}
	

	/**
	 * Converts a single DTU skema group into an internal skema bit-flag.
	 * 
	 * The DTU skema is parsed by exploiting the fact that all parts of such a skema 
	 * are valid hexadecimals and thus the Integer.parseInt is used on it, attempting to 
	 * parse it as a hexadecimal.
	 *  
	 * 
	 * @param DTUplan A single DTU skema group (e.g. E1A for monday morning)
	 * @return A set of internal skema bit-flags or 0, if the input was not a DTU skema group. 
	 * @throws IllegalArgumentException If the DTU plan was malformatted.
	 */
	private int parseSingleDTUSkema(String DTUplan) throws IllegalArgumentException {
		//Reading only the three last characters
		DTUplan = DTUplan.trim();
		int length = DTUplan.length();
		if(length > 3) {
			throw new IllegalArgumentException();
		}
		int skema;
		try {
			skema = Integer.parseInt(DTUplan, 16);
		} catch(Exception e) {
			throw new IllegalArgumentException();
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
			toReturn = INTERNAL_WEDNESDAY_MORNING;
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
			toReturn = INTERNAL_WEDNESDAY_AFTERNOON;
			break;				
		default: 
			throw new IllegalArgumentException();
		}

		if((skema & 0xf00) == 0xf00) {
			toReturn <<= INTERNAL_SHIFT_SPRING;
			toReturn |= INTERNAL_SEASON_SPRING_LONG;
		} else if((skema & 0xf00) == 0xe00) {
			toReturn <<= INTERNAL_SHIFT_AUTUMN;
			toReturn |= INTERNAL_SEASON_AUTUMN_LONG;
		} else {
			throw new IllegalArgumentException();
		}

		return toReturn;
	}

	/**
	 * Get the course ID of the Course.
	 * @return the courseID
	 */
	public String getCourseID() {
		return courseID;
	}

	/**
	 * Transforms an internal skema (bit-flags) to the DTU skema standard. 
	 * This will not print short courses skemas.
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
	 * NOTE: This will return "", if there are no days for long courses.
	 * 
	 * @return The DTU skema in a single string.
	 */
	public String skemaToString() {
		if(0 == (internalSkema & INTERNAL_DAYS)) {
			return "";
		}
		int flag;
		String toReturn = "";
		int autumnDays, springDays;
		
		autumnDays = (internalSkema & INTERNAL_DAYS_AUTUMN) >> INTERNAL_SHIFT_AUTUMN;
		springDays = (internalSkema & INTERNAL_DAYS_SPRING) >> INTERNAL_SHIFT_SPRING;
		
		for(int i = 0 ; i < 10 ; i++) {
			flag = springDays >> i;
			if(0 != (flag & 1)) {
				toReturn += "F" + ((i%5)+1) + (1 == (i/5)?"B":"A") + " ";
			}
		}
		for(int i = 0 ; i < 10 ; i++) {
			flag = autumnDays >> i;
			if(0 != (flag & 1)) {
				toReturn += "E" + ((i%5)+1) + (1 == (i/5)?"B":"A") + " ";
			}
		}
		return toReturn.trim();
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
	 * @return A string with the course IDs. This MAY return null, if no courses are loaded!
	 */
	public String getDependencies() {
		return dependencies;
	}

	/**
	 * Test if two courses share at least one lession during the week.
	 * @param compareTo the course to compare with.
	 * @return 0 if there was no conflicts, else the season in which they have conflicts. 
	 */
	public int conflictingSkema(Course compareTo) {
		int compare = compareTo.getFullSkemaData();
		int currentTest = 0, possibleConflict = 0;
		int conflictPeriods = 0;
		for(int i = 0 ; i < 2 ; i++) {
			switch(i) {
			case 0:
				currentTest = INTERNAL_DAYS_SPRING;
				possibleConflict = INTERNAL_SEASON_SPRING_LONG;
				break;
			case 1:
				currentTest = INTERNAL_DAYS_AUTUMN;
				possibleConflict = INTERNAL_SEASON_AUTUMN_LONG;
				break;
			}
			
			if(0 != ((currentTest & compare) & internalSkema)) {
				conflictPeriods |= possibleConflict;
			}
		}
		conflictPeriods |=  ((compare & internalSkema) & (INTERNAL_SEASON_AUTUMN_SHORT | INTERNAL_SEASON_SPRING_SHORT));
		return conflictPeriods;
	}

	/**
	 * Update the dependencies with a new set. 
	 * This is used by the DatabaseReader when loading the the courses into memory. 
	 * @param dependencies A string containing all the dependencies.
	 */
	public void setDependencies(String dependencies) {
		if(dependencies != null) {
			this.dependencies = dependencies.trim();
		} else this.dependencies = "";
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
	 * 
	 * Note, if the input is 0, it will be silently ignored!
	 * 
	 * @param skema The new bit-flags.
	 */
	public void setFullSkemaData(int skema) {
		if(skema != 0) {
			this.internalSkema = skema;
		}
	}

	/**
	 * This method is mainly used by the DatabaseReader to set or add to the Skema data 
	 * of a course using the data from the files.
	 * 
	 * It uses the Course.parseDTUSkema method.
	 * 
	 * @param skemagruppe The DTU based skema format with exactly one space delimiting each of them.
	 * @param add If true, the new data will be merged with the current rather than overwriting the current. 
	 * @throws IllegalArgumentException If the Skemagruppe is malformatted.
	 */
	public void setSkemagruppe(String skemagruppe, boolean add) throws IllegalArgumentException {
		if(add) {
			internalSkema |= parseDTUSkema(skemagruppe);
		} else {
			internalSkema = parseDTUSkema(skemagruppe);
		}
	}

	/**
	 * Get the amont of courses this one depends on.
	 * @return The amount of courses this one depends on.
	 */
	public int getAmountOfDependencies() {
		if(dependencies == null || dependencies.equals("")) 
			return 0;
		return dependencies.split(" ").length;
	}

	/**
	 * Check if the course has any dependency courses.
	 * @return true if this course has any dependencies.
	 */
	public boolean hasDependencies() {
		return getAmountOfDependencies() != 0;
	}

	/**
	 * Overloading of the Object.equals method. 
	 * This specifies that two courses are the same if their courseIDs are equal.
	 * 
	 * This overloading couses the ArrayList<Course>.contains(Course) to return true 
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
	 * Get the amount of periods this course occupies.
	 * 
	 * @return The amount of periods this courses takes to complete.
	 */
	public int getAmountOfPeriods() {
		if(!isMultiPeriodCourse() )
			return 1;
		int temp = internalSkema & INTERNAL_SEASON_ALL;
		int toReturn = 1;
		//Count amount of bits, the "Brian Kernighan's way".
		//Repeatedly clear the least significant
		for(; temp != 0 ; toReturn++) {
			temp &= temp - 1;
		}
		if(toReturn == 5) {
			toReturn = 4;
		}
		return toReturn;
	}

	/**
	 * Test whether the course is more than one period long
	 * @return true if this course is over more than one period.
	 */
	public boolean isMultiPeriodCourse() {
		return 0 != (internalSkema & INTERNAL_STARTING_PERIOD);
	}

	/**
	 * Update the period/semester data for the course.
	 * 
	 * NB: This method will assume that previous data about days are still valid and update accordingly.
	 * If a period is removed, all days related to it will be removed.
	 * 
	 * @param periods The periods.
	 * @param multiPeriodCourse If true, the input should be treated as the periods the course occupies rather than the periods, it can be selected.
	 */
	public void updateSeason(String periods, boolean multiPeriodCourse) {
		if(periods == null)
			return;
		int temp = 0, days = internalSkema & INTERNAL_DAYS_ALL;
		int clear = 0;
		String[] periodArray = periods.trim().split(" ");
		for(int i = 0 ; i < periodArray.length ; i++ ) {
			if(periodArray[i].equals("E")) {
				temp |= INTERNAL_SEASON_AUTUMN_LONG;
				if(i == 0 && multiPeriodCourse) {
					temp |= INTERNAL_STARTING_PERIOD_AUTUMN_LONG;
				}
			} else if(periodArray[i].equalsIgnoreCase("januar")) {
				temp |= INTERNAL_SEASON_AUTUMN_SHORT;
				if(i == 0 && multiPeriodCourse) {
					temp |= INTERNAL_STARTING_PERIOD_AUTUMN_SHORT;
				}
			} else if(periodArray[i].equals("F")) {
				temp |= INTERNAL_SEASON_SPRING_LONG;
				if(i == 0 && multiPeriodCourse) {
					temp |= INTERNAL_STARTING_PERIOD_SPRING_LONG;
				}
			} else if(periodArray[i].equalsIgnoreCase("juni")) {
				temp |= INTERNAL_SEASON_SPRING_SHORT;
				if(i == 0 && multiPeriodCourse) {
					temp |= INTERNAL_STARTING_PERIOD_SPRING_SHORT;
				}
			}
		}

		if(0 != (days & INTERNAL_DAYS_AUTUMN) && 0 == (temp & INTERNAL_SEASON_AUTUMN_LONG) ) {
				//There was class in the long part of the autumn semester
				//but this course ought not to have it.
				clear |= INTERNAL_DAYS_AUTUMN;
		}

		if(0 != (days & ( INTERNAL_DAYS_SPRING)) && 0 == (temp & INTERNAL_SEASON_SPRING_LONG) ) {

				//There was class in the long part of the spring semester
				//but this course ought not to have it.
				clear |= INTERNAL_DAYS_SPRING;
		}
		internalSkema = temp | (days & ~clear);
	}
	
	
	/**
	 * Check if this course can start in the given semester.
	 * @param semester The semester the course should start in.
	 * @return true if this course can start in the given semester.
	 */
	public boolean canStartInSemester(int semester) {
		boolean toReturn = false;
		if(this.isMultiPeriodCourse()) {
			if((semester & 1) == 1) {
				toReturn = 0 != (internalSkema & (INTERNAL_STARTING_PERIOD_AUTUMN_SHORT | INTERNAL_STARTING_PERIOD_AUTUMN_LONG));
			} else {
				toReturn = 0 != (internalSkema & (INTERNAL_STARTING_PERIOD_SPRING_SHORT | INTERNAL_STARTING_PERIOD_SPRING_LONG));				
			}
		} else {
			if((semester & 1) == 1) {
				toReturn = 0 != (internalSkema & (INTERNAL_SEASON_AUTUMN_SHORT | INTERNAL_SEASON_AUTUMN_LONG));
			} else {
				toReturn = 0 != (internalSkema & (INTERNAL_SEASON_SPRING_SHORT | INTERNAL_SEASON_SPRING_LONG));
			}
		}
		return toReturn;
	}

	/**
	 * Turns the Course into a string in the following format:
	 * 
	 * ID Name, skemagruppe: ("the DTU skema",) "perioder" (, forudgående kurser: [the ID of dependency courses]) 
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s = courseID  + " " + courseName;
		String gruppe = skemaToString();
		if(!gruppe.equals("")) {
			s += ", skemagruppe: " + gruppe;
		}
		if(0 != (internalSkema & INTERNAL_SEASON_SPRING_LONG ) ) {
			s += ", F";
		}
		if(0 != (internalSkema & INTERNAL_SEASON_SPRING_SHORT ) ) {
			s += ", Juni";
		}
		if(0 != (internalSkema & INTERNAL_SEASON_AUTUMN_LONG ) ) {
			s += ", E";
		}
		if(0 != (internalSkema & INTERNAL_SEASON_AUTUMN_SHORT ) ) {
			s += ", Januar";
		}
		if(hasDependencies()){
			s += ", forudgående kurser: " + getDependencies();
		}
		s += " Laengde: " + getAmountOfPeriods() +".";
		return s;
	}
}