package linearLightsOut;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Runs the Linear Lights Out application.
 * 
 * @author <TODO: YOUR NAME HERE>
 * 
 *************************************************************************************** 
 *         REQUIRED HELP CITATION
 * 
 *         TODO: cite your help here or say "only used CSSE220 materials"
 ***************************************************************************************  
 */
public class LinearMain {

	/**
	 * Starts here.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String nButtonsString = JOptionPane
				.showInputDialog("How many buttons would you like?");
		int nButtons = Integer.parseInt(nButtonsString);
		JFrame myFrame = new JFrame();
		// you'll want to think about how you want to manage the state of the game
		//also you want to add some buttons and stuff
		myFrame.setVisible(true);
	}
}
