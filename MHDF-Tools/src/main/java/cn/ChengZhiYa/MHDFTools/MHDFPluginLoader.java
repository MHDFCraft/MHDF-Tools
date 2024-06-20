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
    String title;
    String startDone;
    String stopDone;
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
        title = "&f============&6梦之工具&f============";
        startDone = "&e插件启动完成! 作者:292200693";
        stopDone = "&e插件已卸载! 作者:292200693";

        config = new MHDFConfig();
        asyncCommand = new AsyncCommand();
        initManager = new InitManager();
    }

    public void load() {
        initManager.load();
    }

    public void start() {
        LogUtil.color(title);
        config.loadConfig();
        initManager.start();
        asyncCommand.start();
        LogUtil.color(startDone);
        LogUtil.color(title);
    }

    public void stop() {
        LogUtil.color(title);
        initManager.stop();
        closeDatabase();
        LogUtil.color(stopDone);
        LogUtil.color(title);
    }
}
