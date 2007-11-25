/**
 * 
 */
package ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import exceptions.CorruptStudyPlanFileException;
import exceptions.FilePermissionException;
import exceptions.StudyPlanDoesNotExistException;

/**
 * @author Morten Sørensen
 * Sets up the interface the user will see.
 */
public class Dialog extends UI {

	String input;
	BufferedReader keyboard;
	String courseID;
	String semesterNumber;
	String indtastet[];

	public Dialog(Core core) throws IllegalArgumentException {
		super(core);
	}
	
	public void start(){
		intro();
		//mainProgram();
	}

	/**
	 * Prints the welcome text when the program starts.
	 */
	private void intro() {
		System.out.println("Velkommen til 'Læg en Studieplan'");
		System.out.println("");
		System.out.println("Programmet kender følgende kommandoer:");
		System.out.println("hjælp  tilføj  fjern  udskrivbase  visplan  gem  hent  viskursus  afslut");
		System.out.println("");
		System.out.println("Indtast dine kommandoer efter tegnet '>'");
	}

	/**
	 * The main loop which keeps everything alive.
	 */
	
	///* CLOSED FOR RENOVATION
	public void input() {
		keyboard = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("> ");
			while ((input = this.readInput()) != null) {
				if(input.contains(" ")==true) {
					indtastet = input.split(" ");
					crossroads();
				} else {
					
				}
			}
		} catch (Exception e) {
		}
	}//*/
	
	public boolean commandCheck(int offset){
		if (indtastet[offset].equalsIgnoreCase("afslut")) {
			return true;
		} else if (indtastet[offset].equalsIgnoreCase("hjælp")) {
			return true;
		} else if (indtastet[offset].equalsIgnoreCase("visplan")) {
			return true;
		} else if (indtastet[offset].equalsIgnoreCase("udskrivbase")) {
			return true;
		} else if (indtastet[offset].equalsIgnoreCase("tilføj")) {
			return true;
		} else if (indtastet[offset].equalsIgnoreCase("fjern")) {
			return true;
		} else if (indtastet[offset].equalsIgnoreCase("hent")) {
			return true;
		} else if (indtastet[offset].equalsIgnoreCase("gem")) {
			return true;
		} else if (indtastet[offset].equalsIgnoreCase("viskursus")){
			return true;
		} else {
			return false;
		}
	}
	
	public void crossroads() {
		if (indtastet[0].equalsIgnoreCase("afslut")) {
			end();
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
		} else if (indtastet[0].equalsIgnoreCase("hent")) {
			loadPlan(indtastet);
		} else if (indtastet[0].equalsIgnoreCase("gem")) {
			savePlan(indtastet);
		} else if (indtastet[0].equalsIgnoreCase("visplan")) {
			//TODO
		}else {
			System.out.println("Command not understood");
		}
	}
	/**
	 * When adding a new course, it checks if there are any of the data in a wrong format
	 * (eg. too high semester number, or the course ID contains letters).
	 * @param the command "tilføj" along with data for course and semester (if any).
	 */
	//THIS IS A REWRITE OF ADD, CHANGESEMESTER, CHANGECOURSE, COURSECHECK AND SEMESTERCHECK
	
	private void add(String indtastet[]) {
		while(courseCheck()==false){
			changeCourse();
		}
		while(semesterCheck()==false){
			changeSemester();
		}
		/*some code to actually set the course in the program core*/
		//TODO
	}
	
	/**
	 * Checks if the form of courseID is a possible courseID
	 * @return true if the form is correct, else false
	 */
	private boolean courseCheck() {
		boolean courseCorrect=false;
		try {
			int temp2 = Integer.parseInt(indtastet[1]);
			if(indtastet[1].length() != 5 || temp2 > -1) {
				courseCorrect = true;
			}
		} catch (Exception e) {	
		}
		return courseCorrect;
	}
	
	/**
	 * Checks if the form of semester number is a possible semester number 
	 * @return true if the form is correct, else false
	 */
	private boolean semesterCheck() {
		boolean semesterCorrect=false;
		try {
			int temp2 = Integer.parseInt(indtastet[2]);
			if(temp2 > -1 || temp2 < 20) {
				semesterCorrect = true;
			}
		} catch (Exception e) {	
		}
		return semesterCorrect;
	}
	
	private void changeCourse() {
		//TODO
	}
	
	private void changeSemester() {
		//TODO
	}
	
	//HERE ENDS THE REWRITE
	
	/*
	private void add(String indtastet[]) {
		System.out.println("add started!");
		System.out.println("add initiated");
		while(courseCheck()!=true||semesterCheck()!=true) {
			System.out.println("while loop started");
			switch (Array.getLength(indtastet)) {
			case 1:
				//Breaks the switch
				System.out.println("only got command, changing CourseID");
				changeCourse();
				System.out.println("CourseID change complete");
				break;
			case 2:
				//Checks if the format of course ID
				courseCheck();
				//System.out.println("Changin semester number");
				changeSemester();
				//System.out.println("Semester change complete");
				break;
			case 3:
				//Checks the format of semester number
				courseCheck();
				semesterCheck();
				break;
			}
		}
		System.out.print("indtastet 0: ");
		System.out.println(indtastet[0]);
		System.out.print("indtastet 1: ");
		System.out.println(indtastet[1]);
		System.out.print("indtastet 2: ");
		System.out.println(indtastet[2]);
	}

	private boolean courseCheck() {
		String temp;
		int temp2;
		boolean courseCorrect=false;
		try {
			temp2 = Integer.parseInt(indtastet[1]);
			System.out.println("CourseID is "+temp2+" characters long.");
			while(indtastet[1].length() != 5 || temp2 > -1) {
				System.out.println("CourseID incorrect, Changing CouseID");
				changeCourse();
				System.out.println("CourseID change complete");
			}
			courseCorrect=true;
			System.out.println("CourseID format is: "+courseCorrect);
		} catch (Exception e) {	
		}
		return courseCorrect;
	}
	
	private boolean semesterCheck() {
		boolean semesterCorrect=false;
		String temp;
		int temp2;
		temp = indtastet[2];
		//System.out.println("Semester number string is: "+temp);
		try {
			temp2 = Integer.parseInt(temp);
			//System.out.println("Semester number int is: "+temp);
			while(temp2 <= 0 || temp2 >= 20) {
				//System.out.println("Changin semester number");
				changeSemester();
				//System.out.println("Semester change complete");
			}
			semesterCorrect=true;
			//System.out.println("Semester format is: "+courseCorrect);
		} catch (Exception e) {
		}
		return semesterCorrect;
	}
	
	private void changeCourse() {
		keyboard = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("> ");
			while ((input = this.readInput()) != null) {
				if(input.contains(" ")){
					String indtastet2[];
					indtastet2 = input.split(" ");
					indtastet[1] = indtastet2[0];
					indtastet[2] = indtastet2[1];
				} else {
					indtastet[1]=input;
				}
			}
		} catch (Exception e) {
		}
	}

	private void changeSemester() {
		//unfunctional
			System.out.println("Det indtastede data for semesternummeret var forkert.");
			System.out.println("Indtast det korrekte semesternummer:");
		//unfunctional
	}*/
	
	/**
	 * Remove a course from the users study plan.
	 * If the format of the inputted courseID is wrong, it will ask for a new courseID
	 */
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
			//changeCourse(input);
			remove(indtastet);
		} catch (Exception e) {

		}
	}

	/**
	 * Prints the hjælp-function.
	 * If the funktion is followed by an argument, it will print an more detailed discription of
	 * how to use the function (if it exists).
	 */
	private void helpMe() {
		if (indtastet[1].equalsIgnoreCase("afslut")) {
			System.out.println("Kommandoen afslut sørger for at lukke programmet ned.");
			System.out.println("FIX ME"); //TODO
		} else if (indtastet[1].equalsIgnoreCase("visplan")) {
			System.out.println("Kommandoen visplan viser studieplanen for et valgt semester.");
			System.out.println("Et eksempel på en studieplan kan se således ud: \n");
			testPlan();
			System.out.println("");
			System.out.println("Kommandoen til at fremkalde en studieplan er: \"visplan <semester>\" (uden gåseøjne og større/mindre-end tegn)");
			System.out.println("Bliver et semesternummer ikke indtastet, eller det er af forkert format, vil man blive spurgt efter et nyt");
		} else if (indtastet[1].equalsIgnoreCase("udskrivbase")) {
			System.out.println("Kommandoen udskrivbase udskriver en liste over alle de kuser der er i kursusdatabasen.");
			System.out.println("Den nuværende liste over kurser i databasen ser således ud: \n");
			printDatabaseList();
			System.out.println("");
			System.out.println("Kommandoen modtager ingen argumenter. Ønsker man et mere specifikt resultat, brug viskursus.");
		} else if (indtastet[1].equalsIgnoreCase("tilføj")) {
			System.out.println("Kommandoen tilføj tilføjer et nyt kursus til en studieplan.");
			System.out.println("Kommandoen skal bruge argumenter, men den kan bruges på følgendemåde:");
			System.out.println("\"tilføj <kursusnummer> <semester>\" (uden gåseøjne og større/mindre-end tegn)");
			System.out.println("Indtaster man ingen eller er argumenter af forkert format (fx bruger bogstaver i kursusnummer og/eller semesternummer)");
			System.out.println("vil man blive spurgt efter at indtaste kursusnummer/semester igen hvorefter kommandoen så vil blive udført");
			System.out.println("(såfrem ifald formatet af det nyligt indtastede er i orden).");
		} else if (indtastet[1].equalsIgnoreCase("fjern")) {
			System.out.println("Kommandoen fjern vil fjerne et kursus fra en studieplan.");
			System.out.println("Kommandoen skal bruge et argument, men kan bruges på følgende måde:");
			System.out.println("\"fjern <kursusnummer>\" (uden gåseøjne og større/mindre-end tegn)");
			System.out.println("Indtaster man ingen eller er argumenter af forkert format (fx bruger bogstaver i kursusnummer)");
			System.out.println("vil man blive spurgt efter at indtaste kursusnummer, hvorefter kommandoen vil blive udført (såfrem ifald");
			System.out.println("formatet af det nyligt indtastede er i orden).");
		} else if (indtastet[1].equalsIgnoreCase("hent")) {
			System.out.println("Kommandoen hent indlæser en gemt studieplan.");
			System.out.println("Brugen af kommandoen kan ske på følgende måde:");
			System.out.println("\"hent <studienummer>\" (uden gåseøjne og større/mindre-end tegn)");
			System.out.println("Indtaster man ikke selv et filnavn, vil man blive spurgt efter det.");
			System.out.println("Det vil være anbefalet man har brugt sit studienummer da det i forvejen er uniks. Har man brugt noget andet,");
			System.out.println("og man har været konsekvent med at bruge det, så kan man bruge det.");
		} else if (indtastet[1].equalsIgnoreCase("gem")) {
			System.out.println("Kommandoen gem gemmer en studieplan, så man kan arbejde videre på det på et andet tidspunkt.");
			System.out.println("Brugen af kommandoen kan ske på følgende måde:");
			System.out.println("\"gem <studenternummer>\" (uden gåseøjne og større/mindre-end tegn)");
			System.out.println("Indtaster man ikke selv et filnavn, vil man blive spurgt efter det.");
			System.out.println("Det anbefalede filnavn vil være ens studienummer, hvilket gør det lettere bagefter at finde og eventuelt");
			System.out.println("vidersende til andre. Men så længe man er konsekvent med det man skriver, så er det fint.");
		} else if (indtastet[1].equalsIgnoreCase("viskursus")) {
			System.out.println("Kommandoen viskursus viser en alle detaljer omkring et enkelt kursus.");
			System.out.println("Kommandoen kan bruges på følgende måde:");
			System.out.println("\"virkursus <kursusnummer>\" (uden gåseøjne og større/mindre-end tegn)");
			System.out.println("Indtaster man intet kursusnummer (eller den er af forkert format) vil der blive spurgt efter det.");
		} else if (indtastet[1].equalsIgnoreCase("hjælp")) {
			System.out.println("Denne kommando kommer med en mere uddybende forklaring omkring de forskellige funktioner.");
			System.out.println("Kommandoen kan tage imod argumenter, og kan bruges på følgende måde:");
			System.out.println("\"hjælp <kommando>\" (uden gåseøjne og større/mindre-end tegn)");
			System.out.println("Kommandoen kræver ikke et argument for at fungere. Indtaster man ingen får man standard listen over funktioner.");
			System.out.println("Indtaster man en ukendt kommando får man besked om det.");
		} else if (indtastet[1] != null || indtastet[1] != "") {
			System.out.println("Kommandoen \"" + indtastet[1] + "\" genkendes ikke.");
			System.out.println("Tjek om kommandoen eksisterer ved brug af \"hjælp\"-funktionen");
		} else {
			System.out.println("Programmet kender følgende kommandoer:");
			System.out.println("hjælp - viser denne hjælpe tekst samt forklaring til de forskellige kommandoer");
			System.out.println("tilføj - tilføjer et kursus til kursusplanen. Den mest optimale måde at kalde kommandoen på ville være 'tilføj kursusnummer semesternummer'");
			System.out.println("fjern - fjerner et kursus fra kursusplanen. Den mest optimale måde at kalde kommandoen på ville være 'fjern kursusnummer'");
			System.out.println("udskrivbase - udskriver en liste over kurser i databasen");
			System.out.println("visplan - viser en komplet plan over det valgte semindtastetester");
			System.out.println("virkursus - viser alle info for et enkelt kursus");
			System.out.println("gem - gemmer studieplanen så man kan arbejde videre på det senere");
			System.out.println("hent - indlæser en studieplan så det er muligt man kan arbejde videre på den");
			System.out.println("afslut - afslutter programmet");
			System.out.println("");
			System.out.println("For at få en udvidet forklaring omkring brugen af de enkelte funktioner, indtast hjælp og dernæst kommandoen.");
		}
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
			//We expect people to only start at autumn semester
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
	 * Prints a possible study plan
	 */
	private void testPlan(){
		System.out.println("Semester: 1 e   mandag  tirsdag  onsdag  torsdag  fredag");
		System.out.println("8:00-12:00       -----    02101   01005   -----    02121");
		System.out.println("  Pause");
		System.out.println("13:00-17:00      -----    -----   01005   01017    01005");
	}
	
	private void savePlan(String indtastet[]){
		if(indtastet[1] != null) {
			try {
				getCore().saveStudyPlan(indtastet[1]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		} else {
			//spørger efter filnavn/studienummer
		}
		//TODO
	}
	
	private void loadPlan(String indtastet[]){
		if(indtastet[1] != null) {
			try {
				getCore().loadStudyPlan(indtastet[1]);
			} catch (FilePermissionException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (CorruptStudyPlanFileException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		} else {
			//spørger efter filnavn/studienummer
		}
		//TODO
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