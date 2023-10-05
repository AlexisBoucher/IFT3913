//look in jfreechart directory and print all relatives path of java test file
package com.ift3913;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class tls {
    public static void main(String[] args) throws IOException {
        String directory ="";
        try {
            directory = args[0];
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("One or more argument missing");
            System.exit(1);
        }

        Path path = Paths.get(directory);
        List<Path> paths = findJavaTestFile(path);
        for (Path file: paths){
            String relativePath = file.toString().substring(directory.length());
            int tlocNum = tloc.calcul_tloc(file.toString());
            int tassertNum = tassert.calcul_tassert(file.toString());
            String tcmpNum;
            float Tcmp;
            if(tassertNum!=0){
                Tcmp = (float)tlocNum/(float) tassertNum;
                tcmpNum = String.valueOf(Tcmp);}
            else tcmpNum = "inf";
            System.out.println(relativePath+","
                    +getPackage(file)+", "
                    +file.getFileName()+", "
                    +tlocNum+", "
                    +tassertNum+", "
                    +tcmpNum);
        }

    }

    //Fait une liste des fichiers test java
    public static List<Path> findJavaTestFile(Path path)
         throws IOException {

            //Chemin d'acces qui n'est pas a un dossier
            if (!Files.isDirectory(path)) {
                throw new IllegalArgumentException("Path must be a directory!");
            }

            List<Path> result;

            //regarde chaque fichier dans le dossier et les sous-dossiers et met dans une liste tout les
            //fichiers java contenant le mot test
            try (Stream<Path> walk = Files.walk(path)) {
                result = walk
                        .filter(Files::isRegularFile)   // is a file
                        .filter(p -> p.getFileName().toString().endsWith(".java"))
                        .filter(p -> p.getFileName().toString().toLowerCase().contains("test"))
                        .collect(Collectors.toList());
            }
            return result;

    }

    //retourne le nom du paquet d'un fichier base sur la structure d'un projet maven
    public static String getPackage(Path path){
        //le nom du paquet termine par le nom du dossier contenant le fichier
        Path packageEnd = path.getParent();
        //le nom du paquet commence par le nom du dossier contenue dans le dossier java
        Path packageStart = packageEnd;

        //trouve le chemin du dossier java contenant le chemin du fichier choisi
        while (!packageStart.getParent().endsWith("java")){
            packageStart = packageStart.getParent();
        }
        packageStart = packageStart.getParent();

        //enleve le chemin du dossier java au chemin du dossier contenant le fichier choisi
        String packageName = packageEnd.toString().substring(packageStart.toString().length()+1);

        //remplace par des point les \ et / selon windows ou mac
        packageName = packageName.replace("\\",".");
        packageName = packageName.replace("/",".");
        return packageName;
    }

}
