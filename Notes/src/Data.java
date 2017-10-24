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
		
		PrintWriter pWriter = null;
		try {
			Files.deleteIfExists(Paths.get("save.notes"));
			pWriter = new PrintWriter(new FileWriter("save.notes", true), true);

			pWriter.println("// Autor: Yanko");
			pWriter.println("// Mail: yanko397@web.de");
			pWriter.println();
//			pWriter.println("// Comments starting with double slashes and empty lines will be ignored by the program.");
//			pWriter.println("// Beginnt eine Zeile mit Doppelslash oder ist sie leer, wird sie vom Programm ignoriert.");
//			pWriter.println();
//			pWriter.println("// The first not empty line, if starting with two integers, will define the position of the program on the screen");
			pWriter.println("// Die erste, nicht leere Zeile die mit zwei Zahlen beginnt, gibt die Position des Programms auf dem Bildschirm an");
			pWriter.println();
			pWriter.println((int)stage.getX() + " " + (int)stage.getY());
			pWriter.println();
			
			for(TextArea text : texts)
				pWriter.println(text.getText());
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (pWriter != null) pWriter.close();
	}
	
	public static void restore(Main main) throws IOException {
		
		Stage stage = main.getPrimaryStage();
		ArrayList<Text> texts = main.getTexts();
		
		if(Files.exists(Paths.get("save.notes"))) {
			BufferedReader br = new BufferedReader(new FileReader("save.notes"));

			String ifFails = "";
			try {
				while(ifFails.isEmpty()) {
					String temp = br.readLine();
					if(temp == null) break;
					if(!temp.startsWith("//")) {
						ifFails += temp.trim();
					}
				}
				String[] pos = ifFails.split(" ");
				
				stage.setX(Integer.parseInt(pos[0]));
				stage.setY(Integer.parseInt(pos[1]));
			} catch (Exception e){
				stage.centerOnScreen();
				main.addStuff(ifFails);
			}
			
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
