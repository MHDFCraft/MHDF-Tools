package cn.ChengZhiYa.MHDFTools.task;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.task.player.*;
import cn.ChengZhiYa.MHDFTools.task.server.ServerScoreboardTask;
import cn.ChengZhiYa.MHDFTools.task.server.ServerTimeActionTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class AsyncTask {
    FileConfiguration config;
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    public void start() {
        config = PluginLoader.INSTANCE.getPlugin().getConfig();
        asyncTask(new ServerScoreboardTask(), "ScoreboardSettings.Enable");
        asyncTask(new ServerTimeActionTask(), "TimeActionSettings.Enable");
        asyncTask(new PlayerLoginTask(), "LoginSystemSettings.Enable");
        asyncTask(new PlayerFlightTask(), "FlySettings.Enable");
        asyncTask(new PlayerBackTask(), "BackSettings.Enable");
        asyncTask(new PlayerTpBackTask(), "TpBackSettings.Enable");
        asyncTask(new PlayerVanishTask(), "VanishSettings.Enable");
        asyncTask(new PlayerTpaTask(), "TpaSettings.Enable");
        asyncTask(new PlayerTpaHereTask(), "TpaHereSettings.Enable");
    }

    private void asyncTask(Consumer<ScheduledTask> task, String configKey) {
        if (config.getBoolean(configKey)) {
            Bukkit.getAsyncScheduler().runAtFixedRate(plugin, task, 0, 1, TimeUnit.SECONDS);
        }
    }
}