package cn.ChengZhiYa.MHDFTools.task;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.task.player.*;
import cn.ChengZhiYa.MHDFTools.task.server.ServerScoreboardTask;
import cn.ChengZhiYa.MHDFTools.task.server.ServerTimeActionTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.bukkit.plugin.java.JavaPlugin;

public final class AsyncTask {
    FileConfiguration config;
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    public void start() {
        config = PluginLoader.INSTANCE.getPlugin().getConfig();
        asyncCommand(new ServerScoreboardTask(), "ScoreboardSettings.Enable");
        asyncCommand(new ServerTimeActionTask(), "TimeActionSettings.Enable");
        asyncCommand(new PlayerLoginTask(), "LoginSystemSettings.Enable");
        asyncCommand(new PlayerFlightTask(), "FlySettings.Enable");
        asyncCommand(new PlayerBackTask(), "BackSettings.Enable");
        asyncCommand(new PlayerTpBackTask(), "TpBackSettings.Enable");
        asyncCommand(new PlayerVanishTask(), "VanishSettings.Enable");
        asyncCommand(new PlayerTpaTask(), "TpaSettings.Enable");
        asyncCommand(new PlayerTpaHereTask(), "TpaHereSettings.Enable");

        }

    private void asyncCommand(Consumer<ScheduledTask> task, String configKey) {
        if (config.getBoolean(configKey)) {
            //((BukkitRunnable) task).runTaskTimerAsynchronously(plugin, 0L, 20L);
            Bukkit.getAsyncScheduler().runAtFixedRate(plugin, task, 0, 1, TimeUnit.SECONDS);
        }
    }
}