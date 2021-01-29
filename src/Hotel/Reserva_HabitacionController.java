/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hotel;

import DataBase.ConexionMySQL;
import DataBase.JdbcHelper;
import Entidades.Habitación;
import Entidades.Reserva_Habitacion;
import Fecha.Fecha;
import Repo.HabitaciónRepo;
import Repo.ReservaHabitacionRepo;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Pablo Ramirez
 */
public class Reserva_HabitacionController implements Initializable {
    
    ObservableList<Habitación> dataHabitacion = FXCollections.observableArrayList();
    ObservableList<Habitación> dataHabitacion_valida = FXCollections.observableArrayList();

    @FXML
    private Button btnSalir;
    @FXML
    private Button btnVolver;
    @FXML
    private DatePicker dateIngreso;
    @FXML
    private DatePicker dateSalida;
    @FXML
    private TextField textNumeroHabitacion;
    @FXML
    private TextField textNombre;
    @FXML
    private TextField textRut;
    @FXML
    private TextField textTelefono;
    @FXML
    private TextField textEmail;
    @FXML
    private Label labelHabDisponible;
    @FXML
    private Button btnReservar;
    @FXML
    private Label labelPrecio;
    @FXML
    private CheckBox checkPlural;
    @FXML
    private TextField textNumeroHabitacion_Plural;
    @FXML
    private Text labelAdicional;
    @FXML
    private TableColumn<Habitación, Integer> col_num;
    @FXML
    private TableColumn<Habitación, String> col_tipo;
    @FXML
    private TableColumn<Habitación, String> col_baño;
    @FXML
    private TableView<Habitación> table;
    @FXML
    private Button btnComprobar;
    @FXML
    private Button btnCalculoPrecio;
    @FXML
    private Button btnRefrescar;
    @FXML
    private TextField textApellido;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
           textNumeroHabitacion_Plural.setDisable(true);
           labelAdicional.setVisible(false);
           labelPrecio.setVisible(false);
           
