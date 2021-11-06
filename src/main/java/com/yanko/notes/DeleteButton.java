package com.yanko.notes;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @author Yanko
 * class of a delete button
 */
public class DeleteButton extends Button{

	private Main main = null;
	private Stage primaryStage = null;
	
	private boolean dragged = false;
	private boolean rightclick = false;
	
	/**
	 * initiate the button with the calling object
	 * @param main object calling this function
	 */
	public void init(Main main) {
		this.main = main;
		primaryStage = main.getPrimaryStage();
		setHandlers();
	}
	
	
	/**
	 * sets all handlers for this button
	 */
	private void setHandlers() {
		
		setOnMouseReleased(e -> {
			if(!dragged && !rightclick) main.deleteStuff(getId());
			dragged = false;
			rightclick = false;
		});
		
		
		final Delta dragDelta = new Delta();
		
		setOnMousePressed(e -> {
			// record a delta distance for the drag and drop operation.
			dragDelta.x = primaryStage.getX() - e.getScreenX();
			dragDelta.y = primaryStage.getY() - e.getScreenY();
			
			if(e.isSecondaryButtonDown()) rightclick = true;
		});
		setOnMouseDragged(e -> {
			primaryStage.setX(e.getScreenX() + dragDelta.x);
			primaryStage.setY(e.getScreenY() + dragDelta.y);
			dragged = true;
		});
		setOnMouseEntered(e -> {
			if (!e.isPrimaryButtonDown()) {
				primaryStage.getScene().setCursor(Cursor.HAND);
			}
		});
		setOnMouseExited(e -> {
			if (!e.isPrimaryButtonDown()) {
				primaryStage.getScene().setCursor(Cursor.DEFAULT);
			}
		});
	}
}
