import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.Clock;
import java.time.Duration;

//classe qui imprime toute les classe test java a risque d'etre dans le seuil superieur tloc et tcmp
public class age {
    public static void main(String[] args) throws IOException {

        boolean saveInFile = false;
        String csvFile = "";
        String directory = "";
        float seuil = 0;

        try {
            //donne un fichier de sortie
            if(Objects.equals(args[0], "-o")){
                saveInFile = true;
                csvFile = args[1];
                directory = args[2];
                seuil = Float.parseFloat(args[3]);
            }
            //aucun fichier de sortie
            else {
                directory = args[0];
                seuil = Float.parseFloat(args[1]);
            }

        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("One or more argument missing");
            System.exit(1);
        }

        List<List<Path>> javaFilePath = findJavaFile(directory);

        long ageTotal = 0 ;
        long testAgeTotal = 0;
        java.time.Clock clock =  Clock.systemUTC();

        
        for(Path path : javaFilePath.get(0)){
            FileTime t = Files.getLastModifiedTime(path);
            Instant fileInstant = t.toInstant();
            Instant now = clock.instant(); 
            Duration difference = Duration.between(fileInstant, now);
            long age = difference.toDays();
            ageTotal += age;
        }


        for(Path path : javaFilePath.get(1)){
            FileTime t = Files.getLastModifiedTime(path);
            Instant fileInstant = t.toInstant();
            Instant now = clock.instant(); 
            Duration difference = Duration.between(fileInstant, now);
            long age = difference.toDays();
            testAgeTotal += age;
        }

        float ageMoyen = ageTotal/javaFilePath.get(0).size();
        float testAgeMoyen = testAgeTotal/javaFilePath.get(1).size();
        System.out.println("Age moyen : "+ageMoyen+"    Age test moyen : "+testAgeMoyen);

    }


    public static List<List<Path>> findJavaFile(String directory)

         throws IOException {
            Path path = Paths.get(directory);

            //Chemin d'acces qui n'est pas un dossier
            if (!Files.isDirectory(path)) {
                throw new IllegalArgumentException("Path must be a directory!");
            }


            List<Path> test;
            List<Path> testPaths;
            List<Path> main;
            List<Path> paths;
            List<List<Path>> javaFile= new ArrayList();

            //regarde chaque fichier dans le dossier et les sous-dossiers et met dans une liste tout les
            //fichiers java provenant du dossier main
            try (Stream<Path> walk = Files.walk(path, 2)) {
                test = walk
                        .filter(Files::isDirectory)   // is a file
                        .filter(p -> p.getFileName().toString().toLowerCase().contains("test"))
                        .collect(Collectors.toList());
            }
            try (Stream<Path> walk = Files.walk(test.get(0))) {
                testPaths = walk
                        .filter(Files::isRegularFile)   // is a file
                        .filter(p -> p.getFileName().toString().endsWith(".java"))
                        .collect(Collectors.toList());
            }


            //regarde chaque fichier dans le dossier et les sous-dossiers et met dans une liste tout les
            //fichiers java provenant du dossier test
            try (Stream<Path> walk = Files.walk(path, 2)) {
                main = walk
                        .filter(Files::isDirectory)   // is a file
                        .filter(p -> p.getFileName().toString().toLowerCase().contains("main"))
                        .collect(Collectors.toList());
            }
            try (Stream<Path> walk = Files.walk(main.get(0))) {
                paths = walk
                        .filter(Files::isRegularFile)   // is a file
                        .filter(p -> p.getFileName().toString().endsWith(".java"))
                        .collect(Collectors.toList());
            }

            javaFile.add(0, paths);
            javaFile.add(1, testPaths);
            return javaFile;
    }
}