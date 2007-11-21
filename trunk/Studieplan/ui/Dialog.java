/**
 * 
 */
package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;

/**
 * @author Morten Sørensen
 * Sets up the interface the user will see.
 */
public class Dialog {

	String input;
	BufferedReader keyboard;
	String courseID;
	String semesterNumber;
	String indtastet[];

	public Dialog() {
		intro();
		mainProgram();
	}

	/**
	 * Prints the welcome text when the program starts.
	 */
	private void intro() {
		System.out.println("Velkommen til 'Læg en Studieplan'");
		System.out.println("");
		System.out.println("Programmet kender følgende kommandoer:");
		System.out.println("hjælp  tilføj  fjern  udskrivbase  visplan  gem  hent  afslut");
		System.out.println("");
		System.out.println("Indtast dine kommandoer efter tegnet '>'");
	}

	/**
	 * The main loop which keeps everything alive.
	 */
	public void mainProgram() {
		keyboard = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("> ");
			while ((input = this.readInput()) != null) {
				String[] indtastet = input.split(" ");
				if (indtastet[0].equalsIgnoreCase("afslut")) {
					break;
				} else if (indtastet[0].equalsIgnoreCase("hjælp")) {
					helpMe();
				} else if (indtastet[0].equalsIgnoreCase("visplan")) {
					showPlan(indtastet);
				} else if (indtastet[0].equalsIgnoreCase("udskrivbase")) {
					printDatabaseList();
				} else if (indtastet[0].equalsIgnoreCase("tilføj")) {
					add(indtastet);
				} else if (indtastet[0].equalsIgnoreCase("fjern")) {
					remove(indtastet);
				}
				System.out.print("> ");
			}
		} catch (Exception e) {
		}
		end();
	}
	/**
	 * When adding a new course, it checks if there are any of the data in a wrong format
	 * (eg. too high semester number, or the course ID contains letters).
	 * @param the command "tilføj" along with data for course and semester (if any).
	 */
	private void add(String indtastet[]) {
		String temp;
		int temp2;
		boolean courseFormatOk = false;
		boolean semesterFormatOk = false;
		switch (Array.getLength(indtastet)) {
		case 3:
			//Checks if the format of course ID
			try {
				temp2 = Integer.parseInt(indtastet[1]);
				if (indtastet[1].length() == 5 && temp2 > -1) {
					courseFormatOk = true;
				}
			} catch (Exception e) {
				
			}
		case 2:
			//Checks the format of semester number
			temp = indtastet[2];
			try {
				temp2 = Integer.parseInt(temp);
				if (temp2 > 0 && temp2 <= 20 && temp2 != 0) {
					semesterFormatOk = true;
				}
			} catch (Exception e) {

			}
		case 1:
			//Breaks the switch
			break;
		}

		//Asks for a new course ID (in case the format was missing or wrong)
		if (courseFormatOk == false) {
			System.out.println("Indtast venligst kursusnummeret:");

			keyboard = new BufferedReader(new InputStreamReader(System.in));
			try {
				System.out.print("> ");
				while ((input = this.readInput()) != null) {
					String[] indtastet2 = input.split(" ");
					indtastet[1] = indtastet2[0];
					indtastet[2] = indtastet2[1];
					System.out.print("> ");
				}
			} catch (Exception e) {
			}
			
		}
		//Asks for a new semester (in case it was missing or wrong)
		if (semesterFormatOk == false) {
			System.out.println("Det indtastede data for semesternummeret var forkert.");
			System.out.println("Indtast det korrekte semesternummer:");
		}
		System.out.print("indtastet 0: ");
		System.out.println(indtastet[0]);
		System.out.print("indtastet 1: ");
		System.out.println(indtastet[1]);
		System.out.print("indtastet 2: ");
		System.out.println(indtastet[2]);
	}

	private void changeCourse(String input) {
		//TODO
	}

	private void changeSemester(String input) {
		//TODO
	}

	private void remove(String indtastet[]){
		int temp3;
		try {
			temp3 = Integer.parseInt(indtastet[1]);
			if (indtastet[1].length() == 5 && temp3 > -1) {
				//Kommando sendes til anden class
			}
			System.out.println("Det indtastede data for kursusnummeret var forkert.");
			System.out.println("Indtast det korrekte kursusnummer:");
			System.out.print("> ");
			changeCourse(input);
			remove(indtastet);
		} catch (Exception e) {

		}
	}

	/**
	 * Prints the hjælp-function.
	 */
	private void helpMe() {
		System.out.println("Programmet kender følgende kommandoer:");
		System.out.println("hjælp - viser denne hjælpe tekst samt forklaring til de forskellige kommandoer");
		System.out.println("tilføj - tilføjer et kursus til kursusplanen. Den mest optimale måde at kalde kommandoen på ville være 'tilføj kursusnummer semesternummer'");
		System.out.println("fjern - fjerner et kursus fra kursusplanen. Den mest optimale måde at kalde kommandoen på ville være 'fjern kursusnummer'");
		System.out.println("udskrivbase - udskriver en liste over kurser i databasen");
		System.out.println("visplan - viser en komplet plan over det valgte semindtastetester");
		System.out.println("gem - gemmer studieplanen så man kan arbejde videre på det senere");
		System.out.println("hent - indlæser en studieplan så det er muligt man kan arbejde videre på den");
		System.out.println("afslut - afslutter programmet");
	}
	//Prints the list over available courses
	private void printDatabaseList(){
		//kommando sendes til anden class
	}
	//Prints the plan as it currently is
	private void showPlan(String indtastet[]){
		int temp2;
		String season;
		try {
			temp2 = Integer.parseInt(indtastet[1]);
			//We expect people to only start at the 
			if ((temp2 & 1) == 1){
				season = "e";
			}
			else{
				season = "f";
			}
			if (temp2 > 0 && temp2 <= 20) {
				System.out.println("Semester: "+indtastet[1]+" "+season+"   mandag  tirsdag  onsdag  torsdag  fredag");
				System.out.println("8:00-12:00       test1    test2   test3   test4    test5");
				System.out.println("  Pause");
				System.out.println("13:00-17:00      test1    test2   test3   test4    test5");
			}
		} catch (Exception e) {

		}
	}

	/**
	 * Trims the input to get rid of spaces in the ends.
	 * If the input turns to null, it will throw an exception.
	 * @return the input without spaces
	 * @throws IOException is thrown if the input is null
	 */
	private String readInput() throws IOException {
		return keyboard.readLine().trim();
	}

	/**
	 * Exits the program.
	 */
	private void end() {
		System.out.println("Tak for idag.");
	}
}