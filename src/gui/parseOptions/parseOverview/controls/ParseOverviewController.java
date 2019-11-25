package gui.parseOptions.parseOverview.controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import gui.loadWM.controls.LoadWMControler;
import gui.newModule.controls.NewModuleController;
import gui.parseOptions.addOption.controls.AddOptionController;
import gui.parseOptions.parseExtraOptions.controls.ParseExtraOptionsController;
import gui.parseOptions.parseOverview.Option;
import gui.parseOptions.saveChanges.controls.SaveChangesController;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.usageParser.UsageOption;
import logic.usageParser.WriteUsage;
import logic.writeModule.WriteModule;

public class ParseOverviewController implements Initializable {

	@FXML
	public TableView<Option> tableView;

	@FXML
	private TableColumn<?, ?> checkboxColumn;

	@FXML
	private TableColumn<?, ?> comboboxColumn;

	@FXML
	private Text pageCounterText;

	@FXML
	private Button forwardButton;

	@FXML
	private Button backButton;

	private int pageCounter = 1;

	private int maxPages = 5;

	@FXML
	private GridPane rootPane;

	private String moduleName = "";

	private String outputFolder = ".";

	private boolean keepFile = true;

	private String moduleCall = "";

	private boolean changedData = false;

	private String path ="";
	
	@FXML
	private TableColumn indexColumn;
	
	public void setModuleName(String name) {
		this.moduleName = name; // TODO: was wenn leer
	}

	public void setOutputFolder(String path) {
		if (!path.isEmpty())
			this.outputFolder = path;
	}

	public void setKeepFile(boolean keep) {
		this.keepFile = keep;
	}

	public void setModuleCall(String mCall) {
		this.moduleCall = mCall;
	}

	public String getCall(String callType) {
		if (callType.equals("short")) {
			return shortC;
		} else if (callType.equals("long")) {
			return longC;
		} else
			return "";
	}

	public void updateTableView() {

		String wmPath="";
		
		changedData = false;

		this.tableView.getItems().clear();
		
		if(opts != null && !opts.isEmpty()) {
			for(String path : opts.keySet()) {
				wmPath = path;
				break;
			}
		}
		else if(!path.isEmpty() ) {
			wmPath = path;
		}

		if (!wmPath.isEmpty()) {

			wm = new WriteModule();
			wm.readVariables(wmPath);
			List<UsageOption> allVar = wm.getAllVariables();

			
			for (UsageOption uo : allVar) {
				this.tableView.getItems().add(new Option(this.tableView.getItems().size()+"", uo.getName(), uo.getShortO(), uo.getLongO(),
						uo.getType(), uo.getMinO(), uo.getMaxO(),
						uo.getDefaultV(), uo.getRestriction(), uo.getDescription(),
						uo.getCall(), uo.getCallType()));
			
			}
			}
		tableView.refresh();

	}

	private String shortC = "-";
	private String longC = "-";
	private String shortCb = "-";
	private String longCb = "-";
	
	private DoubleBinding heightProperty;
	private ReadOnlyDoubleProperty widthProperty;

	public void initializeData(DoubleBinding heightProperty, ReadOnlyDoubleProperty widthProperty) {
		int counter =0;
		changedData = false;

		this.heightProperty = heightProperty;
		this.widthProperty = widthProperty;
		
		rootPane.prefHeightProperty().bind(heightProperty);
        rootPane.prefWidthProperty().bind(widthProperty);
		tableView.prefHeightProperty().bind(heightProperty);
        tableView.prefWidthProperty().bind(widthProperty);
		
		ObservableList<Option> dataX = FXCollections.observableArrayList();

		for (String path : opts.keySet()) {
			if(counter == maxPages) {
				return;
			}
			counter++;

			dataX = FXCollections.observableArrayList();
			if (!path.isEmpty()) {

				wm = new WriteModule();
				wm.readVariables(path);
				List<UsageOption> allVar = wm.getAllVariables();

				for(UsageOption uo : allVar) {
					if(uo.getType().equals("boolean")) {
						if (shortCb.equals("-") && uo.getCallType().equals("short")) {
							shortCb = uo.getCall();
						} else if (longC.equals("-") && uo.getCallType().equals("long")) {
							longCb = uo.getCall();
						}
					}
					else {
						if (shortC.equals("-") && uo.getCallType().equals("short")) {
							shortC = uo.getCall();
						} else if (longC.equals("-") && uo.getCallType().equals("long")) {
							longC = uo.getCall();
						}
					}
				dataX.add(new Option(dataX.size()+"", uo.getName(), uo.getShortO(), uo.getLongO(),
							uo.getType(), uo.getMinO(), uo.getMaxO(),
							uo.getDefaultV(), uo.getRestriction(), uo.getDescription(),
							uo.getCall(), uo.getCallType()));
				}
			}
			datas.add(dataX);
		}
		pageCounterText.setText(pageCounter + " of " + maxPages);
		if (maxPages == 1) {
			forwardButton.setDisable(true);
		}
        

	}
	
