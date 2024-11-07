package cn.ChengZhiYa.MHDFTools.util.config;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.exception.ResourceException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public final class ConfigUtil {
    /**
     * 获取插件数据文件夹
     */
    public static File getDataFolder() {
        return Main.instance.getDataFolder();
    }

    /**
     * 获取配置文件实例
     */
    public static FileConfiguration getConfig() {
        return Main.instance.getConfig();
    }

    /**
     * 重新加载配置文件
     */
    public static void reloadConfig() {
        Main.instance.getConfig();
    }

    /**
     * 保存初始配置文件
     */
    public static void saveDefaultConfig() throws ResourceException {
        FileUtil.saveResource("config.yml", "config_zh.yml", false);
        reloadConfig();
    }
}
