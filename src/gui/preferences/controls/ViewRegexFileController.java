package gui.preferences.controls;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ViewRegexFileController{

	@FXML private TextArea xmlTextArea;

	public void setText(String linkToXML) {
	    try {
	        Scanner s = new Scanner(new File(linkToXML));
	        while (s.hasNextLine()) {
	                xmlTextArea.appendText(s.nextLine()+"\n"); 
	        }
		    s.close();

	    } catch (FileNotFoundException ex) {
	        System.err.println(ex);
	    }
	}

	
}
