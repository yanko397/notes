import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application{
	
	private static String[] startArgs = null;
	
	private final int width = 300;
	private final int height = 80;
	private final int deleteButtonWidth = 40;
	private final int deleteButtonInsetX = width - 57;
	private final int deleteButtonInsetY = 27;
	private final int textWidth = width - 75;
	private final int textHeight = 50;
	private final int textInsets = 15;
	private final int absoluteOffset = textHeight + 5;
	
	private int count = 0;
	private int totalOffset = 0;

	private Stage primaryStage;
	private Scene scene;
	private Face face;
	
	private ArrayList<Text> texts = new ArrayList<Text>();
	private ArrayList<Button> deleteButtons = new ArrayList<Button>();

	public enum Direction {UP,DOWN}
	
	public static void main(String[] args) {
		startArgs = args;
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		this.primaryStage = primaryStage;
		
		face = new Face();
		face.init(this);
		face.setPrefWidth(width);
		face.setPrefHeight(height);
		face.setHandlers();
		
		scene = new Scene(face, Color.TRANSPARENT);
		scene.getStylesheets().add(getResource("config.css"));
		
		primaryStage.setTitle("Notes");
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("note.png")));
		primaryStage.setScene(scene);
		
		restore();
		
		
		ContextMenu contextMenu = new ContextMenu();
		face.setOnContextMenuRequested(e -> contextMenu.show(primaryStage, e.getScreenX(), e.getScreenY()));
		
		MenuItem item1 = new MenuItem("Einstellungen");
		item1.setOnAction(e -> System.out.println("ping"));
		MenuItem item2 = new MenuItem("Alle löschen");
		item2.setOnAction(e -> {while(deleteButtons.size() > 1) deleteStuff(deleteButtons.get(0).getId());});
		MenuItem item3 = new MenuItem("Neu starten");
		item3.setOnAction(e -> restartApplication());
		MenuItem item4 = new MenuItem("Beenden");
		item4.setOnAction(e -> exitApplication());
		
		contextMenu.getItems().addAll(item2, item4);
		

		// save the notes, if the stage gains or looses focus
		primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				save();
			}
		});
		
		primaryStage.show();
	}
	
	public void addStuff() {
		addStuff("");
	}
	
	public void addStuff(String revived) {
		
		addText(revived);
		addDeleteButton();
		hideLast();
		
		face.setPrefHeight(height + totalOffset);
		primaryStage.sizeToScene();
		
		totalOffset += absoluteOffset;
		count++;
	}
	
	public void addDeleteButton() {

		DeleteButton deleteButton = new DeleteButton();
		deleteButton.init(this);
		deleteButton.setPrefWidth(deleteButtonWidth);
		deleteButton.setLayoutX(deleteButtonInsetX);
		deleteButton.setLayoutY(deleteButtonInsetY + totalOffset);
		deleteButton.setId("" + count);
		deleteButton.getStyleClass().add("deleteButtons");
		deleteButton.setHandlers();

		deleteButtons.add(deleteButton);
		face.getChildren().add(deleteButton);
	}
	
	public void addText(String revived) {
		
		Text text = new Text();
		text.init(this);
		text.setPrefHeight(textHeight);
		text.setPrefWidth(textWidth);
		text.setLayoutX(textInsets);
		text.setLayoutY(textInsets + totalOffset);
		text.setBlendMode(BlendMode.OVERLAY);
		text.setFont(Font.font(13));
		text.setWrapText(true);
		text.setId("" + count);
		if(!revived.isEmpty()) text.setText(revived);
		text.setHandlers();
		
		texts.add(text);
		face.getChildren().add(text);
	}
	
	public void deleteStuff(String index) {
		
		if(texts.size() == 1) {
			texts.get(0).setText("");
			return;
		}
		
		int foundIndex = 0;
		boolean found = false;
		for(Button button : deleteButtons){
			if(found) {
				button.setLayoutY(button.getLayoutY() - absoluteOffset);
				texts.get(deleteButtons.indexOf(button)).setLayoutY(texts.get(deleteButtons.indexOf(button)).getLayoutY() - absoluteOffset);
			}
			if(button.getId().equals(index)) {
				foundIndex = deleteButtons.indexOf(button);
				found = true;
				face.getChildren().remove(button);
				face.getChildren().remove(texts.get(foundIndex));
			}
		}
		
		face.setPrefHeight(face.getPrefHeight() - absoluteOffset);
		primaryStage.sizeToScene();
		
		checkLast();
		hideLast();
		
		deleteButtons.remove(foundIndex);
		texts.remove(foundIndex);
		
		totalOffset -= absoluteOffset;
	}
	
	public void moveStuff(int index, Direction dir) {
		
		String swap = "";
		if(dir.equals(Direction.DOWN)) {
			if(index != texts.size()-1) {
				swap += texts.get(index).getText();
				texts.get(index).setText(texts.get(index+1).getText());
				texts.get(index+1).setText(swap);
				texts.get(index+1).requestFocus();
				texts.get(index+1).positionCaret(Integer.MAX_VALUE);
				checkLast();
			}
		} else {
			if(index != 0) {
				swap += texts.get(index).getText();
				texts.get(index).setText(texts.get(index-1).getText());
				texts.get(index-1).setText(swap);
				texts.get(index-1).requestFocus();
				texts.get(index-1).positionCaret(Integer.MAX_VALUE);
			}
		}
	}
	
	public void checkLast() {
		
		if(!texts.get(texts.size()-1).getText().trim().isEmpty()) {
			addStuff();
		}
	}
	
	public void hideLast() {
		
		for(Button button : deleteButtons) {
			button.setVisible(true);
		}
		deleteButtons.get(deleteButtons.size()-1).setVisible(false);
	}
	
	public void save() {
		
		PrintWriter pWriter = null;
		try {
			Files.deleteIfExists(Paths.get("saved.txt"));
			pWriter = new PrintWriter(new FileWriter("saved.txt", true), true);

			pWriter.println("// Autor: Yanko");
			pWriter.println("// Mail: yanko397@web.de");
			pWriter.println();
			pWriter.println("// Comments starting with double slashes and empty lines will be ignored by the program.");
			pWriter.println("// Beginnt eine Zeile mit Doppelslash oder ist sie leer, wird sie vom Programm ignoriert.");
			pWriter.println();
			pWriter.println("// The first not empty line, if starting with two integers, will define the position of the program on the screen");
			pWriter.println("// Die erste, nicht leere Zeile die mit zwei Zahlen beginnt, gibt die Position des Programms auf dem Bildschirm an");
			pWriter.println();
			pWriter.println((int)primaryStage.getX() + " " + (int)primaryStage.getY());
			pWriter.println();
			
			for(TextArea text : texts)
				pWriter.println(text.getText());
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (pWriter != null) pWriter.close();
	}
	
	public void restore() throws IOException {
		
		if(Files.exists(Paths.get("saved.txt"))) {
			BufferedReader br = new BufferedReader(new FileReader("saved.txt"));

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
				
				primaryStage.setX(Integer.parseInt(pos[0]));
				primaryStage.setY(Integer.parseInt(pos[1]));
			} catch (Exception e){
				primaryStage.centerOnScreen();
				addStuff(ifFails);
			}
			
			String line = br.readLine();
			while(line != null) {
				if(!line.trim().isEmpty() && !line.startsWith("//")) {
					addStuff(line);
				}
				line = br.readLine();
			}
			if(texts.isEmpty()) addStuff();

			br.close();
		} else {
			addStuff();
		}
		
		checkLast();
	}
	
	public void exitApplication() {
		save();
		System.exit(0);
	}
	
	public void restartApplication() {
		
		save();
		
		try {
			StringBuilder cmd = new StringBuilder();
			cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
			
			for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments())
				cmd.append(jvmArg + " ");
			
			cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
			cmd.append(Main.class.getName()).append(" ");
			
			for (String arg : startArgs)
				cmd.append(arg).append(" ");

			Runtime.getRuntime().exec(cmd.toString());
			System.exit(0);
		} catch (IOException e) {
			addStuff("FEHLER - Neustart nicht möglich");
			checkLast();
		}
	}
	
	public String getResource(String path) {
		return Main.class.getResource(path).toExternalForm();
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public ArrayList<Text> getTexts(){
		return texts;
	}
}
