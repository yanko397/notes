import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Yanko
 * window class with settings for the options and the info window
 */
public class Window extends Stage {

	private VBox box;
	private Scene scene;
	
	/**
	 * constructor
	 * @param title title of the window
	 */
	public Window(String title) {
		box = new VBox();
		box.setPadding(new Insets(10));
		box.setSpacing(10);
		
		scene = new Scene(box, Color.WHITE);

		setTitle(title);
		setScene(scene);
		initStyle(StageStyle.DECORATED);
	}
	
	/**
	 * @param node adds an element to the window
	 */
	public void add(Node node) {
		box.getChildren().add(node);
	}
	
	/**
	 * returns the VBox containing the elements in this window
	 * @return box the VBox in this window
	 */
	public VBox getBox() {
		return box;
	}
}
