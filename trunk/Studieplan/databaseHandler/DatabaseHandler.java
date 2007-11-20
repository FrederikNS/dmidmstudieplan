/**
 * 
 */
package databaseHandler;

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
		
	DatabaseFiles(String filename) {
		this.filename = filename;
	}
		
	public String toString() {
		return filename;
	}
	
	}
}
