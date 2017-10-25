import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Face extends Pane {
	
	private Stage primaryStage = null;
	
	public void init(Main main) {
		primaryStage = main.getPrimaryStage();
		setHandlers();
	}

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
