package cn.ChengZhiYa.MHDFTools.command;

import cn.ChengZhiYa.MHDFTools.MHDFPluginLoader;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.task.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AsyncCommand {
    FileConfiguration getConfig = MHDFTools.instance.getConfig();
    JavaPlugin plugin = MHDFPluginLoader.INSTANCE.getPlugin();

    public void start() {
        asyncCommand(new Scoreboard(), "ScoreboardSettings.Enable");
        asyncCommand(new LoginMessage(), "LoginSystemSettings.Enable");
        asyncCommand(new Fly(), "FlySettings.Enable");
        asyncCommand(new Back(), "BackSettings.Enable");
        asyncCommand(new TpBack(), "TpBackSettings.Enable");
        asyncCommand(new Vanish(), "VanishSettings.Enable");
        asyncCommand(new TpaTime(), "TpaSettings.Enable");
        asyncCommand(new TpaHereTime(), "TpahereSettings.Enable");

        if (getConfig.getBoolean("TimeMessageSettings.Enable")) {
            int delay = getConfig.getInt("TimeMessageSettings.Delay"); //这个是例外 (:
            new TimeMessage().runTaskTimerAsynchronously(plugin, 0L, delay * 20L);
        }
    }

    private void asyncCommand(Runnable task, String configKey) {
        if (getConfig.getBoolean(configKey)) {
            ((BukkitRunnable) task).runTaskTimerAsynchronously(plugin, 0L, 20L);
        }
    }
}