package cn.ChengZhiYa.MHDFTools;

import cn.ChengZhiYa.MHDFTools.config.MHDFConfig;
import cn.ChengZhiYa.MHDFTools.manager.InitManager;
import cn.ChengZhiYa.MHDFTools.manager.ServerManager;
import cn.ChengZhiYa.MHDFTools.task.AsyncTask;
import cn.ChengZhiYa.MHDFTools.task.server.ServerScoreboardTask;
import cn.ChengZhiYa.MHDFTools.utils.message.LogUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.closeDatabase;

@Getter
public enum PluginLoader {
    INSTANCE;

    public static boolean hasPlaceholderAPI = true;
    public static boolean hasVault = true;

    private MHDFConfig config;
    private JavaPlugin plugin;
    private InitManager initManager;
    private ServerManager serverManager;
    private AsyncTask asyncTask;
    private ServerScoreboardTask serverScoreboardTask;

    public void initialize_load(JavaPlugin plugin) {
        this.plugin = plugin;
        initManagers();
        load();
    }

    public void initialize_start(JavaPlugin plugin) {
        this.plugin = plugin;
        start();
    }

    public void initialize_stop(JavaPlugin plugin) {
        this.plugin = plugin;
        stop();
    }

    private void initManagers() {
        //Instance
        serverManager = new ServerManager();
        asyncTask = new AsyncTask();
        config = new MHDFConfig();
        initManager = new InitManager();
        serverScoreboardTask = new ServerScoreboardTask();
    }

    public void load() {
        initManager.load();
    }

    public void start() {
        printLogo();
        config.loadConfig();
        initManager.start();
        asyncTask.start();
        LogUtil.color("&f[MHDF-Tools] &a插件加载完成!");
        LogUtil.color("&f[MHDF-Tools] &a欢迎使用梦东系列插件 交流群号:129139830");
    }

    public void stop() {
        Bukkit.getAsyncScheduler().cancelTasks(PluginLoader.INSTANCE.plugin);
        Bukkit.getGlobalRegionScheduler().cancelTasks(PluginLoader.INSTANCE.plugin);
        printLogo();
        initManager.stop();
        closeDatabase();
        LogUtil.color("&f[MHDF-Tools] &9插件已卸载! 感谢您再次支持!");
        LogUtil.color("&f[MHDF-Tools] &9梦东系列插件 交流群号:129139830");
    }

    private void printLogo() {
        LogUtil.color("&f[MHDF-Tools] &d" + "  __  __ _    _ _____  ______ _______          _");
        LogUtil.color("&f[MHDF-Tools] &d" + " |  \\/  | |  | |  __ \\|  ____|__   __|        | |");
        LogUtil.color("&f[MHDF-Tools] &d" + " | \\  / | |__| | |  | | |__     | | ___   ___ | |___");
        LogUtil.color("&f[MHDF-Tools] &d" + " | |\\/| |  __  | |  | |  __|    | |/ _ \\ / _ \\| / __|");
        LogUtil.color("&f[MHDF-Tools] &d" + " | |  | | |  | | |__| | |       | | (_) | (_) | \\__ \\");
        LogUtil.color("&f[MHDF-Tools] &d" + " |_|  |_|_|  |_|_____/|_|       |_|\\___/ \\___/|_|___/");
        LogUtil.color("&f[MHDF-Tools] &d");
    }

    public String getVersion() {
        return getPlugin().getDescription().getVersion();
    }
}
