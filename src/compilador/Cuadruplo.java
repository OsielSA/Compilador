package compilador;

public class Cuadruplo {
    private String operador;
    private String operador1;
    private String operador2;
    private String resultado;
    private String resultadoValor;

    public Cuadruplo(String operador, String operador1, String operador2, String resultado, String resultadoValor) {
        this.operador = operador;
        this.operador1 = operador1;
        this.operador2 = operador2;
        this.resultado = resultado;
        this.resultadoValor = resultadoValor;
    }

    public String getOperador() {
        return operador;
    }

    public String getOperador1() {
        return operador1;
    }

    public String getOperador2() {
        return operador2;
    }

    public String getResultado() {
        return resultado;
    }
    public String getResultadoValor() {
        return resultadoValor;
    }
}
