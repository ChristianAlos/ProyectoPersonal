package gdr.tmbmetro;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by GDR on 07/01/2016.
 */
public class AutoLog {

    public String obtenerFechaString(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =  (Date) c.getTime();
        String fecha =dateFormat.format(date);
        return fecha;
    }

    public Date obtenerFecha(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =  (Date) c.getTime();
        return date;
    }

    public Date sumarDia(String fechaAc){
        Calendar c = Calendar.getInstance();
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //Pasar String a date, sumar un dia y obtener fecha al formato deseado, Date
        Date parsedDate = null;
        try {
            parsedDate = (Date) parser.parse(fechaAc);
            c.setTime(parsedDate);
            c.add(Calendar.HOUR, 24*7);   //Sumamos 24 horas
            parsedDate = (Date) c.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsedDate;
    }

    public Date StringToFecha(String fechaAc) {   ///Convertimos String fecha en objeto Date
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date parsedDate = null;
        try {
            parsedDate = parser.parse(fechaAc);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsedDate;
    }


    public Boolean compararFechas(String fecha){   ///le sumo un dia a la fecha de sincronización y comparo con la actual del sistema
         // si es mayor la del sistema obligo a sincronizar en la activity correspondiente
        if (fecha == null){
            return false;
        }
        Date fechaSum = sumarDia(fecha);
        Date fechaAct = obtenerFecha();

        if (fechaSum.after(fechaAct)){
            return true;
        }else{
            return false;
        }

    }
}
