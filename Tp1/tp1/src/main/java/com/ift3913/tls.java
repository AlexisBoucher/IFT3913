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

        Path path = Paths.get("..\\jfreechart");
        List<Path> paths = findJavaTestFile(path, ".java");
        paths.forEach(System.out::println);

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
