/**
 * This is the part of the program which is used for user-input and also what is printed to the terminal window
 */
package ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import dataClass.Course;
import exceptions.ConflictingCourseInStudyPlanException;
import exceptions.CorruptStudyPlanFileException;
import exceptions.CourseAlreadyExistsException;
import exceptions.CourseDoesNotExistException;
import exceptions.FilePermissionException;
import exceptions.StudyPlanDoesNotExistException;

/**
 * Sets up the interface the user will see.
 * @author Frederik Nordahl Sabroe
 * @author Morten Sørensen
 */
public class Dialog extends UI {
	/**
	 * This is the variable for the Buffered reader
	 */
	BufferedReader keyboard; 
	/**
	 * This is the variable which contains the Course ID
	 */
	String courseID;
	/**
	 * This is the variable which contains the semester number
	 */
	String semesterNumber;
	/**
	 * This is the String array which contains the users input
	 */
	String indtastet[];
	/**
	 * This is a variable we use to keep track of if the user wants to exit the program
	 */
	boolean killSwitch;
	/**
	 * This is a variable to keep track of if the user has changed the studyplan, incase the user exits, the program will ask to save
	 */
	boolean studyPlanChanged;
	/**
	 * A constant to compare to if input is null
	 */
	public final static int INPUT_NULL = 1;
	/**
	 * A constant to compare to if input is accepted
	 */
	public final static int INPUT_ACCEPTED = 0;
	/**
	 * A constant to compare to if input is not an integer
	 */
	public final static int INPUT_NOT_INT = 2;
	/**
	 * A constant to compare to if input is out of bounds
	 */
	public final static int INPUT_OUT_OF_BOUNDS = 3;
	/**
	 * A constant to compare to if the command is not recognized
	 */
	public final static int COMMAND_NOT_RECOGNIZED = 0;
	/**
	 * A constant to compare to if the command is "Afslut"
	 */
	public final static int COMMAND_AFSLUT = 1;
	/**
	 * A constant to compare to if the command is "Hjælp
	 */
	public final static int COMMAND_HJAELP = 2;
	/**
	 * A constant to compare to if the command is "VisPlan"
	 */
	public final static int COMMAND_VIS_PLAN = 3;
	/**
	 * A constant to compare to if the command is "UdskrivBase"
	 */
	public final static int COMMAND_UDSKRIV_BASE = 4;
	/**
	 * A constant to compare to if the command is "Tilføj
	 */
	public final static int COMMAND_TILFØJ = 5;
	/**
	 * A constant to compare to if the command is "Fjern"
	 */
	public final static int COMMAND_FJERN = 6;
	/**
	 * A constant to compare to if the command is "Hent"
	 */
	public final static int COMMAND_HENT = 7;
	/**
	 * A constant to compare to if the command is "Gem"
	 */
	public final static int COMMAND_GEM = 8;
	/**
	 * A constant to compare to if the command is "VisKursus"
	 */
	public final static int COMMAND_VIS_KURSUS = 9;

	/**
	 * This is the constructor
	 * @param core
	 * @throws IllegalArgumentException
	 */
	public Dialog(Core core) throws IllegalArgumentException {
		super(core);
	}

	/** This is a rewrite of {@link ui.UI#start()}
	 * @see ui.UI#start()
	 * @author Frederik Nordahl Sabroe
	 */
	public void start(){
		intro();
		keyboard = new BufferedReader(new InputStreamReader(System.in));
		indtastet = new String[10];
		try{
			while(killSwitch==false) {
				System.out.println("Indtast venligst din kommando:");
				input(0);
				switch(commandCheck()){
				case COMMAND_NOT_RECOGNIZED:
					continue;
				case COMMAND_AFSLUT:
					killSwitch = true;
					break;
				case COMMAND_HJAELP:
					helpMe();
					break;
				case COMMAND_VIS_PLAN:
					showPlan();
					break;
				case COMMAND_UDSKRIV_BASE:
					printDatabaseList();
					break;
				case COMMAND_TILFØJ:
					add();
					break;
				case COMMAND_FJERN:
					remove();
					break;
				case COMMAND_HENT:
					loadPlan();
					break;
				case COMMAND_GEM:
					savePlan();
					break;
				case COMMAND_VIS_KURSUS:
					showCourse();
					break;
				}
			}
			end();
		} catch (Exception e) {
			//This happens if someone hits CTRL + D in Linux (among things)
			//in that case, a NullPointerException is thrown.
			System.out.println();
			System.err.println("InputStream closed.");
		}
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
	}

