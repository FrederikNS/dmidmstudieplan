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
	 * @param studentID The ID of the new owner.
	 */
	public void setStudent(String studentID) {
		this.studentID = studentID;
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
	 */
	public boolean add(SelectedCourse toAdd) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException, CourseIsMissingDependenciesException {
		if(this.contains(toAdd)) {
			throw new CourseAlreadyExistsException(toAdd.getCourseID());
		}
		int semester = toAdd.getSemester();
		int missingDependencies = toAdd.getAmountOfDependencies();
		String dependencies = toAdd.getDependencies();
		String met = null;
		ArrayList<Course> temp = new ArrayList<Course>();
		
		if(!plan.isEmpty()) {
			SelectedCourse planned[] = plan.toArray(new SelectedCourse[1]);

			Arrays.sort( planned );
			int i = 0, currentSemester = 0;
			for( ; i < planned.length ; i++) {
				currentSemester = planned[i].getSemester();
				if(missingDependencies != 0 && currentSemester < semester) {
					if(dependencies.contains(planned[i].getCourseID())) {
						missingDependencies--;
						met += planned[i].getCourseID()+ " ";
						temp.add(planned[i]);
					}
				}
				else if(currentSemester == semester) {
					if(planned[i].conflictingSkema(toAdd)) {
						throw new ConflictingCourseInStudyPlanException(toAdd.getCourseID(), planned[i].getCourseID());
					}
				}
				else if(currentSemester > semester) {
					//	If we get here, nothing is planned for that semester.
					break;
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
		dependencyCourses.addAll(temp);
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
		if(list[0] == null) 
			return;
		Arrays.sort(list);
		String dependencyList = "";
		dependencyCourses = new ArrayList<Course>();
		for(int i = list.length-1 ; i > -1 ; i--) {
			System.out.println("["+i+"]"+list[i]);
			if(list[i].hasDependencies()) {
				dependencyList += list[i].getCourseID();
			}
			if(dependencyList.contains(list[i].getCourseID())) {
				dependencyCourses.add(list[i]);
			}
		}
		
		System.out.println(studentID + " - " + dependencyList);
		for(int i = 0 ; i < dependencyCourses.size(); i++) {
			System.out.println("["+i+"] "+ dependencyCourses.get(i));
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
	 * <b>FIXME FIXME FIXME</b>
	 */
	public String printSemester(int semester) throws IllegalArgumentException {
//		FIXME
		if(semester < 0 || semester > 20) {
			throw new IllegalArgumentException();
		} 
		SelectedCourse planned[] = plan.toArray(new SelectedCourse[1]);		
		Arrays.sort( planned );
		int plannedSemester;
		int skema = 0;
		String toReturn;


		String[] courses = {"-----","-----","-----","-----","-----",
				"-----","-----","-----","-----","-----"};

		toReturn = "";

		for(int i = 0 ; i < planned.length ; i ++) {
			plannedSemester = planned[i].getSemester();


			
			/*if(plannedSemester == semester) {
				skema = planned[i].getInternalSkemaRepresentation();
				for(int x = 0 ; x < 10 ; x++) {
					if(0 != ((skema >> x) & 1) ) {
						courses[x] = planned[i].getCourseID();
					}
				}

			} else if(plannedSemester > semester) {
				break;
			}*/
		}



		toReturn = "Semester: "+semester+" "+((semester&1)==1?"e":"f")+"   mandag  tirsdag  onsdag  torsdag  fredag\n";
		toReturn += " 8:00-12:00       "+ courses[0] + "    "+ courses[2] + "   "+ courses[4] + "   "+ courses[6] + "    "+ courses[8] + "\n";
		toReturn += "  Pause";
		toReturn += "13:00-17:00       "+ courses[1] + "    "+ courses[3] + "   "+ courses[5] + "   "+ courses[7] + "    "+ courses[9] + "\n";

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
