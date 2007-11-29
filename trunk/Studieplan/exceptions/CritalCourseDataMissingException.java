/**
 * 
 */
package exceptions;

/**
 * @author Niels Thykier
 * Thrown whenever critial course data is missing from the databases.
 */
public class CritalCourseDataMissingException extends Exception {

	private static final long serialVersionUID = -5832061883441907911L;
	/**
	 * Name of the data type that is missing 
	 */
	private String missingData;
	
	/**
	 * @param missingData Name of the data type that is missing.
	 */
	public CritalCourseDataMissingException(String missingData) {
		super("The critial course data \"" + missingData + "\" could not be read in the database");
		this.missingData = missingData;
	}
	
	public String getMissingData() {
		return missingData;
	}
	
}