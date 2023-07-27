package dev.pcloud.docletmarkdown;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
    public static void createDirectory(String path) {
        Path interfacePath = Path.of(path);
        try {
            Files.createDirectories(interfacePath);
            System.out.println("Directory created successfully at " + interfacePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to create directory at" + interfacePath);
            e.printStackTrace();
        }
    }

    public static void writeFile(String path, String content) {
        Path filePath = Path.of(path);
        try {
            Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
            System.out.println("Content written successfully to " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to write content to file at " + filePath.toAbsolutePath());
            e.printStackTrace();
        }
    }
}
