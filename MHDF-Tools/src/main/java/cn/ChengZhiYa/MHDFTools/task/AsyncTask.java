package cn.ChengZhiYa.MHDFTools.task;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.task.player.*;
import cn.ChengZhiYa.MHDFTools.task.server.ServerTimeActionTask;
import com.github.Anon8281.universalScheduler.UniversalRunnable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class AsyncTask {
    FileConfiguration config;
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    public void start() {
        config = PluginLoader.INSTANCE.getPlugin().getConfig();
        asyncTask(new ServerTimeActionTask(), "TimeActionSettings.Enable");
        asyncTask(new PlayerLoginTask(), "LoginSystemSettings.Enable");
        asyncTask(new PlayerFlightTask(), "FlySettings.Enable");
        asyncTask(new PlayerBackTask(), "BackSettings.Enable");
        asyncTask(new PlayerTpBackTask(), "TpBackSettings.Enable");
        asyncTask(new PlayerVanishTask(), "VanishSettings.Enable");
        asyncTask(new PlayerTpaTask(), "TpaSettings.Enable");
        asyncTask(new PlayerTpaHereTask(), "TpaHereSettings.Enable");
    }

    private void asyncTask(UniversalRunnable task, String configKey) {
        if (config.getBoolean(configKey)) {
            task.runTaskTimerAsynchronously(plugin, 0L, 1L);
        }
    }
}