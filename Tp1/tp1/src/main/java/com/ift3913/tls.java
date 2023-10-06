//look in jfreechart directory and print all relatives path of java test file
package com.ift3913;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class tls {
    public static void main(String[] args) throws IOException {
        boolean saveInFile = false;
        String csvFile = "";
        String directory = "";

        try {
            //donne un fichier de sortie
            if(Objects.equals(args[0], "-o")){
                saveInFile = true;
                csvFile = args[1];
                directory = args[2];
            }
            //aucun fichier de sortie
            else {
                directory = args[0];
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("One or more argument missing");
            System.exit(1);
        }


        List<tlsFile> tlsFiles = findJavaTestFile(directory);

        //imprime les donnees
        saveData(tlsFiles,csvFile,saveInFile);
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

    public static void saveData(List<tlsFile> tlsFiles, String csvFile, boolean saveInFile){
        //imprime en ligne de commande
        if(!saveInFile){
            for (tlsFile file: tlsFiles) {

                System.out.println(file.getRelativePath() + ","
                        + file.getPacket() + ", "
                        + file.getName() + ", "
                        + file.getTloc() + ", "
                        + file.getTassert() + ", "
                        + file.getTcmp());

            }
        }

        //enregistrer dans fichier csv
        else{
            try{
                File file = new File(csvFile);
                FileWriter output = new FileWriter(file);
                BufferedWriter writer = new BufferedWriter(output);

                for(tlsFile tlsFile : tlsFiles){
                    writer.write(tlsFile.getRelativePath() + ","
                            + tlsFile.getPacket() + ", "
                            + tlsFile.getName() + ", "
                            + tlsFile.getTloc() + ", "
                            + tlsFile.getTassert() + ", "
                            + tlsFile.getTcmp()+ "\n");
                }
                writer.close();
            }
            catch (Exception e){e.printStackTrace();}
        }
    }


}
