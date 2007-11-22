package ui;

abstract class UI {
	
	private Core core;
	
	protected UI(Core core) {
		this.core = core;
	}
	
	protected Core getCore() {
		return core;
	}
	
}
