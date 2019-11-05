package gui.menu;

import gui.menu.controls.MenuControl;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Starts the ModuleMaker. 
 * @author Amrei Menzel
 *
 */
public class Main extends Application {
	
	private static Main APP;
	
	@Override
    public void start(final Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/menu/fxml/menu.fxml"));
            final Parent root = (Parent) fxmlLoader.load();
            ((MenuControl) fxmlLoader.getController()).setWindowSize(primaryStage);
           
            
            primaryStage.setTitle("Module Maker");
            primaryStage.setMaximized(true);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            
            primaryStage.show();
         
        } catch(Exception e) {
            e.printStackTrace();
        }
        Main.APP = this;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public static HostServices getHostSer() {
    	return Main.APP.getHostServices(); 
    }
}
