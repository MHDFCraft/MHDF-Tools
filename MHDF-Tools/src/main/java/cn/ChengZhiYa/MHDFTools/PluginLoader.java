package cn.ChengZhiYa.MHDFTools;

import cn.ChengZhiYa.MHDFTools.config.MHDFConfig;
import cn.ChengZhiYa.MHDFTools.manager.InitManager;
import cn.ChengZhiYa.MHDFTools.manager.ServerManager;
import cn.ChengZhiYa.MHDFTools.task.AsyncTask;
import cn.ChengZhiYa.MHDFTools.utils.message.ColorLogs;
import com.github.Anon8281.universalScheduler.foliaScheduler.FoliaScheduler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.closeDatabase;

@Getter
public enum PluginLoader {
    INSTANCE;
    @Setter
    private boolean hasPlaceholderAPI;
    @Setter
    private boolean hasVault;

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
        serverManager = new ServerManager();
        asyncTask = new AsyncTask();
        config = MHDFConfig.getInstance();
        initManager = new InitManager();
    }

    public void load() {
        initManager.load();
    }

    public void start() {
        printLogo();
        config.loadConfig();
        initManager.start();
        asyncTask.start();
        ColorLogs.consoleMessage("&f[MHDF-Tools] &a插件加载完成!");
        ColorLogs.consoleMessage("&f[MHDF-Tools] &a欢迎使用梦东系列插件 交流群号:129139830");
    }

    public void stop() {
        printLogo();
        new FoliaScheduler(plugin).cancelTasks();
        initManager.stop();
        closeDatabase();
        ColorLogs.consoleMessage("&f[MHDF-Tools] &9插件已卸载! 感谢您再次支持!");
        ColorLogs.consoleMessage("&f[MHDF-Tools] &9梦东系列插件 交流群号:129139830");
    }

    private void printLogo() {
        ColorLogs.consoleMessage("&f[MHDF-Tools] &d" + "  __  __ _    _ _____  ______ _______          _");
        ColorLogs.consoleMessage("&f[MHDF-Tools] &d" + " |  \\/  | |  | |  __ \\|  ____|__   __|        | |");
        ColorLogs.consoleMessage("&f[MHDF-Tools] &d" + " | \\  / | |__| | |  | | |__     | | ___   ___ | |___");
        ColorLogs.consoleMessage("&f[MHDF-Tools] &d" + " | |\\/| |  __  | |  | |  __|    | |/ _ \\ / _ \\| / __|");
        ColorLogs.consoleMessage("&f[MHDF-Tools] &d" + " | |  | | |  | | |__| | |       | | (_) | (_) | \\__ \\");
        ColorLogs.consoleMessage("&f[MHDF-Tools] &d" + " |_|  |_|_|  |_|_____/|_|       |_|\\___/ \\___/|_|___/");
        ColorLogs.consoleMessage("&f[MHDF-Tools] &d");
    }

    public String getVersion() {
        return getPlugin().getDescription().getVersion();
    }

}
