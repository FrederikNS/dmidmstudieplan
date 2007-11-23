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
import dataClass.CorruptStudyPlanFileException;
import dataClass.StudyPlan;

/**
 * @author Niels Thykier
 *
 */
public class UserDatabase {
	
	
	public UserDatabase() {
	}

	public void exists(String file) throws FileNotFoundException {
		exists(file, "plan");
	}
	
	public void exists(String file, String extension) throws FileNotFoundException {
		File f = new File(file + "." + extension);
		if(!f.exists()) 
			throw new FileNotFoundException(file + "." + extension);
	}
	
	public void saveStudyPlan(StudyPlan plan) throws CannotSaveStudyPlanException, IOException {
		String studentID = plan.getStudent();
		
		if(studentID == null || studentID.equals(""))
			throw new CannotSaveStudyPlanException(plan, "Invalid StudentID");
		
		File f =  new File(studentID + ".plan");
			
		try {
			exists(studentID + ".plan");
			
		}catch (Exception exists) {
			try {
				f.createNewFile();
			} catch (FileNotFoundException creation) {
				throw new CannotSaveStudyPlanException(plan, "Could not create file");
			}
		}
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new ObjectOutputStream(new FileOutputStream(f)) );
			oos.writeObject(plan);
		} catch(FileNotFoundException e) {
			throw new CannotSaveStudyPlanException(plan, "ObjectOutputStream failed to store StudyPlan object");	
		}
	}
	
	public StudyPlan loadStudyPlan(String studentID) throws IOException, CorruptStudyPlanFileException {
		return loadStudyPlan(studentID, "plan");
	}
	
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

	public void deleteFile(String file, String extension) throws FileNotFoundException, FileCouldNotBeDeletedException{
		String filename = file + "." + extension;
		exists(file, extension); 

		if(!new File(filename).delete())
			throw new FileCouldNotBeDeletedException(file + "." + extension);
	}
	
	public void deleteFile(String file) throws FileNotFoundException, FileCouldNotBeDeletedException {
		deleteFile(file, "plan");
	}
	
}
