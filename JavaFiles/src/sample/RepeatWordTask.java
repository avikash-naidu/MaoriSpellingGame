package sample;

import javafx.concurrent.Task;

/**
 * Repeat Word Task on new thread to prevent spamming and disable buttons when required
 * @author Group 20
 *
 */
public class RepeatWordTask extends Task<Object> {
	
	@Override
	protected Object call() throws Exception {
		CommonMethods.bash("festival -b .festivalCommands.scm");
		Thread.sleep(1000);
		return null;
	}
	

}