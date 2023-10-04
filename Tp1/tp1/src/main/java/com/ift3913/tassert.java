package com.ift3913;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/*TASSERT : nombre de assertions JUnit. Il doit juste sortir la valeur du TASSERT à la ligne de commandes*/
public class tassert {


    public static void main(String[] args) {
        if (args.length != 1) {
            System.exit(1);
        }
    
        String filePath = args[0];
        int tassert = calcul_tassert(filePath);
        System.out.println("TASSERT : " + tassert);
    }
    /**
     * 
     * @param dir
     * @return tassert:nombre de ligne contenant les diverses méthodes statiques de la classe org.junit.Assert, 
     */
    public static int calcul_tassert(String dir) {
        int tassert = 0;

        try (BufferedReader line_reader = new BufferedReader(new FileReader(dir))) {
            String line;

            while ((line=line_reader.readLine())!= null) {
                line = line.trim();
                // Vérification: si la ligne contient un appel à une méthode d'assertion JUnit incrementer le tassert
                if (line.startsWith("assert")|| (line.startsWith("fail"))) {
                    tassert++;
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur while reading file : " + e.getMessage());
        }
        return tassert;
    }

}

