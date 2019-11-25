package gui.parseOptions.successful.controls;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;

import java.io.File;

import gui.menu.Main;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;

public class SuccessfulController {

	private String folder;
	private String sh;
	private String xsd;
	@FXML private Button openFolder;
	@FXML private Button startWatchdog;
	@FXML private Hyperlink openXSD;
	@FXML private Hyperlink openSH;
	@FXML private AnchorPane root;
	
	public void setPaths(String folder, String sh, String xsd) {
		this.folder= folder;
		this.sh=sh;
		this.xsd = xsd;
		openXSD.setText(new File(xsd).getAbsolutePath());
		openSH.setText(new File(sh).getAbsolutePath());
	}
	
	public static String getPath(String input) {
		return new File(input).getAbsoluteFile().toURI().toString();
	}

	@FXML public void handleOpenFolderAction(ActionEvent event) {
		Main.getHostSer().showDocument(getPath(this.folder));
	}

	@FXML public void handleStartWatchdogAction(ActionEvent event) {
		
	}

	@FXML public void handleOpenXSDAction(ActionEvent event) {
		Main.getHostSer().showDocument(getPath(this.xsd));
	}
	
	@FXML public void handleOpenSHAction(ActionEvent event) {
		Main.getHostSer().showDocument(getPath(this.sh));
	}

	public void setSize(DoubleBinding heightProperty, ReadOnlyDoubleProperty widthProperty) {
		root.prefHeightProperty().bind(heightProperty);
        root.prefWidthProperty().bind(widthProperty);		
	}
}
