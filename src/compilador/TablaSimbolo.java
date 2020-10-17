package compilador;

public class TablaSimbolo {
    private String simbolo;
    private String rol;
    private String tipo;
    private int posicion;
    private String valor;

    public TablaSimbolo(){};

    public TablaSimbolo(String simbolo, /*String rol,*/ String tipo, int posicion, String valor){
        this.simbolo = simbolo;
        //this.rol = rol;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }


}