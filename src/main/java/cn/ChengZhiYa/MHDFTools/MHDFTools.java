package cn.ChengZhiYa.MHDFTools;

import cn.ChengZhiYa.MHDFTools.Commands.Back;
import cn.ChengZhiYa.MHDFTools.Commands.TpBack;
import cn.ChengZhiYa.MHDFTools.Commands.Vanish;
import cn.ChengZhiYa.MHDFTools.Commands.*;
import cn.ChengZhiYa.MHDFTools.HashMap.BooleanHasMap;
import cn.ChengZhiYa.MHDFTools.Hook.EconomyImplementer;
import cn.ChengZhiYa.MHDFTools.Hook.Metrics;
import cn.ChengZhiYa.MHDFTools.Hook.PlaceholderAPI;
import cn.ChengZhiYa.MHDFTools.Listeners.*;
import cn.ChengZhiYa.MHDFTools.Listeners.Menu.ClickCustomMenu;
import cn.ChengZhiYa.MHDFTools.Listeners.Menu.HomeMenu;
import cn.ChengZhiYa.MHDFTools.Listeners.Menu.MenuArgsCommand;
import cn.ChengZhiYa.MHDFTools.Listeners.Menu.OpenMenu;
import cn.ChengZhiYa.MHDFTools.Tasks.*;
import com.alibaba.fastjson.parser.ParserConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.milkbowl.vault.economy.Economy;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.TimeZone;

import static cn.ChengZhiYa.MHDFTools.Utils.Util.*;
import static cn.ChengZhiYa.MHDFTools.Utils.YamlFileUtil.SaveResource;

public final class MHDFTools extends JavaPlugin implements Listener {
    public static final String Version = "1.3.5";
    public static MHDFTools instance;
    public static boolean PAPI = true;
    public static boolean PLIB = true;
    public static boolean Vault = true;
    public static PluginDescriptionFile descriptionFile;
    public static Statement statement;
    public static HikariDataSource dataSource;

    public static PluginDescriptionFile getDescriptionFile() {
        return descriptionFile;
    }

    @Override
    public void onLoad() {
        instance = this;
        descriptionFile = getDescription();

        ParserConfig.getGlobalInstance().setSafeMode(true);
    }

    @Override
    public void onEnable() {
        ColorLog("&f============&6梦回东方-工具&f============");

        //bstats
        if (getConfig().getBoolean("bStats")) {
            new Metrics(this, 17154);
        }

        {
            if (getDataFolder().getParentFile().isDirectory()) {
                for (File PluginFile : Objects.requireNonNull(getDataFolder().getParentFile().listFiles())) {
                    if (PluginFile.isFile() && PluginFile.getName().contains("MHDF-Tools") && PluginFile.getName().endsWith(".jar")) {
                        String[] PluginName = PluginFile.getName().split("MHDF-Tools");
                        if (!PluginName[1].equals("-" + Version + ".jar")) {
                            PluginFile.delete();
                        }
                    }
                }
            }
        }

        //功能可用检查
        {
            if (!ifSupportGetTps()) {
                ColorLog("&e服务端不是Paper或是服务器版本较旧，已关闭自带TPS变量!");
            }

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
                ColorLog("&c找不到PlaceholderAPI,已关闭PAPI变量解析系统!");
                PAPI = false;
            }

            if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
                ColorLog("&c找不到ProtocolLib,无法启用崩端系统!");
                PLIB = false;
            }

