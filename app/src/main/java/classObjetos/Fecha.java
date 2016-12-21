package classObjetos;

/**
 * Created by krialo_23 on 9/14/16.
 */

public class Fecha {
    private String hora;
    private String dia;

    public Fecha(String d, String h){
        this.dia = d;
        this.hora = h;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}
