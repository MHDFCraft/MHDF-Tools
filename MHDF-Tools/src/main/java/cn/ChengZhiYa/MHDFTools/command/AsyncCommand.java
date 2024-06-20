package cn.ChengZhiYa.MHDFTools.command;

import cn.ChengZhiYa.MHDFTools.MHDFPluginLoader;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.task.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class AsyncCommand  {
    FileConfiguration getConfig = MHDFTools.instance.getConfig();
    JavaPlugin plugin = MHDFPluginLoader.INSTANCE.getPlugin();
    public void start() {
        if (getConfig.getBoolean("ScoreboardSettings.Enable")) {
            new Scoreboard().runTaskTimerAsynchronously(plugin, 0L, 20L);
        }
        if (getConfig.getBoolean("LoginSystemSettings.Enable")) {
            new LoginMessage().runTaskTimerAsynchronously(plugin, 0L, 20L);
        }
        if (getConfig.getBoolean("FlySettings.Enable")) {
            new Fly().runTaskTimerAsynchronously(plugin, 0L, 20L);
        }
        if (getConfig.getBoolean("BackSettings.Enable")) {
            new Back().runTaskTimerAsynchronously(plugin, 0L, 20L);
        }
        if (getConfig.getBoolean("TpBackSettings.Enable")) {
            new TpBack().runTaskTimerAsynchronously(plugin, 0L, 20L);
        }
        if (getConfig.getBoolean("VanishSettings.Enable")) {
            new Vanish().runTaskTimerAsynchronously(plugin, 0L, 20L);
        }
        if (getConfig.getBoolean("TpaSettings.Enable")) {
            new TpaTime().runTaskTimerAsynchronously(plugin, 0L, 20L);
        }
        if (getConfig.getBoolean("TpahereSettings.Enable")) {
            new TpaHereTime().runTaskTimerAsynchronously(plugin, 0L, 20L);
        }
        if (getConfig.getBoolean("TimeMessageSettings.Enable")) {
            new TimeMessage().runTaskTimerAsynchronously(plugin, 0L, getConfig.getInt("TimeMessageSettings.Delay") * 20L);
        }
    }
}
