package cn.ChengZhiYa.MHDFTools.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public final class ResourceAPI {

    public static InputStream getResource(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        URL url = ResourceAPI.class.getClassLoader().getResource(filename);
        if (url == null) {
            return null;
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
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("FilePath cannot be null or empty");
        }
        if (outFileName == null || outFileName.isEmpty()) {
            throw new IllegalArgumentException("OutFileName cannot be null or empty");
        }
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        try (InputStream in = getResource(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found");
            }

            File outFile = new File(filePath, outFileName);
            if (!outFile.exists() || replace) {
                try (FileOutputStream out = new FileOutputStream(outFile)) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save resource to file", e);
        }
    }
}