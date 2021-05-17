package mutoclient;

import java.awt.Dimension;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MutoApp {
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("Muto ~ Launched.\n");
			
			
			Scanner inputReader = new Scanner(System.in);
			
			
			
			while(true) {
				System.out.println("\nEnter Command ('h' for help):");
				String cmd = inputReader.nextLine();
				
				if(cmd.equalsIgnoreCase("h")) {
					System.out.println("We've got you");
					System.out.println("Reference: ");
				} else if (cmd.equalsIgnoreCase("x")) {
					System.out.println("bye!");
					System.out.println("Exiting...");
					
					break;
				} else if (cmd.equals("LAUNCHGUI")) {
					
					main(new String[] {"gui"});
				} else {
					System.out.println("-Invalid command-");
				}
				
			}
			inputReader.close();
			
		} else if(args[0].equalsIgnoreCase("gui")){
			SwingUtilities.invokeLater(new Runnable() {
				
				public void run() {
					JFrame window = new JFrame("Muto ~ Home");
					window.setSize(new Dimension(1280,720));
					window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					window.setVisible(true);
				}
			});
		}
	}
}
