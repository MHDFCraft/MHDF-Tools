package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.manager.init.Starter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginHooks implements Starter {

    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    @Override
    public void init() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        //PlaceholderAPI
        PluginLoader.INSTANCE.setHasPlaceholderAPI(pluginManager.getPlugin("PlaceholderAPI") != null);

        //你自己写别的
    }
}
