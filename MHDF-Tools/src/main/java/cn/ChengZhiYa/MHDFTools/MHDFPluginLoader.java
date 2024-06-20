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
        //Message
        startDone = "&e[MHDFTools] &f插件启动完毕,您好我的朋友! &eQQ= &f129139830";
        stopDone = "&e[MHDFTools] &f插件卸载完毕,再见我的朋友! &eQQ= &f129139830";

        //Instance
        config = new MHDFConfig();
        asyncCommand = new AsyncCommand();
        initManager = new InitManager();
    }

    public void load() {
        initManager.load();
    }

    public void start() {
        LogUtil.color(getLogo());
        config.loadConfig();
        initManager.start();
        asyncCommand.start();
        LogUtil.color(startDone);

        LogUtil.color("&4开光!\n" +
                "&e////////////////////////////////////////////////////////////////////\n" +
                "&e//                          _ooOoo_                               //\n" +
                "&e//                         o8888888o                              //\n" +
                "&e//                         88\" . \"88                            //\n" +
                "&e//                         (| ^_^ |)                              //\n" +
                "&E//                         O\\  =  /O                             //\n" +
                "&e//                      ____/`---'\\____                          //\n" +
                "&e//                    .'  \\\\|     |//  `.                       //\n" +
                "&e//                   /  \\\\|||  :  |||//  \\                     //\n" +
                "&e//                  /  _||||| -:- |||||-  \\                      //\n" +
                "&e//                  |   | \\\\\\  -  /// |   |                    //\n" +
                "&e//                  | \\_|  ''\\---/''  |   |                     //\n" +
                "&e//                  \\  .-\\__  `-`  ___/-. /                     //\n" +
                "&e//                ___`. .'  /--.--\\  `. . ___                    //\n" +
                "&e//              .\"\" '<  `.___\\_<|>_/___.'  >'\"\".             //\n" +
                "&e//            | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |               //\n" +
                "&e//            \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /             //\n" +
                "&e//      ========`-.____`-.___\\_____/___.-`____.-'========        //\n" +
                "&e//                           `=---='                              //\n" +
                "&e//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //\n" +
                "&e//            &4佛祖保佑       &a永不宕机      &6永无BUG&e            //\n" +
                "&e////////////////////////////////////////////////////////////////////");

    }

    public void stop() {
        initManager.stop();
        closeDatabase();
        LogUtil.color(stopDone);
    }
    private String getLogo() {
        return  "" +
                "&6          _____                    _____                    _____                    _____          \n" +
                "&6         /\\    \\                  /\\    \\                  /\\    \\                  /\\    \\         \n" +
                "&6        /::\\____\\                /::\\____\\                /::\\    \\                /::\\    \\        \n" +
                "&6       /::::|   |               /:::/    /               /::::\\    \\              /::::\\    \\       \n" +
                "&6      /:::::|   |              /:::/    /               /::::::\\    \\            /::::::\\    \\      \n" +
                "&6     /::::::|   |             /:::/    /               /:::/\\:::\\    \\          /:::/\\:::\\    \\     \n" +
                "&6    /:::/|::|   |            /:::/____/               /:::/  \\:::\\    \\        /:::/__\\:::\\    \\    \n" +
                "&6   /:::/ |::|   |           /::::\\    \\              /:::/    \\:::\\    \\      /::::\\   \\:::\\    \\   \n" +
                "&6  /:::/  |::|___|______    /::::::\\    \\   _____    /:::/    / \\:::\\    \\    /::::::\\   \\:::\\    \\  \n" +
                "&6 /:::/   |::::::::\\    \\  /:::/\\:::\\    \\ /\\    \\  /:::/    /   \\:::\\ ___\\  /:::/\\:::\\   \\:::\\    \\ \n" +
                "&6/:::/    |:::::::::\\____\\/:::/  \\:::\\    /::\\____\\/:::/____/     \\:::|    |/:::/  \\:::\\   \\:::\\____\\\n" +
                "&6\\::/    / ~~~~~/:::/    /\\::/    \\:::\\  /:::/    /\\:::\\    \\     /:::|____|\\::/    \\:::\\   \\::/    /\n" +
                "&6 \\/____/      /:::/    /  \\/____/ \\:::\\/:::/    /  \\:::\\    \\   /:::/    /  \\/____/ \\:::\\   \\/____/ \n" +
                "&6             /:::/    /            \\::::::/    /    \\:::\\    \\ /:::/    /            \\:::\\    \\     \n" +
                "&6            /:::/    /              \\::::/    /      \\:::\\    /:::/    /              \\:::\\____\\    \n" +
                "&6           /:::/    /               /:::/    /        \\:::\\  /:::/    /                \\::/    /    \n" +
                "&6          /:::/    /               /:::/    /          \\:::\\/:::/    /                  \\/____/     \n" +
                "&6         /:::/    /               /:::/    /            \\::::::/    /                               \n" +
                "&6        /:::/    /               /:::/    /              \\::::/    /                                \n" +
                "&6        \\::/    /                \\::/    /                \\::/____/                                 \n" +
                "&6         \\/____/                  \\/____/                  ~~                                       \n" +
                "&6                                                                                                    ";
    }
    public String getVersion() {
        return "1.4.11";
    }

    public String getBuild() {
        return "224621-0.13";
    }
}
