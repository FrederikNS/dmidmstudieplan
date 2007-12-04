/**
 * 
 */
package exceptions;

import java.io.IOException;

/**
 * This is and exception in case the file could not be deleted
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class FileCouldNotBeDeletedException extends IOException {

	/**
	 * This is a serialized id used for the input/output streams
	 */
	private static final long serialVersionUID = 7368405997918259177L;
	
	/**
	 * This is the filname
	 */
	private String filename;
	
	/**
	 * This prints the error message
	 * @param filename 
	 */
	public FileCouldNotBeDeletedException(String filename) {
		super("The file " + filename + " could not be deleted!");
		this.filename = filename;
	}
	
	/**
	 * Gets the filename
	 * @return The filname
	 */
	public String getFilename() {
		return filename;
	}

}
