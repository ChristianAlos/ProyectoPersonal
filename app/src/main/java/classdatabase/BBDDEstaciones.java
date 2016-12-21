package classdatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import classObjetos.DatosEstaciones;


public class BBDDEstaciones extends SQLiteOpenHelper{


    private final String creaEstaciones = "create table estaciones (numberLine nvarchar(10), nombre nvarchar (255), codigoEstacion nvarchar (25));";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creaEstaciones);
    }
    public BBDDEstaciones(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Integer retornoNumEstacion (String nombreEstacion, String linia){

        int numEst = 0;
        String consulta = "Select codigoEstacion from estaciones where upper(nombre) like upper(\"" + nombreEstacion + "\")" +
                " and upper(numberLine) like upper(" + linia + ");";
        Cursor cursor = getReadableDatabase().rawQuery(consulta, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            numEst = Integer.valueOf(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();


        return numEst;
    }
    public List<Integer> retornoCodListaEstaciones(){
        List<Integer> listaNum = new ArrayList<>();
        String consulta = "SELECT DISTINCT(codigoEstacion) FROM estaciones";
        Cursor cursor = getReadableDatabase().rawQuery(consulta, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            listaNum.add(cursor.getInt(0));
            cursor.moveToNext();
        }
        cursor.close();
        return listaNum;
    }
    public DatosEstaciones retornoEstaciones (String[] codiEstacio){

        Map<String, Integer> diccionario = new HashMap<String, Integer>();
        List<String> estaciones = new ArrayList<>();
        String consulta = "select * from estaciones where numberLine=?";
        Cursor cursor = getReadableDatabase().rawQuery(consulta, codiEstacio);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            estaciones.add(cursor.getString(1));
            diccionario.put(cursor.getString(1), cursor.getInt(1));
            cursor.moveToNext();
        }
        DatosEstaciones datosEstaciones = new DatosEstaciones(diccionario, estaciones);
        cursor.close();


        return datosEstaciones;
    }

    //Obtener la direccion del tren v1 o v2
    public Map<Integer, String> obtenerVias (int line){
        String nombreEstacion = new String();
        Map<Integer, String> estacionesVias = new HashMap<>();

        List<String> nombres = obtenerEstaciones(line);

        //obtenemos el ultimo elemento via 1
        nombreEstacion = nombres.get(nombres.size() -1);
        estacionesVias.put(1, nombreEstacion);

        //obtenemos el primer elemento via 2
        nombreEstacion = nombres.get(0);
        estacionesVias.put(2, nombreEstacion);

        return estacionesVias;
    }
    private List<String> obtenerEstaciones(int linia){

        List<String> nombres = new ArrayList<>();

        String consulta = "Select nombre from estaciones where upper(numberLine) like upper(\"" + linia + "\");";

        Cursor cursor = getReadableDatabase().rawQuery(consulta, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            nombres.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();

        return nombres;
    }

}