	/**
	 * This is the metheod which receives the input from the keyboard
	 * @author Frederik Nordahl Sabroe
	 * @param offset serves to change where the input from the keyboard will be put in the "indtastet[]" string array
	 * @throws IOException triggers if buffered reader which comes from stdin is closed
	 */
	public void input(int offset) throws IOException {
		String temp[];
		String input;
		temp = new String[10];

		System.out.print("> ");
		if((input = this.readInput()) != null) {
			if(input.contains(" ")) {
				temp = input.split(" ");
			} else {
				temp[0] = input;
			}
			for(int rotation = 0;temp.length >= rotation+offset+1;rotation++) {
				indtastet[offset+rotation] = temp[rotation];
			}
		} else {
			throw new IOException("Instream closed");
		}
	}

	/**
	 * Checks if the command is recognized by the program
	 * @author Frederik Nordahl Sabroe
	 * @return is one of the constants defined at the start of the program, which is used by start() to trigger the part of the program the user wishes to use
	 */
	public int commandCheck(){
		if (indtastet[0].equalsIgnoreCase("afslut")) {
			return COMMAND_AFSLUT;
		} else if (indtastet[0].equalsIgnoreCase("hjælp")) {
			return COMMAND_HJAELP;
		} else if (indtastet[0].equalsIgnoreCase("visplan")) {
			return COMMAND_VIS_PLAN;
		} else if (indtastet[0].equalsIgnoreCase("udskrivbase")) {
			return COMMAND_UDSKRIV_BASE;
		} else if (indtastet[0].equalsIgnoreCase("tilføj")) {
			return COMMAND_TILFØJ;
		} else if (indtastet[0].equalsIgnoreCase("fjern")) {
			return COMMAND_FJERN;
		} else if (indtastet[0].equalsIgnoreCase("hent")) {
			return COMMAND_HENT;
		} else if (indtastet[0].equalsIgnoreCase("gem")) {
			return COMMAND_GEM;
		} else if (indtastet[0].equalsIgnoreCase("viskursus")){
			return COMMAND_VIS_KURSUS;
		} else {
			return COMMAND_NOT_RECOGNIZED;
		}
	}

	/**
	 * When adding a new course, it checks if there are any of the data in a wrong format
	 * (eg. too high semester number, or the course ID contains letters).
	 * @author Frederik Nordahl Sabroe
	 * @throws IOException triggers if buffered reader which comes from stdin is closed
	 */
	private void add() throws IOException {
		int test = 0;
		while((test = courseCheck())!=INPUT_ACCEPTED){
			switch(test) {
			case INPUT_NULL:
				System.out.println("Indtast venligst et CourseID:");
				break;
			case INPUT_NOT_INT:
			case INPUT_OUT_OF_BOUNDS:
				System.out.println("The CourseID you entered was incorrect, please try again");
				break;
			}
			input(1);
		}
		while((test = semesterCheck())!=INPUT_ACCEPTED){
			switch(test) {
			case INPUT_NULL:
				System.out.println("Indtast venligst et semesternummer:");
				break;
			case INPUT_NOT_INT:
			case INPUT_OUT_OF_BOUNDS:
				System.out.println("The semester number you entered was incorrect, please try again");
				break;
			}
			input(2);
		}
		try {
			getCore().addCourseToStudyPlan(indtastet[1], Integer.parseInt(indtastet[2]));
			System.out.println("Course added to plan");
			studyPlanChanged = true;
		} catch (ConflictingCourseInStudyPlanException e) {
			System.err.println(e);
		} catch (CourseDoesNotExistException e) {
			System.err.println(e);
		}  catch (StudyPlanDoesNotExistException e) {
			System.err.println(e);
		} catch (CourseAlreadyExistsException e) {
			System.err.println(e);
		}
	}

	/**
	 * Checks if the form of courseID is a possible courseID
	 * @author Frederik Nordahl Sabroe
	 * @return true if the form is correct, else false
	 */
	private int courseCheck() {
		if(indtastet[1]==null)
			return INPUT_NULL;
		try {
			int temp2 = Integer.parseInt(indtastet[1]);
			if(indtastet[1].length() == 5 || temp2 > -1) {
				return INPUT_ACCEPTED;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT_NOT_INT;
		}
		return INPUT_OUT_OF_BOUNDS;
	}

	/**
	 * Checks if the form of semester number is a possible semester number
	 * @author Frederik Nordahl Sabroe 
	 * @return true if the form is correct, else false
	 */
	private int semesterCheck() {
		if(indtastet[1]==null)
			return INPUT_NULL;
		try {
			int temp2 = Integer.parseInt(indtastet[2]);
			if(temp2 > 0 || temp2 < 21) {
				return INPUT_ACCEPTED;
			}
		} catch (Exception e) {
			return INPUT_NOT_INT;
		}
		return INPUT_OUT_OF_BOUNDS;
	}

	/**
	 * Remove a course from the users study plan.
	 * If the format of the inputted courseID is wrong, it will ask for a new courseID
	 */
	private void remove() {
		studyPlanChanged=true;
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
			remove();
		} catch (Exception e) {

		}
	}

