package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.listeners.server.menu.ClickCustomMenu;
import cn.ChengZhiYa.MHDFTools.listeners.server.menu.MenuArgsCommand;
import cn.ChengZhiYa.MHDFTools.listeners.server.menu.OpenMenu;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Menu implements Invitable {
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    @Override
    public void init() {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("MenuSettings.Enable")) {
            Bukkit.getPluginManager().registerEvents(new OpenMenu(), plugin);
            Bukkit.getPluginManager().registerEvents(new ClickCustomMenu(), plugin);
            Bukkit.getPluginManager().registerEvents(new MenuArgsCommand(), plugin);
        }
    }
}
