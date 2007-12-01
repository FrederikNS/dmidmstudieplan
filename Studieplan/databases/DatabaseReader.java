/**
 * 
 */
package databases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dataClass.Course;
import exceptions.FilePermissionException;

/**
 * Reads and parses all the database files. This is used by CourseBase to load all the courses.
 * @author Niels Thykier
 */
public class DatabaseReader {

	/**
	 * enum that handles the (file-)name (and the type) of the databases.
	 * Each enum contains a filename of one of the databasefiles.
	 * @author Niels Thykier
	 */
	enum DatabaseFiles {
		/**
		 * The name database for long courses. 
		 */
		NAME_LONG("navne.txt"),
		/**
		 * The name database for short courses.
		 */
		NAME_SHORT("navne3uger.txt"),
		/**
		 * The cross semester databases for courses extending more than one period/semester.
		 */
		MULTI_PERIOD("flerperioder.txt"),
		/**
		 * The short course database for course appearing in three week periods and are not multi-period courses.
		 */
		SHORT_COURSE("skemagrp3uger.txt"),
		/**
		 * The Skema database
		 */
		SKEMA("skgrpKrav13.txt"),
		/**
		 * The dependency database
		 */
		DEPENDENCY("forud.txt");

		/**
		 * The filename related to the given enum.
		 */
		private String filename;

		/**
		 * Constructor for the enums
		 * @param filename
		 */
		DatabaseFiles(String filename) {
			this.filename = filename;
		}

		/**
		 * Used to get the filename related to the enum.
		 * @return The filename
		 * @see java.lang.Enum#toString()
		 */
		public String toString() {
			return filename;
		}

	}


	/**
	 * Files objects, the databases loaded into memory.
	 */
	private BufferedReader database[] = new BufferedReader[DatabaseFiles.values().length];

	
	/**
	 * Opens the three database files for reading.
	 * @throws FileNotFoundException If one of the database files are missing.
	 * @throws FilePermissionException if permissions is missing to read the file
	 */
	public DatabaseReader() throws FileNotFoundException, FilePermissionException {
		DatabaseFiles[] array  = DatabaseFiles.values();
		for(int i = 0 ; i < array.length ; i++) {
			database[i]  = openFile(array[i].toString());
		}
	}

