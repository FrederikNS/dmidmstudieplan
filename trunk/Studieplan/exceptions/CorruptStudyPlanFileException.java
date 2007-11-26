package exceptions;

import java.io.IOException;

public class CorruptStudyPlanFileException extends IOException {

	private static final long serialVersionUID = -3245788830320658014L;

	private String filename;
	
	public CorruptStudyPlanFileException(String filename) {
		super("The StudyPlan file " + filename + " was corrupt");
		this.filename = filename;
	}
	
	public String getFileName() {
		return filename;
	}
}
