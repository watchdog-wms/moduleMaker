package gui.newModule.controls;

import gui.parseOptions.parseOverview.controls.ParseOverviewController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;
import javafx.scene.control.Tab;
import logic.usageParser.ParseUsage;


/**
 * Dialog to create a new module.
 * 
 * @author Amrei Menzel
 *
 */
public class NewModuleController implements Initializable {

	/**
	 * Call of the programm which should be parsed for the new module.
	 */
	@FXML
	private TextField moduleCall;

	/**
	 * Name of the module.
	 */
	@FXML
	private TextField moduleName;

	/**
	 * Checks if an addiotional parameter is needed for showing the usage.
	 */
	@FXML
	private ToggleGroup addPara;

	/**
	 * Additional parameter for showing the usage.
	 */
	@FXML
	private TextField moduleExtraCall;

	/**
	 * Defines if the addtional parameter is before or after the normal programm
	 * call.
	 */
	@FXML
	private ToggleGroup callOrder;

	/**
	 * Cancel the module creation.
	 */
	@FXML
	private Button cancelButton;

	/**
	 * Defines the output folder.
	 */
	@FXML
	private TextField outputFolder;

	/**
	 * Checks if the wm file should be kept.
	 */
	@FXML
	private ToggleGroup keepWM;

	/**
	 * Checks if a certain regex file is given.
	 */
	@FXML
	private ToggleGroup regex;

	/**
	 * path of the certain regexfile.
	 */
	@FXML
	private TextField regexPath;

	/**
	 * path to regex file could be browsed.
	 */
	@FXML
	private Button browsePath;

	/**
	 * number of solutions that should be shown.
	 */
	@FXML
	private Spinner<?> numberSolutions;

	/**
	 * Controller of the next page.
	 */
	private ParseOverviewController poc;

	/**
	 * defines if the parameter for the usage comes after the call.
	 */
	@FXML
	private RadioButton parameterAfter;

	/**
	 * defines if the parameter for the usage comes before the call.
	 */
	@FXML
	private RadioButton parameterBefore;

	/**
	 * The Text "Path:" for the regex file. Needs to have an idee to change the
	 * color form gray to black.
	 */
	@FXML
	private Text textfieldPath;

	/**
	 * If the input is wrong a warning wil be shown.
	 */
	@FXML
	private Text warning;

	/**
	 * number of solutions that should be shown.
	 */
	@FXML
	private IntegerSpinnerValueFactory numberSolutionsRange;

	/**
	 * The Text "How many solutions do you want to see?:" for the number of
	 * solutions. Needs to have an idee to change the color form gray to black.
	 */
	@FXML
	private Text numberSolutionsText;

	/**
	 * Logic class to parse the given usage.
	 */
	private ParseUsage parseUsage;

	/**
	 * maxpages = max number of solutions = number of regex files in regex file list.
	 */
	private int maxPages;

	/**
	 * tab for creating a module from a usage call.
	 */
	@FXML
	private Tab fromCommandLine;

	/**
	 * path to the usage file.
	 */
	@FXML
	private TextField usageFile;

	/**
	 * name of the module to be created.
	 */
	@FXML
	TextField moduleName2;

	/**
	 * call of the programm.
	 */
	@FXML
	TextField moduleCall2;

	/**
	 * Button for browsing the regex file.
	 */
	@FXML
	Button browseUsageFile2;

	/**
	 * textfield to give path to the regex file
	 */
	@FXML
	TextField usageFile2;

	/**
	 * heightProperty for binding the height.
	 */
	private DoubleBinding heightProperty;

	/**
	 * widthProperty to binging the width.
	 */
	private ReadOnlyDoubleProperty widthProperty;

	/**
	 * the menuPane
	 */
	private GridPane menuPane;

	/**
	 * fxmlLoader for the ParseOverviewController.
	 */
	private FXMLLoader fxmlLoaderPage;

	/**
	 * set the controller for the next page.
	 * 
	 * @param poc
	 *            controller for the next page.
	 */
	public void setParseOverviewController(final ParseOverviewController poc) {
		this.poc = poc;
	}

