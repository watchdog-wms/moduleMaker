package gui.parseOptions.overviewHelp.controls;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

/**
 * The dialog that give informations for the parameter in the parse overview
 * @author Amrei Menzel
 *
 */
public class OverviewHelpController {

	@FXML private Button okButton;

	/**
	 * close the help dialog.
	 * @param event
	 */
	@FXML public void handleClickOKAction(ActionEvent event) {
		Stage stage = (Stage) okButton.getScene().getWindow();
	    stage.close();
	}

}
