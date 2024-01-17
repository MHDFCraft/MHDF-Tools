package cn.ChengZhiYa.ChengToolsReloaded;

import cn.ChengZhiYa.ChengToolsReloaded.Commands.Back;
import cn.ChengZhiYa.ChengToolsReloaded.Commands.Freeze;
import cn.ChengZhiYa.ChengToolsReloaded.Commands.TpBack;
import cn.ChengZhiYa.ChengToolsReloaded.Commands.*;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.Hook.EconomyImplementer;
import cn.ChengZhiYa.ChengToolsReloaded.Hook.Metrics;
import cn.ChengZhiYa.ChengToolsReloaded.Hook.PlaceholderAPI;
import cn.ChengZhiYa.ChengToolsReloaded.Listeners.*;
import cn.ChengZhiYa.ChengToolsReloaded.Listeners.Menu.HomeMenu;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.*;
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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.TimeZone;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.*;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.YamlFileUtil.SaveResource;

public final class ChengToolsReloaded extends JavaPlugin implements Listener {
    public static final String Version = "1.2.3";
    public static ChengToolsReloaded instance;
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
        descriptionFile = this.getDescription();

        ParserConfig.getGlobalInstance().setSafeMode(true);
    }

    @Override
    public void onEnable() {
        ColorLog("&7=============&e橙式插件-橙工具&7=============");

        //bstats
        if (getConfig().getBoolean("bStats")) {
            new Metrics(this, 17154);
        }

        {
            if (getDataFolder().getParentFile().isDirectory()) {
                for (File PluginFile : Objects.requireNonNull(getDataFolder().getParentFile().listFiles())) {
                    if (PluginFile.isFile() && PluginFile.getName().contains("Cheng-Tools-Reloaded") && PluginFile.getName().endsWith(".jar")) {
                        String[] PluginName = PluginFile.getName().split("Cheng-Tools-Reloaded");
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
                    ColorLog("&c当前插件版本不是最新版! 下载链接:https://github.com/ChengZhiNB/Cheng-Tools-Reloaded/releases/");
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
            File PluginHome = new File(String.valueOf(this.getDataFolder()));
            if (!PluginHome.exists()) {
                PluginHome.mkdirs();
            }

            File Config_File = new File(this.getDataFolder(), "config.yml");
            if (!Config_File.exists()) {
                SaveResource(this.getDataFolder().getPath(), "config.yml", "config.yml", true);
            }

            File Lang_File = new File(this.getDataFolder(), "lang.yml");
            if (!Lang_File.exists()) {
                SaveResource(this.getDataFolder().getPath(), "lang.yml", "lang.yml", true);
            }
            LangFileData = YamlConfiguration.loadConfiguration(Lang_File);

            File HomeMenuFile = new File(getDataFolder(), "HomeMenu.yml");
            if (!HomeMenuFile.exists()) {
                SaveResource(this.getDataFolder().getPath(), "HomeMenu.yml", "HomeMenu.yml", true);
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
                BukkitTask Scoreboard = new Scoreboard().runTaskTimerAsynchronously(this, 0L, 20L);
                IntHasMap.getHasMap().put("ScoreboardTaskID", Scoreboard.getTaskId());
            }
            if (getConfig().getBoolean("HomeSystemSettings.Enable")) {
                registerCommand(this, new SetHome(), "设置家", "sethome");
                registerCommand(this, new DelHome(), "删除家", "delhome");
                registerCommand(this, new Home(), "传送至家", "home");
                Bukkit.getPluginManager().registerEvents(new HomeMenu(), this);
            }
            if (getConfig().getBoolean("TimeMessageSettings.Enable")) {
                BukkitTask TimeMessage = new TimeMessage().runTaskTimerAsynchronously(this, 0L, getConfig().getInt("TimeMessageSettings.Delay") * 20L);
                IntHasMap.getHasMap().put("TimeMessageTaskId", TimeMessage.getTaskId());
            }
            if (getConfig().getBoolean("VanillaOpWhitelist.Enable")) {
                BukkitTask WhiteListTask = new VanillaOpWhitelist().runTaskTimerAsynchronously(this, 0L, 20L);
                IntHasMap.getHasMap().put("OpWhiteListTaskID", WhiteListTask.getTaskId());
            }
            if (getConfig().getBoolean("ChatSettings.ChatDelayEnable")) {
                BukkitTask ChatDelayTime = new ChatDelay().runTaskTimerAsynchronously(this, 0L, 20);
                IntHasMap.getHasMap().put("ChatDelayTaskId", ChatDelayTime.getTaskId());
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
            }
            if (getConfig().getBoolean("SuperStopSettings.Enable")) {
                registerCommand(this, new Stop(), "关闭服务器", "stop");
            }
            if (getConfig().getBoolean("FreezeCommandSettings.Enable")) {
                registerCommand(this, new Freeze(), "冻结玩家", "freeze");
                Bukkit.getPluginManager().registerEvents(new cn.ChengZhiYa.ChengToolsReloaded.Listeners.Freeze(), this);
            }
            if (getConfig().getBoolean("MOTDSettings.Enable")) {
                Bukkit.getPluginManager().registerEvents(new MOTD(), this);
            }
            if (getConfig().getBoolean("FlyEnable")) {
                registerCommand(this, new Fly(), "飞行系统", "fly");
                Bukkit.getPluginManager().registerEvents(new AutoFly(), this);
            }
            if (getConfig().getBoolean("BackEnable")) {
                registerCommand(this, new Back(), "Back系统", "back");
                Bukkit.getPluginManager().registerEvents(new cn.ChengZhiYa.ChengToolsReloaded.Listeners.Back(), this);
            }
            if (getConfig().getBoolean("TpBackEnable")) {
                registerCommand(this, new TpBack(), "TpBack系统", "tpback");
                Bukkit.getPluginManager().registerEvents(new cn.ChengZhiYa.ChengToolsReloaded.Listeners.TpBack(), this);
            }
            if (getConfig().getBoolean("TpBackEnable") || getConfig().getBoolean("BackEnable")) {
                registerCommand(this, new UnBack(), "Back系统", "unback");
            }
            if (getConfig().getBoolean("VanishEnable")) {
                registerCommand(this, new Vanish(), "Vanish系统", "vanish");
                registerCommand(this, new Vanish(), "Vanish系统", "v");
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
            if (getConfig().getBoolean("PluginManageSettings.Enable")) {
                registerCommand(this, new PluginManage(), "插件管理系统", "pluginmanage");
                registerCommand(this, new PluginManage(), "插件管理系统", "pm");
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
            if (PLIB) {
                if (getConfig().getBoolean("CrashPlayerEnable")) {
                    registerCommand(this, new CrashPlayerClient(), "崩端系统", "crashplayerclient");
                    registerCommand(this, new CrashPlayerClient(), "崩端系统", "crashclient");
                    registerCommand(this, new CrashPlayerClient(), "崩端系统", "crash");
                }
            }
            if (Vault) {
                if (getConfig().getBoolean("EconomySettings.Enable")) {
                    Bukkit.getServicesManager().register(Economy.class, new EconomyImplementer(), ChengToolsReloaded.instance, ServicePriority.Normal);
                    registerCommand(this, new Money(), "查询", "money");
                    registerCommand(this, new Pay(), "转账", "pay");
                    registerCommand(this, new MoneyAdmin(), "管理员管理", "moneyadmin");
                    registerCommand(this, new MoneyAdmin(), "管理员管理", "ma");
                }
            }

            registerCommand(this, new Reload(), "重载插件", "chengtoolsreload");
            registerCommand(this, new Reload(), "重载插件", "ctreload");
            registerCommand(this, new Reload(), "重载插件", "ctr");
            Bukkit.getPluginManager().registerEvents(new Chat(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
            Bukkit.getPluginManager().registerEvents(new CustomJoinQuitMessage(), this);
            Bukkit.getPluginManager().registerEvents(new JoinTeleportSpawn(), this);
        }

        if (PAPI) {
            Bukkit.getPluginManager().registerEvents(this, this);
            new PlaceholderAPI().register();
        }

        ColorLog("&a插件加载完成! 作者:292200693");
        ColorLog("&7=============&e橙式插件-橙工具&7=============");
    }

    @Override
    public void onDisable() {
        instance = null;
        ColorLog("&7=============&e橙式插件-橙工具&7=============");

        //取消注册经济
        if (Vault) {
            Bukkit.getServicesManager().unregister(Economy.class, new EconomyImplementer());
        }

        //取消注册PAPI
        if (PAPI) {
            new PlaceholderAPI().unregister();
        }

        ColorLog("&c插件卸载完成! 作者:292200693");
        ColorLog("&7=============&e橙式插件-橙工具&7=============");
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
        File Login_File = new File(this.getDataFolder(), "LoginData.yml");
        if (getConfig().getBoolean("HomeSystemSettings.Enable")) {
            File HomeFile = new File(this.getDataFolder() + "/HomeData");
            if (!HomeFile.exists()) {
                HomeFile.mkdirs();
            }
        }
        if (getConfig().getBoolean("EconomySettings.Enable")) {
            File VaultData = new File(this.getDataFolder() + "/VaultData");
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
                PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `ChengTools_Economy` (" +
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
                PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `ChengTools_Home` (" +
                        "`ID` BIGINT NOT NULL AUTO_INCREMENT," +
                        "`Home` VARCHAR(100) NOT NULL DEFAULT ''," +
                        "`Owner` VARCHAR(50) NOT NULL DEFAULT ''," +
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
                PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `ChengTools_Login` (" +
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
