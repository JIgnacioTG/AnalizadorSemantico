/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author LENOVO
 */
public class Archivo {
    
    public static void guardar(String nombre, String extension, String texto) {
        try (FileWriter fw = new FileWriter(nombre+"."+extension)) {
            fw.write(texto);
        }
        catch (IOException ex) {
            System.out.println("No se pudo guardar el archivo.");
        }
    }
    
    public static void guardar(String archivo, String texto) {
        try (FileWriter fw = new FileWriter(archivo)) {
            fw.write(texto);
        }
        catch (IOException ex) {
            System.out.println("No se pudo guardar el archivo.");
        }
    }
    
    
}
