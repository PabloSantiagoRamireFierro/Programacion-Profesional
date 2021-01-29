/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fecha;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Pablo Ramirez
 */

//Local Date -> LONG
public final class Fecha {
    public static long getFechaLong(LocalDate fecha){
        String dia = String.valueOf(fecha.getDayOfMonth());
        String mes = String.valueOf(fecha.getMonthValue());
        String anio = String.valueOf(fecha.getYear());
        String fechaString = dia+"/"+mes+"/"+anio+" 00:00:00";
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date;
        long millis;
        try{
            date = sdf.parse(fechaString);
            millis = date.getTime();
        }catch(Exception ex){
            System.out.println("Error: "+ex);
            millis=0;
        }
        return millis;
    }
    
    //long ->LocalDate
    public static LocalDate getFechaLocalDate(Long millis){
        return new java.sql.Date(millis).toLocalDate();    
    }
    
    //long -> String
    public static String getFechaString(long millis){
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }
}
