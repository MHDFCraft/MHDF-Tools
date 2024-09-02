package cn.ChengZhiYa.MHDFTools.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public final class ResourceAPI {

    private ResourceAPI() {
    }

    public static InputStream getResource(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        URL url = ResourceAPI.class.getClassLoader().getResource(filename);
        if (url == null) {
            throw new IllegalArgumentException("Resource not found: " + filename);
        }

        try {
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to open input stream for resource: " + filename, ex);
        }
    }

    public static void saveResource(String filePath, String outFileName, String resourcePath, boolean replace) {
        validateParameters(filePath, outFileName, resourcePath);

        resourcePath = resourcePath.replace('\\', '/');
        File outFile = new File(filePath, outFileName);

        if (outFile.exists() && !replace) {
            return;
        }

        try (InputStream in = getResource(resourcePath);
             FileOutputStream out = new FileOutputStream(outFile)) {

            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found");
            }

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to save resource to file", e);
        }
    }

    private static void validateParameters(String filePath, String outFileName, String resourcePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("FilePath cannot be null or empty");
        }
        if (outFileName == null || outFileName.isEmpty()) {
            throw new IllegalArgumentException("OutFileName cannot be null or empty");
        }
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }
}