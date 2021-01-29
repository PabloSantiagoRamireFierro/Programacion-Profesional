/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hotel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Pablo Ramirez
 */
public class MenuAdminController implements Initializable {

    @FXML
    private Button btnControl_Habitación;
    @FXML
    private Button btnVolver;
    @FXML
    private Font x1;
    @FXML
    private Color x2;
    @FXML
    private Button btnControl_Servicio;
    @FXML
    private Button btnControl_Usuario;
    @FXML
    private Font x3;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnControl_Habitación_click(ActionEvent event) throws IOException {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Habitaciones.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Habitaciones");
           
            
            //al cerrar la ventana de Libros
            stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent e){
                    Platform.exit();
                    System.exit(0);
                }
            });

            stage.setScene(scene);
            stage.show();
            
            //ocultar la ventana de Login
            ((Node)(event.getSource())).getScene().getWindow().hide(); 
    }

    @FXML
    private void btnVolver_click(ActionEvent event) throws IOException {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("MenuInicio.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Menu");
           
            
            //al cerrar la ventana de Libros
            stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent e){
                    Platform.exit();
                    System.exit(0);
                }
            });

            stage.setScene(scene);
            stage.show();
            
            //ocultar la ventana de Login
            ((Node)(event.getSource())).getScene().getWindow().hide(); 
    }

    @FXML
    private void btnControl_Servicio_click(ActionEvent event) throws IOException {
        Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Servicios.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Servicios");
           
            
            //al cerrar la ventana de Libros
            stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent e){
                    Platform.exit();
                    System.exit(0);
                }
            });

            stage.setScene(scene);
            stage.show();
            
            //ocultar la ventana de Login
            ((Node)(event.getSource())).getScene().getWindow().hide(); 
    }
    

    @FXML
    private void btnControl_Usuario_click(ActionEvent event) throws IOException {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Usuario.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Usuarios");
           
            
            //al cerrar la ventana de Libros
            stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent e){
                    Platform.exit();
                    System.exit(0);
                }
            });

            stage.setScene(scene);
            stage.show();
            
            //ocultar la ventana de Login
            ((Node)(event.getSource())).getScene().getWindow().hide(); 
    }
}


