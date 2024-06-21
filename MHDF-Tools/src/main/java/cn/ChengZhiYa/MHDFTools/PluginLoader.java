package cn.ChengZhiYa.MHDFTools;

import cn.ChengZhiYa.MHDFTools.config.MHDFConfig;
import cn.ChengZhiYa.MHDFTools.manager.InitManager;
import cn.ChengZhiYa.MHDFTools.manager.ServerManager;
import cn.ChengZhiYa.MHDFTools.task.AsyncTask;
import cn.ChengZhiYa.MHDFTools.utils.message.LogUtil;
import lombok.Getter;
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
        asyncTask = new AsyncTask();
        config = new MHDFConfig();
        initManager = new InitManager();
        serverManager = new ServerManager();
    }

    public void load() {
        initManager.load();
    }

    public void start() {
        printLogo("&f[MHDF-Tools] &d");
//        serverManager.unSupportServer();
        config.loadConfig();
        initManager.start();
        asyncTask.start();
        LogUtil.color("&f[MHDF-Tools] &a插件加载完成!");
        LogUtil.color("&f[MHDF-Tools] &a欢迎使用梦东系列插件 交流群号:129139830");
    }

    public void stop() {
        printLogo("&f[MHDF-Tools] &d");
        initManager.stop();
        closeDatabase();
        LogUtil.color("&f[MHDF-Tools] &9插件已卸载! 感谢您再次支持!");
        LogUtil.color("&f[MHDF-Tools] &9梦东系列插件 交流群号:129139830");
    }

    private void printLogo(String prefix) {
        LogUtil.color(prefix + "  __  __ _    _ _____  ______ _______          _");
        LogUtil.color(prefix + " |  \\/  | |  | |  __ \\|  ____|__   __|        | |");
        LogUtil.color(prefix + " | \\  / | |__| | |  | | |__     | | ___   ___ | |___");
        LogUtil.color(prefix + " | |\\/| |  __  | |  | |  __|    | |/ _ \\ / _ \\| / __|");
        LogUtil.color(prefix + " | |  | | |  | | |__| | |       | | (_) | (_) | \\__ \\");
        LogUtil.color(prefix + " |_|  |_|_|  |_|_____/|_|       |_|\\___/ \\___/|_|___/");
        LogUtil.color(prefix);
    }
    public String getVersion() {
        return "2.0.0";
    }

    public String getBuild() {
        return "224621-13.26";
    }

    public void disablePlugin() {
        if (plugin != null) {
            plugin.getPluginLoader().disablePlugin(plugin);
        } else {
            LogUtil.color("&e[MHDFTools] &cPlugin Not Found!");
        }
    }
}
