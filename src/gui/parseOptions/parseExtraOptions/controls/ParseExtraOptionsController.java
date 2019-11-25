package gui.parseOptions.parseExtraOptions.controls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gui.parseOptions.parseExtraOptions.ReturnTypeElement;
import gui.parseOptions.parseOverview.Option;
import gui.parseOptions.parseOverview.controls.ParseOverviewController;
import gui.parseOptions.successful.controls.SuccessfulController;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logic.usageParser.WriteUsage;
import logic.writeModule.WriteModule;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

/**
 * Controller for extra properties to be set.
 * 
 * @author Amrei Menzel
 *
 */
public class ParseExtraOptionsController {

	@FXML
	private GridPane root;

	private String outputFolder;

	/**
	 * call of long option.
	 */
	private String longO;

	/**
	 * call of short option.
	 */
	private String shortO;

	/**
	 * call of long option for booleans
	 */
	private String longOb;

	/**
	 * call of short option of booleans
	 */
	private String shortOb;

	/**
	 * name of module
	 */
	private String moduleName;

	/**
	 * program call
	 */
	private String call;

	@FXML
	private TextField textShort;

	@FXML
	private TextField textLong;

	@FXML
	private TextField textShortB;

	@FXML
	private TextField textLongB;
	private List<ObservableList<Option>> datas;

	private List<Option> data;

	private boolean keepFile = true;

	@FXML
	private VBox fileAndFolders;

	@FXML
	protected VBox sepParameters;

	private ObservableList<ReturnTypeElement> returnTypeElementsData = FXCollections.observableArrayList();

	private List<String> folders = new ArrayList<>();
	private List<String> files = new ArrayList<>();

	@FXML
	private Text textFilesAndFolders;

	@FXML
	private ComboBox<?> typeReturnTypeElement;

	@FXML
	private TextField nameReturnTypeElement;

	@FXML
	private TextField parameterName;

	@FXML
	private TableView<ReturnTypeElement> returnTypeElementTable;

	private Map<String, String> sepParas = new HashMap<>();

	private String defaultParaSep = "default (comma)";

	@FXML
	private VBox returnBox;

	@FXML
	private VBox folderBox;

	private DoubleBinding heightProperty;
	private ReadOnlyDoubleProperty widthProperty;
	private GridPane menuPane;
	private boolean keep;

	public Scene getScene() {
		return root.getScene();
	}

	/**
	 * initialize the required data given from the parseoverviewcontroller.
	 * 
	 * @param moduleName
	 * @param outputFolder
	 * @param call
	 * @param longO
	 * @param shortO
	 * @param datas
	 * @param data
	 * @param keep
	 * @param heightProperty
	 * @param widthProperty
	 * @param menuPane
	 */
	public void initializeRequiredData(String moduleName, String outputFolder, String call, String longO, String shortO,
			List<ObservableList<Option>> datas, List<Option> data, boolean keep, DoubleBinding heightProperty,
			ReadOnlyDoubleProperty widthProperty, GridPane menuPane, String longOb, String shortOb) {
		this.heightProperty = heightProperty;
		this.widthProperty = widthProperty;
		root.prefHeightProperty().bind(heightProperty);
		root.prefWidthProperty().bind(widthProperty);
		this.moduleName = moduleName;
		this.outputFolder = outputFolder;
		this.call = call;
		this.longO = longO;
		this.shortO = shortO;
		this.longOb = longOb;
		this.shortOb = shortOb;
		this.datas = datas;
		this.data = data;
		// setting the calls in readable strings
		textShort.setText((shortO.replaceFirst("\\(\\.\\*\\)", "option")).replaceFirst("\\(\\.\\*\\)", "parameter"));
		textLong.setText(longO.replaceFirst("\\(\\.\\*\\)", "option").replaceFirst("\\(\\.\\*\\)", "parameter"));
		textShortB.setText(shortOb.replaceFirst("\\(\\.\\*\\)", "option"));
		textLongB.setText(longOb.replaceFirst("\\(\\.\\*\\)", "option"));
		getAllFileAndFolders();
		findParameter2Plus();
		this.keepFile = keep;
		returnTypeElementTable.getItems().setAll(returnTypeElementsData);
		this.menuPane = menuPane;
		this.keep = keep;
	}

	// TODO Type als Enum
	/**
	 * Filters files and folders from all options. It's needed to check if they
	 * exist.
	 */
	private void getAllFileAndFolders() {
		for (Option dat : data) {
			if (dat.getType().equals("AbsoluteFilePath")) {
				fileAndFolders.getChildren().add(new CheckBox(dat.getName()));
				files.add(dat.getName());
			} else if (dat.getType().equals("AbsoluteFolderPath")) {
				fileAndFolders.getChildren().add(new CheckBox(dat.getName()));
				folders.add(dat.getName());
			}
		}
		if (files.size() < 1 && folders.size() < 1) {
			this.textFilesAndFolders.setText("No extra existenceCheck needed.");
		}
	}

	/**
	 * find all parameter with minOccurence 2+ for setting separation if needed.
	 */
	private void findParameter2Plus() {
		for (Option dat : data) {
			if (dat.getMax().equals("unbounded") || Integer.parseInt(dat.getMax()) >= 2) {
				HBox hbox = new HBox();
				hbox.getChildren().add(new Text(dat.getName() + ":"));
				hbox.getChildren().add(new TextField("default (comma)"));
				sepParameters.getChildren().add(hbox);
				sepParas.put(dat.getName(), ",");
			}
		}
	}

