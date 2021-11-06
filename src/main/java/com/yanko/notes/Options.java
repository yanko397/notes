package com.yanko.notes;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * @author Yanko
 * class of the options window
 */
public class Options extends Window {

	private Main main;
	private NumberTextField limitTextField;
	
	/**
	 * constructor
	 * @param main object calling this function
	 */
	public Options(Main main) {
		super("Einstellungen");
		this.main = main;
		init();
	}

	/**
	 * sets up the option window
	 */
	private void init() {
		
		Label optionTitleGeneral = new Label("Allgemein");
		optionTitleGeneral.setFont(new Font(20));
		
		limitTextField = new NumberTextField(main.getTextLimit()+"");
		
		Button acceptButton = new Button("Übernehmen");
		acceptButton.setDefaultButton(true);
		acceptButton.setOnAction(e -> accept());

		add(optionTitleGeneral);
		add(new Label("maximale Anzahl Zeichen:"));
		add(limitTextField);
		add(new Label(""));
		add(acceptButton);
		
		sizeToScene();
		show();
	}
	
	public void accept() {
		boolean somethingFailed = false;
		
		// LIMIT TEXT FIELD
		try {
			int inputLimit = Integer.parseInt(limitTextField.getText());
			boolean inputFailed = false;
			for(Text text : main.getTexts()) {
				if(text.getText().length() > inputLimit) {
					inputFailed = true;
					somethingFailed = true;
					
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Achtung");
					alert.setHeaderText(null);
					alert.setContentText("Die eingegebene Zahl darf nicht kleiner sein als deine längste Notiz lang ist.");
					alert.showAndWait();
					
					break;
				}
			}
			
			if(!inputFailed) {
				main.setTextLimit(inputLimit);
			}
		} catch(Exception limitFail) {
			somethingFailed = true;
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Achtung");
			alert.setHeaderText(null);
			alert.setContentText("Die Eingabe ist ungültig.");
			alert.showAndWait();
		}

		if(!somethingFailed) close();
	}
}
