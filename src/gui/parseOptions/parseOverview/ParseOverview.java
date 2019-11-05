package gui.parseOptions.parseOverview;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ParseOverview extends Application {
	
	
	@Override
    public void start(Stage primaryStage) {
        try {
            // Read file fxml and draw interface.
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/gui/parseOptions/parseOverview/fxml/parseOverview.fxml"));
 
            Scene primScene = new Scene(root);
            
            primaryStage.setTitle("Modul Maker");
            primaryStage.setScene(primScene);
            primaryStage.show();
            
         
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