	/**
	 * Retrun back to parseOverviewcontroller.
	 * @param event
	 */
	@FXML
	protected void handleBackAction(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/gui/parseOptions/parseOverview/fxml/parseOverview.fxml"));
			menuPane.getChildren().clear();
			menuPane.getChildren().add(fxmlLoader.load());
			ParseOverviewController poc = (ParseOverviewController) (fxmlLoader.getController());
			poc.setData(datas);
			poc.setActData(data);
			poc.setMaxPages(datas.size());
			poc.setModuleCall(call);
			poc.setModuleName(moduleName);
			poc.setOutputFolder(outputFolder);
			poc.initializeDataWithoutOpts(heightProperty, widthProperty);
			poc.handleClickBackButtonAction(null);
			poc.setKeepFile(keep);
			poc.setMenuPane(menuPane);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Checks if call has changed.
	 */
	private void checkIfCallsChanges() {
		String ts = textShort.getText().replaceFirst("option", "\\(\\.\\*\\)").replaceFirst("parameter",
				"\\(\\.\\*\\)");
		String tl = textLong.getText().replaceFirst("option", "\\(\\.\\*\\)").replaceFirst("parameter", "\\(\\.\\*\\)");
		String tsb = textShortB.getText().replaceFirst("option", "\\(\\.\\*\\)");
		String tlb = textLongB.getText().replaceFirst("option", "\\(\\.\\*\\)");
		if (shortO.equals(ts) && longO.equals(tl) && shortOb.equals(tsb) && longOb.equals(tlb)) {
			return;
		} else {
			for (Option opt : data) {
				// if(!opt.getType().equals("boolean")) {
				if (opt.getCallType().equals("short")) {
					if (opt.getType().equals("boolean")) {
						opt.setCall(tsb);
					} else
						opt.setCall(ts);
				} else if (opt.getCallType().equals("long")) {
					if (opt.getType().equals("boolean"))
						opt.setCall(tlb);
					else
						opt.setCall(tl);

				} else {
					opt.setCall("");
				}
			}
			// }
		}
	}

	/**
	 * Check if folders or files exist.
	 */
	private void checkIfExistenceNeeded() {
		for (Node node : fileAndFolders.getChildren()) {
			CheckBox cb = (CheckBox) node;
			if (!cb.isSelected()) {
				String name = cb.getText();
				if (files.contains(name)) {
					files.remove(name);
				} else if (folders.contains(name)) {
					folders.remove(name);
				}
			}
		}
	}

	/**
	 * check the separation of parameters 2+
	 */
	private void checkParameter2Plus() {
		String lastNode = "";
		for (Node node : sepParameters.getChildren()) {
			HBox hb = (HBox) node;
			for (Node n2 : hb.getChildren()) {
				if (n2 instanceof Text) {
					lastNode = ((Text) n2).getText();
				}
				if (n2 instanceof TextField) {
					TextField tf = (TextField) n2;
					if (tf.getText().equals(defaultParaSep)) {
						if (sepParas.containsKey(lastNode.substring(0, lastNode.length() - 1))) {
							sepParas.remove(lastNode.substring(0, lastNode.length() - 1));
						}
					} else {
						if (sepParas.containsKey(lastNode.substring(0, lastNode.length() - 1))) {
							sepParas.replace(lastNode.substring(0, lastNode.length() - 1), tf.getText());
						}
					}
				}
			}
		}
	}

	@FXML
	protected void handleContinueAction(ActionEvent event) {
		checkIfCallsChanges();
		checkIfExistenceNeeded();
		checkParameter2Plus();
		WriteUsage wu = new WriteUsage();
		wu.writeProcessedDataInFile(this.outputFolder + "/" + this.moduleName + ".wm", data, keepFile);
		WriteModule wm = new WriteModule(moduleName);
		if (returnTypeElementsData.size() > 0)
			wm.setReturnTypeElements(returnTypeElementsData);
		wm.writeInTemplates(this.outputFolder + "/" + this.moduleName + ".wm",
				this.outputFolder + "/" + this.moduleName + ".sh", this.outputFolder + "/" + this.moduleName + ".xsd",
				call, files, folders, sepParas);

		try {
			root.getChildren().removeAll(root.getChildren());
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/gui/parseOptions/successful/fxml/Successful.fxml"));
			menuPane.getChildren().clear();
			menuPane.getChildren().add(fxmlLoader.load());

			SuccessfulController sc = (SuccessfulController) (fxmlLoader.getController());
			sc.setSize(heightProperty, widthProperty);
			sc.setPaths(outputFolder, this.outputFolder + "/" + this.moduleName + ".sh",
					this.outputFolder + "/" + this.moduleName + ".xsd");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void handleAddNewReturnTypeAction(ActionEvent event) {
		ReturnTypeElement newReturnType = new ReturnTypeElement(nameReturnTypeElement.getText(),
				parameterName.getText());

		returnTypeElementsData.add(newReturnType);
		returnTypeElementTable.getItems().add(newReturnType);
		returnTypeElementTable.refresh();
		nameReturnTypeElement.setText("");
		parameterName.setText("");
	}

	@FXML
	public void handleRemoveSelectedReturnTypeElementAction(ActionEvent event) {
		ReturnTypeElement selectedCell = returnTypeElementTable.getSelectionModel().getSelectedItem();
		returnTypeElementTable.getItems().removeAll(returnTypeElementsData);
		returnTypeElementsData.remove(selectedCell);
		returnTypeElementTable.getItems().addAll(returnTypeElementsData);
		returnTypeElementTable.refresh();
	}
}
