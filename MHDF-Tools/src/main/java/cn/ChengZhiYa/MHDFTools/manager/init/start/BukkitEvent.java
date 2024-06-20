package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.MHDFPluginLoader;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.listeners.player.PlayerJoinListener;
import cn.ChengZhiYa.MHDFTools.listeners.player.PlayerSpawnListener;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerJoinLeaveMessageListener;
import cn.ChengZhiYa.MHDFTools.listeners.server.menu.ClickCustomMenu;
import cn.ChengZhiYa.MHDFTools.listeners.server.menu.MenuArgsCommand;
import cn.ChengZhiYa.MHDFTools.listeners.server.menu.OpenMenu;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitEvent implements Invitable {
    JavaPlugin plugin = MHDFPluginLoader.INSTANCE.getPlugin();

    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new ServerJoinLeaveMessageListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerSpawnListener(), plugin);
        //     Bukkit.getPluginManager().registerEvents(new PlayerChatEvent(), plugin);

        if (!MHDFTools.instance.getConfig().getBoolean("MenuEnable")) return; //menu Window Event (:

        Bukkit.getPluginManager().registerEvents(new OpenMenu(), plugin);
        Bukkit.getPluginManager().registerEvents(new ClickCustomMenu(), plugin);
        Bukkit.getPluginManager().registerEvents(new MenuArgsCommand(), plugin);
    }
}
