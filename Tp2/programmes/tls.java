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
            // Donne un fichier de sortie
            if (Objects.equals(args[0], "-o")) {
                saveInFile = true;
                csvFile = args[1];
                directory = args[2];
            } else {
                directory = args[0];
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("One or more arguments are missing");
            System.exit(1);
        }

        List<tlsFile> tlsFiles = findJavaTestFile(directory);

        // Imprime les données
        saveData(tlsFiles, csvFile, saveInFile);
    }

    // Fait une liste des fichiers test Java
    public static List<tlsFile> findJavaTestFile(String directory) throws IOException {
        Path path = Paths.get(directory);

        // Chemin d'accès qui n'est pas un dossier
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        List<Path> paths;
        List<tlsFile> tlsFiles = new ArrayList<>();

        // Regarde chaque fichier dans le dossier et les sous-dossiers et met dans une liste tous les
        // fichiers Java contenant le mot "test"
        try (Stream<Path> walk = Files.walk(path)) {
            paths = walk
                    .filter(Files::isRegularFile) // is a file
                    .filter(p -> p.getFileName().toString().endsWith(".java"))
                    .filter(p -> p.getFileName().toString().toLowerCase().contains("test"))
                    .collect(Collectors.toList());
        }

        // Recueille toutes les informations désirées
        for (Path file : paths) {
            tlsFile tlsFile = new tlsFile(file, directory);

            // Vérifie qu'il y ait au moins 1 assert sinon pas considéré comme classe de test
            if (tlsFile.getTassert() != 0) {
                tlsFiles.add(tlsFile);
            }
        }
        return tlsFiles;
    }

    public static void saveData(List<tlsFile> tlsFiles, String csvFile, boolean saveInFile) {
        // Imprime en ligne de commande

        int tlocGlobal = 0;
        int tassertGlobal = 0;
        float tcmpGlobal = 0;
        int tppGlobal = 0;
        if (!saveInFile) {
            for (tlsFile file : tlsFiles) {
                System.out.println(file.getRelativePath() + ", "
                        + file.getPacket() + ", "
                        + file.getName() + ", "
                        + file.getTloc() + ", "
                        + file.getTassert() + ", "
                        + file.getTcmp());
                tlocGlobal += file.getTloc();
                tassertGlobal += file.getTassert();
                tcmpGlobal += file.getTcmp();
            }
            tcmpGlobal = tcmpGlobal / tlsFiles.size();
            System.out.println("Total, " + tlocGlobal + ", " + tassertGlobal + ", " + tcmpGlobal);
        } else {
            try {
                File file = new File(csvFile);
                FileWriter output = new FileWriter(file);
                BufferedWriter writer = new BufferedWriter(output);

                writer.write("Relative Path, Package, File Name, TLOC, TASSERT, TCMP, TPP\n");

                Map<String, Integer> testsPerPackage = new HashMap<>();

                for (tlsFile tlsFile : tlsFiles) {
                    String packageName = tlsFile.getPacket();
                    int assertionsInClass = tlsFile.getTassert();
                    testsPerPackage.put(packageName, testsPerPackage.getOrDefault(packageName, 0) + assertionsInClass);
                }

                for (tlsFile tlsFile : tlsFiles) {
                    String packageName = tlsFile.getPacket();
                    int assertionsInPackage = testsPerPackage.get(packageName);

                    writer.write(tlsFile.getRelativePath() + ","
                            + tlsFile.getPacket() + ", "
                            + tlsFile.getName() + ", "
                            + tlsFile.getTloc() + ", "
                            + tlsFile.getTassert() + ", "
                            + tlsFile.getTcmp() + ", "
                            + assertionsInPackage + "\n");
                    tlocGlobal += tlsFile.getTloc();
                    tassertGlobal += tlsFile.getTassert();
                    tcmpGlobal += tlsFile.getTcmp();
                    tppGlobal += assertionsInPackage;
                    
                }
                tassertGlobal = tassertGlobal / tlsFiles.size();
                tcmpGlobal = tcmpGlobal / tlsFiles.size();
                tppGlobal = tppGlobal/ tlsFiles.size();

                writer.write("Total, " + tlocGlobal + ", " + tassertGlobal + ", " + tcmpGlobal+", "+ tppGlobal+"\n");

                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}