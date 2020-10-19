package compilador.Analizadores;

import compilador.TablaSimbolo;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class AnalizadorSemantico {
    private HashMap<String, TablaSimbolo> tablaSimbolos;
    private ArrayList<String> listaErroresSemanticos;

    private ArrayList<String> operadoresaritmeticos; //Llenarlos
    private ArrayList<String> operadoreslogicos;//Llenarlos

    public AnalizadorSemantico(String texto) {
        listaErroresSemanticos = new ArrayList<String>();
        tablaSimbolos = new HashMap<>();
        llenarListasOperadores();
        ejecutarAnalisisSemantico(texto);
    }

    private void llenarListasOperadores() {
        operadoresaritmeticos = new ArrayList<>();
        operadoreslogicos = new ArrayList<>();
        //Operadores Aritméticos
        operadoresaritmeticos.add("+");
        operadoresaritmeticos.add("-");
        operadoresaritmeticos.add("*");
        operadoresaritmeticos.add("/");
        operadoresaritmeticos.add("%");

        //Operadores Lógicos
        operadoreslogicos.add("||");
        operadoreslogicos.add("&&");
        operadoreslogicos.add("!");
        operadoreslogicos.add("==");
        operadoreslogicos.add("<=");
        operadoreslogicos.add(">=");
        operadoreslogicos.add("!=");
        operadoreslogicos.add(">");
        operadoreslogicos.add("<");
        operadoreslogicos.add("");
    }

    public void ejecutarAnalisisSemantico(String texto) {
        String[] listaRenglones;
        listaRenglones = texto.split("\n");

        for (int i = 0; i < listaRenglones.length; i++) {
            if (listaRenglones[i].equals(""))
                continue;
            String accion;
            accion = validarTipoAccion(listaRenglones[i]);
            if (accion.equals("declaración")) {
                insertarDeclaracion(listaRenglones[i], i + 1);
            }
            if (accion.equals("asignación")) {
                validarOperacion(listaRenglones[i], i + 1);
            }
        }

        //-Recorrer/immprimir la tabla de simbolos
        //-Imprimir los errores

    }

    public String validarTipoAccion(String renglon) {
        String respuesta = "";

        StringTokenizer tokens = new StringTokenizer(renglon);
        ArrayList<String> arrTokens = new ArrayList<String>();
        while (tokens.hasMoreTokens()) {
            arrTokens.add(tokens.nextToken());
        }
        //Validar si es una declaración, si inicia con algún tipo de dato(int, char, boolean).
        if (arrTokens.get(0).equals("int") || arrTokens.get(0).equals("char") || arrTokens.get(0).equals("boolean") || arrTokens.get(0).equals("float"))
            return "declaración"; //Asignar a respuesta "declaracion".

        if (renglon.indexOf('=') > -1) //Validar si es una asignación/Operación(Teniendo el signo igual.
            return "asignación"; //Asignar a respuesta "asignación".

        return respuesta;
    }

    public void insertarDeclaracion(String renglon, int numeroRenglon) {
        //Separar los elementos de la declaración, ejemplo int a, tiene dos items;
        StringTokenizer tokens = new StringTokenizer(renglon);
        while (tokens.hasMoreTokens()) {
            String tipo = tokens.nextToken();
            String simbolo = tokens.nextToken();
            if (!encontrarSimbolo(simbolo)) {
                tablaSimbolos.put(simbolo, new TablaSimbolo(simbolo, tipo, numeroRenglon, null));
            } else {
                TablaSimbolo simboloAtributos = tablaSimbolos.get(simbolo);

                String error = "Ya existe el simbolo: " + simbolo + " en la linea: " + simboloAtributos.getPosicion();
                listaErroresSemanticos.add(error);
                System.out.print(error);

            }
        }
    }

    public boolean encontrarSimbolo(String simbolo) {
        boolean bRegresa = false;

        if (tablaSimbolos.containsKey(simbolo)) {
            bRegresa = true;
        }

        return bRegresa;
    }

    public void validarOperacion(String renglon, int numeroRenglon) {
        ArrayList<TablaSimbolo> atributoSimbolos = new ArrayList<>();
        ArrayList<String> listaConstantes = new ArrayList<>();

        String tipo = "", valor = null;
        String var = renglon.substring(0, renglon.indexOf('='));
        var = var.replaceAll("\\s", "");
        if (tablaSimbolos.containsKey(var)) {
            tipo = tablaSimbolos.get(var).getTipo();
        }


        String aux = renglon.substring(renglon.indexOf('=') + 1);
        ArrayList<String> operandos = new ArrayList<>();
        ArrayList<String> operadores = new ArrayList<>();

        StringTokenizer tokens = new StringTokenizer(aux);
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();

            boolean isOperador = false;
            if (operadoresaritmeticos.contains(token))
                isOperador = true;
            if (operadoreslogicos.contains(token))
                isOperador = true;

            if (!(isOperador)) {
                if (token.substring(0, 1).equals("\'") && token.substring(token.length() - 1).equals("\'") && token.length() <= 3) {
                    listaConstantes.add("char");
                    valor = token;
                } else if (token.equals("true") || token.equals("false")) {
                    listaConstantes.add("boolean");
                    valor = token;
                } else {
                    try {
                        if (token.indexOf(".") == -1) {
                            int aux1 = Integer.parseInt(token);
                            listaConstantes.add("int");
                            valor = token;
                        } else {
                            float aux2 = Float.parseFloat(token);
                            listaConstantes.add("float");
                            valor = token;
                        }
                    } catch (Exception e) {
                        operandos.add(token);
                    }
                }
            } else // -Obtener los operadores que se estan utiliando en toda la expresión.
                operadores.add(token);
        }

        for (int i = 0; i < operandos.size(); i++) {
            if (encontrarSimbolo(operandos.get(i))) {
                atributoSimbolos.add(obtenerDatosSimbolo(operandos.get(i)));
            } else {
                String error = "No existe el simbolo [" + operandos.get(i) + "]. Error en la linea:" + numeroRenglon;
                listaErroresSemanticos.add(error);
                System.out.println(error);
            }
        }
        String tipoOperandos = validarTipodeDatosOperandos(atributoSimbolos, numeroRenglon);
        String tipoConstantes = validarTipodeDatosCostantes(atributoSimbolos, listaConstantes, numeroRenglon);
        if (!atributoSimbolos.isEmpty())
            validarTipoOperadores(atributoSimbolos, operadores, numeroRenglon);

        String tipoAsigando = null;
        if (tipoOperandos == null)
            tipoAsigando = tipoConstantes;
        else
            tipoAsigando = tipoOperandos;

        if (!(tipo.equals(tipoOperandos) || tipo.equals(tipoConstantes))) {
            String error = "Se intentó asiganar un tipo de dato [" + tipoAsigando + "] en la varibale [" + var + "] de tipo [" + tipo + "]. Error en la linea: " + numeroRenglon;
            listaErroresSemanticos.add(error);
            System.out.println(error);
        }
        if (listaErroresSemanticos.isEmpty()) {
            if (valor != null)
                tablaSimbolos.get(var).setValor(valor);
            else
                tablaSimbolos.get(var).setValor("Sin valor");
            listaErroresSemanticos.add("La variable " + var + " no ha sido declarada aún (Su valor es null)");
        }
        System.out.println(var + ": " + tablaSimbolos.get(var).getValor());

    }

    public TablaSimbolo obtenerDatosSimbolo(String simbolo) {
        return tablaSimbolos.get(simbolo);
    }

    public String validarTipodeDatosOperandos(ArrayList<TablaSimbolo> atributos, int numeroRenglon) {
        String tipoAnterior = null;
        for (int i = 0; i < atributos.size(); i++) {
            if (tipoAnterior == null)
                tipoAnterior = atributos.get(i).getTipo();
            else if (!atributos.get(i).getTipo().equals(tipoAnterior)) {
                String error = "Existe incompatibilidad de tipo. Error en la linea " + numeroRenglon;
                System.out.println(error);
                listaErroresSemanticos.add(error);
                return null;
            }
        }
        return tipoAnterior;
    }

    public String validarTipodeDatosCostantes(ArrayList<TablaSimbolo> atributos, ArrayList<String> listaConstantes, int numeroRenglon) {
        String tipoAnterior = null;
        for (int i = 0; i < listaConstantes.size(); i++) {
            if (tipoAnterior == null)
                tipoAnterior = listaConstantes.get(i);
            else if (!listaConstantes.get(i).equals(tipoAnterior)) {
                String error = "Existe incompatibilidad de tipo. Error en la linea " + numeroRenglon;
                System.out.println(error);
                listaErroresSemanticos.add(error);
                return null;
            }
        }
        return tipoAnterior;
    }

    public void validarTipoOperadores(ArrayList<TablaSimbolo> atributo, ArrayList<String> operadoresRenglon, int numeroRenglon) {
        String tipo = atributo.get(0).getTipo();
        if (tipo.equals("boolean") || tipo.equals("char")) { //Lógico
            for (int i = 0; i < operadoresRenglon.size(); i++) {
                if (!operadoreslogicos.contains(operadoresRenglon.get(i))) {
                    String error = "El operador " + operadoresRenglon.get(i) + " no coincide con  el tipo de dato. Error en la linea: " + numeroRenglon;
                    listaErroresSemanticos.add(error);
                    System.out.println(error);
                    return;
                }
            }
            return;
        }
        if (tipo.equals("int") || tipo.equals("float")) {
            for (int i = 0; i < operadoresRenglon.size(); i++) {
                if (!operadoresaritmeticos.contains(operadoresRenglon.get(i))) {
                    String error = "El operador " + operadoresRenglon.get(i) + " no coincide con  el tipo de dato. Error en la linea: " + numeroRenglon;
                    listaErroresSemanticos.add(error);
                    System.out.println(error);
                    return;
                }
            }
            return;
        }
    }

    public HashMap<String, TablaSimbolo> getTablaSimbolos() {
        return tablaSimbolos;
    }
}
