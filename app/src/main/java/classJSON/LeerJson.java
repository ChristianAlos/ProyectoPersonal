package classJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import classObjetos.DatosTrenes;


public class LeerJson {




    //leer trenes
    public List<List<DatosTrenes>> leerTrenes(JSONObject json, String codigo) throws IOException, JSONException {
        ArrayList<String> res = new ArrayList<>();

        List<List<DatosTrenes>> datosTrenes = new ArrayList<>();
        try {
            String jb1 = json.getString("data");
            JSONObject reader = new JSONObject(jb1);
            datosTrenes = leerLineasTrenes(reader, codigo);

        }catch(Exception i){
            i.printStackTrace();
        }

        return datosTrenes;
    }

    private List<List<DatosTrenes>> leerLineasTrenes(JSONObject json, String codigo) throws IOException, JSONException {


        List <List<DatosTrenes>> dat = new ArrayList<>();

        String varios = new String();
        try {
            //dos vias por lo tanto dos arrays
            JSONArray jr1= json.getJSONObject("arrivals").getJSONArray("tracks");
            //creamos los dos arrays con los valores de las dos vias
            JSONObject via1 = jr1.getJSONObject(0);
            JSONObject via2 = jr1.getJSONObject(1);
            List<DatosTrenes> datosDeVia1 = new ArrayList<>();
            List<DatosTrenes> datosDeVia2 = new ArrayList<>();

            JSONArray infoVia1 = via1.getJSONArray("trainsPositions");
            JSONArray infoVia2 = via2.getJSONArray("trainsPositions");

            for (int i = 0; i < infoVia1.length(); i++) {
                JSONObject lineasJSONArray = infoVia1.getJSONObject(i);
                String nTren = lineasJSONArray.getString("trainCode");
                String sEstacion = lineasJSONArray.getString("stopName");
                String llegadaTiempo = lineasJSONArray.getString("arrivalTime");
                Integer dVia1 = lineasJSONArray.getInt("track");

                datosDeVia1.add(new DatosTrenes(nTren, sEstacion, llegadaTiempo, dVia1));
            }

            for (int i = 0; i < infoVia2.length(); i++) {
                JSONObject lineasJSONArray = infoVia2.getJSONObject(i);
                String nTren = lineasJSONArray.getString("trainCode");
                String sEstacion = lineasJSONArray.getString("stopName");
                String llegadaTiempo = lineasJSONArray.getString("arrivalTime");
                Integer dVia2 = lineasJSONArray.getInt("track");

                datosDeVia2.add(new DatosTrenes(nTren, sEstacion, llegadaTiempo, dVia2));

            }

            dat.add(datosDeVia1);
            dat.add(datosDeVia2);


        }catch (JSONException e){
            e.getMessage();
        }



        return dat;
    }


    //leer estaciones
    public ArrayList<String> leerEstaciones(JSONObject json, String codigo) throws IOException, JSONException {
        ArrayList<String> res= new ArrayList<String>();
        try {
            String jb1 = json.getString("data");
            JSONObject reader = new JSONObject(jb1);
            res= leerLineasEstaciones(reader, codigo);

        }catch(Exception i){
            i.printStackTrace();
        }
        return res;
    }


    private ArrayList<String> leerLineasEstaciones(JSONObject json, String codigo) throws IOException, JSONException {
        String nombreEstacion,sql;
        Integer codigoLinea;
        ArrayList<String> contenido= new ArrayList<String>();
        JSONArray jr1= (JSONArray) json.get("features");
        for (int i=0; i<jr1.length(); i++){
            JSONObject jb2 = jr1.getJSONObject(i);
            String jb1=  jb2.getString("properties");
            JSONObject reader = new JSONObject(jb1);
            nombreEstacion = reader.getString("NOM_ESTACIO");
            nombreEstacion = nombreEstacion.replaceAll("'", "''");
            codigoLinea = reader.getInt("CODI_ESTACIO_LINIA");
            sql= "insert into estaciones (numberLine, nombre, codigoEstacion)"+
                    "values ('"+ codigo + "', '" +
                    nombreEstacion + "', '" + codigoLinea+ "')";
            contenido.add(sql);


        }
        return  contenido;
    }


}
