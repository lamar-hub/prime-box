package com.lamar.primebox.web.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class StorageUtil {

    public static void saveFileToDisk(MultipartFile multipartFile, String fileId, String path) throws IOException {
        final Path filePath = Path.of(path, fileId);
        Files.copy(multipartFile.getInputStream(), filePath);
    }

    public static void deleteFileFromDisk(String fileId, String path) throws IOException {
        final Path filePath = Path.of(path, fileId);

        Files.delete(filePath);
    }

    public static String randomCode() {
        final Random random = new Random();

        return String.format("%04d", random.nextInt(10000));
    }

}
