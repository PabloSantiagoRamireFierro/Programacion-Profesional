/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Pablo Ramirez
 */
public class ConexionMySQL {

    public static String database = "hotel";
    public static String url = "jdbc:mysql://localhost/" + database;
    public static String user = "root";
    public static String password = "";
    
    public static Connection conectar(){
        Connection link = null;
        try{
            Class.forName("org.gjt.mm.mysql.Driver");
            link = DriverManager.getConnection(ConexionMySQL.url,ConexionMySQL.user,ConexionMySQL.password);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"ERROR 0 al Conectar"+ ex + "\nAsegurese de que este conectado.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return link;
    }
 
}