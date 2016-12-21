package classstaic;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import classObjetos.DatosTrenes;


public class Skeleton {
    static final List<String> list=new ArrayList<String>();
    static final Map<String, Integer> dic = new HashMap<String, Integer>();
    public static Spinner s3 = null;

    public static List<String> numerosTren = new ArrayList<>();
    public static List<List<DatosTrenes>> infoTrenes;
    public static boolean via1bool, via2bool;
    public static Button botCarga, botVia1, botVia2;

    public static List<List<DatosTrenes>> listilla;

    public static boolean numerosObtenidos;

    //datos para la primera peticion --datos del instante del tren a cojer --
    public static List<DatosTrenes> listPetitionsTrensData1;
    public static List<DatosTrenes> listPetitionsTrensData2;

    public static boolean pulsaBotVia;
    public static boolean internetNoOk;

    public static boolean activarViasBots;
    public static String numeroDeTrenSpinner;
    public static List<String> alarmas= new ArrayList<>();


    public void llenaLineas(){
        list.add("Linea 1");
        list.add("Linea 2");
        list.add("Linea 3");
        list.add("Linea 4");
        list.add("Linea 5");
        list.add("Linea 11");

        dic.put("Linea 1",1);
        dic.put("Linea 2",2);
        dic.put("Linea 3",3);
        dic.put("Linea 4",4);
        dic.put("Linea 5",5);
        dic.put("Linea 11",11);
    }
    /*
    public static Spinner getS4() {return s4;}
    public static void setS4(Spinner s4) {Skeleton.s4 = s4;}
    */

    public static void setAlarmas(List<String> alarmas){
        if (alarmas!=null){
            for (int i=0;i<alarmas.size();i++){
                Skeleton.alarmas.add(alarmas.get(i));
            }
        }else {
            Skeleton.alarmas = alarmas;
        }
    }


    public static Spinner getS3() {
        return s3;
    }
    public static void setS3(Spinner s3) {
        Skeleton.s3 = s3;
    }

    public List<String> getNumerosTren() {
        return numerosTren;
    }
    public static void setNumerosTren(List<String> numerosTren) {
        Skeleton.numerosTren = numerosTren;
    }

    public List<String> getList() {
        return list;
    }

    public Map<String, Integer> getDic() {
        return dic;
    }

    public static String ultimaConsulta;


    public static String getUltimaConsulta() {
            return ultimaConsulta;
    }

    public static void setUltimaConsulta(String ultimaConsulta) {
        Skeleton.ultimaConsulta = ultimaConsulta;

    }

    public static void vaciarAlarmasSonadas() {
        for (int i=0;i<alarmas.size(); i++){
            if (Long.valueOf(alarmas.get(i))< Calendar.getInstance().getTimeInMillis()){
                alarmas.remove(i);
            }
        }
    }
}