            if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
                ColorLog("&c找不到Vault,无法启用经济系统!");
                Vault = false;
            }
        }

        //更新检测
        if (getConfig().getBoolean("CheckVersion")) {
            try {
                URL url1 = new URL("http://gb95351e.dll.z-j.wang/Cheng-Tools-Reloaded-CheckVersion.html");
                URLConnection urlConnection = url1.openConnection();
                urlConnection.addRequestProperty("User-Agent", "Mozilla");
                urlConnection.setReadTimeout(5000);
                urlConnection.setConnectTimeout(5000);
                InputStream in = url1.openStream();
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader bufr = new BufferedReader(isr);
                String str;
                StringBuilder stringBuilder = new StringBuilder();
                while ((str = bufr.readLine()) != null) {
                    stringBuilder.append(str);
                }
                String NewVersionString = stringBuilder.toString().replace("<!--", "").replace("-->", "");
                if (!NewVersionString.equals(Version)) {
                    ColorLog("&c当前插件版本不是最新版! 下载链接:https://github.com/Love-MHDF/MHDF-Tools/releases/");
                    if (getConfig().getBoolean("AutoUpdate")) {
                        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                            try {
                                int cache = 10 * 1024;
                                CloseableHttpClient httpClient = HttpClients.createDefault();
                                RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).
                                        setConnectionRequestTimeout(5000)
                                        .setSocketTimeout(5000)
                                        .setRedirectsEnabled(true)
                                        .build();
                                HttpGet httpGet = new HttpGet("https://github.moeyy.xyz/https://github.com/BaiShenYaoDog/Cheng-Tools-Reloaded/releases/download/" + NewVersionString + "/Cheng-Tools-Reloaded-" + NewVersionString + ".jar");
                                httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54");
                                httpGet.setConfig(requestConfig);
                                CloseableHttpResponse response = httpClient.execute(httpGet);
                                FileOutputStream fileOutputStream = getOutputStream(response, NewVersionString, cache);
                                fileOutputStream.close();
                                ChatColor("&c自动更新完成,下次重启时生效!");
                                ChatColor("&a请手动删除旧版本插件,谢谢!");
                            } catch (Exception ignored) {
                                ChatColor("&c自动更新失败!");
                            }
                        });
                    }
                    BooleanHasMap.getHasMap().put("IsLast", true);
                } else {
                    ColorLog("&a当前插件版本是最新版!");
                }
                BooleanHasMap.getHasMap().put("CheckVersionError", false);
                in.close();
                isr.close();
                bufr.close();
            } catch (Exception e) {
                ColorLog("&c[Cheng-Tools-Reloaded]获取检测更新时出错!请检查网络连接!");
                BooleanHasMap.getHasMap().put("IsLast", false);
                BooleanHasMap.getHasMap().put("CheckVersionError", true);
            }
        }

        //初始化必要数据
        {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }

            File Config_File = new File(getDataFolder(), "config.yml");
            if (!Config_File.exists()) {
                SaveResource(getDataFolder().getPath(), "config.yml", "config.yml", true);
            }

            File Lang_File = new File(getDataFolder(), "lang.yml");
            if (!Lang_File.exists()) {
                SaveResource(getDataFolder().getPath(), "lang.yml", "lang.yml", true);
            }
            LangFileData = YamlConfiguration.loadConfiguration(Lang_File);

            File MenuHome = new File(getDataFolder(), "Menus");
            if (!MenuHome.exists()) {
                MenuHome.mkdirs();
                if (getConfig().getBoolean("MenuEnable")) {
                    SaveResource(getDataFolder().getPath(), "Menus/CustomMenu.yml", "Menus/CustomMenu.yml", true);
                }
            }

            File HomeMenuFile = new File(getDataFolder(), "Menus/HomeMenu.yml");
            if (!HomeMenuFile.exists()) {
                SaveResource(getDataFolder().getPath(), "Menus/HomeMenu.yml", "Menus/HomeMenu.yml", true);
            }

            File CacheHome = new File(getDataFolder(), "Cache");
            if (!CacheHome.exists()) {
                CacheHome.mkdirs();
            }

            if (Objects.equals(getConfig().getString("DataSettings.Type"), "MySQL")) {
                initializationDatabaseData();
            } else {
                initializationYamlData();
            }
        }

        //初始化功能
        {
            if (getConfig().getBoolean("ScoreboardSettings.Enable")) {
                new Scoreboard().runTaskTimerAsynchronously(this, 0L, 20L);
            }
            if (getConfig().getBoolean("HomeSystemSettings.Enable")) {
                registerCommand(this, new SetHome(), "设置家", "sethome");
                registerCommand(this, new DelHome(), "删除家", "delhome");
                registerCommand(this, new Home(), "传送至家", "home");
                Bukkit.getPluginManager().registerEvents(new HomeMenu(), this);
            }
            if (getConfig().getBoolean("TimeMessageSettings.Enable")) {
                new TimeMessage().runTaskTimerAsynchronously(this, 0L, getConfig().getInt("TimeMessageSettings.Delay") * 20L);
            }
            if (getConfig().getBoolean("SuperListSettings.Enable")) {
                registerCommand(this, new List(), "高级list命令", "superlist");
                registerCommand(this, new List(), "高级list命令", "list");
            }
            if (getConfig().getBoolean("LoginSystemSettings.Enable")) {
                registerCommand(this, new Register(), "注册命令", "register");
                registerCommand(this, new Register(), "注册命令", "reg");
                registerCommand(this, new Login(), "登录命令", "l");
                registerCommand(this, new Login(), "登录命令", "login");
                new LoginMessage().runTaskTimerAsynchronously(this, 0L, 20L);
                Bukkit.getPluginManager().registerEvents(new LoginSystem(), this);
            }
            if (getConfig().getBoolean("BanCommandSettings.Enable")) {
                Bukkit.getPluginManager().registerEvents(new BanCommand(), this);
            }
            if (getConfig().getBoolean("SpawnSettings.Enable")) {
                registerCommand(this, new Spawn(), "Spawn命令", "spawn");
                registerCommand(this, new SetSpawn(), "SetSpawn命令", "setspawn");
                Bukkit.getPluginManager().registerEvents(new ReSpawnTeleportSpawn(), this);
            }
            if (getConfig().getBoolean("SuperStopSettings.Enable")) {
                registerCommand(this, new Stop(), "关闭服务器", "stop");
            }
            if (getConfig().getBoolean("MOTDSettings.Enable")) {
                Bukkit.getPluginManager().registerEvents(new MOTD(), this);
            }
            if (getConfig().getBoolean("FlyEnable")) {
                registerCommand(this, new Fly(), "飞行系统", "fly");
                Bukkit.getPluginManager().registerEvents(new AutoFly(), this);
            }
            if (getConfig().getBoolean("BackSettings.Enable")) {
                registerCommand(this, new Back(), "Back系统", "back");
                Bukkit.getPluginManager().registerEvents(new cn.ChengZhiYa.MHDFTools.Listeners.Back(), this);
            }
            if (getConfig().getBoolean("TpBackSettings.Enable")) {
                registerCommand(this, new TpBack(), "TpBack系统", "tpback");
                Bukkit.getPluginManager().registerEvents(new cn.ChengZhiYa.MHDFTools.Listeners.TpBack(), this);
            }
            if (getConfig().getBoolean("TpBackSettings.Enable") || getConfig().getBoolean("BackSettings.Enable")) {
                registerCommand(this, new UnBack(), "Back系统", "unback");
            }
            if (getConfig().getBoolean("VanishSettings.Enable")) {
                registerCommand(this, new Vanish(), "Vanish系统", "vanish");
                registerCommand(this, new Vanish(), "Vanish系统", "v");
                Bukkit.getPluginManager().registerEvents(new cn.ChengZhiYa.MHDFTools.Listeners.Vanish(), this);
                new cn.ChengZhiYa.MHDFTools.Tasks.Vanish().runTaskTimerAsynchronously(this, 0L, 20L);
                if (getConfig().getBoolean("VanishSettings.SaveVanishData")) {
                    File VanishCacheFile = new File(getDataFolder(), "Cache/VanishCache.yml");
                    if (!VanishCacheFile.exists()) {
                        try {
                            VanishCacheFile.createNewFile();
                        } catch (IOException ignored) {
                        }
                    } else {
                        YamlConfiguration VanishCache = YamlConfiguration.loadConfiguration(VanishCacheFile);
                        if (VanishCache.get("VanishList") != null) {
                            VanishList = VanishCache.getStringList("VanishList");
                        }
                    }
                }
            }
            if (getConfig().getBoolean("IpCommandEnable")) {
                registerCommand(this, new Ip(), "查询玩家IP与IP归属地命令", "ip");
            }
            if (getConfig().getBoolean("EasyGamemodeCommandEnable")) {
                registerCommand(this, new Gamemode(), new Gamemode(), "切换游戏模式", "gamemode");
                registerCommand(this, new Gamemode(), new Gamemode(), "切换游戏模式", "gm");
            }
            if (getConfig().getBoolean("Tpa.Enable")) {
                registerCommand(this, new Tpa(), "Tpa系统", "tpa");
                new TpaTime().runTaskTimerAsynchronously(this, 0L, 20L);
            }
            if (getConfig().getBoolean("InvseeEnable")) {
                registerCommand(this, new Invsee(), "Invsee系统", "invsee");
            }
            if (getConfig().getBoolean("HatEnable")) {
                registerCommand(this, new Hat(), "Hat系统", "hat");
            }
            if (getConfig().getBoolean("Tpahere.Enable")) {
                registerCommand(this, new TpaHere(), "Tpahere系统", "tpahere");
                new TpaHereTime().runTaskTimerAsynchronously(this, 0L, 20L);
            }
            if (getConfig().getBoolean("FastSunCommandEnable")) {
                registerCommand(this, new Sun(), "快速晴天命令", "sun");
            }
            if (getConfig().getBoolean("FastSetTimeCommandEnable")) {
                registerCommand(this, new Day(), "快速天亮命令", "day");
                registerCommand(this, new Night(), "快速天黑命令", "night");
            }
            if (getConfig().getBoolean("AntiTiaoLue")) {
                Bukkit.getPluginManager().registerEvents(new AntiTiaoLue(), this);
            }
            if (getConfig().getBoolean("CommandLink.Enable")) {
                for (String Command : Objects.requireNonNull(getConfig().getConfigurationSection("CommandLink.CommandList")).getKeys(false)) {
                    CommandLinkList.add(Command);
                    registerCommand(this, new TabExecutor() {
                        @Override
                        public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
                            if (sender instanceof Player) {
                                String RunCommand = ChatColor((Player) sender, getConfig().getString("CommandLink.CommandList." + Command + ".Command"));
                                if (args.length != 0) {
                                    for (int i = 0; i < args.length; i++) {
                                        RunCommand = RunCommand.replaceAll("%" + i, args[i]);
                                    }
                                }
                                ((Player) sender).chat("/" + RunCommand);
                            }
                            return false;
                        }

                        @Override
                        public java.util.@Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
                            if (getConfig().getString("CommandLink.CommandList." + Command + "." + (args.length - 1) + "_TabList") != null &&
                                    Objects.equals(getConfig().getString("CommandLink.CommandList." + Command + "." + (args.length - 1) + "_TabList"), "{PlayerList}")) {
                                return null;
                            }
                            return getConfig().getStringList("CommandLink.CommandList." + Command + "." + (args.length - 1) + "_TabList");
                        }
                    }, Command, Command);
                }
            }
            if (getConfig().getBoolean("MenuEnable")) {
                Bukkit.getPluginManager().registerEvents(new OpenMenu(), this);
                Bukkit.getPluginManager().registerEvents(new ClickCustomMenu(), this);
                Bukkit.getPluginManager().registerEvents(new MenuArgsCommand(), this);
            }

            if (PLIB) {
                if (getConfig().getBoolean("CrashPlayerEnable")) {
                    registerCommand(this, new CrashPlayerClient(), "崩端系统", "crashplayerclient");
                    registerCommand(this, new CrashPlayerClient(), "崩端系统", "crashclient");
                    registerCommand(this, new CrashPlayerClient(), "崩端系统", "crash");
                }
                /*
                if (getConfig().getBoolean("TabSettings.Enable")) {
                    new TABFormat().runTaskTimerAsynchronously(this, 0L, 20L);
                }
                */
            }
            if (Vault) {
                if (getConfig().getBoolean("EconomySettings.Enable")) {
                    Bukkit.getServicesManager().register(Economy.class, new EconomyImplementer(), MHDFTools.instance, ServicePriority.Normal);
                    registerCommand(this, new Money(), "查询", "money");
                    registerCommand(this, new Pay(), "转账", "pay");
                    registerCommand(this, new MoneyAdmin(), "管理员管理", "moneyadmin");
                    registerCommand(this, new MoneyAdmin(), "管理员管理", "ma");
                }
            }

            registerCommand(this, new Reload(), "重载插件", "chengtoolsreload");
            registerCommand(this, new Reload(), "重载插件", "ctreload");
            registerCommand(this, new Reload(), "重载插件", "ctr");
            Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
            Bukkit.getPluginManager().registerEvents(new CustomJoinQuitMessage(), this);
            Bukkit.getPluginManager().registerEvents(new JoinTeleportSpawn(), this);
        }

        //跨服模式
        {
            if (getConfig().getBoolean("BungeecordSettings.Enable")) {
                getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
                getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeCordHook());
            }
        }

        if (PAPI) {
            Bukkit.getPluginManager().registerEvents(this, this);
            new PlaceholderAPI().register();
        }

        ColorLog("&e插件启动完成! 作者:292200693");
        ColorLog("&f============&6梦回东方-工具&f============");
    }

    @Override
    public void onDisable() {
        instance = null;

        //取消注册经济
        if (Vault) {
            Bukkit.getServicesManager().unregister(Economy.class, new EconomyImplementer());
        }

        //取消注册PAPI
        if (PAPI) {
            new PlaceholderAPI().unregister();
        }

        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
            if (dataSource != null && !dataSource.isClosed()) {
                dataSource.close();
            }
        } catch (SQLException ignored) {
        }

        //取消注册BC Hook
        {
            if (getConfig().getBoolean("BungeecordSettings.Enable")) {
                getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
                getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord", new BungeeCordHook());
            }
        }

        ColorLog("&f============&6梦回东方-工具&f============");
        ColorLog("&e插件已卸载! 作者:292200693");
        ColorLog("&f============&6梦回东方-工具&f============");
    }

    @NotNull
    public FileOutputStream getOutputStream(CloseableHttpResponse response, String NewVersionString, int cache) throws IOException {
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        FileOutputStream fileOutputStream = new FileOutputStream(new File(getDataFolder().getParentFile(), "Cheng-Tools-Reloaded-" + NewVersionString + ".jar"));
        byte[] buffer = new byte[cache];
        int ch;
        while ((ch = is.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, ch);
        }
        is.close();
        fileOutputStream.flush();
        return fileOutputStream;
    }

    public void initializationYamlData() {
        File Login_File = new File(getDataFolder(), "LoginData.yml");
        if (getConfig().getBoolean("HomeSystemSettings.Enable")) {
            File HomeFile = new File(getDataFolder() + "/HomeData");
            if (!HomeFile.exists()) {
                HomeFile.mkdirs();
            }
        }
        if (getConfig().getBoolean("EconomySettings.Enable")) {
            File VaultData = new File(getDataFolder() + "/VaultData");
            if (!VaultData.exists()) {
                VaultData.mkdirs();
            }
        }
        if (getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!Login_File.exists()) {
                try {
                    Login_File.createNewFile();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public void initializationDatabaseData() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + getConfig().getString("DataSettings.Host") + "/" + getConfig().getString("DataSettings.Database") + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
            config.setUsername(getConfig().getString("DataSettings.User"));
            config.setPassword(getConfig().getString("DataSettings.Password"));
            config.addDataSourceProperty("useUnicode", "true");
            config.addDataSourceProperty("characterEncoding", "utf8");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            dataSource = new HikariDataSource(config);
            statement = dataSource.getConnection().createStatement();
        } catch (SQLException ignored) {
            ColorLog("&c无法连接数据库");
        }
        try {
            {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `MHDFTools_Economy` (" +
                        "`PlayerName` VARCHAR(50) NOT NULL DEFAULT ''," +
                        "`Money` DECIMAL(20,4) NOT NULL DEFAULT 0," +
                        "PRIMARY KEY (`PlayerName`)) " +
                        "COLLATE='utf8mb4_general_ci';");
                ps.executeUpdate();
                ps.close();
                connection.close();
            }
            {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `MHDFTools_Home` (" +
                        "`ID` BIGINT NOT NULL AUTO_INCREMENT," +
                        "`Home` VARCHAR(100) NOT NULL DEFAULT ''," +
                        "`Owner` VARCHAR(50) NOT NULL DEFAULT ''," +
                        "`Server` VARCHAR(50) NOT NULL DEFAULT ''," +
                        "`World` VARCHAR(50) NOT NULL DEFAULT ''," +
                        "`X` DOUBLE NOT NULL DEFAULT 0," +
                        "`Y` DOUBLE NOT NULL DEFAULT 0," +
                        "`Z` DOUBLE NOT NULL DEFAULT 0," +
                        "`Yaw` DOUBLE NOT NULL DEFAULT 0," +
                        "`Pitch` DOUBLE NOT NULL DEFAULT 0," +
                        "PRIMARY KEY (`ID`)," +
                        "INDEX `Home` (`Home`)," +
                        "INDEX `Owner` (`Owner`)) " +
                        "COLLATE='utf8mb4_general_ci';");
                ps.executeUpdate();
                ps.close();
                connection.close();
            }
            {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `MHDFTools_Login` (" +
                        "`PlayerName` VARCHAR(50) NOT NULL DEFAULT ''," +
                        "`Password` VARCHAR(200) NOT NULL DEFAULT ''," +
                        "PRIMARY KEY (`PlayerName`)) " +
                        "COLLATE='utf8mb4_general_ci';");
                ps.executeUpdate();
                ps.close();
                connection.close();
            }
        } catch (SQLException ignored) {
        }
    }
}