	/**
	 * Prints the hjælp-function.
	 * If the funktion is followed by an argument, it will print an more detailed discription of
	 * how to use the function (if it exists).
	 */
	private void helpMe() {
		if (indtastet[1] == null) {
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
		} else if (indtastet[1].equalsIgnoreCase("afslut")) {
			System.out.println("Kommandoen afslut sørger for at lukke programmet ned.");
			System.out.println("Kommandoen tager ikke imod argumenter.");
			System.out.println("Inden programmet bliver lukket ned, så bliver man spurgt om man vil gemme sin studie plan.");
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
			System.out.println("Det vil være anbefalet man har brugt sit studienummer da det i forvejen er unikt. Har man brugt noget andet,");
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
			System.out.println("Indtaster man en ukendt kommando får man besked derom.");
		} else if (indtastet[1] != null || !indtastet[1].equals("") ) {
			System.out.println("Kommandoen \"" + indtastet[1] + "\" genkendes ikke.");
			System.out.println("For at få en liste af genkendte kommandoer, tast \"hjælp\" (uden gåseøjne)");
		} 
	}
	
	/**
	 * Prints the list of available courses
	 */
	private void printDatabaseList(){
		System.out.println(getCore().getCourseBase().toString());
	}
	
	/**
	 * Prints the plan as is
	 */
	private void showPlan(){
		int temp2;
		String season;
		try {
			temp2 = Integer.parseInt(indtastet[1]);
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
	 * Presents the data associated with a certain course given it's ID
	 * @throws IOException
	 */
	private void showCourse() throws IOException {
		while(courseCheck()!=INPUT_ACCEPTED){
			try {
				indtastet[1].trim();	
			} catch(Exception e) {
			}
			if(indtastet[1]==null || indtastet.equals("")) {
				System.out.println("Indtast venligst et CourseID:");
			} else {
				System.out.println("The CourseID you entered was incorrect, please try again");
			}
			input(1);
		}
		try {
			Course course = getCore().findCourse(indtastet[1]);
			System.out.println(course);
		} catch (CourseDoesNotExistException e) {
			System.out.println(e);
		}
		

	}
	
	/**
	 * Prints a demo studyplan
	 */
	private void testPlan(){
		System.out.println("Semester: 1 e   mandag  tirsdag  onsdag  torsdag  fredag");
		System.out.println("8:00-12:00       -----    02101   01005   -----    02121");
		System.out.println("  Pause");
		System.out.println("13:00-17:00      -----    -----   01005   01017    01005");
	}

	/**
	 * Saves the current studyplan
	 */
	private void savePlan(){
		if(indtastet[1] != null) {
			try {
				getCore().saveStudyPlan(indtastet[1]);
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			//spørger efter filnavn/studienummer
		}
		//TODO
	}

	/**
	 * Load a previously saved studyplan
	 */
	private void loadPlan(){
		if(indtastet[1] != null) {
			try {
				getCore().loadStudyPlan(indtastet[1]);
			} catch (FilePermissionException e) {
				System.out.println(e);
			} catch (FileNotFoundException e) {
				System.out.println(e);
			} catch (CorruptStudyPlanFileException e) {
				System.out.println(e);
			} catch (IOException e) {
				System.out.println(e);
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
	 * @author Frederik Nordahl Sabroe
	 */
	private void end(){
		if(studyPlanChanged==true){
			killSwitch = false;
			System.out.println("Vil du gemme din studieplan? (skriv \"gem\" for at gemme eller \"afslut\" for at afslutte uden at gemme");
			System.out.println("woot");
			while (killSwitch==false) {
				try {
					input(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				switch(commandCheck()){
				case COMMAND_AFSLUT:
					killSwitch = true;
					break;
				case COMMAND_GEM:
					savePlan();
					break;
				}
			}
		}
		System.out.println("Tak for idag.");
	}
}