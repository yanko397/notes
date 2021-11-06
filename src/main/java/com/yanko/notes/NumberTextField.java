package com.yanko.notes;

import javafx.scene.control.TextField;

/**
 * @author Yanko
 * a text field that only allows numbers as input
 */
public class NumberTextField extends TextField{
		
		/**
		 * constructor
		 * @param text number that will be in the text field
		 */
		public NumberTextField(String text) {
			super(text);
		}
		
		@Override
		public void replaceText(int start, int end, String text) {
			if(text.matches("[0-9]") || text == "") {
				super.replaceText(start, end, text);
			}
		}
		
		@Override
		public void replaceSelection(String text) {
			if(text.matches("[0-9]") || text == "") {
				super.replaceSelection(text);
			}
		}
	}