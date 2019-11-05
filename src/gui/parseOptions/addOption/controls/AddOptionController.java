package gui.parseOptions.addOption.controls;

import gui.parseOptions.parseOverview.Option;
import gui.parseOptions.parseOverview.controls.ParseOverviewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 
 * Controller to add option to the parse overview.
 * @author Amrei Menzel
 *
 */
public class AddOptionController {
	
	@FXML
	private TextField optionName;
	
	@FXML
	private TextField optionShortOpt;
	
	@FXML
	private TextField optionLongOpt;
	
	@FXML
	private TextField optionMin;
	
	@FXML
	private TextField optionMax;
	
	@FXML
	private TextField optionDefault;
	
	@FXML
	private TextField optionRestriction;
	
	@FXML
	private TextField optionDescription;
	
	@FXML
	private ComboBox<?> optionType;
	
	
	private ParseOverviewController poc;
	
	@FXML
	private ComboBox<?> callType;
	
	@FXML
	private Text warning;
	
	@FXML
	private Button cancelButton;

	
	public void setOverviewController(final ParseOverviewController parseOverviewController) {
		this.poc = parseOverviewController;
	}
	
	
	@FXML
	protected void handleCancelAction(final ActionEvent event) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
	
	/**
	 * Check parameter for the new option and defiens values if nothing is set
	 * @return
	 */
	private boolean checkArguments() {
		if(optionName.getText().isEmpty()) {
			warning.setVisible(true);
			return false;
		}
		if(optionShortOpt.getText().isEmpty()) {
			optionShortOpt.setText("-");
		}
		if(optionLongOpt.getText().isEmpty()) {
			optionLongOpt.setText("-");
		}
		if(optionMin.getText().isEmpty()) {
			optionMin.setText("0");
		}
		if(optionMax.getText().isEmpty()) {
			optionMax.setText("1");
		}
		if(optionDefault.getText().isEmpty()) {
			optionDefault.setText("-");
		}
		if(optionRestriction.getText().isEmpty()) {
			optionRestriction.setText("-");
		}
		if(optionDescription.getText().isEmpty()) {
			optionDescription.setText("-");
		}
		if(optionType.getValue()==null) {
			optionType.getSelectionModel().select(1);
		}
		if(callType.getValue()==null) {
			callType.getSelectionModel().select(2);
		}
		return true;
	}
	
	@FXML
	protected void handleContinueAction(final ActionEvent event) {
		if(checkArguments()) {
			Option option =new Option(poc.getDataSize(), optionName.getText(), optionShortOpt.getText(), optionLongOpt.getText(), optionType.getValue().toString(), optionMin.getText(), optionMax.getText(), optionDefault.getText(), optionRestriction.getText(), optionDescription.getText(),poc.getCall(callType.getValue().toString()), callType.getValue().toString());
			poc.addOption(option);
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			stage.close();
		}
	}

}
