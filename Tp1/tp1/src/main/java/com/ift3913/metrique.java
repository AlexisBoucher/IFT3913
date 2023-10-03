package com.ift3913;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.ift3913.tls;

public class metrique {
     /*Tloc : cette classe affiche le nombre de lignes de code non-vides qui ne sont pas de commentaires dans un fichier. Il doit juste sortir la valeur du TLOC à
     la ligne de commandes */

    /*public static void main(String[] args) throws IOException {
        
        String directory ="";
        try {
            directory = args[0];
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("One or more argument missing");
            System.exit(1);
        }

        Path path = Paths.get(directory);
        List<Path> paths = findJavaTestFile(path, ".java");
        for (Path file: paths){
            String relativePath = file.toString().substring(directory.length());
            System.out.println(relativePath+", "
                    +file.getFileName()+", "
                    +"tloc, tassert, tcmp");
        }
    }*/
    public int tloc (String dir) {
        int tloc = 0;

        try (BufferedReader file_reader = new BufferedReader(new FileReader(dir))) {
            boolean comment_line = false;
            String file_line = file_reader.readLine();

            while (file_line != null) {  
                file_line = file_line.trim();

                if (file_line.isEmpty()) {
                    continue;
                }
                if (file_line.startsWith("//")) {
                    continue;
                } 
                else if (file_line.startsWith("/*")) {
                    if (!file_line.endsWith("*/")) {
                        comment_line = true;
                    }
                    continue;
                }
                //commentaire multi-ligne
                if (comment_line == true) {
                    if (file_line.contains("*/")) {
                        comment_line = false;
                        file_line = file_line.substring(file_line.indexOf("*/") + 2).trim();
                        if (file_line.isEmpty()) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
                tloc++;
            }
        } catch (IOException e) {
            System.err.println("Error while reading file: " + e.getMessage());
            System.exit(1);
        }

        return tloc;
    }
}

