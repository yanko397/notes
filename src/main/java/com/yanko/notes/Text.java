package com.yanko.notes;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

/**
 * @author Yanko
 * class for a text area text
 */
public class Text extends TextArea {

	private Main main = null;
	private ArrayList<Text> texts = null;
	private boolean marked = false;

	/**
	 * initiates the text with the calling object
	 * @param main object calling this function
	 */
	public void init(Main main) {
		this.main = main;
		texts = main.getTexts();
		setHandlers();
	}
	
	public void mark() {
		marked = true;
		getStyleClass().remove("unmarked");
		getStyleClass().add("marked");
//		setOpacity(1);
//		setStyle("-fx-border-color: black;"
//				+ "-fx-border-insets: -1.2;"
//				+ "-fx-border-width: 2;");
	}
	
	public void unmark() {
		marked = false;
		getStyleClass().remove("marked");
		getStyleClass().add("unmarked");
//		setOpacity(0.7);
//		setStyle("-fx-border-color: green;" + 
//				"-fx-border-insets: -1.5;" + 
//				"-fx-border-width: 1;");
	}
	
	/**
	 * sets all handlers for the text area
	 */
	private void setHandlers() {
		
		textProperty().addListener(new ChangeListener<String>( ) {
			@Override
			public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
				if(getText().length() > main.getTextLimit())
					setText(oldValue);
				if(newValue.isEmpty() && oldValue.length() > 0) {
					main.deleteStuff(getId());
					main.getTexts().get(main.getTexts().size()-1).requestFocus();
					main.getTexts().get(main.getTexts().size()-1).positionCaret(Integer.MAX_VALUE);;
				}
			}
		});
		
		setOnMouseClicked(e -> {
			if(e.getClickCount() == 2) {
				deselect();
				if(marked) {
					unmark();
				} else {
					mark();
				}
			}
		});
		
		setOnKeyReleased(e -> {
			if(!getText().trim().equals("") && texts.indexOf(this) >= texts.size()-1) {
				main.addStuff();
			}
		});
		
		setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER) {
				e.consume();
				if(texts.indexOf(this) == texts.size()-1) {
					texts.get(0).requestFocus();
					texts.get(0).positionCaret(Integer.MAX_VALUE);
				} else {
					texts.get(texts.indexOf(this)+1).requestFocus();
					texts.get(texts.indexOf(this)+1).positionCaret(Integer.MAX_VALUE);
				}
			}
			
			if(e.getCode() == KeyCode.TAB) {
				if(!e.isShiftDown()) {
					e.consume();
					if(texts.indexOf(this) == texts.size()-1) {
						texts.get(0).requestFocus();
						texts.get(0).positionCaret(Integer.MAX_VALUE);
					} else {
						texts.get(texts.indexOf(this)+1).requestFocus();
						texts.get(texts.indexOf(this)+1).positionCaret(Integer.MAX_VALUE);
					}
				} else {
					e.consume();
					if(texts.indexOf(this) == 0) {
						texts.get(texts.size()-1).requestFocus();
						texts.get(texts.size()-1).positionCaret(Integer.MAX_VALUE);;
					} else {
						texts.get(texts.indexOf(this)-1).requestFocus();
						texts.get(texts.indexOf(this)-1).positionCaret(Integer.MAX_VALUE);;
					}
				}
			}
			
			if(e.getCode() == KeyCode.D) {
				if(e.isControlDown() && texts.indexOf(this) != texts.size()-1) {
					e.consume();
					if(texts.indexOf(this) > 0) {
						texts.get(texts.indexOf(this)-1).requestFocus();
					} else if(texts.size() > 1){
						texts.get(1).requestFocus();
					}
					main.deleteStuff(getId());
				}
			}
			
			if(e.isControlDown() && e.getCode() == KeyCode.Z && !main.getStack().isEmpty()) {
				e.consume();
				texts.get(texts.size()-1).setText(main.getStack().pop());
				main.checkLast();
			}
			
			if(e.getCode() == KeyCode.UP) {
				e.consume();
				if(e.isAltDown()) {
					main.moveStuff(texts.indexOf(this), Main.Direction.UP);
				} else {
					if(texts.indexOf(this) != 0) {
						texts.get(texts.indexOf(this)-1).requestFocus();
						texts.get(texts.indexOf(this)-1).positionCaret(Integer.MAX_VALUE);
					}
				}
			}
			
			if(e.getCode() == KeyCode.DOWN) {
				if(e.isAltDown()) {
					main.moveStuff(texts.indexOf(this), Main.Direction.DOWN);
				} else {
					if(texts.indexOf(this) != texts.size()-1) {
						texts.get(texts.indexOf(this)+1).requestFocus();
						texts.get(texts.indexOf(this)+1).positionCaret(Integer.MAX_VALUE);
					}
				}
			}
		});
	}
}
