/**
 * 
 */
package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Morten Sørensen
 *
 */
public class Dialog {
	
	String input;
	BufferedReader keyboard;
	String courseID;
	String semesterNumber;
	
	
	public Dialog() {
		intro();
		mainProgram();
	}
	
	/**
	 * Udskriver velkommen-teksten når programmet startes.
	 */
	private void intro(){
		System.out.println("Velkommen til 'Læg en Studieplan'");
		System.out.println("");
		helpMe();
		System.out.println("");
		System.out.println("Indtast dine kommandoer efter tegnet '>'");
	}
	
	/**
	 * Selve hovedprogrammet.
	 */
	public void mainProgram(){
		keyboard = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("> ");
			while( (input = this.readInput() ) != null ){
				String[] indtastet = input.split(" ");
				if(indtastet[0].equalsIgnoreCase("afslut")){
					break;
				}
				else if(indtastet[0].equalsIgnoreCase("hjælp")){
					helpMe();
				}
				else if(indtastet[0].equalsIgnoreCase("visplan")){
					//TODO
				}
				else if(indtastet[0].equalsIgnoreCase("udskrivbase")){
					//TODO
				}
				else if(indtastet[0].equalsIgnoreCase("tilføj")){
					add(indtastet);
				}
				else if(indtastet[0].equalsIgnoreCase("fjern")){
					//courseID = Integer.parseInt(indtastet[1]);
					//TODO
				}
				System.out.print("> ");
			}
		} catch (Exception e) {
		}
		end();
	}
	
	private void add(String indtastet[]){
		String temp;
		int temp2;
		boolean courseFormatOk = false;
		boolean semesterFormatOk = false;
		
		switch(indtastet.length){
			default:
			case 3:
				//Semester
				temp = indtastet[2];
				try {
					temp2 = Integer.parseInt(temp);
					if(temp2 > 0 && temp2 <= 20)
					{
						semesterFormatOk = true;
					}
				}
				catch (Exception e){
					
				}
			case 2:
				//Course
				try{
					temp2 = Integer.parseInt(indtastet[1]);
					if(indtastet[1].length() == 5 && temp2 > -1){
						courseFormatOk = true;
					}
				}
				catch (Exception e){
					
				}
			case 1:
				break;
		}
		
		//Spørg efter manglede / fejlformatterde informationer
		if(semesterFormatOk == false){
			System.out.println("Det indtastede data for semesternummeret var forkert.");
			System.out.println("Indtast det korrekte semesternummer:");
			System.out.print("> ");
			changeCourse(input);
		}
		else if(courseFormatOk == false){
			System.out.println("Det indtastede data for kursusnummeret var forkert.");
			System.out.println("Indtast det korrekte kursusnummer:");
		}
	}
	
	private void changeCourse(String input){
		System.out.print()
	}
	
	/**
	 * Prints the hjælp-function.
	 */
	private void helpMe(){
		System.out.println("Programmet kender følgende kommandoer:");
		System.out.println("hjælp  tilføj  fjern  udskrivbase  visplan  afslut");
	}
	
	/**
	 * Trimmer input for at slippe af med eventuelle mellemrum der er blevet sat foran eller bag ved kommandoen.
	 * Hvis input bliver/er null, vil den kaste en exception.
	 * @return
	 * @throws IOException
	 */
	private String readInput() throws IOException{
		return keyboard.readLine().trim();
	}
	
	/**
	 * Afslutter programmet.
	 */
	private void end(){
		System.out.println("Tak for idag.");
	}
}