import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DeleteButton extends Button{

	private Main main = null;
	private Stage primaryStage = null;
	
	private boolean dragged = false;
	private boolean rightclick = false;
	
	public void init(Main main) {
		
		this.main = main;
		primaryStage = main.getPrimaryStage();
	}
	
	public void setHandlers() {
		
		setOnMouseReleased(e -> {
			if(!dragged && !rightclick) main.deleteStuff(getId());
			dragged = false;
			rightclick = false;
		});
		
		
		final Delta dragDelta = new Delta();
		
		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// record a delta distance for the drag and drop operation.
				dragDelta.x = primaryStage.getX() - mouseEvent.getScreenX();
				dragDelta.y = primaryStage.getY() - mouseEvent.getScreenY();
				
				if(mouseEvent.isSecondaryButtonDown()) rightclick = true;
			}
		});
		
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				primaryStage.setX(mouseEvent.getScreenX() + dragDelta.x);
				primaryStage.setY(mouseEvent.getScreenY() + dragDelta.y);
				dragged = true;
			}
		});
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (!mouseEvent.isPrimaryButtonDown()) {
					primaryStage.getScene().setCursor(Cursor.HAND);
				}
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (!mouseEvent.isPrimaryButtonDown()) {
					primaryStage.getScene().setCursor(Cursor.DEFAULT);
				}
			}
		});
	}
	
	private class Delta {
		double x, y;
	}
}
