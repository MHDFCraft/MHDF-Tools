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

    private static MHDFConfig instance;
    private final File dataFolder = PluginLoader.INSTANCE.getPlugin().getDataFolder();
    private final File[] filesToSave = {
            new File("config.yml"),
            new File("lang.yml"),
            new File("sound.yml")
    };

    public static MHDFConfig getInstance() {
        return instance == null ? (instance = new MHDFConfig()) : instance;
    }

    public void loadConfig() {
        createDir(dataFolder);
        saveResources();
        loadConfigurations();
        handleSpecialFiles();
    }

    private void saveResources() {
        for (File file : filesToSave) {
            saveResource(dataFolder.getPath(), file.getPath(), file.getPath(), false);
        }
    }

    private void loadConfigurations() {
        PluginLoader.INSTANCE.getPlugin().reloadConfig();

        File langFile = new File(dataFolder, "lang.yml");
        LangFileData = YamlConfiguration.loadConfiguration(langFile);

        File soundFile = new File(dataFolder, "sound.yml");
        SoundFileData = YamlConfiguration.loadConfiguration(soundFile);
    }

    private void handleSpecialFiles() {
        if (isHomeSystemOrMenuEnabled()) {
            handleMenuFiles();
        }

        if (isVanishSettingsEnabled()) {
            FileCreator.createFile(new File(dataFolder, "Cache/VanishCache.yml"));
        }
    }

    private boolean isHomeSystemOrMenuEnabled() {
        return PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("HomeSystemSettings.Enable")
                || PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("MenuSettings.Enable");
    }

    private void handleMenuFiles() {
        File menusDir = new File(dataFolder, "Menus");
        if (!menusDir.exists()) {
            createDir(menusDir);
            if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("MenuSettings.Enable")) {
                saveResource(dataFolder.getPath(), "Menus/CustomMenu.yml", "Menus/CustomMenu.yml", false);
            }
        }

        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("HomeSystemSettings.Enable")) {
            saveResource(dataFolder.getPath(), "Menus/HomeMenu.yml", "Menus/HomeMenu.yml", false);
        }
    }

    private boolean isVanishSettingsEnabled() {
        return PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("VanishSettings.Enable")
                && PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("VanishSettings.SaveVanishData");
    }
}