/**
 * 
 */
package dataClass;

/**
 * @author Niels Thykier
 *
 */
public class CritalCourseDataMissingException extends Exception {

	private static final long serialVersionUID = -5832061883441907911L;
	private String missingData;
	
	public CritalCourseDataMissingException(String missingData) {
		this.missingData = missingData;
	}
	
	public String toString() {
		return "The critial course data \"" + missingData + "\" could not be read in the database";
	}
}
