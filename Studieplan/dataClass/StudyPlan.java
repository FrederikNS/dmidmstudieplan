/**
 * 
 */
package dataClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import exceptions.AnotherCourseDependsOnThisCourseException;
import exceptions.ConflictingCourseInStudyPlanException;
import exceptions.CourseAlreadyExistsException;
import exceptions.CourseCannotStartInThisSemesterException;
import exceptions.CourseIsMissingDependenciesException;

/**
 * A StudyPlan containing a list of courses and a studentID.
 * @author Niels Thykier
 */
public class StudyPlan implements Serializable {
	
	/**
	 * serialVersionUID needed so that this class can be Serializable. 
	 */
	private static final long serialVersionUID = -6200618982406378220L;

	/**
	 * ID of the student (or otherwise unique identifier) linking the plan to the student.
	 */
	private String studentID;

	/**
	 * The list of Courses taken.
	 */
	private ArrayList<SelectedCourse> plan;
	/**
	 * The list of Courses, that is depended on.
	 */
	private ArrayList<Course> dependencyCourses;

	/**
	 * Contructor used when loading from this class from classes via the ObjectInputStream class.
	 * @see databases.UserDatabase#loadStudyPlan(String, String)
	 */
	protected StudyPlan() {
	}

	/**
	 * Create a new StudyPlan using studentID as creditentials
	 * @param studentID The studentID of the student, who is making the plan.
	 */
	public StudyPlan(String studentID) {
		plan = new ArrayList<SelectedCourse>();
		dependencyCourses = new ArrayList<Course>();
		this.studentID = studentID;
	}

	/**
	 * Rename the owner of this StudyPlan.
	 * If param is null, nothing will happen.
	 * @param studentID The ID of the new owner.
	 * @throws NullPointerException if no studentID is specified
	 */
	public void setStudent(String studentID) throws NullPointerException {
		if(studentID != null) {
			this.studentID = studentID;
		}
	}

	/**
	 * Get the ID of the student, who is making this plan.
	 * @return The student ID.
	 */
	public String getStudent() {
		return studentID;
	}

	/**
	 * Overloading of the Object.equals method. 
	 * This specifies that two StudyPlans are the same if they studenID fields are equal.
	 * 
	 * This overloading couses the ArrayList<StudyPlan>.contains(StudyPlan) to return true 
	 * if a StudyPlan with the same studentID is already in that ArrayList.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj instanceof StudyPlan) {
			StudyPlan plan = (StudyPlan) obj;
			return plan.getStudent().equalsIgnoreCase(this.studentID);
		}

		return false;
	}

	/**
	 * Test if a course has already been added to the plan.
	 * @param courseID The ID of the course to test for.
	 * @return true if the course is already in the plan.
	 */
	public boolean contains(String courseID) {
		return this.contains(new Course(courseID, " "));
	}

	/**
	 * Test if a course has already been added to the plan.
	 * @param course The course to test for.
	 * @return true if the course is already in the plan.
	 */
	public boolean contains(Course course) {
		if(plan.isEmpty())
			return false;
		return plan.contains(course);
	}

