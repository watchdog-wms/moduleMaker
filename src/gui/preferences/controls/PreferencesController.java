package gui.preferences.controls;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import gui.parseOptions.addOption.controls.AddOptionController;
import gui.parseOptions.parseOverview.Option;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.nio.file.Path;

public class PreferencesController implements Initializable {
	
	private ObservableList<String> data = FXCollections.observableArrayList();

	@FXML
	Button cancelButton;
	@FXML ListView regexFileList;
	@FXML TextField newRegexFilePath;
	@FXML ToggleGroup activateDebug;

	@FXML
	protected void handleCancelAction(ActionEvent event) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
	    stage.close();
	}
	
	@FXML
	protected void handleSaveAction(ActionEvent event) {
		checkDebug();
		FileWriter fw;
		try {
			fw = new FileWriter("RegexFiles/RegexFiles.txt");

			BufferedWriter bw = new BufferedWriter(fw);
			
			for(String dat : data) {
				bw.write(dat);
				bw.newLine();
			}
			
			bw.close();
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Stage stage = (Stage) cancelButton.getScene().getWindow();
	    stage.close();
	}

	private void checkDebug() {
		if(activateDebug.getSelectedToggle().getUserData().equals("yes")) {
			logic.util.Debugger.enable = true;
		}
		else {
			logic.util.Debugger.enable = false;
		}
	}

	@FXML public void handleRemoveSelectedRegexFilesAction(ActionEvent event) {
		List<Integer> selectedCells = regexFileList.getSelectionModel().getSelectedIndices();
		for (int i : selectedCells) {
			regexFileList.getItems().remove(i);
		}
		
	}

	@FXML public void handleBrowseRegexFileAction(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open File");
		File selectedFile = chooser.showOpenDialog(null);
		if (selectedFile != null) {
			newRegexFilePath.setText(selectedFile.getAbsolutePath());
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		FileReader fr;
		try {
			fr = new FileReader("RegexFiles/RegexFiles.txt");

			BufferedReader br = new BufferedReader(fr);
			
			String line;
			while((line = br.readLine()) != null) {
				data.add(line);
			}
			
			br.close();
			fr.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		regexFileList.setItems(data);

		regexFileList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

	}

	@FXML public void handleAddAction(ActionEvent event) {
		if(new File(newRegexFilePath.getText()).exists()) {
			//+1 because of / 
			data.add(newRegexFilePath.getText().substring(System.getProperty("user.dir").length()+1));
			regexFileList.refresh();
		}
	}

	@FXML public void handleViewSelectedRegexFileAction(ActionEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/gui/preferences/fxml/ViewRegexFile.fxml"));

		Parent root1;
		try {
			root1 = (Parent) fxmlLoader.load();

			ViewRegexFileController aoc = (ViewRegexFileController) (fxmlLoader.getController());
			aoc.setText((String)regexFileList.getSelectionModel().getSelectedItem());


			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("View regex file");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
