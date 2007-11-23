/**
 * 
 */
package databaseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import exceptions.CorruptStudyPlanFileException;
import exceptions.FileCouldNotBeDeletedException;
import dataClass.StudyPlan;
import exceptions.CannotSaveStudyPlanException;

/**
 * @author Niels Thykier
 *
 */
public class UserDatabase {
	
	
	public UserDatabase() {
	}

	/**
	 * Test if a .plan file exists. This is the same as 
	 * exists(file, "plan");
	 * @param file the name of the file (without extension)
	 * @throws FileNotFoundException if the file is not found.
	 */
	public void exists(String file) throws FileNotFoundException {
		exists(file, "plan");
	}
	
	/**
	 * Test if a file exists.
	 * @param file the name of the file without extension (without trailing . )
	 * @param extension the extension of the file (without leading .)
	 * @throws FileNotFoundException if file not found.
	 */
	public void exists(String file, String extension) throws FileNotFoundException {
		File f = new File(file + "." + extension);
		if(!f.exists()) 
			throw new FileNotFoundException(file + "." + extension);
	}
	
	/**
	 * @param plan The StudyPlan you wish to save. (it will be stored as the "studentID.plan")
	 * @throws CannotSaveStudyPlanException if the plan could not be saved.
	 */
	public void saveStudyPlan(StudyPlan plan) throws CannotSaveStudyPlanException {
		String studentID = plan.getStudent();
		
		if(studentID == null || studentID.equals(""))
			throw new CannotSaveStudyPlanException(plan, "Invalid StudentID");
		
		File f =  new File(studentID + ".plan");
			
		try {
			exists(studentID + ".plan");
			
		}catch (Exception exists) {
			try {
				f.createNewFile();
			} catch (IOException creation) {
				throw new CannotSaveStudyPlanException(plan, "Could not create file");
			}
		}
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new ObjectOutputStream(new FileOutputStream(f)) );
			oos.writeObject(plan);
		} catch(IOException e) {
			throw new CannotSaveStudyPlanException(plan, "ObjectOutputStream failed to store StudyPlan object");	
		}
	}
	
	/**
	 * This is the same as calling loadStudyPlan(studentID, "plan");
	 * @param studentID load the StudyPlan for the Student ID.
	 * @return the StudyPlan
	 * @throws FileNotFoundException if no filed has been saved for that student.
	 * @throws IOException if Java classes failed.
	 * @throws CorruptStudyPlanFileException if the StudyPlan file existed, but was not valid/readable.
	 */
	public StudyPlan loadStudyPlan(String studentID) throws FileNotFoundException, IOException, CorruptStudyPlanFileException {
		return loadStudyPlan(studentID, "plan");
	}
	
	/**
	 * Load a StudyPlan from a file.
	 * @param file the name of the file without extension and without trailing dot.
	 * @param extension the extension of the file without leading dot.
	 * @return The StudyPlan
	 * @throws FileNotFoundException if no filed has been saved for that student.
	 * @throws IOException if Java classes failed.
	 * @throws CorruptStudyPlanFileException if no filed has been saved for that student.
	 */
	public StudyPlan loadStudyPlan(String file, String extension) throws FileNotFoundException, IOException, CorruptStudyPlanFileException {
		
		//throws FileNotFoundException if it does not exist.
		exists(file, extension);
		
		File f = new File(file + "." + extension);
		ObjectInputStream ois = new ObjectInputStream(new ObjectInputStream(new FileInputStream(f)) );
		Object obj;
		try {
			obj = ois.readObject();
		} catch (ClassNotFoundException e) {
			throw new CorruptStudyPlanFileException(file + "." + extension);
		}
		if(!(obj instanceof StudyPlan)) 
			throw new CorruptStudyPlanFileException(file + "." + extension);
		
		return (StudyPlan) obj;
	}

	/**
	 * Same as calling deleteFile(file, "plan");
	 * @param file name of file without extension and without trailing dot.
	 * @param extension the extension of the file without leading dot.
	 * @throws FileNotFoundException if the file did not exist.
	 * @throws FileCouldNotBeDeletedException if the file could not be deleted.
	 */
	public void deleteFile(String file, String extension) throws FileNotFoundException, FileCouldNotBeDeletedException{
		String filename = file + "." + extension;
		exists(file, extension); 

		if(!new File(filename).delete())
			throw new FileCouldNotBeDeletedException(file + "." + extension);
	}
	
	/**
	 * Same as calling deleteFile(file, "plan");
	 * @param file name of file without extension.
	 * @throws FileNotFoundException if the file did not exist.
	 * @throws FileCouldNotBeDeletedException if the file could not be deleted.
	 */
	public void deleteFile(String file) throws FileNotFoundException, FileCouldNotBeDeletedException {
		deleteFile(file, "plan");
	}
	
}
