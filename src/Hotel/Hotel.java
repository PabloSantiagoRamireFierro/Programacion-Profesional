package Hotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Pablo Ramirez
 */
public class Hotel extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        Scene scene = new Scene(root);
        stage.show();
        
        stage.setResizable(false);
        stage.setTitle("Login");
        
        stage.setScene(scene);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
