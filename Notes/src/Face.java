import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Yanko
 * class of the Pane called Face
 */
public class Face extends Pane {
	
	private Stage primaryStage = null;
	
	/**
	 * initiates the object with the calling object
	 * @param main object calling this function
	 */
	public void init(Main main) {
		primaryStage = main.getPrimaryStage();
		setHandlers();
	}

	/**
	 * sets all handlers for this element
	 */
	private void setHandlers() {

		final Delta dragDelta = new Delta();
		
		setOnMousePressed(e -> {
			// record a delta distance for the drag and drop operation.
			dragDelta.x = primaryStage.getX() - e.getScreenX();
			dragDelta.y = primaryStage.getY() - e.getScreenY();
		});
		setOnMouseDragged(e -> {
			primaryStage.setX(e.getScreenX() + dragDelta.x);
			primaryStage.setY(e.getScreenY() + dragDelta.y);
		});
	}
}