	/**
	 * Cancel the new module creation.
	 * 
	 * @param event
	 */
	@FXML
	protected void handleCancelAction(final ActionEvent event) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * Controlls the input and then it change to the next page. Before changing the
	 * page in calls the class parseUsage too parse the usage.
	 * 
	 * @param event
	 */
	@FXML
	protected void handleContinueAction(final ActionEvent event) {
		// check module name
		if((fromCommandLine.isSelected() && moduleName.getText().length() == 0) || (!fromCommandLine.isSelected() && moduleName2.getText().length() == 0)) {
			warning.setText("No name for the module was set!");
			warning.setVisible(true);
			return;
		}

		// check output folder
		String output = null;
		if (!outputFolder.getText().isEmpty()) {
			output = outputFolder.getText();
		}
		else {
			warning.setText("No output folder was set!");
			warning.setVisible(true);
			return;
		}

		// Sets the usage call
		String usageCall = moduleCall.getText();
		if (addPara.getSelectedToggle().getUserData().equals("yes")) {
			if (callOrder.getSelectedToggle().getUserData().equals("b")) {
				usageCall = moduleExtraCall.getText() + " " + usageCall;
			} else {
				usageCall += " " + moduleExtraCall.getText();
			}
		}

		// Checks if the given folder exist
		int failure = 0;
		// TODO: in logic
		File folder = new File(output);

		String modName = null;
		String modCall = "";

		if (!folder.exists()) {
			failure = -2;
		} else {
			// parse the usage
			if (fromCommandLine.isSelected()) {
				if (regex.getSelectedToggle().getUserData().equals("yes")) {
					failure = parseUsage.parseUsage(usageCall, regexPath.getText());
					maxPages = 1;
				} else {
					failure = parseUsage.findBestUsageAndParse_Usage(usageCall);
					maxPages = numberSolutionsRange.getValue();

				}
				modName = moduleName.getText();
				modCall = moduleCall.getText();
			} else {
				if (regex.getSelectedToggle().getUserData().equals("yes")) {
					failure = parseUsage.parseFile(usageFile.getText(), regexPath.getText(),
							output + "/" + moduleName2.getText() + ".wm");
					maxPages = 1;
				} else {
					failure = parseUsage.findBestUsageAndParse_File(usageFile.getText());
					maxPages = numberSolutionsRange.getValue();

				}
				modName = moduleName2.getText();
				modCall = moduleCall2.getText();
			}
		}

		if (failure >= 0) {
			try {
				menuPane.getChildren().clear();
				menuPane.getChildren().add(fxmlLoaderPage.load());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// Setting the values for the next page and closes the dialog
			poc = (ParseOverviewController) (fxmlLoaderPage.getController());
			poc.setOpts(parseUsage.getOptions());
			poc.setModuleCall(modCall);
			poc.setModuleName(modName);
			poc.setOutputFolder(output);
			poc.setMaxPages(maxPages);
			poc.initializeData(heightProperty, widthProperty);
			poc.updateTableView();
			poc.setKeepFile(keepWM.getSelectedToggle().getUserData().equals("yes"));
			poc.setMenuPane(menuPane);
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			stage.close();
			return;
		}
		// if the input is wrong, a warning will be shown
		switch (failure) {
		case -1:
			warning.setText("Not possible to get usage with call: '" 
					+ usageCall + "'. Please check!");
			warning.setVisible(true);
			break;
		case -2:
			warning.setText("Output folder does not exist. Please check!");
			warning.setVisible(true);
			break;
		case -3:
			warning.setText("Usage file does not exist. Please check!");
			warning.setVisible(true);
			break;
		default:
			break;
		}

	}

	/**
	 * EventHandler for browsing after a location for a output folder.
	 * 
	 * @param event
	 */
	@FXML
	protected void handleLocateFolderAction(final ActionEvent event) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Open File");
		File selectedFolder = chooser.showDialog(null);
		if (selectedFolder != null) {
			outputFolder.setText(selectedFolder.getAbsolutePath());
		}
	}

	/**
	 * EventHandler for browsing after a location for the regex file.
	 * 
	 * @param event
	 */
	@FXML
	protected void handleBrowseRegexFileAction(final ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open File");
		File selectedFile = chooser.showOpenDialog(null);
		if (selectedFile != null) {
			regexPath.setText(selectedFile.getAbsolutePath());
		}
	}

	/**
	 * the radioButtons parameterBefore and parameterAfter and the textfield
	 * moduleExtraCall are only available if an extra parameter for the usage is
	 * needed.
	 * 
	 * @param event
	 */
	@FXML
	public void handleAddParameterAction(final Event event) {
		if (addPara.getSelectedToggle().getUserData().equals("yes")) {
			parameterAfter.setDisable(false);
			parameterAfter.setSelected(true);
			parameterBefore.setDisable(false);
			moduleExtraCall.setDisable(false);
		} else {
			parameterBefore.setSelected(false);
			parameterAfter.setSelected(false);
			parameterAfter.setDisable(true);
			parameterBefore.setDisable(true);
			moduleExtraCall.setDisable(true);
		}
	}

	/**
	 * if an certain regex file should be taken the textfield for the path must be
	 * available. It is not possible to set the number of solution with only one
	 * regex file given.
	 * 
	 * @param event
	 */
	@FXML
	public void handleCertainRegexFileAction(final Event event) {
		if (regex.getSelectedToggle().getUserData().equals("yes")) {
			textfieldPath.setFill(Color.BLACK);
			regexPath.setDisable(false);
			browsePath.setDisable(false);
			numberSolutionsText.setFill(Paint.valueOf("#00000087"));
			numberSolutions.setDisable(true);
		} else {
			textfieldPath.setFill(Paint.valueOf("#00000087"));
			regexPath.setDisable(true);
			browsePath.setDisable(true);
			numberSolutionsText.setFill(Color.BLACK);
			numberSolutions.setDisable(false);

		}
	}

	/**
	 * Initialize the dialog and setting the max nuber of solutions.
	 */
	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		parseUsage = new ParseUsage();
		maxPages = parseUsage.getMaxPages();
		this.numberSolutionsRange.setMax(maxPages);
	}

	/**
	 * if a module shoudl be created from a file you can browse it here.
	 * 
	 * @param event
	 */
	@FXML
	public void handleLocateUsageFileAction(final ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open File");
		File selectedFile = chooser.showOpenDialog(null);
		if (selectedFile != null) {
			usageFile.setText(selectedFile.getAbsolutePath());
		}

	}

	/**
	 * Binds height and width
	 * 
	 * @param doubleBinding
	 * @param widthProperty
	 */
	public void setSizeProperties(final DoubleBinding doubleBinding, 
			final ReadOnlyDoubleProperty widthProperty) {
		this.heightProperty = doubleBinding;
		this.widthProperty = widthProperty;
	}

	/**
	 * Setting the menuPane
	 * 
	 * @param menuPane
	 */
	public void setMenuPane(final GridPane menuPane) {
		this.menuPane = menuPane;
	}

	/**
	 * Setting menu pane and parseOverViewLoader
	 * 
	 * @param menuPane
	 * @param fxmlLoaderPage
	 */
	public void setMenuPaneAndParseOverviewLaoder(final GridPane menuPane, 
			final FXMLLoader fxmlLoaderPage) {
		this.menuPane = menuPane;
		this.fxmlLoaderPage = fxmlLoaderPage;
	}
}
