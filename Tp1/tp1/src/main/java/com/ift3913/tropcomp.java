package com.ift3913;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class tropcomp {
    public static void main(String[] args) throws IOException {
        String directory = "";
        float seuil;
        try {
            directory = args[0];
            seuil = Float.parseFloat(args[1]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("One or more argument missing");
            System.exit(1);
        }

        List<tlsFile> tlsFiles = tls.findJavaTestFile(directory);

        List<Integer> tlocs = new ArrayList<>();
        List<Float> tcmps = new ArrayList<>();

        for (tlsFile tlsFile:tlsFiles){
            tlocs.add(tlsFile.getTloc());
            tcmps.add(tlsFile.getTcmp());
        }
        //classe tloc et tcmp en ordre decroissant
        tlocs.sort(Collections.reverseOrder());
        tcmps.sort(Collections.reverseOrder());

        //print tloc et tcmp en ordre decroissant
        for(int tloc :tlocs){
            System.out.println(tloc+", "+tcmps.remove(0));
        }
    }
}