package cn.ChengZhiYa.MHDFTools.config;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.file.FileCreator;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static cn.ChengZhiYa.MHDFTools.api.ResourceAPI.saveResource;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.LangFileData;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.SoundFileData;
import static cn.ChengZhiYa.MHDFTools.utils.file.FileCreator.createDir;

public class MHDFConfig {
    final File getDataFolder = PluginLoader.INSTANCE.getPlugin().getDataFolder();

    public void loadConfig() {
        createFile();
    }

    public void createFile() {
        createDir(getDataFolder);
        saveResource(getDataFolder.getPath(), "config.yml", "config.yml", false);
        PluginLoader.INSTANCE.getPlugin().reloadConfig(); //配置

        File Lang_File = new File(getDataFolder, "lang.yml");
        saveResource(getDataFolder.getPath(), "lang.yml", "lang.yml", false);
        LangFileData = YamlConfiguration.loadConfiguration(Lang_File); //语言

        File Sound_File = new File(getDataFolder, "sound.yml");
        saveResource(getDataFolder.getPath(), "sound.yml", "sound.yml", false);
        SoundFileData = YamlConfiguration.loadConfiguration(Sound_File); //音效系统

        createDir(new File(getDataFolder, "Cache")); //缓存

        //家系统菜单与菜单系统
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("HomeSystemSettings.Enable")
                || PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("MenuSettings.Enable")) {
            if (!new File(getDataFolder, "Menus").exists()) {
                createDir(new File(getDataFolder, "Menus"));
                if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("MenuSettings.Enable")) {
                    saveResource(PluginLoader.INSTANCE.getPlugin().getDataFolder().getPath(), "Menus/CustomMenu.yml", "Menus/CustomMenu.yml", false);
                }
            }
            //家系统
            if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("HomeSystemSettings.Enable")) {
                saveResource(PluginLoader.INSTANCE.getPlugin().getDataFolder().getPath(), "Menus/HomeMenu.yml", "Menus/HomeMenu.yml", false);
            }
        }

        //隐身系统
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("VanishSettings.Enable")
                && PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("VanishSettings.SaveVanishData")) {
            FileCreator.createFile("Cache/VanishCache.yml");
        }
    }
}