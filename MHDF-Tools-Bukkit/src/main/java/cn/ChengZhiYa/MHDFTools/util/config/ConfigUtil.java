package cn.ChengZhiYa.MHDFTools.util.config;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.exception.FileException;
import cn.ChengZhiYa.MHDFTools.exception.ResourceException;

import java.io.File;

import static cn.ChengZhiYa.MHDFTools.util.config.FileUtil.saveResource;

public final class ConfigUtil {
    /**
     * 获取插件数据文件夹
     */
    public static File getDataFolder() {
        return Main.instance.getDataFolder();
    }

    /**
     * 保存初始配置文件
     */
    public static void saveDefaultConfig() throws ResourceException, FileException {
        saveResource("config.yml", "config_zh.yml", false);
    }
}
