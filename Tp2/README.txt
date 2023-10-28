Alexis Boucher 20217120
Marguerite Mireille Camara 20143122

Lien GitHub:
https://github.com/AlexisBoucher/IFT3913


-Toutes les métriques utilisé dans le rapport sont accessible à partir du dossier metriques
-Tout les programmes excepter jacoco sont accessible dans le dossier programmes 
-jacoco a été ajouté comme plug-in maven au projet jfreechart.
        la partie ajouté pour faire fonctionner jacoco se trouve entièrement dans le pom.xml de jfreechart.
        le code ajouté au pom.xml provient en grande partie de https://www.baeldung.com/jacoco

Exécution programmes: 
            Javac tloc.java,
            Java tloc <directory du fichier à tester>, 
            Javac tassert.java,
            Java tassert <directory du fichier à tester>,
            Javac tls.java,
            Java tls <chemin-de-l'entrée>,
            Java tls -o <chemin-à-la-sortie.csv> <chemin-de-l'entrée>,
            Javac age.java,
            java age <chemin-de-l'entrée>,
            Java age -o <chemin-à-la-sortie.txt> <chemin-de-l'entrée>,
             .\cloc-1.98.exe --include-lang=java ..\jfreechart\src 
                        (pour tout les détails sur le programme cloc voir https://github.com/AlDanial/cloc)
            & <chemin-acces-à-mvn.cmd> jacoco:report -f <chemin-d'accès-jfreechart>
                        (pour tout les détails sur jacoco voir https://github.com/jacoco/jacoco)