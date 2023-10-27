//look in jfreechart directory and print all relatives path of java test file
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
    private static Map<String, Integer> testsPerPackage = new HashMap<>();
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

        Map<String, Integer> testsPerPackage = new HashMap<>();
        List<tlsFile> tlsFiles = findJavaTestFile(directory);

        //imprime les donnees
        saveData(tlsFiles,csvFile,saveInFile);

        // Imprime le nombre de tests par package
        for (Map.Entry<String, Integer> entry : testsPerPackage.entrySet()) {
            System.out.println("Package: " + entry.getKey() + ", Nombre de Tests: " + entry.getValue());
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
                        .filter(Files::isRegularFile) // is a file
                        .filter(p -> p.getFileName().toString().endsWith(".java"))
                        .filter(p -> p.getFileName().toString().toLowerCase().contains("test"))
                        .collect(Collectors.toList());
            }

            //recueil toute les infos desire
            for (Path file: paths) {
                tlsFile tls_File = new tlsFile(file, directory);

                //verifie qu'il y ait au moins 1 assert sinon pas considere comme classe de test
                if(tls_File.getTassert()!=0 ){
                    tlsFiles.add(tls_File);

                // Compte le nombre de tests par package
                String packageName = tls_File.getPacket();
                testsPerPackage.put(packageName, testsPerPackage.getOrDefault(packageName, 0) + 1);
                }
            }
            return tlsFiles;
    }

    public static void saveData(List<tlsFile> tlsFiles, String csvFile, boolean saveInFile){
        //imprime en ligne de commande

        int tlocGlobal=0;
        int tassertGlobal=0;
        Float tcmpGlobal=(float)0;
        if(!saveInFile){
            for (tlsFile file: tlsFiles) {

                System.out.println(file.getRelativePath() + ", "
                        + file.getPacket() + ", "
                        + file.getName() + ", "
                        + file.getTloc() + ", "
                        + file.getTassert() + ", "
                        + tlsFile.getTcmp() + ", "
                        + testsPerPackage.getOrDefault(tlsFile.getPacket(), 0) + "\n");
                tlocGlobal += file.getTloc();
                tassertGlobal += file.getTassert();
                tcmpGlobal += file.getTcmp();         
            }
            tcmpGlobal = tcmpGlobal/tlsFiles.size();
            System.out.println("Total, "+tlocGlobal+", "+tassertGlobal+", "+tcmpGlobal);
        }

        //enregistrer   dans fichier csv
        else{
            try{
                File file = new File(csvFile);
                FileWriter output = new FileWriter(file);
                BufferedWriter writer = new BufferedWriter(output);
                
                writer.write("Relative Path, Package, File Name, TLOC, TASSERT, TCMP, TPP\n");
                
                for(tlsFile tlsFile : tlsFiles){
                    writer.write(tlsFile.getRelativePath() + ","
                            + tlsFile.getPacket() + ", "
                            + tlsFile.getName() + ", "
                            + tlsFile.getTloc() + ", "
                            + tlsFile.getTassert() + ", "
                            + tlsFile.getTcmp() + ", "
                            + testsPerPackage.getOrDefault(tlsFile.getPacket(), 0) + "\n");
                    tlocGlobal += tlsFile.getTloc();
                    tassertGlobal += tlsFile.getTassert();
                    tcmpGlobal += tlsFile.getTcmp();    
                }
                tassertGlobal = tassertGlobal/tlsFiles.size();
                tcmpGlobal = tcmpGlobal/tlsFiles.size();
                writer.write("Total, "+tlocGlobal+", "+tassertGlobal+", "+tcmpGlobal);
               /*  writer.write("\nPackage, Nombre de Tests\n");
                for (Map.Entry<String, Integer> entry : testsPerPackage.entrySet()) {
                writer.write(entry.getKey() + ", " + entry.getValue() + "\n");
                }*/
                writer.close();
            }
            catch (Exception e){e.printStackTrace();}
        }
    }


}