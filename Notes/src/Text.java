import java.util.ArrayList;

import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

public class Text extends TextArea {

	private Main main = null;
	private ArrayList<Text> texts = null;

	public void init(Main main) {
		
		this.main = main;
		texts = main.getTexts();
	}
	
	public void setHandlers() {
		
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