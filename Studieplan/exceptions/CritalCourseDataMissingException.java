/**
 * 
 */
package exceptions;

/**
 * Thrown whenever critial course data is missing from the databases.
 * 
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class CritalCourseDataMissingException extends Exception {

	/**
	 * This is a serialized id used for the input/output streams
	 */
	private static final long serialVersionUID = -5832061883441907911L;
	
	/**
	 * Name of the data type that is missing 
	 */
	private String missingData;
	
	/**
	 * Prints the error message
	 * @param missingData Name of the data type that is missing.
	 */
	public CritalCourseDataMissingException(String missingData) {
		super("The critial course data \"" + missingData + "\" could not be read in the database");
		this.missingData = missingData;
	}
	
	/**
	 * gets the "missing data"
	 * @return the "missing data"
	 */
	public String getMissingData() {
		return missingData;
	}	
}