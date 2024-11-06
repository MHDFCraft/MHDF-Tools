package cn.ChengZhiYa.MHDFTools;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        PluginLoader.INSTANCE.initialize_load(this);
    }

    @Override
    public void onEnable() {
        PluginLoader.INSTANCE.initialize_start(this);
    }

    @Override
    public void onDisable() {
        PluginLoader.INSTANCE.initialize_stop(this);
    }
}
