package cn.ChengZhiYa.MHDFTools.util.config;

import cn.ChengZhiYa.MHDFTools.exception.FileException;
import cn.ChengZhiYa.MHDFTools.exception.ResourceException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public final class FileUtil {
    /**
     * 创建空文件夹
     *
     * @param file 文件路径
     */
    public static void createFolder(File file) throws FileException {
        if (file.exists()) {
            return;
        }
        if (!file.mkdirs()) {
            throw new FileException("无法创建文件夹");
        }
    }

    /**
     * 创建空文件
     *
     * @param file 文件路径
     */
    public static void createFile(File file) throws FileException {
        if (file.exists()) {
            return;
        }
        try {
            if (!file.createNewFile()) {
                throw new FileException("无法创建文件");
            }
        } catch (IOException e) {
            throw new FileException(e);
        }
    }

    /**
     * 保存资源
     *
     * @param filePath     保存目录
     * @param resourcePath 资源目录
     * @param replace      替换文件
     */
    public static void saveResource(@NotNull String filePath, @NotNull String resourcePath, boolean replace) throws ResourceException {
        File file = new File(ConfigUtil.getDataFolder(), filePath);
        if (file.exists() && !replace) {
            return;
        }

        URL url = FileUtil.class.getClassLoader().getResource(resourcePath);
        if (url == null) {
            throw new ResourceException("找不到资源: " + resourcePath);
        }

        URLConnection connection;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connection.setUseCaches(false);

        try (InputStream in = url.openStream()) {
            try (FileOutputStream out = new FileOutputStream(file)) {
                if (in == null) {
                    throw new ResourceException("读取资源 " + resourcePath + " 的时候发生了错误");
                }

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        } catch (IOException e) {
            throw new ResourceException("无法保存资源", e);
        }
    }
}
