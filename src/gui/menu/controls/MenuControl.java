package gui.menu.controls;

import gui.loadWM.controls.LoadWMControler;
import gui.newModule.controls.NewModuleController;
import gui.start.controls.StartController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class which controls the FXML menu.fxml. All pages are open in the menu. The
 * menupage has a menupane.
 * 
 * @author Amrei Menzel
 *
 */
public class MenuControl implements Initializable {

	/**
	 * The menuPane with the menus File and Help.
	 */
	@FXML
	private GridPane menuPane;

	/**
	 * Page in which the pages are loaded
	 */
	@FXML private AnchorPane primaryPane;

	/**
	 * Open the Page LoadWM to load a WM file.
	 * @param event
	 */
	@FXML
	protected void openLoadWM(final ActionEvent event) {
		final FXMLLoader fxmlLoaderPage = new FXMLLoader(
				getClass().getResource("/gui/parseOptions/parseOverview/fxml/parseOverview.fxml"));
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/loadWM/fxml/loadWm.fxml"));
		
		Parent root1;
		try {
			root1 = (Parent) fxmlLoader.load();

			final LoadWMControler wmc = (LoadWMControler) (fxmlLoader.getController());
			wmc.setMenuPaneAndNextPage(menuPane, fxmlLoaderPage);
			wmc.setSizeProperties(primaryPane.heightProperty().subtract(70), menuPane.widthProperty());

			final Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Load wm file");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the Page NewModule to create an new module.
	 * @param event
	 */
	@FXML
	protected void openNewModule(final ActionEvent event) {
		final FXMLLoader fxmlLoaderPage = new FXMLLoader(
				getClass().getResource("/gui/parseOptions/parseOverview/fxml/parseOverview.fxml"));

		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/newModule/fxml/newModule.fxml"));
		Parent root1;
		try {
			root1 = (Parent) fxmlLoader.load();
			
			final NewModuleController nmc = (NewModuleController) (fxmlLoader.getController());
			nmc.setSizeProperties(primaryPane.heightProperty().subtract(70), menuPane.widthProperty());
			nmc.setMenuPaneAndParseOverviewLaoder(menuPane, fxmlLoaderPage);
			

			final Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("New module");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the Preferences.
	 * @param event
	 */
	@FXML
	protected void openPreferences(final ActionEvent event) {

		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/preferences/fxml/Preferences.fxml"));
		Parent root1;
		try {
			root1 = (Parent) fxmlLoader.load();

			final Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Preferences");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the Page newRegex to create a new regex file.
	 * @param event
	 */
	@FXML
	protected void newRegexFile(final ActionEvent event) {
		final FXMLLoader fxmlLoaderPage = new FXMLLoader(getClass().getResource("/gui/newRegex/fxml/newRegex.fxml"));

		menuPane.getChildren().clear();
		try {
			menuPane.getChildren().add(fxmlLoaderPage.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the programm.
	 * @param event
	 */
	@FXML
	protected void handleExitAction(final ActionEvent event) {
		Platform.exit();
		System.exit(0);
	}

	/**
	 * Setting size of startpage.
	 */
	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		final FXMLLoader fxmlLoaderPage = new FXMLLoader(
				getClass().getResource("/gui/start/fxml/Start.fxml"));


		try {
			menuPane.getChildren().clear();
			menuPane.getChildren().add(fxmlLoaderPage.load());
			final StartController startController = (StartController) (fxmlLoaderPage.getController());
			startController.setSizeProperties(primaryPane.heightProperty().subtract(70), menuPane.widthProperty());

		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	}
	

	/**
	 * Binds height und Width property from the given primaryStage.
	 * @param primaryStage
	 */
	public void setWindowSize(final Stage primaryStage) {
		menuPane.minHeightProperty().bind(primaryStage.heightProperty());
        menuPane.minWidthProperty().bind(primaryStage.widthProperty());
	}

}
