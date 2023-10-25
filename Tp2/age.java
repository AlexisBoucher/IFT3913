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

//classe qui imprime l'age moyen des fichiers du programme et des fichiers tests ainsi que l'ecart d'age.
public class age {
    public static void main(String[] args) throws IOException {

        boolean saveInFile = false;
        String fileName = "";
        String directory = "";

        try {
            //donne un fichier de sortie
            if(Objects.equals(args[0], "-o")){
                saveInFile = true;
                fileName = args[1];
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


        saveData(ageMoyen, testAgeMoyen, fileName, saveInFile);

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

    public static void saveData(float ageMoyen, float testAgeMoyen, String fileName, boolean saveInFile){
        float ecartAge=ageMoyen-testAgeMoyen;
        //imprime en ligne de commande
        if(!saveInFile){
            System.out.println("Age en moyenne des fichiers du programme: "+ageMoyen+" jours\nAge en moyenne des fichiers tests: "+testAgeMoyen+" jours");
            if(ageMoyen >= testAgeMoyen){
                System.out.println("L'age moyen des test sont plus jeunes de "+ecartAge+"jour");
            }
            else{
                ecartAge*=-1;
                System.out.println("L'age moyen des test sont plus vieux de "+ecartAge+"jour");
            }
        }

        //enregistrer dans fichier txt
        else{
            try{
                File file = new File(fileName);
                FileWriter output = new FileWriter(file);
                BufferedWriter writer = new BufferedWriter(output);

                
                writer.write("Age en moyenne des fichiers du programme: "+ageMoyen+" jours\nAge en moyenne des fichiers tests: "+testAgeMoyen+" jours\n");

                if(ageMoyen >= testAgeMoyen){
                writer.write("L'age moyen des test sont plus jeunes de "+ecartAge+"jour");
                }
                else{
                    ecartAge*=-1;
                    writer.write("L'age moyen des test sont plus vieux de "+ecartAge+"jour");
                }
                writer.close();
            }
            catch (Exception e){e.printStackTrace();}
        }
    }
}