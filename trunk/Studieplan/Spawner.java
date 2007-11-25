import dataClass.ProgramCore;

/**
 * This is our spawner. It's the only class that contains the method "main", and therefore
 * it's the first java will run. This class will make an instance of the default UI, and it
 * will take care of the rest.
 * @author Morten Sørensen
 *
 */
public class Spawner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new ProgramCore(args);
		} catch (Exception e) {
			System.exit(1);
		}
	}

}