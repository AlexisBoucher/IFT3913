package com.ift3913;


import java.nio.file.Path;


public class tlsFile {
    //info importante
    protected String relativePath;
    protected int Tloc;
    protected int Tassert;
    protected float Tcmp;
    protected Path name;
    protected String packet;

    //constructeur
    public tlsFile(Path path, String directory) {
        this.relativePath= path.toString().substring(directory.length());
        this.name= path.getFileName();
        this.Tloc= tloc.calcul_tloc(path.toString());
        this.Tassert= tassert.calcul_tassert(path.toString());
        if (Tassert!=0){
            this.Tcmp= (float)Tloc /(float)Tassert;
        }
        this.packet= findPackage(path);
    }

    //retourne le nom du paquet d'un fichier base sur la structure d'un projet maven
    public static String findPackage(Path path){
        //le nom du paquet termine par le nom du dossier contenant le fichier
        Path packageEnd = path.getParent();
        //le nom du paquet commence par le nom du dossier contenue dans le dossier java
        Path packageStart = packageEnd;

        //trouve le chemin du dossier java contenant le chemin du fichier choisi
        while (!packageStart.getParent().endsWith("java")){
            packageStart = packageStart.getParent();
        }
        packageStart = packageStart.getParent();

        //enleve le chemin du dossier java au chemin du dossier contenant le fichier choisi
        String packageName = packageEnd.toString().substring(packageStart.toString().length()+1);

        //remplace par des point les \ et / selon windows ou mac
        packageName = packageName.replace("\\",".");
        packageName = packageName.replace("/",".");
        return packageName;
    }

    //getter
    public String getRelativePath() {
        return relativePath;
    }

    public Float getTcmp() {
        return Tcmp;
    }

    public int getTassert() {
        return Tassert;
    }

    public int getTloc() {
        return Tloc;
    }

    public Path getName() {
        return name;
    }

    public String getPacket() {
        return packet;
    }
}
