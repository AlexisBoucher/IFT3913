//look in jfreechart directory and print all relatives path of java test file
package com.ift3913;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class tls {
    public static void main(String[] args) throws IOException {
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

    }

    public static List<Path> findJavaTestFile(Path path, String fileExtension)
            throws IOException {

        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk
                    .filter(Files::isRegularFile)   // is a file
                    .filter(p -> p.getFileName().toString().endsWith(fileExtension))
                    .filter(p -> p.getFileName().toString().toLowerCase().contains("test"))
                    .collect(Collectors.toList());
        }
        return result;

    }


}
