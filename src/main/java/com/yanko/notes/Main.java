package com.yanko.notes;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Yanko
 * main class
 */
public class Main extends Application {
	
	private static String[] startArgs = null;
	
	public static final String	VERSION			= "1.7.2";
	public static final String	AUTHOR			= "Johannes Bräuer";
	public static final String	MAIL			= "yanko397@web.de";
	public static final int		DEFAULT_LIMIT	= 100;
	
	private final int width = 300;
	private final int height = 80;
	private final int deleteButtonWidth = 40;
	private final int deleteButtonInsetX = width - 57;
	private final int deleteButtonInsetY = 27;
	private final int textWidth = width - 75;
	private final int textHeight = 50;
	private final int textInsets = 15;
	private final int absoluteOffset = textHeight + 5;
	
	private int textLimit = DEFAULT_LIMIT;
	
	private int count = 0;
	private int totalOffset = 0;

	private Stage primaryStage;
	private Scene scene;
	private Face face;
	
	private Deque<String> stack = new LinkedList<String>();
	private ArrayList<Text> texts = new ArrayList<Text>();
	private ArrayList<DeleteButton> deleteButtons = new ArrayList<DeleteButton>();

	public enum Direction {UP,DOWN}
	
	/**
	 * launches the fx application
	 * @param args
	 */
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
		
		scene = new Scene(face, Color.TRANSPARENT);
		scene.getStylesheets().add("config.css");
		
		primaryStage.setTitle("Notes");
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.getIcons().add(new Image("note.png"));
		primaryStage.setScene(scene);
		
		new RightclickMenu(this);
		
		Data.restore(this);
		
		
		// save the notes, if the stage gains or looses focus
		primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				Data.save(Main.this);
			}
		});

		primaryStage.show();
		
		// so you can minimize by clicking on taskbar icon
		UglyShit.behinderterHaesslicherKack();
	}
	
	/**
	 * add stuff with an empty string
	 */
	public void addStuff() {
		addStuff("");
	}
	
	/**
	 * adds a text area with text and an corresponding delete button
	 * @param revived string to be pasted in the box
	 */
	public void addStuff(String revived) {
		
		addText(revived);
		addDeleteButton();
		hideLast();
		
		face.setPrefHeight(height + totalOffset);
		primaryStage.sizeToScene();
		
		totalOffset += absoluteOffset;
		count++;
	}
	
	/**
	 * adds a delete button
	 */
	private void addDeleteButton() {

		DeleteButton deleteButton = new DeleteButton();
		deleteButton.init(this);
		deleteButton.setPrefWidth(deleteButtonWidth);
		deleteButton.setLayoutX(deleteButtonInsetX);
		deleteButton.setLayoutY(deleteButtonInsetY + totalOffset);
		deleteButton.setId(count+"");
		deleteButton.getStyleClass().add("deleteButtons");

		deleteButtons.add(deleteButton);
		face.getChildren().add(deleteButton);
	}
	
	/**
	 * adds a text area with the given text in it
	 * @param revived string to be pasted in the box
	 */
	private void addText(String revived) {
		
		Text text = new Text();
		text.init(this);
		text.setPrefHeight(textHeight);
		text.setPrefWidth(textWidth);
		text.setLayoutX(textInsets);
		text.setLayoutY(textInsets + totalOffset);
		text.setBlendMode(BlendMode.OVERLAY);
		text.setFont(new Font(13));
		text.setWrapText(true);
		text.setId(count+"");
		text.setStyle("-fx-border-color: green;" + 
				"-fx-border-insets: -1.5;" + 
				"-fx-border-width: 1;");
		text.getStyleClass().add("text");
		if(revived.startsWith("*marked*")) {
			text.mark();
			revived = revived.substring(8);
		} else text.unmark();
		text.setText(revived);
		
		texts.add(text);
		face.getChildren().add(text);
	}
	
	
	/**
	 * deletes a text area and the corresponding delete button at a given index
	 * @param index index of the elements to delete
	 */
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
		
		if(!texts.get(foundIndex).getText().trim().isEmpty())
			stack.push(texts.get(foundIndex).getText());
		
		deleteButtons.remove(foundIndex);
		texts.remove(foundIndex);
		
		totalOffset -= absoluteOffset;
	}
	
	/**
	 * elements at the given index and the next elements above or below them switch places
	 * @param index index of the current element
	 * @param dir direction to move the current element
	 */
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
	
	/**
	 * checks if the last text area is empty; if it isn't it adds an empty one
	 */
	public void checkLast() {
		if(!texts.get(texts.size()-1).getText().trim().isEmpty()) {
			addStuff();
		}
	}
	
	/**
	 * shows all delete buttons and hides the last one
	 */
	public void hideLast() {
		for(Button button : deleteButtons)
			button.setVisible(true);
		deleteButtons.get(deleteButtons.size()-1).setVisible(false);
	}
	
	/**
	 * exits the application
	 */
	public void exitApplication() {
		Data.save(this);
		Platform.exit();
	}

	// ======================== getter and setter ============================ //
	
	public String[] getArgs() {return startArgs;}
	public Stage getPrimaryStage() {return primaryStage;}
	public Face getFace() {return face;}
	public ArrayList<Text> getTexts() {return texts;}
	public ArrayList<DeleteButton> getDeleteButtons() {return deleteButtons;}
	public Deque<String> getStack() {return stack;}
	public int getTextLimit() {return textLimit;}
	public void setTextLimit(int value) {textLimit = value;}
}
