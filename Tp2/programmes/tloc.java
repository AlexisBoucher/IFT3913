import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

 /*Tloc : cette classe affiche le nombre de lignes de code non-vides qui ne sont pas de commentaires dans un fichier. Il doit juste sortir la valeur du TLOC à
la ligne de commandes */
public class tloc {
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.exit(1);
        }
    
        String filePath = args[0];
        int tloc = calcul_tloc(filePath);
        System.out.println("TLOC : " + tloc);
    }

    /**
     * 
     * @param dir
     * @return tloc:nombre de ligne de code excluant les commentaires
     */
    public static int calcul_tloc (String dir) {
        int tloc = 0;

        try (BufferedReader file_reader = new BufferedReader(new FileReader(dir))) {
            boolean comment_line = false;
            String file_line;

            while ((file_line = file_reader.readLine()) != null) { 
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