	/**
	 * Adds a course to the StudyPlan
	 * @param toAdd The SelectedCourse to add.
	 * @return True if the course was added.
	 * @throws CourseAlreadyExistsException Thrown if the course was already added to the plan.
	 * @throws ConflictingCourseInStudyPlanException Thrown if another course had at least one lession at the same time as the one to be added.
	 * @throws CourseIsMissingDependenciesException Thrown if the course being added has unmet dependencies.
	 * @throws CourseCannotStartInThisSemesterException Thrown if the course being added cannot start in the semester it wants to. 
	 */
	public boolean add(SelectedCourse toAdd) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException, CourseIsMissingDependenciesException, CourseCannotStartInThisSemesterException {
		if(this.contains(toAdd)) {
			throw new CourseAlreadyExistsException(toAdd.getCourseID());
		}
		if(!toAdd.canStartInSemester(toAdd.getStartingSemester()) ) {
			throw new CourseCannotStartInThisSemesterException(toAdd.getCourseID(), toAdd.getStartingSemester());
			
		}
		
		int missingDependencies = toAdd.getAmountOfDependencies();
		String dependencies = toAdd.getDependencies();
		String met = null;
		ArrayList<Course> temp = new ArrayList<Course>();
		
		if(!plan.isEmpty()) {
			SelectedCourse planned[] = plan.toArray(new SelectedCourse[1]);

			Arrays.sort( planned );
			int i = 0, placement = 0;
			for( ; i < planned.length ; i++) {
				placement = planned[i].getIsInSameSemester(toAdd);
				if(missingDependencies != 0 && placement < 0) {
					if(dependencies.contains(planned[i].getCourseID())) {
						missingDependencies--;
						met += planned[i].getCourseID()+ " ";
						temp.add(planned[i]);
					}
				}
				else if(placement == 0) {
					if(planned[i].conflictingSkema(toAdd)) {
						throw new ConflictingCourseInStudyPlanException(toAdd.getCourseID(), planned[i].getCourseID());
					}
				}

			}
		}
		
		if(missingDependencies > 0) {
			if(met != null) {
				String[] metArray = met.trim().split(" ");
				for(int i = 0 ; i < metArray.length ; i++) {
					dependencies = dependencies.replaceFirst(metArray[1], "").trim();
				}
			}
			throw new CourseIsMissingDependenciesException(toAdd.getCourseID(), dependencies);
		}
		for(int i = 0 ; i < temp.size(); i++) {
			if(!dependencyCourses.contains(temp.get(i))) {
				dependencyCourses.add(temp.get(i));
			}
		}
		System.out.println();
		return plan.add(toAdd);
	}

	/**
	 * Removes a course from the plan, if it was added.
	 * @param toRemove The ID of the course to remove.
	 * @return true if the course was in the list.
	 * @throws AnotherCourseDependsOnThisCourseException Thrown if another course depends on the course that was to be removed. 
	 * @throws IllegalArgumentException Thrown if the ID of the course would cause the Course(String, String) constructor to fail.
	 * @see dataClass.Course#Course(String, String) 
	 */
	public boolean remove(String toRemove) throws IllegalArgumentException, AnotherCourseDependsOnThisCourseException {
		return remove(new Course(toRemove, " ") );
	}

	/**
	 * Used by StudyPlan to recalculate the dependencies stucture.
	 */
	private void recalculateDependencies() {
		SelectedCourse[] list = plan.toArray(new SelectedCourse[1]);
		if(list == null || list[0] == null) 
			return;
		Arrays.sort(list);
		String dependencyList = "";
		dependencyCourses = new ArrayList<Course>();
		for(int i = list.length-1 ; i > -1 ; i--) {
			if(list[i].hasDependencies()) {
				dependencyList += list[i].getDependencies();
			}
			if(dependencyList.contains(list[i].getCourseID())) {
				dependencyCourses.add(list[i]);
			}
		}
	}
	
	/**
	 * Removes a course from the plan, if it was added.
	 * @param toRemove The course to remove.
	 * @return true if the course was in the list.
	 * @throws AnotherCourseDependsOnThisCourseException Thrown if another course depends on the course that was to be removed. 
	 */
	public boolean remove(Course toRemove) throws AnotherCourseDependsOnThisCourseException {
		if(!plan.contains(toRemove)) {
			return false;
		}
		if(dependencyCourses.contains(toRemove)) {
			throw new AnotherCourseDependsOnThisCourseException(toRemove.getCourseID(), "");
		}
		boolean toReturn = plan.remove(toRemove);
		recalculateDependencies();
		return toReturn;
	}

