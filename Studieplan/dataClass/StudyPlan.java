/**
 * 
 */
package dataClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import exceptions.ConflictingCourseInStudyPlanException;
import exceptions.CourseAlreadyExistsException;
import exceptions.CourseDoesNotExistException;

/**
 * @author Niels Thykier
 * The correct StudyPlan for a user.
 */
public class StudyPlan implements Serializable, CourseSkemaData {

	private static final long serialVersionUID = -6200618982406378220L;

	private String studentID;
	
	private ArrayList<SelectedCourse> plan;
	
	public StudyPlan() {
		plan = new ArrayList<SelectedCourse>();
	}
	
	public StudyPlan(String studentID) {
		plan = new ArrayList<SelectedCourse>();
		this.studentID = studentID;
	}
	
	public void setStudent(String studentID) {
		this.studentID = studentID;
	}
	
	public String getStudent() {
		return studentID;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof StudyPlan) {
			StudyPlan plan = (StudyPlan) obj;
			return plan.getStudent().equalsIgnoreCase(this.studentID);
		}
		
		return false;
	}

	public boolean contains(String courseID) {
		return this.contains(new Course(courseID));
	}
	
	public boolean contains(Course course) {
		if(plan.isEmpty())
			return false;
		return this.contains(course.getCourseID());
	}
	
	public boolean add(SelectedCourse toAdd) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException {
		if(this.contains(toAdd)) {
			throw new CourseAlreadyExistsException(toAdd.getCourseID());
		}
		int semester = toAdd.getSemester();
		boolean doAdd = false;
		if(!plan.isEmpty()) {
			SelectedCourse planned[] = plan.toArray(new SelectedCourse[1]);
		
			Arrays.sort( planned );
			int i = 0, currentSemester = 0;
			for( ; i < planned.length ; i++) {
				currentSemester = planned[i].getSemester();
				if(currentSemester == semester)
					break;
				if(currentSemester > semester) {
				//	If we get here, nothing is planned for that semester.
					doAdd = true;
					break;
				}
				
			}
			if(!doAdd) {
				for( ; i < planned.length ; i++)  {
					currentSemester = planned[i].getSemester();
					if(currentSemester > semester) {
						break;
					} 
					if(planned[i].conflictingSkema(toAdd)) {
						throw new ConflictingCourseInStudyPlanException(toAdd.getCourseID(), planned[i].getCourseID());
					}
				}
			}
		}
		
		return plan.add(toAdd);
	}
	
	public boolean remove(String toRemove) throws CourseDoesNotExistException {
		return remove(new Course(toRemove) );
	}
	
	public boolean remove(Course toRemove) throws CourseDoesNotExistException {
		if(!this.contains(toRemove) ) {
			throw new CourseDoesNotExistException(toRemove.getCourseID());
		}
		
		return plan.remove(toRemove);
	}
	
	public String printSemester(int semester) throws IllegalArgumentException {
		if(semester < 0 || semester > 20) {
			throw new IllegalArgumentException();
		}
		SelectedCourse planned[] = plan.toArray(new SelectedCourse[1]);		
		Arrays.sort( planned );
		int plannedSemester;
		int skema = 0;
		String[] courses = new String[10];
		for(int i = 0 ; i < courses.length ; i++ ) {
			courses[i] = "-----";
		}
		
		String toReturn = "";
		
		for(int i = 0 ; i < planned.length ; i ++) {
			plannedSemester = planned[i].getSemester();
			
			 
			
			if(plannedSemester == semester) {
				skema = planned[i].getInternalSkemaRepresentation();
				for(int x = 0 ; x < 10 ; x++) {
					if(0 != (skema >> x) ) {
						courses[x] = planned[i].getCourseID();
					}
				}
				
			} else if(plannedSemester > semester) {
				break;
			}
		}
		
		
		
		toReturn = "Semester: "+semester+" "+((semester&1)==1?"e":"f")+"   mandag  tirsdag  onsdag  torsdag  fredag\n";
		toReturn += " 8:00-12:00       "+ courses[0] + "    "+ courses[2] + "   "+ courses[4] + "   "+ courses[6] + "    "+ courses[8] + "\n";
		toReturn += "  Pause";
		toReturn += "13:00-17:00       "+ courses[1] + "    "+ courses[3] + "   "+ courses[5] + "   "+ courses[7] + "    "+ courses[9] + "\n";
		
		return toReturn;
	}

	public String toString() {
		return "StudyPlan for " + studentID;
	}
	
}
