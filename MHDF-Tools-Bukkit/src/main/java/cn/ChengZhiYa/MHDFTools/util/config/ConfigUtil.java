package cn.ChengZhiYa.MHDFTools.util.config;

import cn.ChengZhiYa.MHDFTools.util.download.exception.FilesUtil;
import cn.ChengZhiYa.MHDFTools.util.download.exception.ResourceUtil;

import static cn.ChengZhiYa.MHDFTools.util.config.FileUtil.saveResource;

public final class ConfigUtil {

    public static void saveDefaultConfig() throws ResourceUtil, FilesUtil {
        saveResource("config.yml", "config_zh.yml", false);
    }

    public static void saveDefaultLang() throws ResourceUtil, FilesUtil {
        saveResource("lang.yml", "lang_zh.yml", false);
    }
}