	/**
	 * This will make a String that will contain a full skema over a single semester.
	 * 
	 * It will print both the long and the short period of the semester.
	 * 
	 * @param semester The semester it should print.
	 * @return The skema for the semester.
	 * @throws IllegalArgumentException Thrown if SelectedCourse.isValidSemester(int) would return false with semester as input.
	 */
	public String printSemester(int semester) throws IllegalArgumentException {
		if(!SelectedCourse.isValidSemester(semester)) {
			throw new IllegalArgumentException();
		} 
		int semesterEnd = (semester<<1);
		int semesterStart = semesterEnd - 1;
		SelectedCourse planned[] = plan.toArray(new SelectedCourse[1]);		
		Arrays.sort( planned );
		long semesterPattern = 0L;
		int shiftShort = 0;
		int shiftLong = 0;
		
		if( (semester & 1) == 1) {
			semesterPattern = Course.INTERNAL_DAYS_AUTUMN_LONG | Course.INTERNAL_DAYS_AUTUMN_SHORT;
			shiftShort = Course.INTERNAL_SHIFT_AUTUMN_SHORT;
			shiftLong = Course.INTERNAL_SHIFT_AUTUMN_LONG;
		} else {
			semesterPattern = Course.INTERNAL_DAYS_SPRING_LONG | Course.INTERNAL_DAYS_SPRING_SHORT;
			shiftShort = Course.INTERNAL_SHIFT_SPRING_SHORT;
			shiftLong = Course.INTERNAL_SHIFT_SPRING_LONG;
		}
		
	    long skema = 0L, skemaTest = 0L;
		String toReturn = "";


		String[][] courses = {{"-----","-----","-----","-----","-----",
				  				"-----","-----","-----","-----","-----"},
				  			   {"-----","-----","-----","-----","-----",
				  				"-----","-----","-----","-----","-----"}};
		
		for(int i = 0 ; i < planned.length ; i++) {
			
			if(planned[i].getIsInPeriod(semesterStart, semesterEnd) == 0) {
				skema = planned[i].getFullSkemaData() & semesterPattern;
				
				for(int period = 0 ; period < 2 ; period++) {
					if(period == 0) {
						skemaTest = skema >> shiftLong;
					} else {
						skemaTest = skema >> shiftShort;
					}
					if(skemaTest != 0) {
						for(int j = 0 ; j < 5 ; j++) {
							if(((skemaTest>>j) & 1) == 1) {
								courses[period][j] = planned[i].getCourseID();
							}
						}				
						if((skemaTest & Course.INTERNAL_WEDNESDAY_AFTERNOON) != 0)  {
							courses[period][5] = planned[i].getCourseID();
						}
						if((skemaTest & Course.INTERNAL_THURSDAY_MORNING) != 0)  {
							courses[period][6] = planned[i].getCourseID();
						}
						if((skemaTest & Course.INTERNAL_THURSDAY_AFTERNOON) != 0)  {
							courses[period][7] = planned[i].getCourseID();
						}
						if((skemaTest & Course.INTERNAL_FRIDAY_MORNING) != 0)  {
							courses[period][8] = planned[i].getCourseID();
						}
						if((skemaTest & Course.INTERNAL_FRIDAY_AFTERNOON) != 0)  {
							courses[period][9] = planned[i].getCourseID();
						}
					}
				}
			}
		}

		String semesterString = (semester < 10?" ":"") + semester;
		toReturn  = "Semester: "+ semesterString +" "+((semester&1)==1?"e":"f")+" 13-ugers  mandag  tirsdag  onsdag  torsdag  fredag\n";
		toReturn += " 8:00-12:00              "+ courses[0][0] + "    "+ courses[0][2] + "   "+ courses[0][4] + "   "+ courses[0][6] + "    "+ courses[0][8] + "\n";
		toReturn += "  Pause\n";
		toReturn += "13:00-17:00              "+ courses[0][1] + "    "+ courses[0][3] + "   "+ courses[0][5] + "   "+ courses[0][7] + "    "+ courses[0][9] + "\n";
		toReturn += "3-ugers perioden af semester: " + semesterString + "\n";
		toReturn += " 8:00-12:00              "+ courses[1][0] + "    "+ courses[1][2] + "   "+ courses[1][4] + "   "+ courses[1][6] + "    "+ courses[1][8] + "\n";
		toReturn += "  Pause\n";
		toReturn += "13:00-17:00              "+ courses[1][1] + "    "+ courses[1][3] + "   "+ courses[1][5] + "   "+ courses[1][7] + "    "+ courses[1][9];		
		return toReturn;
	}

	/**
	 * Generates a short String containing who this study plan belongs too.
	 * 
	 * To see the courses on a given semester, use printSemester()
	 * 
	 * @see #printSemester(int)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s= "StudyPlan for " + studentID;
		for(int i = 0 ; i < plan.size(); i++) {
			s+= "\n" + plan.get(i).toString();
		}	 
		return s;  
	}

}
