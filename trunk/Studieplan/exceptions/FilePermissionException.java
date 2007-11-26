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

	private static final long serialVersionUID = -5271055091084146911L;
	private String missingPermissions;
	
	
	public FilePermissionException(String missingPermissions) {
		super("Missing vital file permission(s): " + missingPermissions);
		this.missingPermissions = missingPermissions;
	}


	/**
	 * @return the missingPermissions
	 */
	public String getMissingPermissions() {
		return missingPermissions;
	}

}
