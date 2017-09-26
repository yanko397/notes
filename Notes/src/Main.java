import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
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
		
		addContextMenu();
		
		Data.restore(this);
		
		
		// save the notes, if the stage gains or looses focus
		primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				Data.save(primaryStage, texts);
			}
		});

		primaryStage.show();
		
		// so you can minimize by clicking on taskbar icon
		UglyShit.behinderterHaesslicherKack();
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
	
	private void addDeleteButton() {

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
	
	private void addText(String revived) {
		
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
	
	private void addContextMenu() {
		
		ContextMenu contextMenu = new ContextMenu();
		face.setOnContextMenuRequested(e -> contextMenu.show(primaryStage, e.getScreenX(), e.getScreenY()));
		
		MenuItem options = new MenuItem("Einstellungen");
		options.setOnAction(e -> showOptions());
		MenuItem deleteAll = new MenuItem("Alle löschen");
		deleteAll.setOnAction(e -> {while(deleteButtons.size() > 1) deleteStuff(deleteButtons.get(0).getId());});
		MenuItem minimize = new MenuItem("Minimieren");
		minimize.setOnAction(e -> primaryStage.setIconified(true));
		MenuItem restart = new MenuItem("Neu starten");
		restart.setOnAction(e -> {
			Data.save(primaryStage, texts);
			UglyShit.restartApplication(startArgs);
		});
		MenuItem exit = new MenuItem("Beenden");
		exit.setOnAction(e -> exitApplication());
		
		contextMenu.getItems().addAll(options, deleteAll, minimize, exit);
	}
	
	public void showOptions() {
		
		VBox optionBox = new VBox();
		optionBox.prefWidth(100);
		optionBox.prefHeight(100);
		optionBox.getChildren().add(new TextField("fuck you xD"));
		
		Scene optionScene = new Scene(optionBox, Color.GRAY);

		Stage options = new Stage();
		options.setScene(optionScene);
		options.initStyle(StageStyle.DECORATED);
		
		options.sizeToScene();
		options.show();
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
	
	public void exitApplication() {
		Data.save(primaryStage, texts);
		System.exit(0);
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
