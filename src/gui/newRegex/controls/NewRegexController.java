package gui.newRegex.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import logic.writeRegex.RegexWriter;

/**
 * Controller for creating a new regex file.
 * 
 * @author Amrei Menzel
 *
 */
public class NewRegexController {

	/**
	 * Start of the usage line.
	 */
	@FXML
	private TextField startUsageLine;
	private String usageLine = "-";

	/**
	 * regex for the required option in usage line.
	 */
	@FXML
	private TextField requiredOption;
	private String required = "-";

	/**
	 * regex for the optional option in usage line.
	 */
	@FXML
	private TextField optionalOption;
	private String optional = "-";

	/**
	 * regex to parse the short option from usage.
	 */
	@FXML
	private TextField shortOptionHelp;
	private String shortHelp = "-";

	/**
	 * regex for calling the short option.
	 */
	@FXML
	private TextField shortOptionCall;
	private String shortCall = "-";

	/**
	 * regex to parse the long option from usage.
	 */
	@FXML
	private TextField longOptionHelp;
	private String longHelp = "-";

	/**
	 * regex for calling the long option.
	 */
	@FXML
	private TextField longOptionCall;
	private String longCall = "-";

	/**
	 * regex for calling the short option if it is a boolean.
	 */
	@FXML
	private TextField shortOptionCallBoolean;
	private String shortCallb = "-";

	/**
	 * regex for calling the long option if it is a boolean.
	 */
	@FXML
	private TextField longOptionCallBoolean;
	private String longCallb = "-";

	/**
	 * regex to parse the short option from usage if it is a boolean.
	 */
	@FXML
	private TextField shortOptionHelpBoolean;
	private String shortHelpb = "-";

	/**
	 * regex to parse the long option from usage if it is a boolean.
	 */
	@FXML
	private TextField longOptionHelpBoolean;
	private String longHelpb = "-";

	/**
	 * separation between short and long options.
	 */
	@FXML
	private TextField sepOptions;
	private String sep = "-";

	/**
	 * regex to parse the type from usage.
	 */
	@FXML
	private TextField typeLabel;
	private String type = "-";

	/**
	 * String representation of type integer.
	 */
	@FXML
	private TextField typeInteger;
	private String tInt = "-";

	/**
	 * string epresentation of type string.
	 */
	@FXML
	private TextField typeString;
	private String tString = "-";

	/**
	 * String representation of type boolean.
	 */
	@FXML
	private TextField typeBoolean;
	private String tBool = "-";

	/**
	 * String representation of type file.
	 */
	@FXML
	private TextField typeFile;
	private String tFile = "-";

	/**
	 * String representation of type folder.
	 */
	@FXML
	private TextField typeFolder;
	private String tFolder = "-";

	/**
	 * String representation of type double.
	 */
	@FXML
	private TextField typeDouble;
	private String tDouble = "-";

	/**
	 * regex to parse the default value from the usage.
	 */
	@FXML
	private TextField defaultValue;
	private String def = "-";

	/**
	 * regex to parse the description from the usage.
	 */
	@FXML
	private TextField description;
	private String des = "-";

	/**
	 * Message if input is wrong or incomplete.
	 */
	@FXML
	private Text warningMessage;

	/**
	 * root pane.
	 */
	@FXML
	private AnchorPane rootPane;

