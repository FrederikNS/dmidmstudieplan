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
		if(indtastet.size() == 1){
			
		}
		else if(indtastet.size() == 2){
			
		}
		courseID = indtastet[1];
		semesterNumber = indtastet[2];
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