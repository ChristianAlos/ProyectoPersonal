package classObjetos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by krialo_23 on 8/5/16.
 */

public class DatosEstaciones {

    private Map<String, Integer> diccionario;
    private List<String> estaciones;

    public DatosEstaciones (Map<String, Integer> map, List<String> est){
        this.diccionario = map;
        this.estaciones = est;
    }

    public Map<String, Integer> getDiccionario() {
        return diccionario;
    }

    public void setDiccionario(Map<String, Integer> diccionario) {
        this.diccionario = diccionario;
    }

    public List<String> getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(List<String> estaciones) {
        this.estaciones = estaciones;
    }



}
