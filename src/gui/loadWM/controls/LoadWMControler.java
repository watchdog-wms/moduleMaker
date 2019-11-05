package gui.loadWM.controls;

import java.io.File;
import gui.parseOptions.parseOverview.controls.ParseOverviewController;
import java.io.IOException;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;

/**
 * Class which controls the FXML loadWm.fxml. There you can load an old wm.file
 * and parse it to the parseOptionsOverview.
 * 
 * @author Amrei Menzel
 *
 */
public class LoadWMControler {

	@FXML
	private TextField wmPath;

	@FXML
	private HBox newModuleName;

	@FXML
	private Text newModuleNameText;

	@FXML
	private Button cancelButton;

	private ParseOverviewController parseOverviewController;

	@FXML
	private TextField moduleName;

	@FXML
	private TextField outputFolder;

	@FXML
	private ToggleGroup keepFile;

	@FXML
	private TextField moduleCall;

	private boolean renameModule = false;
	
	private GridPane menuPane;
	private FXMLLoader fxmlLoaderPage;

	private DoubleBinding heightProperty;

	private ReadOnlyDoubleProperty widthProperty;

	public void setOverviewController(final ParseOverviewController poc) {
		this.parseOverviewController = poc;
	}

	/**
	 * Opens a window, where you can browse after a wm file.
	 * 
	 * @param event
	 */
	@FXML
	protected void locateFile(final ActionEvent event) {
		final FileChooser chooser = new FileChooser();
		chooser.setTitle("Open File");
		// allow only wm files
		final FileChooser.ExtensionFilter extFilter = 
				new FileChooser.ExtensionFilter("watchdog module files (*.wm)", "*.wm");
		chooser.getExtensionFilters().add(extFilter);
		final File selectedFile = chooser.showOpenDialog(null);
		if (selectedFile != null) {
			// set path to textfield
			wmPath.setText(selectedFile.getAbsolutePath());
		}
	}

	/**
	 * Opens a window, where you can chose an output folder.
	 * 
	 * @param event
	 */
	@FXML
	protected void handleLocateFolderAction(final ActionEvent event) {
		final DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Open File");
		final File selectedFolder = chooser.showDialog(null);
		if (selectedFolder != null) {
			outputFolder.setText(selectedFolder.getAbsolutePath());
		}
	}

	/**
	 * After chosing that you want a new module name you can set it.
	 * 
	 * @param event
	 */
	@FXML
	protected void enableNewModuleName(final ActionEvent event) {
		newModuleName.setDisable(false);
		newModuleNameText.setFill(Color.BLACK);
		renameModule = true;
	}

	/**
	 * After chosing that you don't want a new module name you can't set it. The
	 * textfield for the modulename will be disabled.
	 * 
	 * @param event
	 */
	@FXML
	protected void disableNewModuleName(final ActionEvent event) {
		newModuleName.setDisable(true);
		newModuleNameText.setFill(Paint.valueOf("#00000087"));
		renameModule = false;
	}

	/**
	 * You can close this dialog via a cancelbutton. The stage will be closed.
	 * 
	 * @param event
	 */
	@FXML
	protected void handleCloseWindowAction(final ActionEvent event) {
		final Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * After setting a wm file the file will be loaded in the parseOverview.
	 */
	private void setTableValuesAndUpdate() {
		parseOverviewController.setWMPath(wmPath.getText());
		if (renameModule) {
			parseOverviewController.setModuleName(moduleName.getText());
			// if the module shouldn't be renamed the old name must be get from the path to
			// the wm file
		} else {
			if (wmPath.getText().contains("/") && wmPath.getText().contains(".")) {
				parseOverviewController.setModuleName(wmPath.getText().
						substring(wmPath.getText().lastIndexOf("/") + 1,
						wmPath.getText().lastIndexOf(".")));
			}
		}
		parseOverviewController.setOutputFolder(outputFolder.getText());
		parseOverviewController.setMaxPages(1);

		parseOverviewController.setModuleCall(moduleCall.getText());

		parseOverviewController.setKeepFile(keepFile.getSelectedToggle().
				getUserData().toString().equals("yes"));
		// set the size of the parseOverviewController
		parseOverviewController.initializeDataWithoutOpts(heightProperty, widthProperty);
		parseOverviewController.updateTableView();

	}

	/**
	 * After filling out the required fields, the window will be closed and the data
	 * will be loarded in the parseOverview.
	 * 
	 * @param event
	 */
	@FXML
	protected void handleContinueAction(final ActionEvent event) {
		menuPane.getChildren().clear();
		try {
			menuPane.getChildren().add(fxmlLoaderPage.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		parseOverviewController = (ParseOverviewController) (fxmlLoaderPage.getController());
		parseOverviewController.setMenuPane(menuPane);
		final Stage stage = (Stage) cancelButton.getScene().getWindow();
		setTableValuesAndUpdate();

		stage.close();

	}


	public void setMenuPaneAndNextPage(final GridPane menuPane,final FXMLLoader fxmlLoaderPage) {
		this.menuPane = menuPane;
		this.fxmlLoaderPage = fxmlLoaderPage;
	}

	public void setSizeProperties(final DoubleBinding doubleBinding, final ReadOnlyDoubleProperty widthProperty) {
		this.heightProperty = doubleBinding;
		this.widthProperty = widthProperty;
	}

}