	public void initializeDataWithoutOpts(DoubleBinding heightProperty, ReadOnlyDoubleProperty widthProperty) {
		this.heightProperty = heightProperty;
		this.widthProperty = widthProperty;
		
		rootPane.prefHeightProperty().bind(heightProperty);
        rootPane.prefWidthProperty().bind(widthProperty);
		tableView.prefHeightProperty().bind(heightProperty);
        tableView.prefWidthProperty().bind(widthProperty);
        
        pageCounterText.setText(pageCounter + " of " + maxPages);
		if (maxPages == 1) {
			forwardButton.setDisable(true);
		}
		
		tableView.getItems().clear();
		tableView.refresh();
	}

	public List<ObservableList<Option>> getData() {
		return datas;
	}

	public ObservableList<Option> getActData() {
		return this.tableView.getItems();
	}
	
	public void setData(List<ObservableList<Option>> datas) {
		this.datas=datas;
	}

	public void setActData(List<Option> data2 ) {
		this.tableView.setItems((ObservableList<Option>) data2);
	}

	// Weitere spalte: callType short oder long..
	@FXML
	protected void handleNextPageAction(ActionEvent event) {
		if (changedData || this.hasChangedOptions()) {
			if (saveChanges()) {
				datas.get(pageCounter - 1).clear();
				datas.get(pageCounter - 1).addAll(FXCollections.observableArrayList(this.tableView.getItems()));
			}
		}
		
		/*rootPane.getChildren().removeAll(rootPane.getChildren());*/
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/gui/parseOptions/parseExtraOptions/fxml/parseExtraOptionsNew.fxml"));
		//rootPane.getChildren().add(fxmlLoader.load());
		try {
			menuPane.getChildren().clear();
			menuPane.getChildren().add(fxmlLoader.load());
			// page = fxmlLoaderPage.load();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ParseExtraOptionsController peoc = (ParseExtraOptionsController) (fxmlLoader.getController());
		if(longC.equals("-") && shortC.equals("-") && longCb.equals("-") && shortCb.equals("-")) {
			searchAfterCalls();
		}
		peoc.initializeRequiredData(this.moduleName, this.outputFolder, this.moduleCall, longC, shortC, datas,
				this.tableView.getItems(), keepFile, heightProperty, widthProperty, menuPane, longCb, shortCb);
		
	}

	private void searchAfterCalls() {
		for(Option opt : this.tableView.getItems()) {
			if(!opt.getCall().equals("-")) {
				if(opt.getType().equals("boolean")){
					if(opt.getCallType().equals("short")) {
						shortCb = opt.getCall();
					}
					else if(opt.getCallType().equals("long")) {
						longCb = opt.getCall();
					}
				}
				else {
					if(opt.getCallType().equals("short")) {
						shortC = opt.getCall();
					}
					else if(opt.getCallType().equals("long")) {
						longC = opt.getCall();
					}
				}
			}
		}
	}

	@FXML
	protected void handleDeleteAction(ActionEvent event) {
		ObservableList<Option> selectedCells = tableView.getSelectionModel().getSelectedItems();
		int index = -1;
		
		for(Option opt: selectedCells) {
			index = Integer.parseInt(opt.getIndex());
			for(Option o : tableView.getItems()) {
				int otherIndex = Integer.parseInt((String) indexColumn.getCellData(o));
				if(otherIndex > index) {
					otherIndex--;
					o.setIndex(otherIndex+"");
				}
				
			}
		}
		tableView.getItems().removeAll(selectedCells);
		tableView.getSelectionModel().clearSelection();
		tableView.refresh();
		changedData = true;
	}

	@FXML
	protected void handleNewOptionAction(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/gui/parseOptions/addOption/fxml/AddOption.fxml"));

		Parent root1;
		try {
			root1 = (Parent) fxmlLoader.load();

			AddOptionController aoc = (AddOptionController) (fxmlLoader.getController());
			aoc.setOverviewController(this);

			// LoadWMControler wmc = (LoadWMControler) (fxmlLoader.getController());
			// wmc.setOverviewController((ParseOverviewController)
			// (fxmlLoaderPage.getController()));
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("New Option");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ObservableList<Option> dataX = tableView.getItems();
		// datas.set(pageCounter-1, dataX);
		changedData = true;
	}

	@FXML
	public void handleClickForwardButtonAction(ActionEvent event) {
		if (changedData || this.hasChangedOptions()) {
			if (event != null && saveChanges()) {
				this.resetChangedOptions();
				datas.get(pageCounter - 1).clear();
				datas.get(pageCounter - 1).addAll(FXCollections.observableArrayList(this.tableView.getItems()));
			}
		}
		changedData = false;
		if (pageCounter < maxPages) {
			pageCounter++;
			pageCounterText.setText(pageCounter + " of " + maxPages);
			if (pageCounter >= 1) {
				backButton.setDisable(false);
			}
		}
		if (pageCounter == maxPages) {
			forwardButton.setDisable(true);
		}
		pageCounterText.setText(pageCounter + " of " + maxPages);
		
		tableView.getItems().clear();
		tableView.getItems().addAll(FXCollections.observableArrayList(datas.get(pageCounter - 1)));
		tableView.refresh();
		this.resetChangedOptions();

	}

	/**
	 * resets all options after save (e.g. no modifications anymore)
	 */
	private void resetChangedOptions() {
		for(Option o : tableView.getItems()) {
			o.resetIsMod();
		}
	}

	/**
	 * tests if there are any modifications on options
	 * @return
	 */
	private boolean hasChangedOptions() {
		for(Option o : tableView.getItems()) {
			if(o.isModified())
				return true;
		}
		return false;
	}

	@FXML
	public void handleClickBackButtonAction(ActionEvent event) {
		if (changedData || this.hasChangedOptions()) {
			if (event != null && saveChanges()) {
				this.resetChangedOptions();
				datas.get(pageCounter - 1).clear();
				datas.get(pageCounter - 1).addAll(FXCollections.observableArrayList(tableView.getItems()));
			}
		}
		changedData = false;
		if (pageCounter > 1) {
			pageCounter--;
			pageCounterText.setText(pageCounter + " of " + maxPages);
			if (pageCounter <= maxPages) {
				forwardButton.setDisable(false);
			}
		}
		if (pageCounter == 1) {
			backButton.setDisable(true);
		}
		tableView.getItems().clear();
		tableView.getItems().addAll(FXCollections.observableArrayList(datas.get(pageCounter - 1)));
		tableView.refresh();
		this.resetChangedOptions();
	}

	private WriteModule wm; // TODO: umbennen

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		tableView.getItems().clear();
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	private Option getOption(String name) {
		for (Option opt : tableView.getItems()) {
			if (opt.getName().equals(name)) {
				return opt;
			}
		}
		return null;
	}

	public void addOption(Option option) {
		tableView.getItems().add(option);
		tableView.refresh();
	}

	private Map<String, Integer> opts;

	private List<ObservableList<Option>> datas = new ArrayList<ObservableList<Option>>();

	private boolean save = false;

	private GridPane menuPane;

	public void setSave(boolean s) {
		this.save = s;
	}

	private boolean saveChanges() {
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/gui/parseOptions/saveChanges/fxml/SaveChanges.fxml"));

		Parent root1;
		try {
			root1 = (Parent) fxmlLoader.load();

			SaveChangesController scc = (SaveChangesController) (fxmlLoader.getController());
			scc.setOverviewController(this);

			// LoadWMControler wmc = (LoadWMControler) (fxmlLoader.getController());
			// wmc.setOverviewController((ParseOverviewController)
			// (fxmlLoaderPage.getController()));
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Save?");
			stage.setScene(new Scene(root1));
			stage.showAndWait();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return save;
	}

	public void setOpts(Map<String, Integer> opts) {
		datas = new ArrayList<ObservableList<Option>>();
		this.opts = opts;

	}

	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
		pageCounterText.setText(pageCounter + " of " + maxPages);
		if (maxPages == 1) {
			forwardButton.setDisable(true);
		}
	}

	public void setWMPath(String path) {
		this.path = path;
	}

	public String getDataSize() {
		// TODO Auto-generated method stub
		return tableView.getItems().size()+"";
	}
	
	@FXML
	protected void handleOpenHelpAction(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/gui/parseOptions/overviewHelp/fxml/OverviewHelp.fxml"));

		Parent root1;
		try {
			root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Help");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setMenuPane(GridPane menuPane) {
		this.menuPane=menuPane;
		
	}

}
