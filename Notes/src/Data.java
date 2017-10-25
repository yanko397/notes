import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Data {

	public static void save(Stage stage, ArrayList<Text> texts, boolean limited) {
		
		PrintWriter saveWriter = null;
		PrintWriter configWriter = null;
		try {
			Files.deleteIfExists(Paths.get("save.notes"));
			saveWriter = new PrintWriter(new FileWriter("save.notes", true), true);
			message(saveWriter);
			
			for(TextArea text : texts)
				saveWriter.println(text.getText());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Files.deleteIfExists(Paths.get("config.notes"));
			configWriter = new PrintWriter(new FileWriter("config.notes", true), true);
			message(configWriter);
			
			configWriter.println("POSITION_X: " + (int)stage.getX());
			configWriter.println("POSITION_Y: " + (int)stage.getY());
			configWriter.println("LIMITED_CHARACTERS_ENABLED: " + limited);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (saveWriter != null) saveWriter.close();
		if (configWriter != null) configWriter.close();
	}
	
	private static void message(PrintWriter w) {
		w.println("// Autor: Yanko");
		w.println("// Mail: yanko397@web.de");
		w.println();
		w.println("// Wärend der Laufzeit des Programms werden hier geänderte Einträge wieder überschrieben.");
		w.println();
	}
	
	public static void restore(Main main) throws IOException {
		
		ArrayList<String[]> options = new ArrayList<String[]>();
		ArrayList<Text> texts = main.getTexts();
		
		// CONFIG
		if(Files.exists(Paths.get("config.notes"))) {
			BufferedReader br = new BufferedReader(new FileReader("config.notes"));
			String line = br.readLine();
			while(line != null) {
				if(!line.trim().isEmpty() && !line.trim().startsWith("//")) {
					options.add(line.trim().split(": "));
				}
				line = br.readLine();
			}
			br.close();
			
			for(String[] s : options) {
				switch(s[0]){
				case "POSITION_X":
					try {
						main.getPrimaryStage().setX(Integer.parseInt(s[1]));
					} catch(Exception e) {
						main.getPrimaryStage().centerOnScreen();
					}
					break;
					
				case "POSITION_Y":
					try {
						main.getPrimaryStage().setY(Integer.parseInt(s[1]));
					} catch(Exception e) {
						main.getPrimaryStage().centerOnScreen();
					}
					break;
					
				case "LIMITED_CHARACTERS_ENABLED":
					try {
						main.setLimited(Boolean.parseBoolean(s[1]));
					} catch(Exception e) {
						main.setLimited(false);
					}
					break;
					
				default:
					break;
				}
			}
		}
		
		// SAVE
		if(Files.exists(Paths.get("save.notes"))) {
			BufferedReader br = new BufferedReader(new FileReader("save.notes"));
			String line = br.readLine();
			while(line != null) {
				if(!line.trim().isEmpty() && !line.startsWith("//")) {
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
}
