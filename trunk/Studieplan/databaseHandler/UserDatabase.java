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

	public boolean exists(String file) {
		return exists(file, "plan");
	}
	
	public boolean exists(String file, String extension) {
		File f = new File(file + "." + extension);
		return f.exists();
	}
	
	public boolean saveStudyPlan(StudyPlan plan) throws IOException {
		String studentID = plan.getStudent();
		if(studentID == null || studentID.equals(""))
			return false;
		File f = new File(studentID + ".plan");
		if(!f.exists()) {
			try {
				f.createNewFile();
			}catch (FileNotFoundException e) {
				
			}
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new ObjectOutputStream(new FileOutputStream(f)) );
			oos.writeObject(plan);
		} catch(FileNotFoundException e) {
			
		}
		return true;
	}
	
	public StudyPlan loadStudyPlan(String studentID) throws IOException, CorruptStudyPlanFileException {
		return loadStudyPlan(studentID, "plan");
	}
	
	public StudyPlan loadStudyPlan(String file, String extension) throws IOException, CorruptStudyPlanFileException {
		if(!exists(file, extension)) {
			throw new FileNotFoundException();
		}
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

	public boolean deleteFile(String file, String extension) {
		String filename = file + "." + extension;
		if(!exists(file, extension)) 
			return false;
		return new File(filename).delete();
	}
	
	public boolean deleteFile(String file) {
		return deleteFile(file, "plan");
	}
	
}