	/**
	 * This is called by the constructor to open the database files.
	 * @param filename The name of the file to open.
	 * @return A BufferedReader to read the file.
	 * @throws FileNotFoundException if the file is not found.
	 * @throws FilePermissionException if permissions is missing to read the file
	 */
	private BufferedReader openFile(String filename) throws FileNotFoundException, FilePermissionException {
		File f = new File(filename);
		if(!f.exists() )
			throw new FileNotFoundException(f.getAbsolutePath());
		if(!f.canRead() )
			throw new FilePermissionException("read");
		return new BufferedReader(new FileReader(f));
	}

	
	public ArrayList<Course> loadAllCourses() {
		ArrayList<Course> name = new ArrayList<Course>();
		ArrayList<Course> skema = new ArrayList<Course>();
		ArrayList<Course> multiPeriod = new ArrayList<Course>();
		ArrayList<Course> shortCourse = new ArrayList<Course>();
		String input, data[];
		long startTime = System.currentTimeMillis();
		try {
			while((input = database[DatabaseFiles.NAME_LONG.ordinal()].readLine()) != null ) {
				data = input.split(" ", 2);
				try {
					if(data[0].length() == 5 && Integer.parseInt(data[0]) >-1) {
						name.add(new Course(data[0], data[1]));
					}
				} catch(NumberFormatException e) {
				}
			}			
		} catch (IOException e) {
		}
		try {
			while((input = database[DatabaseFiles.NAME_SHORT.ordinal()].readLine()) != null ) {
				data = input.split(" ", 2);
				try {
					if(data[0].length() == 5 && Integer.parseInt(data[0]) >-1) {
						name.add(new Course(data[0], data[1]));
					}
				} catch(NumberFormatException e) {
				}
			}			
		} catch (IOException e) {
		}
		long endTimeName = System.currentTimeMillis() - startTime;
		int size = name.size();
		try {
			while((input = database[DatabaseFiles.SHORT_COURSE.ordinal()].readLine()) != null ) {
				data = input.split(" ", 2);
				try {
					if(data[0].length() == 5 && Integer.parseInt(data[0]) >-1) {
						for(int i = 0 ; i < size ; i++) {
							Course course = name.get(i);
							if(course.isSameCourseID(data[0])) {
								course.updateSeason(data[1], true);
								shortCourse.add(course);
								break;
							}
						}
					}
				}catch(NumberFormatException e) {
				}
			}
		}catch(IOException e) {
		}
		long endTimeShort = System.currentTimeMillis() - startTime;
		try {
			while((input = database[DatabaseFiles.MULTI_PERIOD.ordinal()].readLine()) != null ) {
				data = input.split(" ", 2);
				try {
					if(data[0].length() == 5 && Integer.parseInt(data[0]) >-1) {
						for(int i = 0 ; i < size ; i++) {
							Course course = name.get(i);
							if(course.isSameCourseID(data[0])) {
								course.updateSeason(data[1], true);
								multiPeriod.add(course);
								break;
							}
						}
					}
				}catch(NumberFormatException e) {
				}
			}
		}catch(IOException e) {
		}
		long endTimeMulti = System.currentTimeMillis() - startTime;
		try {
			while((input = database[DatabaseFiles.SKEMA.ordinal()].readLine()) != null ) {
				data = input.split(" ", 2);
				try {
					if(data[0].length() == 5 && Integer.parseInt(data[0]) >-1) {
						for(int i = 0 ; i < size ; i++) {
							Course course = name.get(i);
							if(course.isSameCourseID(data[0])) {
								course.setSkemagruppe(data[1].split(" "), null, true);
								skema.add(course);
								break;
							}
						}

					}
				} catch(NumberFormatException e) {
				}
			}			
		} catch (IOException e) {
		}
		long endTimeSkema = System.currentTimeMillis() - startTime;

		
		int depending = 0;
		size = skema.size();
		try {
			while((input = database[DatabaseFiles.DEPENDENCY.ordinal()].readLine()) != null ) {
				data = input.split(" ", 2);
				try {
					if(data[0].length() == 5 && Integer.parseInt(data[0]) >-1) {
						for(int i = 0 ; i < size ; i++) {
							Course course = skema.get(i);
							if(course.isSameCourseID(data[0])) {
								course.setDependencies(data[1]);
								depending++;
							}
						}

					}
				} catch(NumberFormatException e) {
					System.out.println(e);
				}
			}			
		} catch (IOException e) {
			
		}
		long endTimeDepend = System.currentTimeMillis() - startTime;
		
		
		Course temp1, temp2;
		int doubleCourses = 0;
		for(int i = 0 ; i < skema.size() ; i++) {
			temp1 = skema.get(i);
			for(int j = i ; j < skema.size() ; j++) {
				temp2 = skema.get(j);
				if( (i < j) && temp1.equals(temp2) ) {
					skema.remove(j);
					doubleCourses++;
				}
				
			}
		}
		long end = System.currentTimeMillis() - startTime;
		int idleness = 0;
		while((System.currentTimeMillis() - startTime) < 290 ) {
			//asm("nop");
			idleness++;
		}
		
		System.out.println("Load Stastistics:");
		System.out.println("Total load time: " + end + "ms.");
		System.out.println("Half-times: ");
		System.out.println(" - name load: " + endTimeName + "ms");
		System.out.println(" - short course load: " + endTimeShort + "ms");
		System.out.println(" - multi-period load: " + endTimeMulti + "ms");
		System.out.println(" - skema load: " + endTimeSkema + "ms");
		System.out.println(" - dependency load: " + endTimeDepend + "ms");
		System.out.println("Idle time: " + (290 - end )+ "ms. \"NOP\"s: " + idleness);
		System.out.println("Total amount of courses: " + skema.size());
		System.out.println(" - Short courses: " + shortCourse.size());
		System.out.println(" - Multi-period courses: " + multiPeriod.size());
		System.out.println("Removed " + doubleCourses + " courses apearing twice.");
		return skema;
	}
}
