package cn.ChengZhiYa.MHDFTools.utils.file;

import cn.ChengZhiYa.MHDFTools.PluginLoader;

import java.io.File;
import java.io.IOException;

public final class FileCreator {

    private static File getDataFile(String path) {
        return new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), path);
    }

    public static void createDir(File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static void createDir(String path) {
        File directory = getDataFile(path);
        createDir(directory);
    }

    public static void createFile(String path) {
        File file = getDataFile(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}