import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * @author Yanko
 * class for the right click menu
 */
public class RightclickMenu extends ContextMenu {
	
	/**
	 * constructor
	 * @param main object calling this function
	 */
	public RightclickMenu(Main main) {
		init(main);
	}
	
	/**
	 * sets up the rightclick menu
	 * @param main object calling this function
	 */
	private void init(Main main) {
		main.getFace().setOnContextMenuRequested(e -> show(main.getPrimaryStage(), e.getScreenX(), e.getScreenY()));
		
		MenuItem options = new MenuItem("Einstellungen");
		options.setOnAction(e -> new Options(main));
		MenuItem info = new MenuItem("Info");
		info.setOnAction(e -> new Info(main));
		MenuItem undo = new MenuItem("Löschen rückgängig machen");
		undo.setOnAction(e -> {
			if(!main.getStack().isEmpty()) {
				main.getTexts().get(main.getTexts().size()-1).setText(main.getStack().pop());
				main.checkLast();
			}
		});
		MenuItem deleteAll = new MenuItem("Alle löschen");
		deleteAll.setOnAction(e -> {while(main.getDeleteButtons().size() > 1) main.deleteStuff(main.getDeleteButtons().get(0).getId());});
		MenuItem minimize = new MenuItem("Minimieren");
		minimize.setOnAction(e -> main.getPrimaryStage().setIconified(true));
		MenuItem restart = new MenuItem("Neu starten");
		restart.setOnAction(e -> {
			Data.save(main);
			UglyShit.restartApplication(main.getArgs());
		});
		MenuItem exit = new MenuItem("Beenden");
		exit.setOnAction(e -> main.exitApplication());
		
		getItems().add(options);
		getItems().add(info);
		getItems().add(undo);
		getItems().add(deleteAll);
		getItems().add(minimize);
		getItems().add(exit);
	}
}
