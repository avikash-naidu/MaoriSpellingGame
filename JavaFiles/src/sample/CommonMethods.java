package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class contains methods that are used in several other classes
 * Those classes access them from here to avoid duplication of code.
 * @author Group 20
 *
 */
public class CommonMethods {
	
	/**
	 * This method takes in a string and executes it in bash
	 * @param command
	 */
	public static void bash(String command) {
		ProcessBuilder bash = new ProcessBuilder();
		bash.command("bash", "-c", command);
		try {
			bash.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method extracts a line from a file depending on the command read in
	 * @param command
	 * @return line extracted
	 */
	public static String lineGet(String command) {
		String line = new String();
		try {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			Process process = pb.start();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			//read the output of the process input stream
			line = stdout.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}
}