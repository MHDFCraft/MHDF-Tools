package cn.ChengZhiYa.MHDFTools;

import cn.ChengZhiYa.MHDFTools.command.AsyncCommand;
import cn.ChengZhiYa.MHDFTools.config.MHDFConfig;
import cn.ChengZhiYa.MHDFTools.manager.InitManager;
import cn.ChengZhiYa.MHDFTools.utils.message.LogUtil;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.closeDatabase;

@Getter
public enum MHDFPluginLoader {
    INSTANCE;

    public static boolean hasPlaceholderAPI = true;
    public static boolean hasProtocolLib = true;
    public static boolean hasVault = true;
    String start;
    String startDone;
    String stop;
    private MHDFConfig config;
    private JavaPlugin plugin;
    private InitManager initManager;
    private AsyncCommand asyncCommand;

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
        //startMessage
        start = "&f============&6梦回东方-工具&f============";
        startDone = "&e插件启动完成! 作者:292200693"
                + "\n&f============&6梦回东方-工具&f============";
        //Plugin
        config = new MHDFConfig();
        asyncCommand = new AsyncCommand();
        initManager = new InitManager();

        //stopMessage
        stop = "&f============&6梦回东方-工具&f============" +
                "\n&e插件已卸载! 作者:292200693" +
                "\n&f============&6梦回东方-工具&f============";
    }

    public void load() {
        initManager.load();
    }

    public void start() {
        LogUtil.color(start);
        config.loadConfig();
        initManager.start();
        asyncCommand.start();
        // deleteOldPlugins();
        LogUtil.color(startDone);
    }
    public void stop() {
        initManager.stop();
        closeDatabase();
        LogUtil.color(stop);
    }
}
