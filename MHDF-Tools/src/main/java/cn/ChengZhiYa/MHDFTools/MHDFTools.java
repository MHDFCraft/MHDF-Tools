package cn.ChengZhiYa.MHDFTools;

import org.bukkit.plugin.java.JavaPlugin;

public final class MHDFTools extends JavaPlugin {

    public static MHDFTools instance;

    @Override
    public void onLoad() {
        instance = this;
        MHDFPluginLoader.INSTANCE.initialize_load(this);
    }

    @Override
    public void onEnable() {
        MHDFPluginLoader.INSTANCE.initialize_start(this);
    }

    @Override
    public void onDisable() {
        MHDFPluginLoader.INSTANCE.initialize_stop(this);
        instance = null;
    }
}
