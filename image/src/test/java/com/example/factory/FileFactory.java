package com.example.factory;

import org.springframework.mock.web.MockMultipartFile;

import java.io.*;
import java.net.URLConnection;

public class FileFactory {

    private final static String JPEG_PATH = "src/test/resources/files/test.jpg";
    private final static String PNG_PATH = "src/test/resources/files/test.png";

    public static MockMultipartFile getFileWithPngFormat() throws IOException {
        return getFileByPath(PNG_PATH);
    }

    public static MockMultipartFile getFileWithJpegFormat() throws IOException {
        return getFileByPath(JPEG_PATH);
    }

    private static MockMultipartFile getFileByPath(String path) throws IOException {
        File file = new File(path);
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
        return new MockMultipartFile("file", file.getName(), mimeType, inputStream);
    }
}
