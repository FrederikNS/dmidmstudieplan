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
 *
 */
public class StudyPlan implements Serializable {

	private static final long serialVersionUID = -6200618982406378220L;

	private String studentID;
	
	private ArrayList<SelectedCourse> plan;
	
	public StudyPlan() {
		plan = new ArrayList<SelectedCourse>();
	}
	
	public StudyPlan(String studentID) {
		this.studentID = studentID;
	}
	
	public void setStudent(String studentID) {
		this.studentID = studentID;
	}
	
	public String getStudent() {
		return studentID;
	}
	
	public boolean equals(String studentID) {
		return studentID.equalsIgnoreCase(this.studentID);
	}
	
	public boolean equals(StudyPlan study) {
		return equals(study.getStudent());
	}

	public boolean contains(String courseID) {
		return plan.contains(new Course(courseID));
	}
	
	public boolean contains(Course course) {
		return plan.contains(course);
	}
	
	public boolean add(SelectedCourse toAdd) throws CourseAlreadyExistsException, ConflictingCourseInStudyPlanException {
		if(this.contains(toAdd)) {
			throw new CourseAlreadyExistsException(toAdd.getCourseID());
		}
		int semester = toAdd.getSemester();
		boolean doAdd = false;
		
		SelectedCourse planned[] = plan.toArray(new SelectedCourse[1]);
		
		Arrays.sort( planned );
		int i = 0, currentSemester = 0;
		for( ; i < planned.length ; i++) {
			currentSemester = planned[i].getSemester();
			if(currentSemester == semester)
				break;
			if(currentSemester > semester) {
				//If we get here, nothing is planned for that semester.
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
				if(planned[i].compareSkema(toAdd)) {
					throw new ConflictingCourseInStudyPlanException(toAdd.getCourseID(), planned[i].getCourseID());
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
		String toReturn = "";
		
		for(int i = 0 ; i < planned.length ; i ++) {
			plannedSemester = planned[i].getSemester();
			
			 
			
			if(plannedSemester == semester) {
				
			}
			else if(plannedSemester > semester) {
				break;
			}
		}
		
		return toReturn;
	}

	public String toString() {
		return "StudyPlan for " + studentID;
	}
	
}
