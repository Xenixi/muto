package mutoclient;

import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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

import mutoclient.config.Format;

import org.ini4j.Profile.Section;

public class MutoApp {

	static Wini config;
	static List<String> paths;
	static String connectedServer = null;
	
	
	//conf
	static int timeout;
	static String defaultPort;
	
	public static void main(String[] args) throws IOException {
		File configFile = new File("./config.ini");
		if (!configFile.exists()) {
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
		config.put("Muto", "Program Runs", (config.fetch("Muto", "Program Runs") == null ? 1
				: Integer.parseInt(config.fetch("Muto", "Program Runs")) + 1));

	

		paths = new ArrayList<String>();
		if (config.fetch("Libraries", "Paths") != null) {

			for (String p : config.fetch("Libraries", "Paths").split(",")) {
				paths.add(p);

			}

		}
		if(config.fetch("Network", "Timeout") == null) {
			config.put("Network", "Timeout", "2000");
		}
		if(config.fetch("Network", "DefaultPort") == null) {
			config.put("Network", "DefaultPort", "8657");
		}
		
		timeout = Integer.parseInt(config.fetch("Network", "Timeout"));
		defaultPort = config.fetch("Network", "DefaultPort");
		config.store();
		if (args.length == 0) {
			// CLI MODE - (Advanced)
			System.out.println("Muto ~ Launched.\n");

			Scanner inputReader = new Scanner(System.in);

			// command loop (runs until program exit or GUI launch)

			while (true) {
				// main prompt
				if (connectedServer == null) {
					System.out.println("\n[DISCONNECTED] {n/a}\nEnter Command ('h' for help):");
				} else {
					System.out.println("\n[CONNECTED] {Server=" + connectedServer.split(":")[0] + " Port="
							+ connectedServer.split(":")[1] + "}\nEnter Command ('h' for help):");
				}

				String cmd = inputReader.nextLine();

				if (cmd.equalsIgnoreCase("h")) {
					// help command
					System.out.println("We've got you");
					System.out.println("Reference: ");
					System.out.println(
							"Help - h\nExit - x\nOpen GUI - LAUNCHGUI\n\nLibrary Add - la | LA\nLibrary Remove - lrm | LRM\n\nOpen Config File - cf | config | configuration");
				} else if (cmd.equalsIgnoreCase("x")) {
					// exit command
					System.out.println("bye!");
					System.out.println("Exiting...");

					break;
				} else if (cmd.equals("LAUNCHGUI")) {
					// Launch the graphical user interface

					main(new String[] { "gui" });

				} else if (cmd.startsWith("LA ") || cmd.startsWith("la ")) {
					// Library add functionality

					String path = cmd.substring(3).toLowerCase();
					File f = new File(path);

					if (f.isDirectory() && !paths.contains(f.getAbsolutePath())) {

						paths.add(f.getAbsolutePath());

						config.put("Libraries", "Paths", Format.INIFormat.listToStringComma(paths));
						config.store();

						System.out.println("Library added successfully: '" + f.getAbsolutePath() + "'");

					} else {
						System.out.println("Invalid Directory!");
					}

				} else if (cmd.startsWith("LRM ") || cmd.startsWith("lrm ")) {
					// library remove functionality

					String path = cmd.substring(4).toLowerCase();
					File f = new File(path);

					if (paths.contains(f.getAbsolutePath())) {
						paths.remove(f.getAbsolutePath());
						System.out.println("Successfully removed library from tracking");

						config.put("Libraries", "Paths", Format.INIFormat.listToStringComma(paths));
						config.store();
					} else {
						System.out.println("Specified parameter not found");
					}

				} else if (cmd.equalsIgnoreCase("cf") || cmd.equalsIgnoreCase("config")
						|| cmd.equalsIgnoreCase("configuration")) {
					// launch config in system editor functionality
					if (Desktop.isDesktopSupported()) {
						Desktop.getDesktop().edit(configFile);
						System.out.println("Launched system editor (config.ini)");
					}

				} else if (cmd.startsWith("cn ") || cmd.startsWith("CN ")) {
					String server = cmd.substring(3).contains(":") ? cmd.substring(3).split(":")[0] : cmd.substring(3);
					//default port for MUTO is 8657
					int port = Integer.parseInt((cmd.substring(3).contains(":") ? cmd.substring(3).split(":")[1] : defaultPort));
					try {
					InetAddress adr = InetAddress.getByName(server);
					InetSocketAddress socketAddress = new InetSocketAddress(adr.getHostAddress(), port);
					
					
					if (server.equals("") || adr == null) {
						throw new UnknownHostException();
					} else {
						
						System.out.println("Attempting connection to '" + server + ":" + port + "'");
						Socket soc = new Socket();
						
						try {
							soc.connect(socketAddress, timeout);
							
							//implement multithreading here for connection handling and begin server-side code
						} catch (Exception d) {
							System.out.println("Failed to connect to server '" + socketAddress.getAddress() + ":" + socketAddress.getPort() + "'");
						}
					}
					} catch(UnknownHostException e) {
						System.out.println("Invalid internet address");
					}

				} else {
					// catch all unrecognized input --
					System.out.println("-Invalid command-");
				}

			}
			inputReader.close();

		} else if (args[0].equalsIgnoreCase("gui")) {
			// GUI MODE --
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					JFrame window = new JFrame("Muto ~ Home");
					window.setSize(new Dimension(1280, 720));
					window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					window.setVisible(true);
				}
			});
		}
	}
}
