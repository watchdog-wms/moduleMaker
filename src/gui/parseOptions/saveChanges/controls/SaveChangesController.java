package gui.parseOptions.saveChanges.controls;

import gui.parseOptions.parseOverview.controls.ParseOverviewController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class SaveChangesController {

	private ParseOverviewController poc;
	
	@FXML
	private Button yesButton;
	
	public void setOverviewController(ParseOverviewController parseOverviewController) {
		this.poc = parseOverviewController;
	}

	@FXML public void handleDoNotSaveAction(ActionEvent event) {
		poc.setSave(false);
		Stage stage = (Stage) yesButton.getScene().getWindow();
		stage.close();
	}

	@FXML public void handleSaveChangesAction(ActionEvent event) {
		poc.setSave(true);
		Stage stage = (Stage) yesButton.getScene().getWindow();
		stage.close();
	}
	
	

}
