package cn.ChengZhiYa.MHDFTools.util;

import cn.ChengZhiYa.MHDFTools.MHDFTools;

import java.io.File;
import java.io.IOException;

public final class FileUtil {
    public static void createDir(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void createDir(String path) {
        File file = new File(MHDFTools.instance.getDataFolder(), path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void createFile(String path) {
        File file = new File(MHDFTools.instance.getDataFolder(), path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
    }
}
