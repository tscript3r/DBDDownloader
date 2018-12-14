package pl.tscript3r.dbdd.utility;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class Logger {

	private static JButton bExecute;
	private static JProgressBar progressBar;
	private static JTextArea taOutput;
	private static final String PREFIX = "> ";
	private static Integer progressValue = 0;
	
	public static void setTextArea(JTextArea jTextArea) {
		taOutput = jTextArea;
	}
	
	public static void setProgressBar(JProgressBar progressBar) {
		Logger.progressBar = progressBar;
	}

	public static void setbExecute(JButton bExecute) {
		Logger.bExecute = bExecute;
	}
	
	public static void print(String... message) {
		StringBuilder output = new StringBuilder(PREFIX);
		for(String msg : message) 
			output.append(msg);
		if(taOutput != null) {
			if (taOutput.getText().length() > 1)
				taOutput.append("\r\n");
			taOutput.append(output.toString());
		} else 
			System.out.println(output.toString());
	}
	
	public static void disableUI() {
		progressBar.setMaximum(11);
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		progressValue = 0;		
		taOutput.setText("");
		bExecute.setEnabled(false);
		bExecute.updateUI();
	}
	
	public static void enableUI() {
		bExecute.setEnabled(true);
		bExecute.updateUI();
	}
	
	public static void updateProgressBar() {
		progressBar.setValue(progressValue++);
		progressBar.updateUI();

	}
}
