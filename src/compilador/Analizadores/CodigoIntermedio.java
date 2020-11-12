package compilador.Analizadores;

import compilador.Simbolo;

import java.util.ArrayList;
import java.util.HashMap;

public class CodigoIntermedio {
    ArrayList<Simbolo> tablaSimbolos;
    ArrayList<Expresion> listaExpresiones;

    public CodigoIntermedio(ArrayList<Simbolo> tablaSimbolos){
        this.tablaSimbolos = tablaSimbolos;
        obtenerExpresiones();
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
