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
public class StudyPlan extends ArrayList<Course> implements Serializable {

	private static final long serialVersionUID = -5338908112285902395L;

	private String student;
	
	public StudyPlan() {
	}
	
	public StudyPlan(String student) {
		this.student = student;
	}
	
	public void setStudent(String student) {
		this.student = student;
	}
	
	public String getStudent() {
		return student;
	}
	
}
