/**
 * 
 */
package databaseHandler;

/**
 * @author Niels Thykier
 *
 */
public interface DatabaseHandler {
	
	enum DatabaseFiles {
	KRAV("kursuskrav.txt"),
	NAVN("kursusnavne.txt"),
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
