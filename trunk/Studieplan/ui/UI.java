package ui;

/**
 * A base class that all UserInterfaces must extend (Human <-> Computer communication)
 * 
 * The UI will be constructed by a Core class, which will call the UI.start() command, when 
 * the core is ready for User interaction. 
 * @see Core
 * @author Morten Sørensen
 */
public abstract class UI {
	
	/**
	 * This sets up a reference from the name core to the part of the program named Core
	 */
	private Core core;
	
	/**
	 * Checks if the core is alive
	 * @param core This is the reference to Core
	 * @throws IllegalArgumentException Thrown if core is null
	 */
	protected UI(Core core) throws IllegalArgumentException {
		if(core == null)
			throw new IllegalArgumentException();
		this.core = core;
	}
	
	/**
	 * Fetches the core
	 * @return returns the core of the program
	 */
	protected Core getCore() {
		return core;
	}
	
	/**
	 * Readies the abstract class start(), does nothing yet, just exists
	 */
	public abstract void start();	
}