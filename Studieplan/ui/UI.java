package ui;

/**
 * @author Morten Sørensen
 * A base class that all UserInterfaces must extend (Human <-> Computer communication)
 * 
 * The UI will be constructed by a Core class, which will call the UI.start() command, when 
 * the core is ready for User interaction. 
 * @see Core
 */
public abstract class UI {
	
	private Core core;
	
	protected UI(Core core) {
		this.core = core;
	}
	
	protected Core getCore() {
		return core;
	}
	
	public abstract void start();
	
}
