package cn.ChengZhiYa.MHDFTools.util.network;

import cn.ChengZhiYa.MHDFTools.util.download.exception.DownloadUtil;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public final class HttpUtil {
    public static URLConnection openConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(5));
        connection.setReadTimeout((int) TimeUnit.SECONDS.toMillis(5));
        return connection;
    }

    public static void downloadFile(URLConnection connection, Path savePath) throws DownloadUtil {
        try {
            Files.write(savePath, downloadFile(connection));
        } catch (IOException e) {
            throw new DownloadUtil(e);
        }
    }

    private static byte[] downloadFile(URLConnection connection) throws DownloadUtil {
        try {
            try (InputStream in = connection.getInputStream()) {
                byte[] bytes = ByteStreams.toByteArray(in);
                if (bytes.length == 0) {
                    throw new DownloadUtil("无可下载文件");
                }
                return bytes;
            }
        } catch (Exception e) {
            throw new DownloadUtil(e);
        }
    }
}
