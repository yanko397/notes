import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.scene.control.TextArea;

/**
 * @author Yanko
 * contains methods so save or restore data from or to files
 */
public class Data {

	/**
	 * saves data to a config file and all notes to a save file
	 * @param main object calling this function
	 */
	public static void save(Main main) {
		
		// CONFIG
		PrintWriter configWriter = null;
		try {
			Files.deleteIfExists(Paths.get("notes.config"));
			configWriter = new PrintWriter(new FileWriter("notes.config", true), true);
			message(configWriter);
			
			configWriter.println("POSITION_X: " + (int)(main.getPrimaryStage()).getX());
			configWriter.println("POSITION_Y: " + (int)(main.getPrimaryStage()).getY());
			configWriter.println("CHARACTERS_LIMIT: " + main.getTextLimit());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (configWriter != null) configWriter.close();
		
		// SAVE
		PrintWriter saveWriter = null;
		try {
			Files.deleteIfExists(Paths.get("notes.save"));
			saveWriter = new PrintWriter(new FileWriter("notes.save", true), true);
			message(saveWriter);
			
			for(TextArea text : main.getTexts()) {
				if(text.getStyleClass().contains("marked")) {
					saveWriter.println("*marked*" + text.getText());
				} else {
					saveWriter.println(text.getText());
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (saveWriter != null) saveWriter.close();
	}
	
	/**
	 * restores information from a config and a save file
	 * @param main object calling this function
	 * @throws IOException
	 */
	public static void restore(Main main) throws IOException {
		
		ArrayList<String[]> options = new ArrayList<String[]>();
		ArrayList<Text> texts = main.getTexts();
		boolean posBroken = false;
		
		// CONFIG
		if(Files.exists(Paths.get("notes.config"))) {
			BufferedReader br = new BufferedReader(new FileReader("notes.config"));
			String line = br.readLine();
			while(line != null) {
				if(!line.trim().isEmpty() && !line.trim().startsWith("//")) {
					options.add(line.trim().split(":"));
				}
				line = br.readLine();
			}
			br.close();
			
			for(String[] s : options) {
				switch(s[0].trim()){
				case "POSITION_X":
					try {
						if(posBroken) {
							main.getPrimaryStage().centerOnScreen();
						} else {
							main.getPrimaryStage().setX(Integer.parseInt(s[1].trim()));
						}
					} catch(Exception e) {
						posBroken = true;
						main.getPrimaryStage().centerOnScreen();
					}
					break;
					
				case "POSITION_Y":
					try {
						if(posBroken) {
							main.getPrimaryStage().centerOnScreen();
						} else {
							main.getPrimaryStage().setY(Integer.parseInt(s[1].trim()));
						}
					} catch(Exception e) {
						posBroken = true;
						main.getPrimaryStage().centerOnScreen();
					}
					break;
					
				case "CHARACTERS_LIMIT":
					try {
						main.setTextLimit(Integer.parseInt(s[1].trim()));
					} catch(Exception e) {
						main.setTextLimit(Main.DEFAULT_LIMIT);
					}
					break;
					
				default:
					break;
				}
			}
		}
		
		// SAVE
		if(Files.exists(Paths.get("notes.save"))) {
			BufferedReader br = new BufferedReader(new FileReader("notes.save"));
			String line = br.readLine();
			while(line != null) {
				if(!line.trim().isEmpty() && !line.startsWith("//")) {
					if(line.length() > main.getTextLimit()) main.setTextLimit(line.length());
					main.addStuff(line);
				}
				line = br.readLine();
			}
			if(texts.isEmpty()) main.addStuff();
			br.close();
		} else {
			main.addStuff();
		}
		
		main.checkLast();
	}
	
	/**
	 * writes a message
	 * @param w writer that shall write the message
	 */
	private static void message(PrintWriter w) {
		w.println("// Autor: Yanko");
		w.println("// Mail: yanko397@web.de");
		w.println();
		w.println("// Wärend der Laufzeit des Programms werden hier geänderte Einträge wieder überschrieben.");
		w.println();
	}
}
