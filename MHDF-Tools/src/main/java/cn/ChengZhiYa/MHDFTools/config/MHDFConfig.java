package cn.ChengZhiYa.MHDFTools.config;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.file.fileCreator;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static cn.ChengZhiYa.MHDFTools.api.YamlAPI.saveResource;
import static cn.ChengZhiYa.MHDFTools.utils.Util.*;
import static cn.ChengZhiYa.MHDFTools.utils.file.fileCreator.createDir;

public class MHDFConfig {
    final File getDataFolder = MHDFTools.instance.getDataFolder();

    public void loadConfig() {
        createFile();
        createVanishCache();
    }

    public void createFile() {
        createDir(getDataFolder);
        saveResource(getDataFolder.getPath(), "config.yml", "config.yml", false);
        MHDFTools.instance.reloadConfig(); //配置

        File Lang_File = new File(getDataFolder, "lang.yml");
        saveResource(getDataFolder.getPath(), "lang.yml", "lang.yml", false);
        LangFileData = YamlConfiguration.loadConfiguration(Lang_File); //语言

        File Sound_File = new File(getDataFolder, "sound.yml");
        saveResource(getDataFolder.getPath(), "sound.yml", "sound.yml", false);
        SoundFileData = YamlConfiguration.loadConfiguration(Sound_File); //音效系统

        createDir(new File(getDataFolder, "Cache")); //缓存

        //家系统菜单与菜单系统
        if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")
                || MHDFTools.instance.getConfig().getBoolean("MenuSettings.Enable")) {
            createDir(new File(getDataFolder, "Menus"));
            if (MHDFTools.instance.getConfig().getBoolean("MenuSettings.Enable")) {
                saveResource(MHDFTools.instance.getDataFolder().getPath(), "Menus/CustomMenu.yml", "Menus/CustomMenu.yml", false);
            }
            //家系统
            if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
                saveResource(MHDFTools.instance.getDataFolder().getPath(), "Menus/HomeMenu.yml", "Menus/HomeMenu.yml", false);
            }
            //隐身系统
            if (MHDFTools.instance.getConfig().getBoolean("VanishSettings.Enable")
                    && MHDFTools.instance.getConfig().getBoolean("VanishSettings.SaveVanishData")) {
                fileCreator.createFile("Cache/VanishCache.yml");
            }
        }
    }


    public void createVanishCache() {
        if (MHDFTools.instance.getConfig().getBoolean("VanishSettings.SaveVanishData")) {

            File VanishCacheFile = new File(getDataFolder, "Cache/VanishCache.yml");

            if (VanishCacheFile.exists()) {
                YamlConfiguration VanishCache = YamlConfiguration.loadConfiguration(VanishCacheFile);

                if (VanishCache.get("VanishList") != null) {
                    VanishList = VanishCache.getStringList("VanishList");
                }
            }
        }
    }
}