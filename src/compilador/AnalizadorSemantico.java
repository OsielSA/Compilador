package compilador;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class AnalizadorSemantico {
    private HashMap<String,TablaSimbolo> tablaSimbolos;
    private ArrayList<String> listaErroresSemanticos;

    public AnalizadorSemantico(String texto){
        listaErroresSemanticos = new ArrayList<String>();
        tablaSimbolos = new HashMap<>();
        ejecutarAnalisisSemantico(texto);
    }
    public void ejecutarAnalisisSemantico(String texto){
        String [] listaRenglones;
        listaRenglones = texto.split("\n");

        for (int i=0; i<listaRenglones.length; i++){
            String accion;
            accion = validarTipoAccion(listaRenglones[i]);
            if(accion.equals("declaración")){
                insertarDeclaracion(listaRenglones[i], i+1);
            }
            if(accion.equals("declaración")){
                //-Mandar llamar función validarOperacion enviandole el texto del
                //                 renglon y el numeroRenglon.
            }
        }

        //-Recorrer/immprimir la tabla de simbolos
        //-Imprimir los erroree

    }
    public String  validarTipoAccion(String renglon){
        String respuesta = "";

        StringTokenizer tokens = new StringTokenizer(renglon);
        ArrayList<String> arrTokens = new ArrayList<String>();
        while(tokens.hasMoreTokens()){
            arrTokens.add(tokens.nextToken());
        }
        //Validar si es una declaración, si inicia con algún tipo de dato(int, char, boolean).
        if(arrTokens.get(0).equals("int") || arrTokens.get(0).equals("char") || arrTokens.get(0).equals("boolean"))
            return "declaración"; //Asignar a respuesta "declaracion".

        if(renglon.indexOf('=') > -1) //Validar si es una asignación/Operación(Teniendo el signo igual.
            return "asignación"; //Asignar a respuesta "asignación".

        return respuesta;
    }
    public void insertarDeclaracion(String renglon, int numeroRenglon){
        //Separar los elementos de la declaración, ejemplo int a, tiene dos items;
        StringTokenizer tokens = new StringTokenizer(renglon);
        while(tokens.hasMoreTokens()){
            String tipo = tokens.nextToken();
            String simbolo = tokens.nextToken();
            if(!encontrarSimbolo(simbolo)){
                tablaSimbolos.put(simbolo, new TablaSimbolo(simbolo, tipo, numeroRenglon, null ));
            }
            else{
                TablaSimbolo simboloAtributos = tablaSimbolos.get(simbolo);
                listaErroresSemanticos.add("Ya existe el simbolo: "+ simbolo +" en la linea: "+ simboloAtributos.getPosicion());
            }
        }
    }
    public boolean encontrarSimbolo(String simbolo){
        boolean bRegresa = false;

        if (tablaSimbolos.containsKey(simbolo))
        {
            bRegresa = true;
        }

        return bRegresa;
    }

}
