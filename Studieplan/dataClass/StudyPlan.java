/**
 * 
 */
package dataClass;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Niels Thykier
 *
 */
public class StudyPlan extends ArrayList<SelectedCourse> implements Serializable {

	private static final long serialVersionUID = -5338908112285902395L;

	private String studentID;
	
	public StudyPlan() {
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
	
}
