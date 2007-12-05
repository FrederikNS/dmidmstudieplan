package exceptions;

import java.io.IOException;

/**
 * Exception for the case where the studyplan file is corrupt
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class CorruptStudyPlanFileException extends IOException {

	/**
	 * This is a serialized id used for the input/output streams
	 */
	private static final long serialVersionUID = -3245788830320658014L;

	/**
	 * The filename
	 */
	private String filename;
	
	/**
	 * Prints the error
	 * @param filename The filename
	 */
	public CorruptStudyPlanFileException(String filename) {
		super("The StudyPlan file " + filename + " was corrupt");
		this.filename = filename;
	}
	
	/**
	 * Gets the filename
	 * @return The filename
	 */
	public String getFileName() {
		return filename;
	}
}