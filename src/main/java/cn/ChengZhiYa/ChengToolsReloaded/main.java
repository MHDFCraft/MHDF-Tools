package cn.ChengZhiYa.ChengToolsReloaded;

import cn.ChengZhiYa.ChengToolsReloaded.Commands.*;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.Listener.*;
import cn.ChengZhiYa.ChengToolsReloaded.Metrics.Metrics;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.*;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.YamlFileUtil;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class main extends JavaPlugin implements Listener {
    public static YamlFileUtil Yaml;
    public static main main;
    public static Boolean PAPI = true;
    public static Boolean PLIB = true;
    private static PluginDescriptionFile descriptionFile;
    private static Plugin plugin;

    public static PluginDescriptionFile getDescriptionFile() {
        return descriptionFile;
    }

    public static main getInstance() {
        return main;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onLoad() {
        main = this;
        plugin = this;
        descriptionFile = this.getDescription();
        Yaml = new YamlFileUtil();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            ColorLog("&c找不到PlaceholderAPI,已关闭PAPI变量解析系统!");
            PAPI = false;
        }

        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            ColorLog("&c找不到ProtocolLib,无法启用崩端系统!");
            PLIB = false;
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        ColorLog("&7=============&e橙式插件-橙工具&7=============");

        Metrics metrics = new Metrics(this, 17154);

        if (!isPaper()) {
            ColorLog("&e服务端不是Paper或是服务器版本较旧，已关闭自带TPS变量!");
        }

        try {
            if (getConfig().getBoolean("CheckVersion")) {
                BooleanHashMap.Set("IsLast", CheckVersion());
                BooleanHashMap.Set("CheckVersionError", false);
                if (!BooleanHashMap.Get("IsLast")) {
                    ColorLog("&c当前插件版本不是最新版! 下载链接:https://github.com/ChengZhiNB/Cheng-Tools-Reloaded/releases/");
                } else {
                    ColorLog("&a当前插件版本是最新版!");
                }
            }
        } catch (Exception exception) {
            ColorLog("&c检测更新时出错!");
            BooleanHashMap.Set("IsLast", false);
            BooleanHashMap.Set("CheckVersionError", true);
        }

        File PluginHome = new File(String.valueOf(this.getDataFolder()));

        File Config_File = new File(this.getDataFolder(), "config.yml");
        File Login_File = new File(this.getDataFolder(), "LoginData.yml");
        File Title_File = new File(this.getDataFolder(), "TitleData.yml");

        if (!PluginHome.exists()) {
            PluginHome.mkdirs();
        } //创建插件配置目录

        if (getConfig().getBoolean("HomeSystemSettings.Enable")) {
            File HomeData = new File(this.getDataFolder() + "/HomeData");
            if (!HomeData.exists()) {
                HomeData.mkdirs();
            }
        }

        if (getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!Login_File.exists()) {
                try {
                    Login_File.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (getConfig().getBoolean("PlayerTitleSettings.Enable")) {
            if (!Title_File.exists()) {
                try {
                    Title_File.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!Config_File.exists()) {
            Yaml.saveYamlFile(this.getDataFolder().getPath(), "config.yml", "config.yml", true);
        } //生成config.yml文件

        if (getConfig().getBoolean("ScoreboardSettings.Enable")) {
            BukkitTask Scoreboard = new Scoreboard_Task(this).runTaskTimer(this, 0L, 20L);
            IntHashMap.Set("ScoreboardTaskID", Scoreboard.getTaskId());
        }

        if (getConfig().getBoolean("EasyGamemodeCommandEnable")) {
            multi.registerCommand(this, new Gamemode(), new Gamemode(), "切换游戏模式", "Gamemode");
            multi.registerCommand(this, new Gamemode(), new Gamemode(), "切换游戏模式", "gm");
        }

        if (getConfig().getBoolean("PlayerTitleSettings.Enable")) {
            multi.registerCommand(this, new PlayerTitle(), "称号系统", "PlayerTitle");
            multi.registerCommand(this, new PlayerTitle(), "称号系统", "plt");
            multi.registerCommand(this, new PlayerTitle(), "称号系统", "pt");
        }

        if (getConfig().getBoolean("TimeMessageSettings.Enable")) {
            BukkitTask TimeMessage = new TimeMessage_Task(this).runTaskTimer(this, 0L, getConfig().getInt("TimeMessageSettings.Delay") * 20L);
            IntHashMap.Set("TimeMessageTaskId", TimeMessage.getTaskId());
        }

        if (getConfig().getBoolean("FlyEnable")) {
            multi.registerCommand(this, new Fly(), "飞行系统", "Fly");
            Bukkit.getPluginManager().registerEvents(new PlayerChangedWorld(), this);
        }

        if (getConfig().getBoolean("BackEnable")) {
            multi.registerCommand(this, new Back(), "Back系统", "Back");
            multi.registerCommand(this, new UnBack(), "Back系统", "Unback");
            Bukkit.getPluginManager().registerEvents(new PlayerDeath(), this);
        }

        if (getConfig().getBoolean("VanishEnable")) {
            multi.registerCommand(this, new Vanish(), "Vanish系统", "Vanish");
            multi.registerCommand(this, new Vanish(), "Vanish系统", "v");
        }

        if (PLIB) {
            if (getConfig().getBoolean("CrashPlayerEnable")) {
                multi.registerCommand(this, new CrashPlayerClient(), "崩端系统", "CrashPlayerClient");
                multi.registerCommand(this, new CrashPlayerClient(), "崩端系统", "CrashClient");
                multi.registerCommand(this, new CrashPlayerClient(), "崩端系统", "Crash");
            }
        }

        if (getConfig().getBoolean("TpaEnable")) {
            multi.registerCommand(this, new Tpa(), "Tpa系统", "Tpa");
            multi.registerCommand(this, new TpaAccept(), "接受TPA", "TpaAccept");
            multi.registerCommand(this, new TpaDefuse(), "拒绝TPA", "TpaDefuse");
            BukkitTask TpaTime = new Tpa_Time(this).runTaskTimer(this, 0L, 20);
            IntHashMap.Set("TpaTimeTaskId", TpaTime.getTaskId());
            BukkitTask TpaDetect = new Tpa_Detect(this).runTaskTimer(this, 0L, 60 * 20);
            IntHashMap.Set("TpaDetectTaskId", TpaDetect.getTaskId());
        }

        if (getConfig().getBoolean("HomeSystemSettings.Enable")) {
            multi.registerCommand(this, new SetHome(), "设置家", "sethome");
            multi.registerCommand(this, new DelHome(), "删除家", "delhome");
            multi.registerCommand(this, new Home(), "传送至家", "home");
        }

        if (getConfig().getBoolean("PluginManageEnable")) {
            multi.registerCommand(this, new PluginManage(), "插件管理系统", "PluginManage");
            multi.registerCommand(this, new PluginManage(), "插件管理系统", "pm");
        }

        if (getConfig().getBoolean("FastSunCommandEnable")) {
            multi.registerCommand(this, new Sun(), "快速晴天命令", "Sun");
        }

        if (getConfig().getBoolean("FastSetTimeCommandEnable")) {
            multi.registerCommand(this, new Day(), "快速天亮命令", "Day");
            multi.registerCommand(this, new Night(), "快速天黑命令", "Night");
        }

        if (getConfig().getBoolean("ChatSettings.ChatDelayEnable")) {
            BukkitTask ChatDelayTime = new ChatDelay_Time(this).runTaskTimer(this, 0L, 20);
            IntHashMap.Set("ChatDelayTaskId", ChatDelayTime.getTaskId());
        }

        if (getConfig().getBoolean("SuperListSettings.Enable")) {
            multi.registerCommand(this, new List(), "高级list命令", "SuperList");
            multi.registerCommand(this, new List(), "高级list命令", "List");
        }

        if (getConfig().getBoolean("LoginSystemSettings.Enable")) {
            multi.registerCommand(this, new Register(), "注册命令", "register");
            multi.registerCommand(this, new Register(), "注册命令", "reg");
            multi.registerCommand(this, new Login(), "登录命令", "l");
            multi.registerCommand(this, new Login(), "登录命令", "login");
            BukkitTask LoginMessage = new LoginMessage_Task(this).runTaskTimer(this, 0L, 20L);
            IntHashMap.Set("LoginMessageTaskId", LoginMessage.getTaskId());
            Bukkit.getPluginManager().registerEvents(new PlayerMove(), this);
            Bukkit.getPluginManager().registerEvents(new EntityDamageByBlock(), this);
            Bukkit.getPluginManager().registerEvents(new EntityDamageByEntity(), this);
            Bukkit.getPluginManager().registerEvents(new EntityPickupItem(), this);
            Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
            Bukkit.getPluginManager().registerEvents(new InventoryOpen(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerBedEnter(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerDropItem(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerEditBook(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerFish(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerInteractAtEntity(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerInteractEntity(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerItemConsume(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerPickupArrow(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerShearEntity(), this);
            Bukkit.getPluginManager().registerEvents(new SignChange(), this);
        }

        if (getConfig().getBoolean("BanCommandSettings.Enable")) {
            Bukkit.getPluginManager().registerEvents(new PlayerCommandSend(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerChatTabComplete(), this);
        }

        if (getConfig().getBoolean("SuperStopSettings.Enable")) {
            multi.registerCommand(this, new Stop(), "关闭服务器", "Stop");
        }

        if (getConfig().getBoolean("FreezeCommandSettings.Enable")) {
            multi.registerCommand(this, new Freeze(), "冻结玩家", "Freeze");
        }

        if (getConfig().getBoolean("MOTDSettings.Enable")) {
            if (isPaper()) {
                Bukkit.getPluginManager().registerEvents(new PaperServerListPing(), this);
            } else {
                Bukkit.getPluginManager().registerEvents(new ServerListPing(), this);
            }
        }

        multi.registerCommand(this, new Reload(), "重载插件", "ChengToolsReload");
        multi.registerCommand(this, new Reload(), "重载插件", "CTReload");
        multi.registerCommand(this, new Reload(), "重载插件", "CTR");
        Bukkit.getPluginManager().registerEvents(new PlayerChat(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocess(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawn(), this);
        if (PAPI) {
            Bukkit.getPluginManager().registerEvents(this, this);
            new PlaceholderAPI(this).register();
        }
        ColorLog("&a插件加载完成! 作者:292200693");
        ColorLog("&7=============&e橙式插件-橙工具&7=============");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        main = null;
        ColorLog("&7=============&e橙式插件-橙工具&7=============");
        ClearAllHashMap();
        ColorLog("&c插件卸载完成! 作者:292200693");
        ColorLog("&7=============&e橙式插件-橙工具&7=============");
    }
}
