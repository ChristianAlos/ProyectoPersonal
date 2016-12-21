package classObjetos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krialo_23 on 8/5/16.
 */

public class DatosTrenes {



    private List<String> numDeTrenes;
    private List<String> listaDeEstaciones;
    private List<String> horasTrenes;
    private List<Integer> viaTen;

    private String numDeTren;
    private String listaDeEstacion;
    private String horasTren;
    private Integer viaT;

    public DatosTrenes(String nTrenes, String lEstaciones, String llegadas, Integer viaT){
        this.numDeTren = nTrenes;
        this.listaDeEstacion = lEstaciones;
        this.horasTren = llegadas;
        this.viaT = viaT;
    }

    public DatosTrenes(List<String> nTrenes, List<String> lEstaciones, List<String> llegadas, List<Integer> viaT){
        this.numDeTrenes = nTrenes;
        this.listaDeEstaciones = lEstaciones;
        this.horasTrenes = llegadas;
        this.viaTen = viaT;
    }


    public String getNumDeTren() {
        return numDeTren;
    }

    public void setNumDeTren(String numDeTren) {
        this.numDeTren = numDeTren;
    }

    public String getListaDeEstacion() {
        return listaDeEstacion;
    }

    public void setListaDeEstacion(String listaDeEstacion) {
        this.listaDeEstacion = listaDeEstacion;
    }

    public String getHorasTren() {
        return horasTren;
    }

    public void setHorasTren(String horasTren) {
        this.horasTren = horasTren;
    }

    public Integer getViaT() {
        return viaT;
    }

    public void setViaT(Integer viaT) {
        this.viaT = viaT;
    }
    public List<Integer> getViaTen() {return viaTen;}

    public void setViaTen(List<Integer> viaTen) {this.viaTen = viaTen;}

    public List<String> getHorasTrenes() {return horasTrenes;}

    public void setHorasTrenes(List<String> horasTrenes) {this.horasTrenes = horasTrenes;}

    public List<String> getNumDeTrenes() {
        return numDeTrenes;
    }

    public void setNumDeTrenes(List<String> numDeTrenes) {
        this.numDeTrenes = numDeTrenes;
    }

    public List<String> getListaDeEstaciones() {
        return listaDeEstaciones;
    }

    public void setListaDeEstaciones(List<String> listaDeEstaciones) {
        this.listaDeEstaciones = listaDeEstaciones;
    }






}
