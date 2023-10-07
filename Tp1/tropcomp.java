import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

//classe qui imprime toute les classe test java a risque d'etre dans le seuil superieur tloc et tcmp
public class tropcomp {
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

        List<tlsFile> tlsFiles = tls.findJavaTestFile(directory);
        //calcule le nombre de fichier qui devrait etre dans le top seuil%
        int numberInTop = (int)(seuil*tlsFiles.size()/100);

        List<Integer> tlocs = new ArrayList<>();
        List<Float> tcmps = new ArrayList<>();

        for (tlsFile tlsFile:tlsFiles){
            tlocs.add(tlsFile.getTloc());
            tcmps.add(tlsFile.getTcmp());
        }

        //classe tloc et tcmp en ordre decroissant
        tlocs.sort(Collections.reverseOrder());
        tcmps.sort(Collections.reverseOrder());

        //toute valeur superieur a ce nombre sera considere dans le top seuil%
        int minSeuilTlocValue= tlocs.get(numberInTop);
        float minSeuilTcmpValue= tcmps.get(numberInTop);

        //enleve de la file tout les fichiers en bas ou egal au seuil tloc et tcmp
        tlsFiles.removeIf(file -> file.getTloc() <= minSeuilTlocValue || file.getTcmp() <= minSeuilTcmpValue);

        //imprime les donnees si aucun fichier csv donne pour enregistrer les donnees
        tls.saveData(tlsFiles,csvFile,saveInFile);
    }

}