/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hotel;

import DataBase.JdbcHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Pablo Ramirez
 */
public class MenuReservaController implements Initializable {

    @FXML
    private Button btnReservaHabitacion;
    @FXML
    private Button btnReservaServicios;
    @FXML
    private Button btnVolver;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnReservaHabitacion_click(ActionEvent event) throws IOException {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Reserva_Habitacion.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Reserva");
           
            
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
    private void btnReservaServicios_click(ActionEvent event) throws IOException {
        Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Reserva_Servicios.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Reserva");
           
            
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
    private void btnVolver_click(ActionEvent event) throws IOException, SQLException {
        String query = "SELECT * FROM admin";
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
        String admin;
        
        while(rs.next()){
            admin =rs.getString("estado");  
            if((admin.equals("SI")) ){
            
            
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
            else if ((admin.equals("NO"))){
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("MenuInicio_1.fxml"));
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
    }
    }
    
}
