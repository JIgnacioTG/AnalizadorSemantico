/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ignacio
 */
public class AnalizadorSemantico {
    
    private static ArrayList<Integer> posError;
    private static ArrayList<String> txtError;
    private static ArrayList<String> variable;
    private static ArrayList<String> tipoVar;
    private static ArrayList<String> valorVar;
    private static ArrayList<String> tokens;
    private static ArrayList<String> valorTokens;
    private static int numIDE, numOA, numPR, numCE, numCF, numOR, numOB;
    
    /**
     * @param codigo
     */
    public static void analizar(String codigo) {
        
        // Objeto para la construcción de strings.
        StringBuilder stb = new StringBuilder();
        
        // Se inicializan las listas.
        posError = new ArrayList<>();
        txtError = new ArrayList<>();
        variable = new ArrayList<>();
        tipoVar = new ArrayList<>();
        valorVar = new ArrayList<>();
        tokens = new ArrayList<>();
        valorTokens = new ArrayList<>();
        numIDE = 0; numOA = 0; numPR = 0; numCE = 0; numCF = 0; numOR = 0; numOB = 0;
        
        // Se dívide el código por lineas.
        String[] lineaCodigo = codigo.split("\\n");
        
        // En caso de que el código se encuentre en una sola línea, se divide con el delimitador.
        if (lineaCodigo.length == 1) {
            lineaCodigo = codigo.split(";");
            
            // Ahora se debe volver a agregar el delimitador
            for (int i = 0; i < lineaCodigo.length; i++) {
                stb = new StringBuilder();
                stb.append(lineaCodigo[i]).append(";");
                
                // Se elimina el espacio en blanco al principio.
                if (i > 0) {
                    stb.deleteCharAt(0);
                }
                lineaCodigo[i] = stb.toString();
            }
            
        }
        
        // La verificación línea por línea empieza en esta parte
        for (int i = 0; i < lineaCodigo.length; i++) {
            
            // Variable que almacena el número de línea.
            int linea = i + 1;
            
            // Ahora la verificación por línea se checa por palabras divididas por espacios.
            String[] palabra = lineaCodigo[i].split("\\s");
            
            // Variable encargada de almacenar la variable base.
            String var = "";
            
            // Variable encargada de guardar el tipo de variable de la línea.
            String tipo = "null";
            
            // Variable encargada de guardar el valor de la variable.
            String valor = "null";
            
            // Bandera para saber si el tipo base ha sido asignado anteriormente.
            Boolean tipoBase = false;
            
            // Bandera para saber si la variable base ha sido asignada anteriormente.
            Boolean varBase = false;
            
            // Bandera para saber que parte del código define el valor de la variable.
            Boolean asignacion = false;
            
            // Bandera para saber si un error fue encontrado.
            Boolean error = false;
            
            for (int j = 0; j < palabra.length; j++) {
                
                // Bandera para saber si el token es nuevo.
                Boolean nuevo = true;
                
                // El tipo de variable base sólo es leída y almacenada con la primera palabra.
                if (j == 0) {
                    
                    // Se declara la expresión regular para encontrar palabras reservadas que definen el tipo.
                    Pattern patron = Pattern.compile("int$|float$|double$");
                    Matcher coincidencia = patron.matcher(palabra[j]);
                    
                    // Sí coincide, el tipo es almacenado y se continua con la siguiente variable.
                    if (coincidencia.matches()) {
                        tipo = palabra[j];
                        tipoBase = true;
                        
                        // Se agrega el token.
                        if (numPR > 0) {
                            // Primero debemos verificar que el token no exista.
                            // t guarda la posición del token a analizar.
                            int t = 0;
                            // tokensReg guarda el total de tokens registrados hasta el momento.
                            int tokensReg = tokens.size();
                            while (t < tokensReg) {
                                
                                // Si el token existe.
                                if (valorTokens.get(t).equals(palabra[j])) {
                                    // Se guarda el token existente.
                                    tokens.add(tokens.get(t));
                                    valorTokens.add(valorTokens.get(t));
                                    nuevo = false;
                                    break;
                                }
                                
                                t++;
                                
                            }
                            
                            if (nuevo) {
                                // Se guarda el nuevo token.
                                numPR++;
                                tokens.add("PR" +numPR);
                                valorTokens.add(palabra[j]);
                            }
                        }
                        // Si no hay ningún token, se registra el primero
                        else {
                            numPR++;
                            tokens.add("PR" +numPR);
                            valorTokens.add(palabra[j]);
                        }
                        
                        
                        continue;
                    }
                }
                
                // Se verifica si se trata de las palabras reservadas do o while.
                Pattern patron = Pattern.compile("do$|while$");
                Matcher coincidencia = patron.matcher(palabra[j]);
                
                // Si coinicide, se verifica la siguiente palabra.
                if (coincidencia.matches()) {
                    
                    // Se agrega el token.
                        if (numPR > 0) {
                            // Primero debemos verificar que el token no exista.
                            // t guarda la posición del token a analizar.
                            int t = 0;
                            // tokensReg guarda el total de tokens registrados hasta el momento.
                            int tokensReg = tokens.size();
                            while (t < tokensReg) {
                                
                                // Si el token existe.
                                if (valorTokens.get(t).equals(palabra[j])) {
                                    // Se guarda el token existente.
                                    tokens.add(tokens.get(t));
                                    valorTokens.add(valorTokens.get(t));
                                    nuevo = false;
                                    break;
                                }
                                
                                t++;
                                
                            }
                            
                            if (nuevo) {
                                // Se guarda el nuevo token.
                                numPR++;
                                tokens.add("PR" +numPR);
                                valorTokens.add(palabra[j]);
                            }
                        }
                        // Si no hay ningún token, se registra el primero
                        else {
                            numPR++;
                            tokens.add("PR" +numPR);
                            valorTokens.add(palabra[j]);
                        }
                    
                    continue;
                }
                
                // Se verifica si es el operador de asignación.
                patron = Pattern.compile("^[=]$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Se activa la bandera que almacena el valor de la variable.
                if (coincidencia.matches()) {
                    asignacion = true;
                    
                    // Se inicia StringBuilder para empezar almacenar el valor de la variable.
                    stb = new StringBuilder();
                    
                    // Se registra el token de asignación.
                    tokens.add("OAS");
                    valorTokens.add("=");
                    
                    continue;
                }
                
                // Se verifica si es el delimitador.
                patron = Pattern.compile("^[;]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Se registra el token de delimitador.
                    tokens.add("DEL");
                    valorTokens.add(";");
                    
                    continue;
                }
                
                // Se verifica si es un parentesis.
                patron = Pattern.compile("[(]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Se registra el token de parentesis.
                    tokens.add("PAR1");
                    valorTokens.add("(");
                    
                    continue;
                }
                
                // Se verifica si es un parentesis.
                patron = Pattern.compile("[)]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Se registra el token de parentesis.
                    tokens.add("PAR2");
                    valorTokens.add(")");
                    
                    continue;
                }
                
                // Se verifica si es un corchete.
                patron = Pattern.compile("[{]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Se registra el token de delimitador.
                    tokens.add("COR1");
                    valorTokens.add("{");
                    
                    continue;
                }
                
                // Se verifica si es un corchete.
                patron = Pattern.compile("[}]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Se registra el token de delimitador.
                    tokens.add("COR2");
                    valorTokens.add("}");
                    
                    continue;
                }
                
                // Se verifica si se trata de una constante entera.
                patron = Pattern.compile("^[-]?(\\d)+$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Se activa la bandera que almacena la constante entera.
                if (coincidencia.matches()) {
                    
                    // Al tratarse de una asignación, la constante se almacena.
                    if (asignacion) {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Se agrega el token.
                        if (numCE > 0) {
                            // Primero debemos verificar que el token no exista.
                            // t guarda la posición del token a analizar.
                            int t = 0;
                            // tokensReg guarda el total de tokens registrados hasta el momento.
                            int tokensReg = tokens.size();
                            while (t < tokensReg) {
                                
                                // Si el token existe.
                                if (valorTokens.get(t).equals(palabra[j])) {
                                    // Se guarda el token existente.
                                    tokens.add(tokens.get(t));
                                    valorTokens.add(valorTokens.get(t));
                                    nuevo = false;
                                    break;
                                }
                                
                                t++;
                                
                            }
                            
                            if (nuevo) {
                                // Se guarda el nuevo token.
                                numCE++;
                                tokens.add("CE" +numCE);
                                valorTokens.add(palabra[j]);
                            }
                        }
                        // Si no hay ningún token, se registra el primero
                        else {
                            numCE++;
                            tokens.add("CE" +numCE);
                            valorTokens.add(palabra[j]);
                        }
                    
                    continue;
                }
                
                // Se verifica si se trata de una constante flotante.
                patron = Pattern.compile("^[-]?(\\d)+(.(\\d)+)$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Se activa la bandera que almacena la constante flotante.
                if (coincidencia.matches()) {
                    
                    // Se verifica que si existe incompatibilidad de tipos.
                    if (tipo.equalsIgnoreCase("int")) {
                        posError.add(linea);
                        txtError.add("Incompatibilidad de tipos: " +var+ " es un int y " +palabra[j]+ " es un float.");
                        
                        error = true;
                    }
                    
                    // En caso contrario, almacenar la constante flotante.
                    else {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Se agrega el token.
                        if (numCF > 0) {
                            // Primero debemos verificar que el token no exista.
                            // t guarda la posición del token a analizar.
                            int t = 0;
                            // tokensReg guarda el total de tokens registrados hasta el momento.
                            int tokensReg = tokens.size();
                            while (t < tokensReg) {
                                
                                // Si el token existe.
                                if (valorTokens.get(t).equals(palabra[j])) {
                                    // Se guarda el token existente.
                                    tokens.add(tokens.get(t));
                                    valorTokens.add(valorTokens.get(t));
                                    nuevo = false;
                                    break;
                                }
                                
                                t++;
                                
                            }
                            
                            if (nuevo) {
                                // Se guarda el nuevo token.
                                numCF++;
                                tokens.add("CF" +numCF);
                                valorTokens.add(palabra[j]);
                            }
                        }
                        // Si no hay ningún token, se registra el primero
                        else {
                            numCF++;
                            tokens.add("CF" +numCF);
                            valorTokens.add(palabra[j]);
                        }
                    
                    continue;
                }
                
                // Se verifica si se trata de un operador aritmético.
                patron = Pattern.compile("^[+-/*]$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Se activa la bandera que almacena al operador aritmético.
                if (coincidencia.matches()) {
                    
                    // Al tratarse de una asignación, el operador aritmético se almacena.
                    if (asignacion) {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Se agrega el token.
                        if (numOA > 0) {
                            // Primero debemos verificar que el token no exista.
                            // t guarda la posición del token a analizar.
                            int t = 0;
                            // tokensReg guarda el total de tokens registrados hasta el momento.
                            int tokensReg = tokens.size();
                            while (t < tokensReg) {
                                
                                // Si el token existe.
                                if (valorTokens.get(t).equals(palabra[j])) {
                                    // Se guarda el token existente.
                                    tokens.add(tokens.get(t));
                                    valorTokens.add(valorTokens.get(t));
                                    nuevo = false;
                                    break;
                                }
                                
                                t++;
                                
                            }
                            
                            if (nuevo) {
                                // Se guarda el nuevo token.
                                numOA++;
                                tokens.add("OA" +numOA);
                                valorTokens.add(palabra[j]);
                            }
                        }
                        // Si no hay ningún token, se registra el primero
                        else {
                            numOA++;
                            tokens.add("OA" +numOA);
                            valorTokens.add(palabra[j]);
                        }
                    
                    continue;
                }
                
                // Se verifica si se trata de un operador relacional.
                patron = Pattern.compile("([<>][=]?)$|([!=][=])$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Se activa la bandera que almacena al operador relacional.
                if (coincidencia.matches()) {
                    
                    // Al tratarse de una asignación, el operador relacional se almacena.
                    if (asignacion) {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Se agrega el token.
                        if (numOR > 0) {
                            // Primero debemos verificar que el token no exista.
                            // t guarda la posición del token a analizar.
                            int t = 0;
                            // tokensReg guarda el total de tokens registrados hasta el momento.
                            int tokensReg = tokens.size();
                            while (t < tokensReg) {
                                
                                // Si el token existe.
                                if (valorTokens.get(t).equals(palabra[j])) {
                                    // Se guarda el token existente.
                                    tokens.add(tokens.get(t));
                                    valorTokens.add(valorTokens.get(t));
                                    nuevo = false;
                                    break;
                                }
                                
                                t++;
                                
                            }
                            
                            if (nuevo) {
                                // Se guarda el nuevo token.
                                numOR++;
                                tokens.add("OR" +numOR);
                                valorTokens.add(palabra[j]);
                            }
                        }
                        // Si no hay ningún token, se registra el primero
                        else {
                            numOR++;
                            tokens.add("OR" +numOR);
                            valorTokens.add(palabra[j]);
                        }
                    
                    continue;
                }
                
                // Se verifica si se trata de un operador booleano.
                patron = Pattern.compile("([&][&])$|([|][|])$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Se activa la bandera que almacena al operador booleano
                if (coincidencia.matches()) {
                    
                    // Al tratarse de una asignación, el operador booleano se almacena.
                    if (asignacion) {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Se agrega el token.
                        if (numOB > 0) {
                            // Primero debemos verificar que el token no exista.
                            // t guarda la posición del token a analizar.
                            int t = 0;
                            // tokensReg guarda el total de tokens registrados hasta el momento.
                            int tokensReg = tokens.size();
                            while (t < tokensReg) {
                                
                                // Si el token existe.
                                if (valorTokens.get(t).equals(palabra[j])) {
                                    // Se guarda el token existente.
                                    tokens.add(tokens.get(t));
                                    valorTokens.add(valorTokens.get(t));
                                    nuevo = false;
                                    break;
                                }
                                
                                t++;
                                
                            }
                            
                            if (nuevo) {
                                // Se guarda el nuevo token.
                                numOB++;
                                tokens.add("OB" +numOB);
                                valorTokens.add(palabra[j]);
                            }
                        }
                        // Si no hay ningún token, se registra el primero
                        else {
                            numOB++;
                            tokens.add("OB" +numOB);
                            valorTokens.add(palabra[j]);
                        }
                    
                    continue;
                }
                
                // Se verifica si es una variable.
                patron = Pattern.compile("^[A-Za-z_$][\\w$]*$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Sí coincide se asigna el tipo base si no ha sido declarado antes.
                if (coincidencia.matches()) {
                    
                    // Se verifica si esta es la variable base de la línea, de ser así, se almacena.
                    if (tipoBase && !varBase) {
                        var = palabra[j];
                        varBase = true;
                    }
                    
                    // Bandera indicadora de si se pasa a la siguiente variable.
                    Boolean salto = false;
                    
                    // Variable donde se almacena el tipo de la variable que se analiza.
                    String tipoPalabra = tipo;
                    
                    // Variable donde se almacena el valor de la variable que se analiza.
                    String valorPalabra = valor;
                    
                    // Se busca si la variable no ha sido registrada antes
                    for (int k = 0; k < variable.size(); k++) {
                        
                        // Si ha sido registrada antes.
                        if (variable.get(k).equals(palabra[j])) {
                            
                            // Se obtiene y almacena el tipo de la variable.
                            tipoPalabra = tipoVar.get(k);
                            
                            // Se analiza si la variable ha sido declarada previamente.
                            if (!tipo.equalsIgnoreCase("null") && !asignacion) {
                                posError.add(linea);
                                txtError.add("La variable " + palabra[j] + " ha sido declarada previamente.");

                                error = true;
                            }
                            
                            // Se obtiene y almacena el valor de la variable.
                            valorPalabra = valorVar.get(k);
                            
                            // Se indica que la palabra se ha encontrado.
                            salto = true;
                            break;
                        }
                        
                    }
                    
                    // Se verifica de nuevo si esta es la variable de la base de línea pero sin declaración.
                    if (j == 0) {
                        var = palabra[j];
                        tipo = tipoPalabra;
                        varBase = true;
                    }
                    
                    // Se analiza si la variable no ha sido declarada.
                    if (tipoPalabra.equalsIgnoreCase("null")) {
                        posError.add(linea);
                        txtError.add("Variable " + palabra[j] + " no declarada.");
                        
                        error = true;
                    }
                    
                    // Se analiza si se esta intentando asignar una variable incompatible o no inicializada.
                    if (asignacion) {
                        
                        // Se analiza si el tipo base no es compatible con el tipo que se analiza.
                        if (tipo.equalsIgnoreCase("int") && (tipoPalabra.equalsIgnoreCase("double") || tipoPalabra.equalsIgnoreCase("float"))) {
                            posError.add(linea);
                            txtError.add("Incompatibilidad de tipos: " +var+ " es un int y " +palabra[j]+ " es " +tipoPalabra+ ".");

                            error = true;
                        }
                        
                        // Se analiza si la variable no se encuentra inicializada.
                        if (valorPalabra.equalsIgnoreCase("null")) {
                            posError.add(linea);
                            txtError.add("Variable " + palabra[j] + " no inicializada.");

                            error = true;
                        }
                        
                        // Si ningún error fue encontrado, se almacena en stb para después asignar el valor a la variable.
                        if (!error) {
                            stb.append(palabra[j]).append(" ");
                        }
                    }
                    
                    // Se agrega el token.
                        if (numIDE > 0) {
                            // Primero debemos verificar que el token no exista.
                            // t guarda la posición del token a analizar.
                            int t = 0;
                            // tokensReg guarda el total de tokens registrados hasta el momento.
                            int tokensReg = tokens.size();
                            while (t < tokensReg) {
                                
                                // Si el token existe.
                                if (valorTokens.get(t).equals(palabra[j])) {
                                    // Se guarda el token existente.
                                    tokens.add(tokens.get(t));
                                    valorTokens.add(valorTokens.get(t));
                                    nuevo = false;
                                    break;
                                }
                                
                                t++;
                                
                            }
                            
                            if (nuevo) {
                                // Se guarda el nuevo token.
                                numIDE++;
                                tokens.add("IDE" +numIDE);
                                valorTokens.add(palabra[j]);
                            }
                        }
                        // Si no hay ningún token, se registra el primero
                        else {
                            numIDE++;
                            tokens.add("IDE" +numIDE);
                            valorTokens.add(palabra[j]);
                        }
                    
                    // Se verifica la siguiente palabra si no es necesario agregar.
                    if (salto) {
                        continue;
                    }
                    
                    // La variable se registra
                    variable.add(palabra[j]);
                    tipoVar.add(tipoPalabra);
                    valorVar.add(valorPalabra);
                    
                }
                
            }
            
            // Si la línea tuvo una asignación, esta se agrega en esta fase.
            if (asignacion) {
                
                // Si la línea no tuvo ningún error, se procede a la asignación.
                if (!error) {
                    
                    // Primero debemos encontrar la posición de la variable a asignar.
                    for (int j = 0; j < variable.size(); j++) {
                        
                        // Al encontrar la variable, se le asigna su nuevo valor.
                        if (variable.get(j).equalsIgnoreCase(var)) {
                            valorVar.set(j, stb.toString());
                            break;
                        }
                    }
                }
            }
        }
    }
    
    // Método para obtener las variables.
    public static String[] obtenerVariables() {
        
        // Número de variables registradas.
        int tamanio = variable.size();
        
        // Arreglo que almacena todas las variables.
        String[] variables = new String[tamanio];
        
        // Se recorre el ArrayList de variables.
        for (int i = 0; i < tamanio; i++) {
            variables[i] = variable.get(i);
        }
        
        // Se regresan todas las variables.
        return variables;
    }
    
    // Método para obtener el número de variables.
    public static int obtenerNumVariables() {
        return variable.size();
    }
    
    // Método para obtener el número de errores.
    public static int obtenerNumErrores() {
        return txtError.size();
    }
    
    // Método para obtener el número de tokens.
    public static int obtenerNumTokens() {
        return tokens.size();
    }
    
    // Método para obtener los tipos de variables.
    public static String[] obtenerTipoVar() {
        
        // Número de variables registradas.
        int tamanio = tipoVar.size();
        
        // Arreglo que almacena todas las variables.
        String[] tipos = new String[tamanio];
        
        // Se recorre el ArrayList de variables.
        for (int i = 0; i < tamanio; i++) {
            tipos[i] = tipoVar.get(i);
        }
        
        // Se regresan todas las variables.
        return tipos;
    }
    
    // Método para obtener los valores de las variables.
    public static String[] obtenerValoresVar() {
        
        // Número de variables registradas.
        int tamanio = valorVar.size();
        
        // Arreglo que almacena todas las variables.
        String[] valores = new String[tamanio];
        
        // Se recorre el ArrayList de variables.
        for (int i = 0; i < tamanio; i++) {
            valores[i] = valorVar.get(i);
        }
        
        // Se regresan todas las variables.
        return valores;
    }
    
    // Método para la posición del error.
    public static int[] obtenerPosError() {
        
        // Número de variables registradas.
        int tamanio = posError.size();
        
        // Arreglo que almacena todas las variables.
        int[] posiciones = new int[tamanio];
        
        // Se recorre el ArrayList de variables.
        for (int i = 0; i < tamanio; i++) {
            posiciones[i] = posError.get(i);
        }
        
        // Se regresan todas las variables.
        return posiciones;
    }
    
    // Método para obtener el texto de los errores.
    public static String[] obtenerTxtError() {
        
        // Número de variables registradas.
        int tamanio = txtError.size();
        
        // Arreglo que almacena todas las variables.
        String[] errores = new String[tamanio];
        
        // Se recorre el ArrayList de variables.
        for (int i = 0; i < tamanio; i++) {
            errores[i] = txtError.get(i);
        }
        
        // Se regresan todas las variables.
        return errores;
    }
    
    // Método para obtener los tokens.
    public static String[] obtenerTokens() {
        
        // Número de tokens registrados.
        int tamanio = tokens.size();
        
        // Arreglo que almacena todos los tokens.
        String[] tokensArray = new String[tamanio];
        
        // Se recorre el ArrayList de tokens.
        for (int i = 0; i < tamanio; i++) {
            tokensArray[i] = tokens.get(i);
        }
        
        // Se regresan todas las tokens.
        return tokensArray;
    }
    
    // Método para obtener los valores de los tokens.
    public static String[] obtenerValoresTokens() {
        
        // Número de valores de tokens registrados.
        int tamanio = valorTokens.size();
        
        // Arreglo que almacena todas las variables.
        String[] valorTokensArray = new String[tamanio];
        
        // Se recorre el ArrayList de variables.
        for (int i = 0; i < tamanio; i++) {
            valorTokensArray[i] = valorTokens.get(i);
        }
        
        // Se regresan todas las variables.
        return valorTokensArray;
    }
    
    // Método para guardar el archivo de texto.
    public static void guardarArchivo(String archivo, String texto) {
        
        try (FileWriter fw = new FileWriter(archivo)) {
            fw.write(texto);
        }
        catch (IOException ex) {
            System.out.println("No se pudo guardar el archivo.");
        }
    }
    
    // Metodo encargado de la optimización del código.
    public static void optimizarCodigo() {
        
        // Objeto para la construcción de strings.
        StringBuilder stb = new StringBuilder();
        
        // Se recorren los tokens
    }
    
}
