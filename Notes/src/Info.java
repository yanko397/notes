import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * @author Yanko
 * class of the info window
 */
public class Info extends Window {

	/**
	 * initiates the info window with the calling object
	 * @param main object calling this function
	 */
	public Info(Main main) {
		super("Info");
		init(main);
	}
	
	/**
	 * sets up the info window
	 * @param main object calling this function
	 */
	private void init(Main main) {
		Label controlTitle = new Label();
		controlTitle.setText("Steuerung");
		controlTitle.setFont(new Font(20));
		
		Label infoTitle = new Label();
		infoTitle.setText("Info");
		infoTitle.setFont(new Font(20));
		
		Button okButton = new Button("OK");
		okButton.setOnAction(e -> close()); 
		
		add(controlTitle);
		add(new Label("Strg + D:\n"
				+ "Löschen der aktuellen Notiz"));
		add(new Label("Alt + Hoch/Runter:\n"
				+ "Verschieben der aktuellen Notiz"));
		add(new Label("Strg + Z:\n"
				+ "Zuletzt gelöschtes wieder hinzufügen"));
		add(new Label(""));
		add(infoTitle);
		add(new Label("Autor:  \t" + Main.AUTHOR));
		add(new Label("Mail:   \t" + Main.MAIL));
		add(new Label("Version:\t" + Main.VERSION));
		add(new Label(""));
		add(okButton);

		sizeToScene();
		show();
	}
}
