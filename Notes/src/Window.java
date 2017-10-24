import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Window extends Stage {

	private VBox box;
	private Scene scene;
	
	public Window(String title) {
		box = new VBox();
		box.setPadding(new Insets(10));
		box.setSpacing(10);
		
		scene = new Scene(box, Color.WHITE);

		setTitle(title);
		setScene(scene);
		initStyle(StageStyle.DECORATED);
	}
	
	public void add(Node node) {
		box.getChildren().add(node);
	}
	
	public VBox getBox() {
		return box;
	}
}
