/**
 * 
 */
package databases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.GenericDeclaration;
import java.util.ArrayList;
import java.util.Iterator;

import exceptions.ConflictingCourseInStudyPlanException;
import exceptions.CorruptStudyPlanFileException;
import exceptions.CourseAlreadyExistsException;
import exceptions.FileCouldNotBeDeletedException;
import exceptions.FilePermissionException;
import dataClass.SelectedCourse;
import dataClass.StudyPlan;
import exceptions.CannotSaveStudyPlanException;

/**
 * Handles the saving and loading of StudyPlans.
 * @author Niels Thykier
 */
public class UserDatabase {


	/**
	 * Constuctor
	 */
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
	 * Saves a StudyPlan using the studentID as name using a ".plan" extension
	 * @param plan The StudyPlan you wish to save. (it will be stored as the "studentID.plan")
	 * @throws CannotSaveStudyPlanException if the plan could not be saved.
	 * @throws FilePermissionException if the needed permissions were missing.
	 */
	public void saveStudyPlan(StudyPlan plan) throws CannotSaveStudyPlanException, FilePermissionException {
		String studentID = plan.getStudent();

		if(studentID == null || studentID.equals(""))
			throw new CannotSaveStudyPlanException(plan, "Invalid StudentID");

		File f =  new File(studentID + ".plan");

		try {
			exists(studentID + ".plan");
			deleteFile(studentID + ".plan");
			f.createNewFile();			
		}catch (Exception exists) {
			try {
				f.createNewFile();
			} catch (IOException creation) {
				throw new CannotSaveStudyPlanException(plan, "Could not create file");
			}
		}

		if(!f.canWrite() ) 
			throw new FilePermissionException("write");
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(plan);
		} catch(IOException e) {
			throw new CannotSaveStudyPlanException(plan, "ObjectOutputStream failed to store StudyPlan object");	
		}
		ArrayList<SelectedCourse> list = plan.getCourses();
		try {
			oos.writeObject(list);
		} catch (IOException e) {
			System.out.println("Saving a course caused IO Exception");
			System.out.println(e);
		}


	}

	/**
	 * This is the same as calling loadStudyPlan(studentID, "plan");
	 * @param studentID load the StudyPlan for the Student ID.
	 * @return the StudyPlan
	 * @throws FileNotFoundException if no filed has been saved for that student.
	 * @throws IOException if Java classes failed.
	 * @throws CorruptStudyPlanFileException if the StudyPlan file existed, but was not valid/readable.
	 * @throws FilePermissionException If needed permissions were missing.
	 */
	public StudyPlan loadStudyPlan(String studentID) throws FilePermissionException, FileNotFoundException, IOException, CorruptStudyPlanFileException {
		return loadStudyPlan(studentID, "plan");
	}

	/**
	 * Load a StudyPlan from a file.
	 * @param file the name of the file without extension and without trailing dot.
	 * @param extension the extension of the file without leading dot.
	 * @return The StudyPlan
	 * @throws FileNotFoundException if no filed has been saved for that student.
	 * @throws CorruptStudyPlanFileException If a file was saved for that student, but it contained garbage.
	 * @throws FilePermissionException If needed permissions were missing.
	 */
	public StudyPlan loadStudyPlan(String file, String extension) throws FilePermissionException, FileNotFoundException, CorruptStudyPlanFileException {

		//throws FileNotFoundException if it does not exist.
		exists(file, extension);


		File f = new File(file + "." + extension);
		if(!f.canRead()) 
			throw new FilePermissionException("read");
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(f)) ;
		} catch (IOException e1) {
			System.err.println("1: " + e1);
			throw new CorruptStudyPlanFileException(file + "." + extension);
		}

		Object obj;
		try {
			obj = ois.readObject();
		} catch (ClassNotFoundException e) {
			throw new CorruptStudyPlanFileException(file + "." + extension);
		} catch (IOException e) {
			System.err.println("2: " + e);
			throw new CorruptStudyPlanFileException(file + "." + extension);
		}
		if(obj == null)
			throw new CorruptStudyPlanFileException(file + "." + extension);
		if(!(obj instanceof StudyPlan)) 
			throw new CorruptStudyPlanFileException(file + "." + extension);

		StudyPlan toReturn = (StudyPlan) obj;
		System.out.println("loading courses...");
		try {
			obj = ois.readObject();
			System.out.println("Found something");

			System.out.println(obj.getClass().getName());
			if(obj instanceof ArrayList) {
				ArrayList arrayList = (ArrayList) obj;
				System.out.println("ArrayList " + arrayList.size());

				for(int i = 0 ; i < arrayList.size() ; i++) {
					System.out.println("Item: " + i);	
					try {
						Object loop = arrayList.get(i);
						if(loop == null) {
							System.out.println("is null");
						}
						else if(loop instanceof SelectedCourse) {
							try {
								System.out.println("Selected Course");
								SelectedCourse toAdd = null;
								try {
									toAdd = (SelectedCourse) loop;
								}catch(Throwable e){
									System.err.println(e);
								}
								try{
									toReturn.add(toAdd);
									System.out.println("Added");
								}catch(Throwable e){
									System.err.println(e);
								}
							} catch (Exception e) {
								System.out.println(e);

							}
						}
					} catch(Throwable e) {
						System.out.println(e);
					}
				}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return toReturn;
		}

		/**
		 * Same as calling deleteFile(file, "plan");
		 * @param file name of file without extension and without trailing dot.
		 * @param extension the extension of the file without leading dot.
		 * @throws FileNotFoundException if the file did not exist.
		 * @throws FileCouldNotBeDeletedException if the file could not be deleted.
		 * @throws FilePermissionException If needed permissions were missing. 
		 */
		public void deleteFile(String file, String extension) throws FileNotFoundException, FileCouldNotBeDeletedException, FilePermissionException{
			String filename = file + "." + extension;
			exists(file, extension); 
			File f = new File(filename);

			if(!f.canWrite())
				throw new FilePermissionException("write");

			if(!f.delete())
				throw new FileCouldNotBeDeletedException(file + "." + extension);
		}

		/**
		 * Same as calling deleteFile(file, "plan");
		 * @param file name of file without extension.
		 * @throws FileNotFoundException if the file did not exist.
		 * @throws FileCouldNotBeDeletedException if the file could not be deleted.
		 * @throws FilePermissionException If needed permissions were missing.
		 */
		public void deleteFile(String file) throws FileNotFoundException, FileCouldNotBeDeletedException, FilePermissionException {
			deleteFile(file, "plan");
		}

	}
