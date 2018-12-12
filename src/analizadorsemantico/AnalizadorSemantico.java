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
 * @author LENOVO
 */
public class AnalizadorSemantico {
    
    private static ArrayList<Integer> posError;
    private static ArrayList<String> IDEUtil;
    private static ArrayList<String> txtError;
    private static ArrayList<String> variable;
    private static ArrayList<String> tipoVar;
    private static ArrayList<String> valorVar;
    private static ArrayList<String> tokens;
    private static ArrayList<String> valorTokens;
    private static ArrayList<String> optTokens;
    private static ArrayList<String> optValores;
    private static int numIDE, numOA, numPR, numCE, numCF, numOR, numOB;
    private static String ultimoIDE;
    private static String saltoLinea;
    private static Triplo triplo;
    
    /**
     * @param codigo
     */
    public static void analizar(String codigo) {
        
        // Objeto para la construcción de strings.
        StringBuilder stb = new StringBuilder();
        
        // Se inicializan las listas.
        posError = new ArrayList<>();
        IDEUtil = new ArrayList<>();
        txtError = new ArrayList<>();
        variable = new ArrayList<>();
        tipoVar = new ArrayList<>();
        valorVar = new ArrayList<>();
        tokens = new ArrayList<>();
        valorTokens = new ArrayList<>();
        optTokens = new ArrayList<>();
        optValores = new ArrayList<>();
        numIDE = 0; numOA = 0; numPR = 0; numCE = 0; numCF = 0; numOR = 0; numOB = 0;
        ultimoIDE = "";
        saltoLinea = System.getProperty("line.separator");
        
        // Variable que almacena la posición.
        int pos = 0;
        
        // Antes de iniciar, se deben eliminar los tabs del código para evitar errores adicionales.
        codigo = codigo.replace("\t", "");
        
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
            
            // Bandera para saber si se manejo un ciclo do-while.
            Boolean ciclo = false;
            
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
                        
                        pos++;
                        continue;
                    }
                }
                
                // Se verifica si se trata de las palabras reservadas do o while.
                Pattern patron = Pattern.compile("do$|while$");
                Matcher coincidencia = patron.matcher(palabra[j]);
                
                // Si coinicide, se verifica la siguiente palabra.
                if (coincidencia.matches()) {
                    
                    // La bandera se activa.
                    ciclo = true;
                    
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
                    
                        pos++;
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
                    
                    pos++;
                    continue;
                }
                
                // Se verifica si es el delimitador.
                patron = Pattern.compile("^[;]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Se registra el token de delimitador.
                    tokens.add("DEL");
                    valorTokens.add(";");
                    
                    pos++;
                    continue;
                }
                
                // Se verifica si es un parentesis.
                patron = Pattern.compile("[(]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Al tratarse de una asignación, el parentesis se almacena.
                    if (asignacion) {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Se registra el token de parentesis.
                    tokens.add("PAR1");
                    valorTokens.add("(");
                    
                    pos++;
                    continue;
                }
                
                // Se verifica si es un parentesis.
                patron = Pattern.compile("[)]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Al tratarse de una asignación, el parentesis se almacena.
                    if (asignacion) {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Se registra el token de parentesis.
                    tokens.add("PAR2");
                    valorTokens.add(")");
                    
                    pos++;
                    continue;
                }
                
                // Se verifica si es un corchete.
                patron = Pattern.compile("[{]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Se registra el token de delimitador.
                    tokens.add("COR1");
                    valorTokens.add("{");
                    
                    pos++;
                    continue;
                }
                
                // Se verifica si es un corchete.
                patron = Pattern.compile("[}]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Se registra el token de delimitador.
                    tokens.add("COR2");
                    valorTokens.add("}");
                    
                    pos++;
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
                    
                    pos++;
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
                    
                    pos++;
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
                    
                    pos++;
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
                    
                    pos++;
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
                    
                    pos++;
                    continue;
                }
                
                // Se verifica si es una variable.
                patron = Pattern.compile("^[A-Za-z_$][\\w$]*$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Sí coincide se asigna el tipo base si no ha sido declarado antes.
                if (coincidencia.matches()) {
                    
                    // Variable donde se almacena el tipo de la variable que se analiza.
                    String tipoPalabra = "null";
                    
                    // Se verifica si esta es la variable base de la línea, de ser así, se almacena.
                    if (tipoBase && !varBase) {
                        var = palabra[j];
                        tipoPalabra = tipo;
                        varBase = true;
                    }
                    
                    // Bandera indicadora de si se pasa a la siguiente variable.
                    Boolean salto = false;
                    
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
                                
                                // Se debe registrar la posición de esta variable que esta siendo utilizada en una asignación.
                                if (asignacion || ciclo) {
                                    IDEUtil.add(tokens.get(t));
                                }
                                // Pero si se trata de una variable que no se esta asignando o no esta en un ciclo.
                                else if (!asignacion && !ciclo) {
                                    // Se guarda, al final se conserva la ultima.
                                    ultimoIDE = tokens.get(t);
                                    System.out.println(ultimoIDE);
                                }
                            
                                break;
                            }
                                
                            t++;
                                
                        }
                            
                        if (nuevo) {
                            // Se guarda el nuevo token.
                            numIDE++;
                            tokens.add("IDE" +numIDE);
                            valorTokens.add(palabra[j]);
                            // Pero si se trata de una variable que no se esta asignando o no esta en un ciclo.
                            if (!asignacion && !ciclo) {
                                // Se guarda, al final se conserva la ultima.
                                ultimoIDE = "IDE" +numIDE;
                                System.out.println(ultimoIDE);
                            }
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
                        pos++;
                        continue;
                    }
                    
                    // La variable se registra
                    variable.add(palabra[j]);
                    tipoVar.add(tipoPalabra);
                    valorVar.add(valorPalabra);
                    
                    pos++;
                    
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
        
        // Se agrega como utilizado el ultimo IDE.
        IDEUtil.add(ultimoIDE);
        
        // Ahora el código se optimiza
        optimizarCodigo();
        
        // Se genera la tripleta
        generarTripleta();
        
        // Se genera el ensamblador
        generarEnsamblador();
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
        
        // Listas temporales para tokens optimizados.
        optTokens = new ArrayList<>();
        optValores = new ArrayList<>();
        
        // Se recorren los tokens
        int tamanioTokens = tokens.size();
        
        for (int i = 0; i < tamanioTokens; i++) {
            String actualToken = tokens.get(i);
            String actualValor = valorTokens.get(i);
            String preToken = "";
            String posToken = "";
            Boolean ultimo = true;
            
            // Se guarda el token anterior necesario para algunas comprobaciones.
            if (i > 0) {
                preToken = tokens.get(i-1);
            }
            
            // Se guarda el token posterior necesario para algunas comprobaciones.
            if (i < tamanioTokens-1) {
                posToken = tokens.get(i+1);
                ultimo = false;
            }
            
            // Si hay un delimitador.
            if (actualToken.equalsIgnoreCase("DEL")) {
                
                // Si no es el último token.
                if (!ultimo) {
                    
                    // Verificar si el siguiente token es un corchete.
                    if (posToken.equalsIgnoreCase("COR2")) {
                        
                        // Se agrega el delimitador tal cual.
                        stb.append(actualValor);
                        optTokens.add(actualToken);
                        optValores.add(actualValor);
                    }
                    
                    // Si no es un corchete
                    else {
                        
                        // Se agrega el delimitador junto a un salto de línea.
                        stb.append(actualValor).append(saltoLinea);
                        optTokens.add(actualToken);
                        optValores.add(actualValor);
                    }
                }
                
                // Si es el último token.
                else {
                    
                    // Se agrega tal cual el delimitador.
                    stb.append(actualValor);
                    optTokens.add(actualToken);
                    optValores.add(actualValor);
                }
            }
            
            // Si hay una palabra reservada
            else if (actualToken.contains("PR")) {
                
                // Si el siguiente es un IDE
                if (posToken.contains("IDE")) {
                    
                    // Bandera para eliminación de código
                    Boolean eliminar = true;

                    // Se debe verificar si este IDE es utilizado
                    for (int j = 0; j < IDEUtil.size(); j++) {

                        // Si el IDE ha sido utilizado, se marca la bandera de Eliminar como falsa.
                        if (posToken.equalsIgnoreCase(IDEUtil.get(j))) {
                            eliminar = false;
                            break;
                        }
                    }

                    // Si la variable se debe eliminar
                    if (eliminar) {
                        
                        // Se salta la lectura de la PR
                        i++;
                        
                        // Mientras no se llegue a un delimitador, saltar tokens.
                        while (!tokens.get(i).equalsIgnoreCase("DEL")) {
                            i++;
                        }

                        // En este caso el valor no sera añadido asi que se continua con el ciclo.
                        continue;
                    }
                    
                }
                
                // Si el siguiente token es un parentesis inicial.
                if (posToken.equalsIgnoreCase("PAR1")) {
                    // Si el siguiente es un IDE
                    if (tokens.get(i+2).contains("IDE")) {

                        // Bandera para eliminación de código
                        Boolean eliminar = true;

                        // Se debe verificar si este IDE es utilizado
                        for (int j = 0; j < IDEUtil.size(); j++) {

                            // Si el IDE ha sido utilizado, se marca la bandera de Eliminar como falsa.
                            if (tokens.get(i+2).equalsIgnoreCase(IDEUtil.get(j))) {
                                eliminar = false;
                                break;
                            }
                        }

                        // Si la variable se debe eliminar
                        if (eliminar) {

                            // Se salta la lectura de la PR y PAR
                            i+=2;

                            // Mientras no se llegue a un delimitador, saltar tokens.
                            while (!tokens.get(i).equalsIgnoreCase("DEL")) {
                                i++;
                            }

                            // En este caso el valor no sera añadido asi que se continua con el ciclo.
                            continue;
                        }

                    }
                }
                
                // Si el anterior token es un parentesis
                if (preToken.equalsIgnoreCase("PAR2")) {
                    
                    // Se realiza un salto de línea
                    stb.append(saltoLinea);
                    optTokens.add(actualToken);
                    optValores.add(actualValor);
                }
                
                // Se escribe la palabra del token y un espacio.
                stb.append(actualValor).append(" ");
                optTokens.add(actualToken);
                optValores.add(actualValor);
                
            }
            
            // Si hay un corchete inicial
            else if (actualToken.equalsIgnoreCase("COR1")) {
                
                // Se escribe un salto de línea y se escribe el corchete inicial.
                stb.append(saltoLinea);
                stb.append(actualValor);
                optTokens.add(actualToken);
                optValores.add(actualValor);
            }
            
            // Si hay un corchete final
            else if (actualToken.equalsIgnoreCase("COR2")) {
                
                // Se escribe el corchete final y un salto de línea.
                stb.append(actualValor);
                stb.append(saltoLinea);
                optTokens.add(actualToken);
                optValores.add(actualValor);
            }
            
            // Si hay un identificador
            else if (actualToken.contains("IDE")) {
                
                // Bandera para eliminación de código
                Boolean eliminar = true;
                
                // Se debe verificar si este IDE es utilizado
                for (int j = 0; j < IDEUtil.size(); j++) {
                    
                    // Si el IDE ha sido utilizado, se marca la bandera de Eliminar como falsa.
                    if (actualToken.equalsIgnoreCase(IDEUtil.get(j))) {
                        eliminar = false;
                        break;
                    }
                }
                
                // Si la variable se debe eliminar
                if (eliminar) {
                    
                    // Mientras no se llegue a un delimitador, saltar tokens.
                    while (!tokens.get(i).equalsIgnoreCase("DEL")) {
                        i++;
                    }
                    
                    // En este caso el valor no sera añadido asi que se continua con el ciclo.
                    continue;
                }
                
                stb.append(actualValor);
                optTokens.add(actualToken);
                optValores.add(actualValor);
            }
            
            // Con cualquier otro token, se escribe tal cual.
            else {
                stb.append(actualValor);
                optTokens.add(actualToken);
                optValores.add(actualValor);
            }
            
            
        }
        
        // Se guardan los tokens optimizados
        tokens = optTokens;
        valorTokens = optValores;
        
        // Se guarda el código optimizado
        Archivo.guardar("Codigo Optimizado.txt", stb.toString());
        
    }
    
    public static void generarTripleta () {
        
        // Indica el numero de instruccion.
        int numeroinstruccion = 1;
        
        // Numero de temporal
        int numerotemporal = 1;
        
        // Numero de condicion
        int numwhile = 1;
        
        // Variables que almacenaran la tripleta
        ArrayList<Integer> numeroIns = new ArrayList<>();
        ArrayList<String> objeto = new ArrayList<>();
        ArrayList<String> fuente = new ArrayList<>();
        ArrayList<String> operador = new ArrayList<>();
        ArrayList<Integer> posOperacion = new ArrayList<>();
        ArrayList<Integer> posComparacion = new ArrayList<>();
        ArrayList<Integer> numinsdo = new ArrayList<>();
        
        // se verifican los tokens uno por uno.
        for (int i = 0; i < tokens.size(); i++) {
            
            // cuando es una palabra reservada
            if(tokens.get(i).contains("PR")) {
                
                // si es un "do" se registra.
                if (valorTokens.get(i).equalsIgnoreCase("do")) {
                    posOperacion.add(numeroinstruccion);
                    numinsdo.add(numeroinstruccion);
                }
                
                // si es un "while".
                else if (valorTokens.get(i).equalsIgnoreCase("while")) {
                    
                    // se registra
                    posComparacion.add(numeroinstruccion);
                    
                    // Se guarda en la tripleta la variable a comprobar
                    numeroIns.add(numeroinstruccion);
                    objeto.add("T" +numerotemporal);
                    fuente.add(valorTokens.get(i+2));
                    operador.add("=");
                    numeroinstruccion++;
                    
                    // Se analiza con que se debe comparar
                    numeroIns.add(numeroinstruccion);
                    objeto.add("T" +numerotemporal);
                    fuente.add(valorTokens.get(i+4));
                    operador.add(valorTokens.get(i+3));
                    numeroinstruccion++;
                    
                    // Si es verdadero, debe continuar con la siguiente instruccion.
                    int sigIns = numeroinstruccion + 2;
                    numeroIns.add(numeroinstruccion);
                    objeto.add("Tr" +numwhile);
                    fuente.add("TRUE");
                    operador.add(numinsdo.remove(numinsdo.size()-1)+"");
                    numeroinstruccion++;
                    
                    // Si es falso, se debe regresar al anterior do
                    numeroIns.add(numeroinstruccion);
                    objeto.add("Tr" +numwhile);
                    fuente.add("FALSE");
                    operador.add(sigIns+"");
                    numeroinstruccion++;
                    numerotemporal++;
                    numwhile++;
                    
                }
                
                else {
                    continue;
                }
                
            }
            
            // Si el token es un operador de asignación.
            if(tokens.get(i).contains("OAS")) {
                
                // Se considera si hay un delimitador despúes de su siguiente token
                if (tokens.get(i+2).equalsIgnoreCase("DEL")) {
                    
                    // De ser así estamos ante una asignación simple y la tripleta.
                    numeroIns.add(numeroinstruccion);
                    objeto.add(valorTokens.get(i-1));
                    fuente.add(valorTokens.get(i+1));
                    operador.add("=");
                    numeroinstruccion++;
                }
                
                // En este caso, tenemos una probable operación matemática.
                else {
                    
                    // Variable a la que se asignará el resultado final.
                    String variableAsig = valorTokens.get(i-1);
                    
                    // se almacenan los tokens de la operacion.
                    ArrayList<String> tokensOperacion = new ArrayList<>();
                    
                    // Saltamos el guardado del operador de asignación.
                    i++;
                    
                    while (!tokens.get(i).equalsIgnoreCase("DEL")) {
                        tokensOperacion.add(valorTokens.get(i));
                        i++;
                    }
                    
                    // Se va recorriendo la lista hasta que no queden variables por comparar
                    while (tokensOperacion.size() > 1) {
                        
                        // Si hay un parentesis en la operación.
                        if (tokensOperacion.indexOf(")") != -1) {
                            
                            // Se almacenan las posiciones.
                            int posFin = tokensOperacion.indexOf(")");
                            int posIni = posFin - 2;
                            
                            // Se revisa que antes dos tokens anteriores no sean "(".
                            if (!tokensOperacion.get(posIni).equalsIgnoreCase("(")) {
                                
                                // Se busca el inicio de este parentesis
                                for (int n = posFin; n >= 0; n--) {
                                    
                                    // Si se encuentra el parentesis.
                                    if (tokensOperacion.get(n).equalsIgnoreCase("(")) {
                                        // Se guarda la posicion.
                                        posIni = n;
                                        
                                        // Se termina el ciclo.
                                        break;
                                    }
                                }
                                
                                // Se guarda la operacion que se encuentra dentro del parentesis.
                                ArrayList<String> tokensParentesis = new ArrayList<>();
                                
                                // Se recorre el arreglo hasta que encontremos el final del parentesis.
                                int posReemplazo = posIni + 1;
                                while(!tokensOperacion.get(posReemplazo+1).equalsIgnoreCase(")")) {
                                    // Se guarda la operacion
                                    tokensParentesis.add(tokensOperacion.remove(posReemplazo));
                                    // Si es el último
                                    if (tokensOperacion.get(posReemplazo+1).equalsIgnoreCase(")")) {
                                        tokensParentesis.add(tokensOperacion.get(posReemplazo));
                                    }
                                }
                                
                                // Se analiza lo que se encuentra adentro del parentesis.
                                while (tokensParentesis.size() > 1) {
                                    
                                    // Si hay una multiplicación en la operación.
                                    if (tokensParentesis.lastIndexOf("*") != -1) {

                                        // Se almacena la penúltima variable en el triplo y se elimina de la lista.
                                        numeroIns.add(numeroinstruccion);
                                        objeto.add("T" +numerotemporal);
                                        fuente.add(tokensParentesis.remove(tokensParentesis.lastIndexOf("*")-1));
                                        operador.add("=");
                                        numeroinstruccion++;

                                        // En el triplo se almacena la operacion realizada.
                                        numeroIns.add(numeroinstruccion);
                                        objeto.add("T" +numerotemporal);
                                        fuente.add(tokensParentesis.remove(tokensParentesis.lastIndexOf("*")+1));
                                        operador.add(tokensParentesis.get(tokensParentesis.lastIndexOf("*")));
                                        numeroinstruccion++;

                                        // Se agrega a la lista el triplo realizado.
                                        tokensParentesis.set(tokensParentesis.lastIndexOf("*"),"T" +numerotemporal);

                                        // Se aumenta el contador del triplo
                                        numerotemporal++;

                                        continue;
                                    }

                                    // Si hay una division en la operación.
                                    else if (tokensParentesis.lastIndexOf("/") != -1) {

                                        // Se almacena la antepenúltima variable en el triplo y se elimina de la lista.
                                        numeroIns.add(numeroinstruccion);
                                        objeto.add("T" +numerotemporal);
                                        fuente.add(tokensParentesis.remove(tokensParentesis.lastIndexOf("/")-1));
                                        operador.add("=");
                                        numeroinstruccion++;

                                        // En el triplo se almacena la operacion realizada.
                                        numeroIns.add(numeroinstruccion);
                                        objeto.add("T" +numerotemporal);
                                        fuente.add(tokensParentesis.remove(tokensParentesis.lastIndexOf("/")+1));
                                        operador.add(tokensParentesis.get(tokensParentesis.lastIndexOf("/")));
                                        numeroinstruccion++;

                                        // Se agrega a la lista el triplo realizado.
                                        tokensParentesis.set(tokensParentesis.lastIndexOf("/"),"T" +numerotemporal);

                                        // Se aumenta el contador del triplo
                                        numerotemporal++;

                                        continue;
                                    }

                                    // Si solo quedan sumas y restas.
                                    else {

                                        // Se almacena la antepenúltima variable en el triplo y se elimina de la lista.
                                        numeroIns.add(numeroinstruccion);
                                        objeto.add("T" +numerotemporal);
                                        fuente.add(tokensParentesis.remove(tokensParentesis.size()-3));
                                        operador.add("=");
                                        numeroinstruccion++;

                                        // En el triplo se almacena la operacion realizada.
                                        numeroIns.add(numeroinstruccion);
                                        objeto.add("T" +numerotemporal);
                                        fuente.add(tokensParentesis.remove(tokensParentesis.size()-1));
                                        operador.add(tokensParentesis.remove(tokensParentesis.size()-1));
                                        numeroinstruccion++;

                                        // Se agrega a la lista el triplo realizado.
                                        tokensParentesis.add("T" +numerotemporal);

                                        // Se aumenta el contador del triplo
                                        numerotemporal++;
                                    }
                                    
                                }
                                
                                // Ahora la variable temporal restante se pasa al tokensOperacion.
                                tokensOperacion.set(posReemplazo, tokensParentesis.get(0));
                                
                                // El ciclo analiza la siguiente variable.
                                continue;
                            }
                            
                            // En este caso, la operación no tiene mas operaciones alrededor
                            else {
                                
                                // Primero debemos comprobar que no estemos en el final del lado derecho.
                                if (posFin < tokensOperacion.size()-1) {
                                    
                                    // Se verifica que haya una suma, resta, division o multiplicacion o parentesis final del lado derecho
                                    if (tokensOperacion.get(posFin+1).equalsIgnoreCase("+") || tokensOperacion.get(posFin+1).equalsIgnoreCase("-") || tokensOperacion.get(posFin+1).equalsIgnoreCase("/") || tokensOperacion.get(posFin+1).equalsIgnoreCase("*") || tokensOperacion.get(posFin+1).equalsIgnoreCase(")")) {
                                        
                                        // De ser así, se elimina el parentesis
                                        tokensOperacion.remove(posFin);
                                    }
                                    
                                    // En cualquier otro caso, se cambia el parentesis por una multiplicación
                                    else {
                                        tokensOperacion.set(posFin, "*");
                                    }
                                        
                                }
                                
                                // Al ser el final, solamente se elimina el parentesis.
                                else {
                                    tokensOperacion.remove(posFin);
                                }
                                
                                // Ahora debemos comprobar que no estemos al principio del lado izquierdo.
                                if (posIni > 0) {
                                    // Se verifica que haya una suma, resta, division, multiplicacion o parentesis del lado izquierdo.
                                    if (tokensOperacion.get(posIni-1).equalsIgnoreCase("+") || tokensOperacion.get(posIni-1).equalsIgnoreCase("-") || tokensOperacion.get(posIni-1).equalsIgnoreCase("/") || tokensOperacion.get(posIni-1).equalsIgnoreCase("*") || tokensOperacion.get(posIni-1).equalsIgnoreCase(")") || tokensOperacion.get(posIni-1).equalsIgnoreCase("(")) {
                                        
                                        // De ser así, se elimina el parentesis
                                        tokensOperacion.remove(posIni);
                                    }
                                    
                                    // En cualquier otro caso, se cambia el parentesis por una multiplicación
                                    else {
                                        tokensOperacion.set(posIni, "*");
                                    }
                                    
                                }
                                
                                // Al ser el principio, solamente se elimina el parentesis.
                                else {
                                    tokensOperacion.remove(posIni);
                                }
                                
                            }
                            
                            continue;
                        }
                        
                        // Si hay una multiplicación en la operación.
                        else if (tokensOperacion.lastIndexOf("*") != -1) {
                            
                            // Se almacena la antepenúltima variable en el triplo y se elimina de la lista.
                            numeroIns.add(numeroinstruccion);
                            objeto.add("T" +numerotemporal);
                            fuente.add(tokensOperacion.remove(tokensOperacion.lastIndexOf("*")-1));
                            operador.add("=");
                            numeroinstruccion++;

                            // En el triplo se almacena la operacion realizada.
                            numeroIns.add(numeroinstruccion);
                            objeto.add("T" +numerotemporal);
                            fuente.add(tokensOperacion.remove(tokensOperacion.lastIndexOf("*")+1));
                            operador.add(tokensOperacion.get(tokensOperacion.lastIndexOf("*")));
                            numeroinstruccion++;

                            // Se agrega a la lista el triplo realizado.
                            tokensOperacion.set(tokensOperacion.lastIndexOf("*"),"T" +numerotemporal);
                            
                            // Se aumenta el contador del triplo
                            numerotemporal++;
                            
                            continue;
                        }
                        
                        // Si hay una division en la operación.
                        else if (tokensOperacion.lastIndexOf("/") != -1) {
                            
                            // Se almacena la antepenúltima variable en el triplo y se elimina de la lista.
                            numeroIns.add(numeroinstruccion);
                            objeto.add("T" +numerotemporal);
                            fuente.add(tokensOperacion.remove(tokensOperacion.lastIndexOf("/")-1));
                            operador.add("=");
                            numeroinstruccion++;

                            // En el triplo se almacena la operacion realizada.
                            numeroIns.add(numeroinstruccion);
                            objeto.add("T" +numerotemporal);
                            fuente.add(tokensOperacion.remove(tokensOperacion.lastIndexOf("/")+1));
                            operador.add(tokensOperacion.get(tokensOperacion.lastIndexOf("/")));
                            numeroinstruccion++;

                            // Se agrega a la lista el triplo realizado.
                            tokensOperacion.set(tokensOperacion.lastIndexOf("/"),"T" +numerotemporal);
                            
                            // Se aumenta el contador del triplo
                            numerotemporal++;
                            
                            continue;
                        }
                        
                        // Si solo quedan sumas y restas.
                        else {
                            
                            // Se almacena la antepenúltima variable en el triplo y se elimina de la lista.
                            numeroIns.add(numeroinstruccion);
                            objeto.add("T" +numerotemporal);
                            fuente.add(tokensOperacion.remove(tokensOperacion.size()-3));
                            operador.add("=");
                            numeroinstruccion++;

                            // En el triplo se almacena la operacion realizada.
                            numeroIns.add(numeroinstruccion);
                            objeto.add("T" +numerotemporal);
                            fuente.add(tokensOperacion.remove(tokensOperacion.size()-1));
                            operador.add(tokensOperacion.remove(tokensOperacion.size()-1));
                            numeroinstruccion++;

                            // Se agrega a la lista el triplo realizado.
                            tokensOperacion.add("T" +numerotemporal);
                            
                            // Se aumenta el contador del triplo
                            numerotemporal++;
                        }
                        
                    }
                    
                    // El resultado final es almacenado en la variable
                    numeroIns.add(numeroinstruccion);
                    objeto.add(variableAsig);
                    fuente.add(tokensOperacion.get(0));
                    operador.add("=");
                    numeroinstruccion++;
                    
                }
            }
        }
        
        // Ahora se genera el texto de la tripleta.
        StringBuilder texto = new StringBuilder("|\t\t|\tDato Origen\t|\tDato Fuente\t|\tOperador|");
        
        texto.append(saltoLinea);
        for (int i = 0; i < numeroIns.size(); i++) {
            texto.append("|\t").append(numeroIns.get(i)).append("\t|\t\t")
                    .append(objeto.get(i)).append("\t|\t\t")
                    .append(fuente.get(i)).append("\t|\t")
                    .append(operador.get(i)).append("\t|"+saltoLinea);
        }
        texto.append("|\t").append(numeroIns.size()+1).append("\t|\t\t...\t|\t\t...\t|\t...\t|");
        
        // se guarda la tripleta
        triplo = new Triplo(numeroIns, objeto, fuente, operador, posOperacion, posComparacion);
        
        // Se guarda el archivo.
        Archivo.guardar("Codigo Intermedio.txt", texto.toString());
    }
    
    public static void generarEnsamblador() {
        
        // Se crean variables referentes a los registros.
        String AH = "";
        String AL = "";
        String CL = "";
        
        // Variables con el numero de ciclo y condicion.
        int numdo = 1;
        int numwhile = 1;
        
        // Se guarda la posición del primer do y while.
        int numinsdo = 0;
        int numinswhile = 0;
        try {
            numinsdo = triplo.posOperacion.remove(0) - 1;
            numinswhile = triplo.posComparacion.remove(0) - 1;
        }
        catch (IndexOutOfBoundsException ex) {
            // No hay ciclos.
            numinsdo = -1;
            numinswhile = -1;
        }
        
        // Pila que almacena siempre el ultimo ciclo.
        ArrayList<Integer> ultdo = new ArrayList<>();
        
        // En dado caso de que la pila se vacíe antes, se usa este String.
        String finaldo = "";
        
        // Variable que contendrá el código ensamblador
        StringBuilder stb = new StringBuilder();
        
        // Lo divertido del ensamblador comienza en esta parte.
        for (int i = 0; i < triplo.numeroIns.size(); i++) {
            
            // Se verifica si estamos ante una instruccion do.
            if (i == numinsdo) {
                
                finaldo = "Operacion" + numdo;
                stb.append(finaldo+":"+saltoLinea);
                ultdo.add(numdo);
                numdo++;
                
                // Se elimina la posicion de este do.
                try {
                    while (numinsdo == triplo.posOperacion.get(0) - 1) {
                        numinsdo = triplo.posOperacion.remove(0) - 1;
                    }
                    numinsdo = triplo.posOperacion.remove(0) - 1;
                }
                catch (IndexOutOfBoundsException ex) {
                    // No hay más ciclos.
                    numinsdo = -1;
                }
            }
            
            // Si estamos ante una instruccion while.
            if (i == numinswhile) {
                
                stb.append("Comparacion"+numwhile+":"+saltoLinea);
                numwhile++;
                
                // Se pasa la variable a comparar a AL.
                stb.append("\tMOV AL, "+triplo.fuente.get(i)+saltoLinea);
                AL = triplo.fuente.get(i);
                i++;
                
                // Ahora se compara lo que tiene AL.
                stb.append("\tCMP AL, "+triplo.fuente.get(i)+saltoLinea);
                AL = triplo.fuente.get(i);
                
                int posciclo = 0;
                
                // Se procede a leer que comparación se esta realizando.
                // Si se trata de un menor.
                if (triplo.operador.get(i).equalsIgnoreCase("<")) {
                    try {
                        posciclo = ultdo.remove(ultdo.size()-1);
                        stb.append("\tJL Operacion" +posciclo+saltoLinea);
                    }
                    catch (ArrayIndexOutOfBoundsException ex) {
                        stb.append("\tJL "+finaldo+saltoLinea);
                        finaldo = "";
                    }
                    
                }
                
                // Si se trata de un menor o igual que.
                else if (triplo.operador.get(i).equalsIgnoreCase("<=")) {
                    try {
                        posciclo = ultdo.remove(ultdo.size()-1);
                        stb.append("\tJLE Operacion"+posciclo+saltoLinea);
                    }
                    catch (ArrayIndexOutOfBoundsException ex) {
                        stb.append("\tJLE "+finaldo+saltoLinea);
                        finaldo = "";
                    }
                }
                
                // Si se trata de un mayor.
                else if (triplo.operador.get(i).equalsIgnoreCase(">")) {
                    try {
                        posciclo = ultdo.remove(ultdo.size()-1);
                        stb.append("\tJG Operacion"+posciclo+saltoLinea);
                    }
                    catch (ArrayIndexOutOfBoundsException ex) {
                        stb.append("\tJG "+finaldo+saltoLinea);
                        finaldo = "";
                    }
                }
                
                // Si se trata de un mayor o igual que.
                else if (triplo.operador.get(i).equalsIgnoreCase(">=")) {
                    try {
                        posciclo = ultdo.remove(ultdo.size()-1);
                        stb.append("\tJGE Operacion"+posciclo+saltoLinea);
                    }
                    catch (ArrayIndexOutOfBoundsException ex) {
                        stb.append("\tJGE "+finaldo+saltoLinea);
                        finaldo = "";
                    }
                }
                
                // Si se trata de un igual que.
                else if (triplo.operador.get(i).equalsIgnoreCase("==")) {
                    try {
                        posciclo = ultdo.remove(ultdo.size()-1);
                        stb.append("\tJE Operacion"+posciclo+saltoLinea);
                    }
                    catch (ArrayIndexOutOfBoundsException ex) {
                        stb.append("\tJE "+finaldo+saltoLinea);
                        finaldo = "";
                    }
                }
                
                // Si se trata de un diferente de.
                else if (triplo.operador.get(i).equalsIgnoreCase("!=")) {
                    try {
                        posciclo = ultdo.remove(ultdo.size()-1);
                        stb.append("\tJNE Operacion"+posciclo+saltoLinea);
                    }
                    catch (ArrayIndexOutOfBoundsException ex) {
                        stb.append("\tJNE "+finaldo+saltoLinea);
                        finaldo = "";
                    }
                }
                
                // Se saltan las dos lineas que contienen la instrucción while
                i = i + 2;
                
                // Se elimina la posicion de este while.
                try {
                    numinswhile = triplo.posComparacion.remove(0) - 1;
                }
                catch (IndexOutOfBoundsException ex) {
                    // No hay más ciclos.
                    numinswhile = -1;
                }
                
                // Si a continuación no nos encontramos con un while o un do,
                // se considera que es continuación del ciclo anterior.
                if (i + 1 != numinsdo && i + 1 != numinswhile && i + 1 < triplo.numeroIns.size()) {
                    if (!finaldo.isEmpty()) {
                        stb.append("\tJMP "+finaldo+"restante"+saltoLinea+finaldo+"restante:"+saltoLinea);
                    }
                    else {
                        stb.append("\tJMP exit"+saltoLinea+"exit:"+saltoLinea);
                    }
                }
            }
            
            // Se verifica que el operador es una asignación.
            else if (!triplo.objeto.get(i).contains("T") && !triplo.fuente.get(i).contains("T")) {
                
                stb.append("\tMOV "+triplo.objeto.get(i)+", "+triplo.fuente.get(i)+saltoLinea);
                
            }
            
            // Si solamente la columna 2 no contiene una variable temporal, estamos ante una asignación.
            else if (!triplo.objeto.get(i).contains("T")) {
                
                // Se mueve de AL el resultado a la variable correspondiente.
                stb.append("\tMOV "+triplo.objeto.get(i)+", AL"+saltoLinea);
                
                // Se limpia la variable para detectar que lo que almacena BH ya no es importante.
                AL = "";
                
            }
            
            // De no ser ningun caso anterior, estamos ante una operación aritmetica.
            else {
                
                // Si AL se encuentra vacía, se procede a realizar la operación.
                if (AL.isEmpty()) {
                    
                    // Se mueve a AL el primer número.
                    stb.append("\tMOV AL, "+triplo.fuente.get(i)+saltoLinea);
                    AL = triplo.fuente.get(i);
                    
                    // Se mueve al siguiente número.
                    i++;
                    
                    // Si no estamos ante una división, se utiliza el registro AH
                    if (!triplo.operador.get(i).equalsIgnoreCase("/")) {
                        stb.append("\tMOV AH, "+triplo.fuente.get(i)+saltoLinea);
                        AH = triplo.fuente.get(i);
                    }
                    
                    // Pero si se trata de una división.
                    else {
                        
                        // Se limpia la parte alta de AX si no se encuentra vacía.
                        if (!AH.isEmpty()) {
                            stb.append("\tMOV AH, 0"+saltoLinea);
                            AH = "";
                        }
                        
                        // Se pasa a CL el divisor.
                        stb.append("\tMOV CL, "+triplo.fuente.get(i)+saltoLinea);
                        CL = triplo.fuente.get(i);
                        
                        // Se realiza la división y se indica los registros con información.
                        stb.append("\tDIV CL"+saltoLinea);
                        AH = "Residuo";
                        AL = "Resultado";
                    }
                    
                    // Si se trata de una multiplicación.
                    if (triplo.operador.get(i).equalsIgnoreCase("*")) {
                        stb.append("\tMUL AL, AH"+saltoLinea);
                        AL = "AL * AH";
                    }
                    
                    // Si se trata de una resta.
                    else if (triplo.operador.get(i).equalsIgnoreCase("-")) {
                        stb.append("\tSUB AL, AH"+saltoLinea);
                        AL = "AL - AH";
                    }
                    
                    // Si se trata de una suma.
                    else if (triplo.operador.get(i).equalsIgnoreCase("+")) {
                        stb.append("\tADD AL, AH"+saltoLinea);
                        AL = "AL + AH";
                    }
                }
                
                // Si AL tiene guardada información, se utiliza la misma para seguir utilizandose.
                else {
                    
                    // Si no estamos ante una división, se utiliza el registro AH
                    if (!triplo.operador.get(i).equalsIgnoreCase("/")) {
                        stb.append("\tMOV AH, "+triplo.fuente.get(i)+saltoLinea);
                        AH = triplo.fuente.get(i);
                    }
                    
                    // Pero si se trata de una división.
                    else {
                        
                        // Se limpia la parte alta de AX si no se encuentra vacía.
                        if (!AH.isEmpty()) {
                            stb.append("\tMOV AH, 0"+saltoLinea);
                            AH = "";
                        }
                        
                        // Se pasa a CL el divisor.
                        stb.append("\tMOV CL, AL"+saltoLinea);
                        CL = triplo.fuente.get(i);
                        
                        // Se pasa a AL el número a dividir.
                        stb.append("\tMOV AL, "+triplo.fuente.get(i)+saltoLinea);
                        CL = triplo.fuente.get(i);
                        
                        // Se realiza la división y se indica los registros con información.
                        stb.append("\tDIV CL"+saltoLinea);
                        AH = "Residuo";
                        AL = "Resultado";
                        
                        // Se salta la instrucción que hace un guardado de dos temporales.
                        i++;
                    }
                    
                    // Se salta la instrucción que hace un guardado de dos temporales.
                    i++;
                    
                    // Si se trata de una multiplicación.
                    if (triplo.operador.get(i).equalsIgnoreCase("*")) {
                        stb.append("\tMUL AL, AH"+saltoLinea);
                        AL = "AL * AH";
                    }
                    
                    // Si se trata de una resta.
                    else if (triplo.operador.get(i).equalsIgnoreCase("-")) {
                        stb.append("\tSUB AL, AH"+saltoLinea);
                        AL = "AL - AH";
                    }
                    
                    // Si se trata de una suma.
                    else if (triplo.operador.get(i).equalsIgnoreCase("+")) {
                        stb.append("\tADD AL, AH"+saltoLinea);
                        AL = "AL + AH";
                    }
                    
                }
                
            }
            
            // Si la siguiente instrucción es un ciclo.
            if (i + 1 == numinsdo) {
                
                // Se escribe un salto hacia el ciclo.
                stb.append("\tJMP Operacion"+numdo+saltoLinea);
                
            }
            
            // Si la siguiente instrucción es una condicion.
            if (i + 1 == numinswhile) {
                
                // Se escribe un salto hacia el ciclo.
                stb.append("\tJMP Comparacion"+numwhile+saltoLinea);
                
            }
            
            // Si estamos en la ultima linea.
            if (i + 1 == triplo.numeroIns.size()) {
                stb.append("\tEND");
            }
            
        }
        
        Archivo.guardar("Codigo Objeto.txt", stb.toString());
    }
    
}
