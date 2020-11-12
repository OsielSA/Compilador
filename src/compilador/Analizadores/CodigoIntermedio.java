package compilador.Analizadores;

import compilador.Cuadruplo;
import compilador.Simbolo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CodigoIntermedio {
    ArrayList<Simbolo> tablaSimbolos;
    ArrayList<Expresion> listaExpresiones;
    ArrayList<Cuadruplo> listaCuadruplos;

    public CodigoIntermedio(ArrayList<Simbolo> tablaSimbolos){
        this.tablaSimbolos = tablaSimbolos;
        obtenerExpresiones();
        generarCuadruplos();
    }

    private void obtenerExpresiones() {
        listaExpresiones = new ArrayList<>();
        for (Simbolo simbolo : tablaSimbolos) {
            String expresion = simbolo.getValor();
            // Cuádruplos: Expresión de 4 elementos
            if (expresion.split("\\s+").length >= 3) {
                listaExpresiones.add(new Expresion(simbolo.getSimbolo(), expresion));
            }
        }
    }

    private void generarCuadruplos() {
        for (Expresion ex : listaExpresiones) {
            cabeceraImpresion(ex);
            listaCuadruplos = new ArrayList<>();
            ArrayList<String> elementos = new ArrayList<>(Arrays.asList(ex.expresion.split("\\s+")));

            int contadorTmp = 1;
            for (int i = 0; i < elementos.size(); i++) {
                if (elementos.get(i).equals("/") || elementos.get(i).equals("*")) {
                    generarCuadruplo(elementos, i, contadorTmp);
                    contadorTmp++;
                    i = -1;
                } else if (!elementos.contains("/") && !elementos.contains("*")  && (elementos.get(i).equals("+") || elementos.get(i).equals("-"))) {
                    generarCuadruplo(elementos, i, contadorTmp);
                    contadorTmp++;
                    i = -1;
                } /*else if (elementos.size() == 1) {
                    listaCuadruplos.add(new Cuadruplo(
                            ex.id + " :=",  // Operador
                            elementos.get(i),       // Operador1
                            "",            // Operador2
                            ""              // Resultado
                    ));
                }*/

            }
            cuerpoImpresion(ex);
        }
    }
    private void generarCuadruplo(ArrayList<String> elementos, int i, int contador) {
        String tmp = generarTmp() + contador;
        String tmpValor;
        int aux = 0;
        int v1 = 0;
        int v2 = 0;
        try{
            v1 = Integer.parseInt(elementos.get(i - 1));
        }catch (Exception e){
            v1 = getValorVar(elementos.get(i - 1));
        }
        try{
            v2 = Integer.parseInt(elementos.get(i + 1));
        }catch (Exception e){
            v2 = getValorVar(elementos.get(i + 1));
        }
        if(elementos.get(i).equals("/")){
            aux = v1 / v2;
        }else if(elementos.get(i).equals("*")){
            aux = v1 * v2;
        }else if(elementos.get(i).equals("+")){
            aux = v1 + v2;
        }else if(elementos.get(i).equals("-")){
            aux = v1 - v2;
        }
        tmpValor = ""+aux;
        listaCuadruplos.add(new Cuadruplo(
                elementos.get(i),       // Operador
                elementos.get(i - 1),   // Operador1
                elementos.get(i + 1),   // Operador2
                tmp,                    // Resultado
                tmpValor                // ResultadoValor
        ));
        elementos.set(i, tmp);
        elementos.remove(i + 1);
        elementos.remove(i - 1);

    }

    private int getValorVar(String var) {
        //Buscar si es un cuadruplo
        for(Cuadruplo c: listaCuadruplos){
            if(var.equals(c.getResultado()))
                return Integer.parseInt(c.getResultadoValor());
        }
        //Buscar si es una variable
        for(Simbolo s: tablaSimbolos){
            if(var.equals(s.getSimbolo())) {
                String aux = s.getValor().replaceAll("\\s", "");
                return Integer.parseInt(aux);
            }
        }
        return 0;
    }

    private String generarTmp() {
        return new Random().nextFloat() >= 0.5 ? "tmpDaniel" : "tmpOsiel";
    }

    private void cabeceraImpresion(Expresion ex){
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("Expresión: "+ex.id + " = " + ex.expresion);
        System.out.println("___________________________________________________________________________________________________");
        System.out.printf("%15s %15s %15s %15s %25s\n", "Operador", "Operando1", "Operando2", "Resultado", "Valor de Temporal");
        System.out.println("___________________________________________________________________________________________________");
    }
    private void cuerpoImpresion(Expresion ex) {
        for (Cuadruplo cuadruplo : listaCuadruplos) {
            System.out.format("%15s %15s %15s %15s %15s\n",cuadruplo.getOperador() , cuadruplo.getOperador1(), cuadruplo.getOperador2(), cuadruplo.getResultado(), cuadruplo.getResultadoValor());
        }
        Cuadruplo c = listaCuadruplos.get(listaCuadruplos.size()-1);
        System.out.println(ex.id + " = " + c.getResultadoValor());
        asignarValor(ex.id, c.getResultadoValor());
        System.out.println("___________________________________________________________________________________________________");
        System.out.println();
    }

    private void asignarValor(String id, String resultadoValor) {
        for(Simbolo s: tablaSimbolos){
            if(id.equals(s.getSimbolo())) {
                s.setValor(resultadoValor);
            }
        }
    }

    //-------------------------------------------------
    private class Expresion {
        public String id;
        public String expresion;

        private Expresion(String id, String expresion) {
            this.id = id;
            this.expresion = expresion;
        }

        @Override
        public String toString() {
            return "Expresion{" +
                    "id='" + id + '\'' +
                    ", expresion='" + expresion + '\'' +
                    '}';
        }
    }
}