           configurarTabla();
        try {
            rellenarTablaHabitacion();
        } catch (SQLException ex) {
            Logger.getLogger(Reserva_HabitacionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void btnSalir_click(ActionEvent event) throws SQLException {
        String query = "DELETE FROM admin";
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        System.exit(0);
    }

    @FXML
    private void btnVolver_click(ActionEvent event) throws IOException {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("MenuReserva.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Menu de reserva");
           
            
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
    private void btnReservar_click(ActionEvent event) throws SQLException {
        String Nombre,Apellido, Rut,Telefono,email,f_entradaString,f_salidaString, nHabitacion;
        long f_entradaLong, f_salidaLong;
        int numeroHabitacion, precio;
        Connection conn = ConexionMySQL.conectar();
        nHabitacion = textNumeroHabitacion.getText();
        Nombre = textNombre.getText();
        Apellido = textApellido.getText();
        Rut = textRut.getText();
        Telefono = textTelefono.getText();
        email = textEmail.getText();
        boolean esEnteroNHAB = isInteger(nHabitacion);
        boolean esEnteroTelefono = isInteger(Telefono);
        Pattern pat = Pattern.compile("[a-zA-z]");
        Matcher mat = pat.matcher(Nombre);
        Pattern pat2 = Pattern.compile("[a-zA-z]");
        Matcher mat2 = pat2.matcher(Apellido);
        Pattern pat1 = Pattern.compile("[a-zA-z]");
        Matcher mat1 = pat1.matcher(textNumeroHabitacion_Plural.getText());
                
        //datos vacios
        if(((dateIngreso.getValue())== null)||((dateSalida.getValue())==null)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingrese Fechas");
            return;
        }
        else if("".equals(textNumeroHabitacion.getText())||"".equals(Nombre) ||"".equals(Rut) ||"".equals(email) || "".equals(Telefono)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Rellene todos los campos");
            return;
        }
        
        f_entradaLong = Fecha.getFechaLong(dateIngreso.getValue());
        f_salidaLong = Fecha.getFechaLong(dateSalida.getValue());
        f_entradaString = Fecha.getFechaString(f_entradaLong);
        f_salidaString = Fecha.getFechaString(f_salidaLong);
        
        String[] len = email.split("@");
        int length = len.length;
        System.out.println(length);

        //Fechas   
        String añoIn = String.valueOf(f_entradaString.charAt(6)) + String.valueOf(f_entradaString.charAt(7)) + String.valueOf(f_entradaString.charAt(8)) + String.valueOf(f_entradaString.charAt(9));
        String mesIn = String.valueOf(f_entradaString.charAt(3)) + String.valueOf(f_entradaString.charAt(4));
        String diaIn = String.valueOf(f_entradaString.charAt(0)) + String.valueOf(f_entradaString.charAt(1));
            
        String añoOut = String.valueOf(f_salidaString.charAt(6)) + String.valueOf(f_salidaString.charAt(7)) + String.valueOf(f_salidaString.charAt(8)) + String.valueOf(f_salidaString.charAt(9));
        String mesOut = String.valueOf(f_salidaString.charAt(3)) + String.valueOf(f_salidaString.charAt(4));
        String diaOut = String.valueOf(f_salidaString.charAt(0)) + String.valueOf(f_salidaString.charAt(1));

        Date tiempofecha = new Date();
        Calendar now = Calendar.getInstance();
        int añoActual = (now.get(Calendar.YEAR));
        int mesActual = (now.get(Calendar.MONTH)+1);
        int diaActual = (now.get(Calendar.DAY_OF_MONTH));
        
        if(validarRut(Rut)==false){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("RUT Invalido. Trate nuevamente");
        }
        //nombre mal escrito
        else if(mat.find() == false) {
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingresar un nombre correcto");
        }
        else if(mat2.find() == false) {
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingresar un apellido correcto");
        }
        else if((mat1.find() == true) && (checkPlural.isSelected())) {
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingresar numeros correctos");
            return;
        }
        else if("".equals(textNumeroHabitacion_Plural.getText())&&(checkPlural.isSelected())){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingresar algun numero o quite este campo");
        }
        //MAIL VALIDATION
        else if(!email.contains(".")){
            System.out.println("no dot");
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("No tiene punto. Ingrese mail valido");
            textEmail.setText("");
        }
        else if(!email.contains("@")){
            System.out.println("no at");
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Sin @. Ingrese correctamente el mail");
            textEmail.setText("");
        }
        else if(length > 2){
            System.out.println("too many ats");
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Demasiados @. Ingrese correctamente el mail");
            textEmail.setText("");
        }
        //fecha
        else if(f_entradaLong > f_salidaLong){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El Ingreso es después que la salida. Ingresa fechas correctas");
        }
        else if(Integer.parseInt(añoIn) < añoActual){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El año no es correcto. Ingresa otra fecha de ingreso"); 
        }
        else if(Integer.parseInt(añoIn) == añoActual && Integer.parseInt(mesIn) < mesActual){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El mes no es correcto. Ingresa otra fecha de ingreso");
        }
        else if(Integer.parseInt(añoIn) == añoActual && Integer.parseInt(mesIn) == mesActual && Integer.parseInt(diaIn) < diaActual){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El día no es correcto. Ingresa otra fecha de ingreso"); 
        }
        else if(Integer.parseInt(añoOut) < Integer.parseInt(añoIn)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El año no es correcto. Ingresa otra fecha de salida");   
        }
        else if(Integer.parseInt(añoOut) == Integer.parseInt(añoIn) && Integer.parseInt(mesOut) < Integer.parseInt(mesIn)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El mes no es correcto. Ingresa otra fecha de salida");
        }
        else if(Integer.parseInt(añoOut) == Integer.parseInt(añoIn) && Integer.parseInt(mesOut) == Integer.parseInt(mesIn) && Integer.parseInt(diaOut) < Integer.parseInt(diaIn)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El dia no es correcto. Ingresa otra fecha de salida");   
        }
        //Ingresar numeros validos
        else if(esEnteroNHAB == false){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingresar un numero de habitacion correcto");
        }
        else if(esEnteroTelefono == false){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingresar un numero de telefono correcto");
        }
        //si existen o no    
        else if(dbexisteRegistro(conn, nHabitacion) == false){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("No existe la habitacion N°" + nHabitacion);
        }
        else if(dbexisteRegistro(conn, nHabitacion, f_entradaLong) == true){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ya esta reservada la Habitacion N° " + nHabitacion);
        }
        else{
            numeroHabitacion = Integer.parseInt(nHabitacion);
            if("".equals(textNumeroHabitacion_Plural.getText())){
                int PrecioPieza = 0;
                Reserva_Habitacion reserva;
                
                long tiempo = f_salidaLong - f_entradaLong;
                tiempo = tiempo/86400000;
                ReservaHabitacionRepo reservaRepo = new ReservaHabitacionRepo();
                PrecioPieza = reservaRepo.precio(numeroHabitacion);
                PrecioPieza = (int) (PrecioPieza*tiempo);

                reserva = new Reserva_Habitacion(f_entradaLong,f_entradaString,f_salidaLong,f_salidaString, numeroHabitacion, Nombre,Apellido, Rut, Telefono, email, PrecioPieza,"NO");
                ReservaHabitacionRepo reservaRepo2 = new ReservaHabitacionRepo();
                reservaRepo2.crear(reserva);
                
                vaciarCampos();
            }
            else {
                int PrecioTotal = 0;
                String[] numeros;
                numeros = textNumeroHabitacion_Plural.getText().split(",");
                int largo =numeros.length;
                int[] lista_numeros = new int [largo+2];

                for(int i =0; i < largo; i++){
                    lista_numeros[i] = Integer.parseInt(numeros[i]);
                    if(dbexisteRegistro(conn, numeros[i]) == false){
                        ReservaHabitacionRepo method = new ReservaHabitacionRepo();
                        method.alerta("No existe la habitacion N°" + numeros[i]);
                        return;
                    }
                }

                for(int i = 0; i < largo;i++){
                    Reserva_Habitacion reserva;
                    int PrecioPieza = 0;
                    long tiempo = f_salidaLong - f_entradaLong;
                    tiempo = tiempo/86400000;
                    ReservaHabitacionRepo reservaRepo = new ReservaHabitacionRepo();
                    PrecioPieza = reservaRepo.precio(lista_numeros[i]);
                    PrecioPieza = (int) (PrecioPieza*tiempo);

                    reserva = new Reserva_Habitacion(f_entradaLong,f_entradaString,f_salidaLong,f_salidaString, lista_numeros[i], Nombre,Apellido, Rut, Telefono, email, PrecioPieza, "NO");
                    ReservaHabitacionRepo reservaRepo2 = new ReservaHabitacionRepo();
                    reservaRepo2.crear(reserva);
                    PrecioTotal = PrecioTotal + PrecioPieza;
                }
                //el primero 
                int PrecioPieza = 0;
                Reserva_Habitacion reserva;
                
                long tiempo = f_salidaLong - f_entradaLong;
                tiempo = tiempo/86400000;
                ReservaHabitacionRepo reservaRepo = new ReservaHabitacionRepo();
                PrecioPieza = reservaRepo.precio(numeroHabitacion);
                PrecioPieza = (int) (PrecioPieza*tiempo);
                reserva = new Reserva_Habitacion(f_entradaLong,f_entradaString,f_salidaLong,f_salidaString, numeroHabitacion, Nombre,Apellido, Rut, Telefono, email, PrecioPieza, "NO");
                ReservaHabitacionRepo reservaRepo2 = new ReservaHabitacionRepo();
                reservaRepo2.crear(reserva);
                
                vaciarCampos();

            }
        }
    }


    @FXML
    private void checkPlural_click(ActionEvent event) {
        if(checkPlural.isSelected()){
           textNumeroHabitacion_Plural.setDisable(false);
           labelAdicional.setVisible(true);
        }
        else{
           textNumeroHabitacion_Plural.setDisable(true);
           labelAdicional.setVisible(false);
           textNumeroHabitacion_Plural.setText("");
        }
    }
    
    public void configurarTabla(){
        col_num.setCellValueFactory(new PropertyValueFactory<>("num"));
        col_tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        col_baño.setCellValueFactory(new PropertyValueFactory<>("baño"));
        table.setItems(dataHabitacion);
    }
    
    public void rellenarTablaHabitacion() throws SQLException{
        dataHabitacion.clear();
        HabitaciónRepo habitacionRepo = new HabitaciónRepo();
        ObservableList<Habitación> habitaciones = habitacionRepo.buscarTodos();
        dataHabitacion.setAll(habitaciones);
    }
    
    private void vaciarCampos(){
        textNumeroHabitacion.setText("");
        textNombre.setText("");
        textRut.setText("");
        textTelefono.setText("");
        textEmail.setText("");
        dateIngreso.setValue(null);
        dateSalida.setValue(null);
        labelPrecio.setText("0");
    }

    @FXML
    private void btnComprobar_click(ActionEvent event) throws SQLException {
        
        dataHabitacion_valida.clear();
        
        if(((dateIngreso.getValue())== null)||((dateSalida.getValue())==null)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingrese Fechas");
            return;
        }
        else if(dateIngreso.getValue() == dateSalida.getValue()){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Reserva por un solo día, Informar si esta seguro al Cliente");
            return;
        }
        long f_entradaLong, f_salidaLong;
        f_entradaLong = Fecha.getFechaLong(dateIngreso.getValue());
        f_salidaLong = Fecha.getFechaLong(dateSalida.getValue());
        String f_entradaString = Fecha.getFechaString(f_entradaLong);
        String f_salidaString = Fecha.getFechaString(f_salidaLong);
        
         //Fechas   
        String añoIn = String.valueOf(f_entradaString.charAt(6)) + String.valueOf(f_entradaString.charAt(7)) + String.valueOf(f_entradaString.charAt(8)) + String.valueOf(f_entradaString.charAt(9));
        String mesIn = String.valueOf(f_entradaString.charAt(3)) + String.valueOf(f_entradaString.charAt(4));
        String diaIn = String.valueOf(f_entradaString.charAt(0)) + String.valueOf(f_entradaString.charAt(1));
            
        String añoOut = String.valueOf(f_salidaString.charAt(6)) + String.valueOf(f_salidaString.charAt(7)) + String.valueOf(f_salidaString.charAt(8)) + String.valueOf(f_salidaString.charAt(9));
        String mesOut = String.valueOf(f_salidaString.charAt(3)) + String.valueOf(f_salidaString.charAt(4));
        String diaOut = String.valueOf(f_salidaString.charAt(0)) + String.valueOf(f_salidaString.charAt(1));

        Date tiempofecha = new Date();
        
        System.out.println(tiempofecha);
        Calendar now = Calendar.getInstance();
        int añoActual = (now.get(Calendar.YEAR));
        int mesActual = (now.get(Calendar.MONTH)+1);
        int diaActual = (now.get(Calendar.DAY_OF_MONTH));

        //restricciones fechas
        if(f_entradaLong > f_salidaLong){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El Ingreso es después que la salida. Ingresa fechas correctas");
        }
        else if(Integer.parseInt(añoIn) < añoActual){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El año no es correcto. Ingresa otra fecha de ingreso"); 
        }
        else if(Integer.parseInt(añoIn) == añoActual && Integer.parseInt(mesIn) < mesActual){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El mes no es correcto. Ingresa otra fecha de ingreso");
        }
        else if(Integer.parseInt(añoIn) == añoActual && Integer.parseInt(mesIn) == mesActual && Integer.parseInt(diaIn) < diaActual){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El día no es correcto. Ingresa otra fecha de ingreso"); 
        }
        else if(Integer.parseInt(añoOut) < Integer.parseInt(añoIn)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El año no es correcto. Ingresa otra fecha de salida");   
        }
        else if(Integer.parseInt(añoOut) == Integer.parseInt(añoIn) && Integer.parseInt(mesOut) < Integer.parseInt(mesIn)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El mes no es correcto. Ingresa otra fecha de salida");
        }
      
        else if(Integer.parseInt(añoOut) == Integer.parseInt(añoIn) && Integer.parseInt(mesOut) == Integer.parseInt(mesIn) && Integer.parseInt(diaOut) < Integer.parseInt(diaIn)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El dia no es correcto. Ingresa otra fecha de salida");   
        }
        else{
            String query = "SELECT * FROM habitaciones_reserva";
            JdbcHelper jdbc = new JdbcHelper();
            ResultSet rs = jdbc.realizarConsulta(query);
            while(rs.next()){
                
                try{
                    long fechaingreso= rs.getLong("fechaingreso");
                    long fechasalida= rs.getLong("fechasalida");
                    int numero_habitacion = rs.getInt("num_habitacion");
                    if((fechaingreso >= f_entradaLong)&&(fechasalida<=f_salidaLong)||(fechaingreso<= f_salidaLong)&&(fechasalida>=f_entradaLong)){
                        
                        String query2 = "SELECT * FROM habitaciones WHERE hab_num ="+numero_habitacion;
                        JdbcHelper jdbc2 = new JdbcHelper();
                        ResultSet rs2 = jdbc2.realizarConsulta(query2);
                        
                        while(rs2.next()){
                            int hab_num= rs2.getInt("hab_num");
                            String hab_tipo = rs2.getString("hab_tipo");
                            String hab_baño = rs2.getString("hab_baño");
                            int hab_precio = rs2.getInt("hab_precio");
                            dataHabitacion_valida.add(new Habitación(hab_num,hab_tipo,hab_baño,hab_precio));
                        }
                }
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null,"Error al buscar reserva"
                            + ": " +ex,
                            "Error",JOptionPane.ERROR_MESSAGE);
                }
            }
            int resultados;
            resultados = dataHabitacion_valida.size();
            col_num.setCellValueFactory(new PropertyValueFactory<>("num"));
            col_tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
            col_baño.setCellValueFactory(new PropertyValueFactory<>("baño"));
            table.setItems(dataHabitacion_valida);
            labelHabDisponible.setText("Habitaciones Reservadas : " +resultados);
        }
    }

    @FXML
    private void btnCalculoPrecio_click(ActionEvent event) throws SQLException {
        
        Connection conn = ConexionMySQL.conectar();
        if("".equals(textNumeroHabitacion.getText())){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Rellene el N°Habitación que desea calcular");
            return;
        }
        else if("".equals(textNumeroHabitacion_Plural.getText())&&(checkPlural.isSelected())){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingresar algún N°Habitación adicional o quite este campo");
            return;
        }
        int numeroHabitacion;
        numeroHabitacion = Integer.parseInt(textNumeroHabitacion.getText());
        long f_entradaLong = Fecha.getFechaLong(dateIngreso.getValue());
        long f_salidaLong = Fecha.getFechaLong(dateSalida.getValue());
        String f_entradaString = Fecha.getFechaString(f_entradaLong);
        String f_salidaString = Fecha.getFechaString(f_salidaLong);
        int PrecioPieza;
        int PrecioPiezas;
        int PrecioTotal = 0;
        
        String añoIn = String.valueOf(f_entradaString.charAt(6)) + String.valueOf(f_entradaString.charAt(7)) + String.valueOf(f_entradaString.charAt(8)) + String.valueOf(f_entradaString.charAt(9));
        String mesIn = String.valueOf(f_entradaString.charAt(3)) + String.valueOf(f_entradaString.charAt(4));
        String diaIn = String.valueOf(f_entradaString.charAt(0)) + String.valueOf(f_entradaString.charAt(1));
            
        String añoOut = String.valueOf(f_salidaString.charAt(6)) + String.valueOf(f_salidaString.charAt(7)) + String.valueOf(f_salidaString.charAt(8)) + String.valueOf(f_salidaString.charAt(9));
        String mesOut = String.valueOf(f_salidaString.charAt(3)) + String.valueOf(f_salidaString.charAt(4));
        String diaOut = String.valueOf(f_salidaString.charAt(0)) + String.valueOf(f_salidaString.charAt(1));

        Date tiempofecha = new Date();
        Calendar now = Calendar.getInstance();
        int añoActual = (now.get(Calendar.YEAR));
        int mesActual = (now.get(Calendar.MONTH)+1);
        int diaActual = (now.get(Calendar.DAY_OF_MONTH));
        
        if(f_entradaLong > f_salidaLong){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El Ingreso es después que la salida. Ingresa fechas correctas");
        }
        else if(Integer.parseInt(añoIn) < añoActual){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El año no es correcto. Ingresa otra fecha de ingreso"); 
        }
        else if(Integer.parseInt(añoIn) == añoActual && Integer.parseInt(mesIn) < mesActual){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El mes no es correcto. Ingresa otra fecha de ingreso");
        }
        else if(Integer.parseInt(añoIn) == añoActual && Integer.parseInt(mesIn) == mesActual && Integer.parseInt(diaIn) < diaActual){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El día no es correcto. Ingresa otra fecha de ingreso"); 
        }
        else if(Integer.parseInt(añoOut) < Integer.parseInt(añoIn)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El año no es correcto. Ingresa otra fecha de salida");   
        }
        else if(Integer.parseInt(añoOut) == Integer.parseInt(añoIn) && Integer.parseInt(mesOut) < Integer.parseInt(mesIn)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El mes no es correcto. Ingresa otra fecha de salida");
        }
      
        else if(Integer.parseInt(añoOut) == Integer.parseInt(añoIn) && Integer.parseInt(mesOut) == Integer.parseInt(mesIn) && Integer.parseInt(diaOut) < Integer.parseInt(diaIn)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El dia no es correcto. Ingresa otra fecha de salida");   
        }
        else if(dbexisteRegistro(conn, textNumeroHabitacion.getText()) == false){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("No existe la habitacion N°" + numeroHabitacion); 
        }
        else{
            long tiempo = f_salidaLong - f_entradaLong;
            tiempo = tiempo/86400000;

            if("".equals(textNumeroHabitacion_Plural.getText())){

                ReservaHabitacionRepo reservaRepo = new ReservaHabitacionRepo();
                PrecioPieza = reservaRepo.precio(numeroHabitacion);
                PrecioPieza = (int) (PrecioPieza*tiempo);
                labelPrecio.setText(String.valueOf(PrecioPieza));
                labelPrecio.setVisible(true);
            }
            else {
                String[] numeros;
                numeros = textNumeroHabitacion_Plural.getText().split(",");
                int largo =numeros.length;

                int[] lista_numeros = new int [largo];

                for(int i =0; i < largo; i++){
                    lista_numeros[i] = Integer.parseInt(numeros[i]);
                    if(dbexisteRegistro(conn, numeros[i]) == false){
                        ReservaHabitacionRepo method = new ReservaHabitacionRepo();
                        method.alerta("No existe la habitacion N°" + numeros[i]);
                        return;
                    }
                }

                for(int i = 0; i < largo;i++){
                    ReservaHabitacionRepo reservaRepo = new ReservaHabitacionRepo();
                    PrecioPiezas = reservaRepo.precio(lista_numeros[i]);
                    PrecioPiezas = (int) (PrecioPiezas*tiempo);
                    PrecioTotal = PrecioTotal + PrecioPiezas;
                }

                ReservaHabitacionRepo reservaRepo = new ReservaHabitacionRepo();
                PrecioPieza = reservaRepo.precio(numeroHabitacion);
                PrecioPieza = (int) (PrecioPieza*tiempo);
                PrecioTotal = PrecioTotal + PrecioPieza;
                labelPrecio.setText(String.valueOf(PrecioTotal));
                labelPrecio.setVisible(true);
            }
        }
        
        
    }
    @FXML
    private void btnRefrescar_click(ActionEvent event) throws SQLException {
        rellenarTablaHabitacion();
        configurarTabla();
        labelHabDisponible.setText("Habitaciones del Hotel " );
        
    }

    public  boolean dbexisteRegistro(Connection Conn, String id_a_buscar){
         System.out.println("sin fecha");
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
            
                    sSQL = "SELECT * FROM habitaciones WHERE hab_num =" + id_a_buscar;
             
                 


             oSt = Conn.createStatement();
             oRs = oSt.executeQuery(sSQL);

             if(oRs.next()){
                if(oRs.getRow() > 0){ 
                    dbexisteRegistro= true;
                }
             }

             if (oSt != null) {oSt.close();oSt = null;}
             if (oRs != null) {oRs.close();oRs = null;}
         }catch(SQLException err){

             oSt = null;
             oRs = null;
             sSQL=null;
         }catch(Exception err){

             oSt = null;
             oRs = null;
             sSQL=null;  
         }finally{
             oSt = null;
             oRs = null;
             sSQL=null;
         }
         return dbexisteRegistro;
    }
    public  boolean dbexisteRegistro(Connection Conn, String id_a_buscar, long fecha){
        
         System.out.println("con fecha");
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             
           
                    sSQL = "SELECT * FROM habitaciones_reserva WHERE fechaingreso = '" + fecha + "' AND num_habitacion = '" + id_a_buscar + "'";
             
               


             oSt = Conn.createStatement();
             oRs = oSt.executeQuery(sSQL);

             if(oRs.next()){
                if(oRs.getRow() > 0){
                    dbexisteRegistro= true;
                }
             }

             if (oSt != null) {oSt.close();oSt = null;}
             if (oRs != null) {oRs.close();oRs = null;}
         }catch(SQLException err){

             oSt = null;
             oRs = null;
             sSQL=null;
         }catch(Exception err){

             oSt = null;
             oRs = null;
             sSQL=null;  
         }finally{
             oSt = null;
             oRs = null;
             sSQL=null;
         }
         return dbexisteRegistro;
    }
    
    public static boolean isInteger(String s) {
    try { //Try to make the input into an integer
        Integer.parseInt(s);
        return true; //Return true if it works
    }
    catch( Exception e ) { 
        return false; //If it doesn't work return false
    }
    }
    public static boolean validarRut(String rut) {
 
    boolean validacion = false;
    try {
    rut =  rut.toUpperCase();
    rut = rut.replace(".", "");
    rut = rut.replace("-", "");
    int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

    char dv = rut.charAt(rut.length() - 1);

    int m = 0, s = 1;
    for (; rutAux != 0; rutAux /= 10) {
        s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
    }
    if (dv == (char) (s != 0 ? s + 47 : 75)) {
        validacion = true;
    }

    } catch (java.lang.NumberFormatException e) {
    } catch (Exception e) {
    }
    return validacion;
    }
}
