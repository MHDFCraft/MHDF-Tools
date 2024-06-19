package cn.chengzhiya.mhdftools;

import cn.chengzhiya.mhdftools.command.Back;
import cn.chengzhiya.mhdftools.command.Fly;
import cn.chengzhiya.mhdftools.command.TpBack;
import cn.chengzhiya.mhdftools.command.Trash;
import cn.chengzhiya.mhdftools.command.Vanish;
import cn.chengzhiya.mhdftools.command.*;
import cn.chengzhiya.mhdftools.hook.Metrics;
import cn.chengzhiya.mhdftools.hook.PlaceholderAPI;
import cn.chengzhiya.mhdftools.listener.*;
import cn.chengzhiya.mhdftools.listener.fastuse.CraftingTable;
import cn.chengzhiya.mhdftools.listener.fastuse.EnderChest;
import cn.chengzhiya.mhdftools.listener.fastuse.ShulkerBox;
import cn.chengzhiya.mhdftools.listener.menu.ClickCustomMenu;
import cn.chengzhiya.mhdftools.listener.menu.HomeMenu;
import cn.chengzhiya.mhdftools.listener.menu.MenuArgsCommand;
import cn.chengzhiya.mhdftools.listener.menu.OpenMenu;
import cn.chengzhiya.mhdftools.task.*;
import cn.chengzhiya.mhdftools.util.libraries.classpath.ReflectionClassPathAppender;
import cn.chengzhiya.mhdftools.util.libraries.dependencies.Dependency;
import cn.chengzhiya.mhdftools.util.libraries.dependencies.DependencyManager;
import cn.chengzhiya.mhdftools.util.libraries.dependencies.DependencyManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static cn.chengzhiya.mhdftools.hook.Vault.hookVault;
import static cn.chengzhiya.mhdftools.hook.Vault.unHookVault;
import static cn.chengzhiya.mhdftools.util.BCUtil.getServerName;
import static cn.chengzhiya.mhdftools.util.FileUtil.createDir;
import static cn.chengzhiya.mhdftools.util.FileUtil.createFile;
import static cn.chengzhiya.mhdftools.util.Util.*;
import static cn.chengzhiya.mhdftools.util.database.DatabaseUtil.closeDatabase;
import static cn.chengzhiya.mhdftools.util.database.DatabaseUtil.initializationDatabaseData;
import static cn.chengzhiya.mhdftools.util.menu.MenuUtil.runAction;
import static cn.chengzhiya.mhdfpluginapi.Util.ColorLog;
import static cn.chengzhiya.mhdfpluginapi.YamlFileUtil.SaveResource;

public final class MHDFTools extends JavaPlugin implements Listener {
    public static MHDFTools instance;
    public static boolean PAPI = true;
    public static boolean PLIB = true;
    public static boolean Vault = true;

