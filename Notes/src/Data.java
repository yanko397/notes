import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.scene.control.TextArea;

public class Data {

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
			
			for(TextArea text : main.getTexts())
				saveWriter.println(text.getText());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (saveWriter != null) saveWriter.close();
	}
	
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
						main.setLimit(Integer.parseInt(s[1].trim()));
					} catch(Exception e) {
						main.setLimit(Main.DEFAULT_LIMIT);
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
					if(line.length() > main.getTextLimit()) main.setLimit(line.length());
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
	
	private static void message(PrintWriter w) {
		w.println("// Autor: Yanko");
		w.println("// Mail: yanko397@web.de");
		w.println();
		w.println("// Wärend der Laufzeit des Programms werden hier geänderte Einträge wieder überschrieben.");
		w.println();
	}
}
