/**
 * 
 */
package exceptions;

import java.io.IOException;

/**
 * @author Niels Thykier
 *
 */
public class FilePermissionException extends IOException {

	/**
	 * This is a serialized id used for the input/output streams
	 */
	private static final long serialVersionUID = -5271055091084146911L;
	
	/**
	 * The missing permission 
	 */
	private String missingPermissions;
	
	
	/**
	 * The exception in case the file has insufficient permissions
	 * @param missingPermissions The missing permissions
	 */
	public FilePermissionException(String missingPermissions) {
		super("Missing vital file permission(s): " + missingPermissions);
		this.missingPermissions = missingPermissions;
	}


	/**
	 * Gets the missing permissions
	 * @return the missingPermissions
	 */
	public String getMissingPermissions() {
		return missingPermissions;
	}

}
