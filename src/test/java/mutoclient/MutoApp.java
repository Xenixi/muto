package mutoclient;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Options;
import org.ini4j.Wini;
import org.ini4j.Profile.Section;

public class MutoApp {
	
	static Wini config;
	
	public static void main(String[] args) throws IOException {
		File configFile = new File("./config.ini");
		if(!configFile.exists()) {
			configFile.createNewFile();
		}
		
		
		try {
			 config = new Wini(configFile);
			
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("CATASTROPHIC FAILURE (It doesn't work)");
			System.err.println("Shutting down...");
			e.printStackTrace();
		}
		
		config.put("Muto", "Version", 1.1);
		config.put("Muto", "Program Runs", (config.fetch("Muto", "Program Runs") == null ? 1 : Integer.parseInt(config.fetch("Muto", "Program Runs")) + 1));
		
		if(config.fetch("Libraries", "Number") == null) {
			config.put("Libraries", "Number", 0);
		}
		
		
		
		
		config.store();
		if(args.length == 0) {
			System.out.println("Muto ~ Launched.\n");
			
			
			Scanner inputReader = new Scanner(System.in);
			
			
			
			while(true) {
				System.out.println("\nEnter Command ('h' for help):");
				String cmd = inputReader.nextLine();
				
				if(cmd.equalsIgnoreCase("h")) {
					System.out.println("We've got you");
					System.out.println("Reference: ");
					System.out.println("Help - h\nExit - x\nOpen GUI - LAUNCHGUI\n\nLibrary Add - LA\nLibrary Remove - LRM");
				} else if (cmd.equalsIgnoreCase("x")) {
					System.out.println("bye!");
					System.out.println("Exiting...");
					
					break;
				} else if (cmd.equals("LAUNCHGUI")) {
					
					main(new String[] {"gui"});
					
				} else if (cmd.startsWith("LA ")) {
					String path = cmd.replace("LA ", "");
					File f = new File(path);
					if(f.isDirectory()) {
						int i = Integer.parseInt(config.fetch("Libraries", "Number"));
						config.put("Libraries", "Path"+i, f.getAbsolutePath());
						config.store();
						System.out.println("Operation successful. Added library '" + f.getAbsolutePath() + "'");
						///use one  line all paths or something
						///!!!!!!!!!!!!!!!!!!!!!!!!
					} else {
						System.out.println("Invalid Directory!");
					}
					
				} else if(cmd.equals("LRM")) {
					
				}
					else {
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
