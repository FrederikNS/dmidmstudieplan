/**
 * 
 */
package exceptions;

import java.io.IOException;

/**
 * @author Niels Thykier
 *
 */
public class FileCouldNotBeDeletedException extends IOException {


	private static final long serialVersionUID = 7368405997918259177L;
	private String filename;
	/**
	 * 
	 */
	public FileCouldNotBeDeletedException(String filename) {
		super("The file " + filename + " could not be deleted!");
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}

}
