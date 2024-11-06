package cn.ChengZhiYa.MHDFTools.util.config;

import cn.ChengZhiYa.MHDFTools.exception.FileException;
import cn.ChengZhiYa.MHDFTools.exception.ResourceException;

import static cn.ChengZhiYa.MHDFTools.util.config.FileUtil.saveResource;

public final class ConfigUtil {
    public static void saveDefaultConfig() throws ResourceException, FileException {
        saveResource("config.yml", "config_zh.yml", false);
    }

    public static void saveDefaultLang() throws ResourceException, FileException {
        saveResource("lang.yml", "lang_zh.yml", false);
    }
}
