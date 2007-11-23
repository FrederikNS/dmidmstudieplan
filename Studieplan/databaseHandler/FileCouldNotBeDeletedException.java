/**
 * 
 */
package databaseHandler;

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
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}

}
