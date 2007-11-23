package exceptions;

public class CorruptStudyPlanFileException extends Exception {

	private static final long serialVersionUID = -3245788830320658014L;

	private String filename;
	
	public CorruptStudyPlanFileException(String filename) {
		this.filename = filename;
	}
	
	public String getFileName() {
		return filename;
	}
	
	public String toString() {
		return "The StudyPlan file " + filename + " was corrupt";
	}
	
}
