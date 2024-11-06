package cn.ChengZhiYa.MHDFTools;

import cn.ChengZhiYa.MHDFTools.manager.VersionManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public static Main instance;
    public static VersionManager versionManager;

    @Override
    public void onLoad() {
        instance = this;
        versionManager = new VersionManager();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
