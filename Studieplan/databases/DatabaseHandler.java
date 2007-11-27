/**
 * 
 */
package databases;

/**
 * @author Niels Thykier
 * Interface implemented by all classes that write to or read from databases.
 */
public interface DatabaseHandler {
	
	/**
	 * @author Niels Thykier
	 * enum that handles the (file-)name (and the type) of the databases.
	 */
	enum DatabaseFiles {
	/**
	 * The dependency database
	 */
	KRAV("kursuskrav.txt"),
	/**
	 * The name database 
	 */
	NAVN("kursusnavne.txt"),
	/**
	 * The Skema database
	 */
	SKEMA("kursusskema.txt");
	
	private String filename;
		
	/**
	 * Constructor for the enums
	 * @param filename
	 */
	DatabaseFiles(String filename) {
		this.filename = filename;
	}
		
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return filename;
	}
	
	}
}
