package gui.start.controls;

import gui.menu.Main;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class StartController {

	@FXML TabPane rootPane;

	@FXML public void handleClickedDokuEN(ActionEvent event) {
		Main.getHostSer().showDocument("DocumentationEN.pdf");
	}
	
	public void setSizeProperties(DoubleBinding doubleBinding, ReadOnlyDoubleProperty widthProperty) {
		rootPane.prefHeightProperty().bind(doubleBinding);
        rootPane.prefWidthProperty().bind(widthProperty);		
	}
}