    public static void initializationYamlData() {
        //家系统数据文件夹
        if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
            createDir("HomeData");
        }
        //经济系统数据文件夹
        if (MHDFTools.instance.getConfig().getBoolean("EconomySettings.Enable")) {
            createDir("VaultData");
        }
        //登录系统
        if (MHDFTools.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            createFile("LoginData.yml");
        }
        //飞行系统
        if (MHDFTools.instance.getConfig().getBoolean("FlySettings.Enable")) {
            createFile("Cache/FlyCache.yml");
        }
    }

    @Override
    public void onLoad() {
        instance = this;

        DependencyManager dependencyManager = new DependencyManagerImpl(this, new ReflectionClassPathAppender(this.getClassLoader()));
        java.util.List<Dependency> dependencies = new ArrayList<>();
        dependencies.add(Dependency.FAST_JSON);
        dependencies.add(Dependency.HikariCP);
        dependencies.add(Dependency.HTTPCLIENT);
        dependencies.add(Dependency.COMMONS_LANG);
        dependencyManager.loadDependencies(dependencies);
    }

    @Override
    public void onEnable() {
        ColorLog("&f============&6梦回东方-工具&f============");

        //bstats
        if (getConfig().getBoolean("bStats")) {
            new Metrics(this, 17154);
        }

        deleteOldPlugins();

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
            checkUpdate();
        }

        //初始化必要数据
        {
            createDir(getDataFolder());
            SaveResource(getDataFolder().getPath(), "config.yml", "config.yml", false);
            reloadConfig();

            File Lang_File = new File(getDataFolder(), "lang.yml");
            SaveResource(getDataFolder().getPath(), "lang.yml", "lang.yml", false);
            LangFileData = YamlConfiguration.loadConfiguration(Lang_File);

            File Sound_File = new File(getDataFolder(), "sound.yml");
            SaveResource(getDataFolder().getPath(), "sound.yml", "sound.yml", false);
            SoundFileData = YamlConfiguration.loadConfiguration(Sound_File);

            createDir(new File(getDataFolder(), "Cache"));

            //家系统菜单与菜单系统
            if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable") || MHDFTools.instance.getConfig().getBoolean("MenuSettings.Enable")) {
                createDir(new File(getDataFolder(), "Menus"));
                if (MHDFTools.instance.getConfig().getBoolean("MenuSettings.Enable")) {
                    SaveResource(MHDFTools.instance.getDataFolder().getPath(), "Menus/CustomMenu.yml", "Menus/CustomMenu.yml", false);
                }
            }
            //家系统
            if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
                SaveResource(MHDFTools.instance.getDataFolder().getPath(), "Menus/HomeMenu.yml", "Menus/HomeMenu.yml", false);
            }
            //隐身系统
            if (MHDFTools.instance.getConfig().getBoolean("VanishSettings.Enable") && MHDFTools.instance.getConfig().getBoolean("VanishSettings.SaveVanishData")) {
                createFile("Cache/VanishCache.yml");
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
            if (getConfig().getBoolean("TrashSettings.Enable")) {
                registerCommand(this, new Trash(),"垃圾桶","MHDFTools.Command.Trash","trash");
            }
            if (getConfig().getBoolean("FastUseSettings.EnderChest")) {
                Bukkit.getPluginManager().registerEvents(new EnderChest(), this);
            }
            if (getConfig().getBoolean("FastUseSettings.ShulkerBox")) {
                Bukkit.getPluginManager().registerEvents(new ShulkerBox(), this);
            }
            if (getConfig().getBoolean("FastUseSettings.CraftingTable")) {
                Bukkit.getPluginManager().registerEvents(new CraftingTable(), this);
            }
            if (getConfig().getBoolean("HomeSystemSettings.Enable")) {
                registerCommand(this, new SetHome(), "设置家", "MHDFTools.Command.SetHome", "sethome");
                registerCommand(this, new DelHome(), "删除家", "MHDFTools.Command.DelHome", "delhome");
                registerCommand(this, new Home(), "传送至家", "MHDFTools.Command.Home", "home");
                Bukkit.getPluginManager().registerEvents(new HomeMenu(), this);
            }
            if (getConfig().getBoolean("TimeMessageSettings.Enable")) {
                new TimeMessage().runTaskTimerAsynchronously(this, 0L, getConfig().getInt("TimeMessageSettings.Delay") * 20L);
            }
            if (getConfig().getBoolean("SuperListSettings.Enable")) {
                registerCommand(this, new List(), "高级list命令", "MHDFTools.Command.List", "superlist");
                registerCommand(this, new List(), "高级list命令", "MHDFTools.Command.List", "list");
            }
            if (getConfig().getBoolean("LoginSystemSettings.Enable")) {
                registerCommand(this, new Register(), "注册命令", "MHDFTools.Command.Register", "register");
                registerCommand(this, new Register(), "注册命令", "MHDFTools.Command.Register", "reg");
                registerCommand(this, new Login(), "登录命令", "MHDFTools.Command.Login", "l");
                registerCommand(this, new Login(), "登录命令", "MHDFTools.Command.Login", "login");
                new LoginMessage().runTaskTimerAsynchronously(this, 0L, 20L);
                Bukkit.getPluginManager().registerEvents(new LoginSystem(), this);
            }
            if (getConfig().getBoolean("BanCommandSettings.Enable")) {
                Bukkit.getPluginManager().registerEvents(new BanCommand(), this);
            }
            if (getConfig().getBoolean("SpawnSettings.Enable")) {
                registerCommand(this, new Spawn(), "Spawn命令", "MHDFTools.Command.Spawn", "spawn");
                registerCommand(this, new SetSpawn(), "SetSpawn命令", "MHDFTools.Command.SetSpawn", "setspawn");
                Bukkit.getPluginManager().registerEvents(new ReSpawnTeleportSpawn(), this);
            }
            if (getConfig().getBoolean("SuperStopSettings.Enable")) {
                registerCommand(this, new Stop(), "关闭服务器", "MHDFTools.Command.Stop", "stop");
            }
            if (getConfig().getBoolean("MOTDSettings.Enable")) {
                Bukkit.getPluginManager().registerEvents(new MOTD(), this);
            }
            if (getConfig().getBoolean("FlySettings.Enable")) {
                registerCommand(this, new Fly(), "飞行系统", "MHDFTools.Command.Fly", "fly");
                registerCommand(this, new FlyTime(), "限时飞行系统", "MHDFTools.Command.FlyTime", "flytime");
                new cn.chengzhiya.mhdftools.task.Fly().runTaskTimerAsynchronously(this, 0L, 20L);
                Bukkit.getPluginManager().registerEvents(new AutoFly(), this);
            }
            if (getConfig().getBoolean("BackSettings.Enable")) {
                registerCommand(this, new Back(), "Back系统", "MHDFTools.Command.Back", "back");
                Bukkit.getPluginManager().registerEvents(new cn.chengzhiya.mhdftools.listener.Back(), this);
                new cn.chengzhiya.mhdftools.task.Back().runTaskTimerAsynchronously(this, 0L, 20L);
            }
            if (getConfig().getBoolean("TpBackSettings.Enable")) {
                registerCommand(this, new TpBack(), "TpBack系统", "MHDFTools.Command.TpBack", "tpback");
                Bukkit.getPluginManager().registerEvents(new cn.chengzhiya.mhdftools.listener.TpBack(), this);
                new cn.chengzhiya.mhdftools.task.TpBack().runTaskTimerAsynchronously(this, 0L, 20L);
            }
            if (getConfig().getBoolean("TpBackSettings.Enable") || getConfig().getBoolean("BackSettings.Enable")) {
                registerCommand(this, new UnBack(), "Back系统", "MHDFTools.Command.UnBack", "unback");
            }
            if (getConfig().getBoolean("VanishSettings.Enable")) {
                registerCommand(this, new Vanish(), "Vanish系统", "MHDFTools.Command.Vanish", "vanish");
                registerCommand(this, new Vanish(), "Vanish系统", "MHDFTools.Command.Vanish", "v");
                Bukkit.getPluginManager().registerEvents(new cn.chengzhiya.mhdftools.listener.Vanish(), this);
                new cn.chengzhiya.mhdftools.task.Vanish().runTaskTimerAsynchronously(this, 0L, 20L);
                if (getConfig().getBoolean("VanishSettings.SaveVanishData")) {
                    File VanishCacheFile = new File(getDataFolder(), "Cache/VanishCache.yml");
                    if (VanishCacheFile.exists()) {
                        YamlConfiguration VanishCache = YamlConfiguration.loadConfiguration(VanishCacheFile);
                        if (VanishCache.get("VanishList") != null) {
                            VanishList = VanishCache.getStringList("VanishList");
                        }
                    }
                }
            }
            if (getConfig().getBoolean("IpSettings.Enable")) {
                registerCommand(this, new Ip(), "查询玩家IP与IP归属地命令", "MHDFTools.Command.Ip", "ip");
            }
            if (getConfig().getBoolean("EasyGamemodeSettings.Enable")) {
                registerCommand(this, new Gamemode(), "切换游戏模式", "MHDFTools.Command.Gamemode", "gamemode");
                registerCommand(this, new Gamemode(), "切换游戏模式", "MHDFTools.Command.Gamemode", "gm");
            }
            if (getConfig().getBoolean("TpaSettings.Enable")) {
                registerCommand(this, new Tpa(), "Tpa系统", "MHDFTools.Command.Tpa", "tpa");
                new TpaTime().runTaskTimerAsynchronously(this, 0L, 20L);
            }
            if (getConfig().getBoolean("InvseeSettings.Enable")) {
                registerCommand(this, new Invsee(), "Invsee系统", "MHDFTools.Command.Invsee", "invsee");
            }
            if (getConfig().getBoolean("HatSettings.Enable")) {
                registerCommand(this, new Hat(), "Hat系统", "MHDFTools.Command.Hat", "hat");
            }
            if (getConfig().getBoolean("TpahereSettings.Enable")) {
                registerCommand(this, new TpaHere(), "Tpahere系统", "MHDFTools.Command.TpaHere", "tpahere");
                new TpaHereTime().runTaskTimerAsynchronously(this, 0L, 20L);
            }
            if (getConfig().getBoolean("FastSunSettings.Enable")) {
                registerCommand(this, new Sun(), "快速晴天命令", "MHDFTools.Command.Sun", "sun");
            }
            if (getConfig().getBoolean("FastSetTimeSettings.Enable")) {
                registerCommand(this, new Day(), "快速天亮命令", "MHDFTools.Command.Sun", "day");
                registerCommand(this, new Night(), "快速天黑命令", "MHDFTools.Command.Night", "night");
            }
            if (getConfig().getBoolean("CommandLinkSettings.Enable")) {
                for (String Command : Objects.requireNonNull(getConfig().getConfigurationSection("CommandLinkSettings.CommandList")).getKeys(false)) {
                    CommandLinkList.add(Command);
                    registerCommand(this, new TabExecutor() {
                        @Override
                        public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
                            if (getConfig().getBoolean("CommandLinkSettings.CommandList." + Command + ".OnlyPlayer")) {
                                if (!(sender instanceof Player)) {
                                    sender.sendMessage(i18n("OnlyPlayer"));
                                    return false;
                                }
                            }
                            java.util.List<String> actionList = new ArrayList<>();
                            if (getConfig().getString("CommandLinkSettings.CommandList." + Command + ".ActionList." + args.length) != null) {
                                getConfig().getStringList("CommandLinkSettings.CommandList." + Command + ".ActionList." + args.length).forEach(action -> {
                                    for (int i = 0; i < args.length; i++) {
                                        action = action.replaceAll("%" + (i + 1), args[i]);
                                    }
                                    actionList.add(action);
                                });
                            } else {
                                actionList.addAll(getConfig().getStringList("CommandLinkSettings.SettingsCommandList." + Command + ".ActionList.Default"));
                            }
                            runAction(sender, null, actionList);
                            return false;
                        }

                        @Override
                        public java.util.@Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
                            java.util.List<String> playerList = getPlayerList(getConfig().getBoolean("CommandLinkSettings.CommandList." + Command + ".BungeeCordGetPlayerList"));
                            java.util.List<String> tabList = new ArrayList<>();
                            if (getConfig().getString("CommandLinkSettings.CommandList." + Command + ".TabList." + args.length) != null) {
                                for (String tab : getConfig().getStringList("CommandLinkSettings.CommandList." + Command + ".TabList." + args.length)) {
                                    if (tab.equals("{PlayerList}")) {
                                        tabList.addAll(playerList);
                                    } else {
                                        tabList.add(tab);
                                    }
                                }
                            } else {
                                tabList = null;
                            }
                            return tabList;
                        }
                    }, Command, getConfig().getString("CommandLinkSettings.CommandList." + Command + "." + "Permission"), Command);
                }
            }
            if (getConfig().getBoolean("MenuEnable")) {
                Bukkit.getPluginManager().registerEvents(new OpenMenu(), this);
                Bukkit.getPluginManager().registerEvents(new ClickCustomMenu(), this);
                Bukkit.getPluginManager().registerEvents(new MenuArgsCommand(), this);
            }

            if (PLIB) {
                if (getConfig().getBoolean("CrashPlayerSettings.Enable")) {
                    registerCommand(this, new Crash(), "崩端系统", "MHDFTools.Command.Crash", "crash");
                }
            }
            if (Vault) {
                if (getConfig().getBoolean("EconomySettings.Enable")) {
                    hookVault();
                    registerCommand(this, new Money(), "查询", "MHDFTools.Command.Money", "money");
                    registerCommand(this, new Pay(), "转账", "MHDFTools.Command.Pay", "pay");
                    registerCommand(this, new MoneyAdmin(), "管理员管理", "MHDFTools.Command.MoneyAdmin", "moneyadmin");
                }
            }
            registerCommand(this, new cn.chengzhiya.mhdftools.command.MHDFTools(), "插件主命令", "MHDFTools.Command.MHDFTools", "mhdftools");
            Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
            Bukkit.getPluginManager().registerEvents(new CustomJoinQuitMessage(), this);
            Bukkit.getPluginManager().registerEvents(new JoinTeleportSpawn(), this);
        }

        //跨服模式
        {
            if (getConfig().getBoolean("BungeecordSettings.Enable")) {
                getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
                getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeCordHook());
                getServerName();
            }
        }

        if (PAPI) {
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
            unHookVault();
        }

        //取消注册PAPI
        if (PAPI) {
            new PlaceholderAPI().unregister();
        }

        closeDatabase();

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
}
