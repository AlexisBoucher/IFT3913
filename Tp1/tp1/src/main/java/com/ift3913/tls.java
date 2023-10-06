//look in jfreechart directory and print all relatives path of java test file
package com.ift3913;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class tls {
    public static void main(String[] args) throws IOException {
        String directory = "";
        try {
            directory = args[0];
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("One or more argument missing");
            System.exit(1);
        }


        List<tlsFile> tlsFiles = findJavaTestFile(directory);

        //imprime les donnees
        for (tlsFile file: tlsFiles) {
            System.out.println(file.getRelativePath()+","
                    +file.getPacket()+", "
                    +file.getName()+", "
                    +file.getTloc()+", "
                    +file.getTassert()+", "
                    +file.getTcmp());
        }
    }

    //Fait une liste des fichiers test java
    public static List<tlsFile> findJavaTestFile(String directory)

         throws IOException {
            Path path = Paths.get(directory);

            //Chemin d'acces qui n'est pas a un dossier
            if (!Files.isDirectory(path)) {
                throw new IllegalArgumentException("Path must be a directory!");
            }

            List<Path> paths;
            List<tlsFile> tlsFiles = new ArrayList<>();

            //regarde chaque fichier dans le dossier et les sous-dossiers et met dans une liste tout les
            //fichiers java contenant le mot test
            try (Stream<Path> walk = Files.walk(path)) {
                paths = walk
                        .filter(Files::isRegularFile)   // is a file
                        .filter(p -> p.getFileName().toString().endsWith(".java"))
                        .filter(p -> p.getFileName().toString().toLowerCase().contains("test"))
                        .collect(Collectors.toList());
            }

            //recueil toute les infos desire
            for (Path file: paths) {
                tlsFile tls_File = new tlsFile(file, directory);

                //verifie qu'il y ait au moin 1 assert sinon pas considere comme classe de test
                if(tls_File.getTassert()!=0 ){
                    tlsFiles.add(tls_File);
                }
            }
            return tlsFiles;
    }


}
