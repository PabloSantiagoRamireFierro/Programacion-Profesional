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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Pablo Ramirez
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtUsuario;
    @FXML
    private Button btnIngresar;
    @FXML
    private Button btnSalir;
    @FXML
    private PasswordField txtContrasena;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
  
    }    

    @FXML
    private void btnIngresar_click(ActionEvent event) throws IOException, SQLException {
        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();
        String admin;

        String query = "SELECT * FROM usuario WHERE nombre_usuario = '"+usuario+"'AND contraseña = '"+contrasena+"'";
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
            if(rs.next()){
            admin =rs.getString("admin");

            
            switch (admin) {
                case "SI":
                    {
                        String query2 = "INSERT INTO admin (estado)VALUES ('"+admin+"')";
                        boolean exito = jdbc.ejecutarQuery(query2);
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
                        });     stage.setScene(scene);
                        stage.show();
                        //ocultar la ventana de Login
                        ((Node)(event.getSource())).getScene().getWindow().hide();
                        break;
                    }
                case "NO":
                    {
                        String query3 = "INSERT INTO admin (estado)VALUES ('"+admin+"')";
                        boolean fallo = jdbc.ejecutarQuery(query3);
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
                        });     stage.setScene(scene);
                        stage.show();
                        //ocultar la ventana de Login
                        ((Node)(event.getSource())).getScene().getWindow().hide();
                        break;
                    }
                default:
                    JOptionPane.showMessageDialog(null,"Usuario o contraseña erroneo"
                            + "Intentelo nuevamente ",
                            "Error",JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
            else{
               JOptionPane.showMessageDialog(null,"Usuario o contraseña erroneo"
                            + "Intentelo nuevamente ",
                            "Error",JOptionPane.ERROR_MESSAGE);
           }
        
    }
    
    

    @FXML
    private void btnSalir_click(ActionEvent event) {
        System.exit(0);
    }
}