	/**
	 * Checks if all required textfields are filled out and setting the input
	 * strings. Required are the start of usage line and either short or long
	 * option.
	 * 
	 * @return
	 */
	private boolean checkTextFields() {
		if (startUsageLine.getText().isEmpty()) {
			warningMessage.setText("Start of Usageline is required.");
			warningMessage.setVisible(true);
			return false;
		} else
			usageLine = startUsageLine.getText();
		if (shortOptionHelp.getText().isEmpty() && longOptionHelp.getText().isEmpty()) {
			warningMessage.setText("Either shortOption or longOption must be set.");
			warningMessage.setVisible(true);
			return false;
		}
		if (!shortOptionHelp.getText().isEmpty()) {
			if (shortOptionCall.getText().isEmpty()) {
				warningMessage.setText("Short option: Please set call");
				warningMessage.setVisible(true);
				return false;
			}
			if (!shortOptionHelp.getText().contains("option")) {
				warningMessage.setText("Short option: Please use the Keyword 'option'");
				warningMessage.setVisible(true);
				return false;
			}
			shortHelp = shortOptionHelp.getText();
			shortCall = shortOptionCall.getText();
		}
		if (!longOptionHelp.getText().isEmpty()) {
			if (longOptionCall.getText().isEmpty()) {
				warningMessage.setText("Long option: Please set call");
				warningMessage.setVisible(true);
				return false;
			}
			if (!longOptionHelp.getText().contains("option")) {
				warningMessage.setText("Long option: Please use the Keyword 'option'");
				warningMessage.setVisible(true);
				return false;
			}
			longHelp = longOptionHelp.getText();
			longCall = longOptionCall.getText();
		}
		if (!longOptionCallBoolean.getText().isEmpty()) {
			longCallb = longOptionCallBoolean.getText();
		}
		if (!shortOptionCallBoolean.getText().isEmpty()) {
			shortCallb = shortOptionCallBoolean.getText();
		}
		if (!longOptionHelpBoolean.getText().isEmpty()) {
			longHelpb = longOptionHelpBoolean.getText();
		}
		if (!shortOptionHelpBoolean.getText().isEmpty()) {
			shortHelpb = shortOptionHelpBoolean.getText();
		}
		if (!requiredOption.getText().isEmpty()) {
			if (!requiredOption.getText().contains("option")) {
				warningMessage.setText("Required: Please use the Keyword 'option'or leave the textfield blank");
				warningMessage.setVisible(true);
				return false;
			}
			required = requiredOption.getText();
		}
		if (!optionalOption.getText().isEmpty()) {
			if (!optionalOption.getText().contains("option")) {
				warningMessage.setText("Optional: Please use the Keyword 'option'or leave the textfield blank");
				warningMessage.setVisible(true);
				return false;
			}
			optional = optionalOption.getText();
		}
		// Checks if the fileds have input. if not the regex will be set to "-".
		if (!sepOptions.getText().isEmpty()) {
			sep = sepOptions.getText();
		}
		if (!typeLabel.getText().isEmpty()) {
			type = typeLabel.getText();
		}
		if (!typeInteger.getText().isEmpty()) {
			tInt = typeInteger.getText();
		}
		if (!typeString.getText().isEmpty()) {
			tString = typeString.getText();
		}
		if (!typeBoolean.getText().isEmpty()) {
			tBool = typeBoolean.getText();
		}
		if (!typeFile.getText().isEmpty()) {
			tFile = typeFile.getText();
		}
		if (!typeFolder.getText().isEmpty()) {
			tFolder = typeFolder.getText();
		}
		if (!typeDouble.getText().isEmpty()) {
			tDouble = typeDouble.getText();
		}
		if (!defaultValue.getText().isEmpty()) {
			def = defaultValue.getText();
		}
		if (!description.getText().isEmpty()) {
			des = description.getText();
		}
		return true;
	}

	/**
	 * After clicking Continue you can save the file.
	 * 
	 * @param event
	 */
	@FXML
	public void handleContinueAction(final ActionEvent event) {
		if (!checkTextFields()) {
			return;
		}
		RegexWriter regexWriter = new RegexWriter();
		regexWriter.parseInputValues(usageLine, required, optional, shortCall, longCall, shortHelp, longHelp, sep, type, tInt,
				tString, tBool, tFile, tFolder, def, des, shortCallb, longCallb, shortHelpb, longHelpb, tDouble);
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open File");
		File selectedFile = chooser.showSaveDialog(null);
		if (selectedFile != null) {
			regexWriter.writeXMLString(selectedFile.getAbsolutePath());
			try {
				rootPane.getChildren().removeAll(rootPane.getChildren());
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/newRegex/fxml/saveRegex.fxml"));
				rootPane.getChildren().add(fxmlLoader.load());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
