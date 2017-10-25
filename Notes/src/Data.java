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

	public static void save(Stage stage, ArrayList<Text> texts) {
		
		PrintWriter saveWriter = null;
		PrintWriter configWriter = null;
		try {
			Files.deleteIfExists(Paths.get("save.notes"));
			saveWriter = new PrintWriter(new FileWriter("save.notes", true), true);

			saveWriter.println("// Autor: Yanko");
			saveWriter.println("// Mail: yanko397@web.de");
			saveWriter.println();
			saveWriter.println("// Wärend der Laufzeit des Programms werden hier geänderte Einträge wieder überschrieben.");
			saveWriter.println();
			
			for(TextArea text : texts)
				saveWriter.println(text.getText());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Files.deleteIfExists(Paths.get("config.notes"));
			configWriter = new PrintWriter(new FileWriter("config.notes", true), true);
			
			configWriter.println("// Autor: Yanko");
			configWriter.println("// Mail: yanko397@web.de");
			configWriter.println();
			configWriter.println("// Wärend der Laufzeit des Programms werden hier geänderte Einträge wieder überschrieben.");
			configWriter.println();
			
			configWriter.println("POSITION_X: " + (int)stage.getX());
			configWriter.println("POSITION_Y: " + (int)stage.getY());
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (saveWriter != null) saveWriter.close();
	}
	
	public static void restore(Main main) throws IOException {
		
		ArrayList<String[]> options = new ArrayList<String[]>();
		ArrayList<Text> texts = main.getTexts();
		
		if(Files.exists(Paths.get("config.notes"))) {
			BufferedReader br = new BufferedReader(new FileReader("config.notes"));
			
			String line = br.readLine();
			while(line != null) {
				if(!line.trim().isEmpty() && !line.trim().startsWith("//")) {
					options.add(line.trim().split(" "));
				}
				line = br.readLine();
			}
			
			br.close();
//			stage.centerOnScreen();
		}
		
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
