package cn.ChengZhiYa.ChengToolsReloaded;

import cn.ChengZhiYa.ChengToolsReloaded.Commands.*;
import cn.ChengZhiYa.ChengToolsReloaded.Event.*;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.ChatDelay_Time;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.TimeMessage_Task;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.Tpa_Detect;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.Tpa_Time;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.YamlFileUtil;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ColorLog;

public final class main extends JavaPlugin {
    public static YamlFileUtil Yaml;
    public static main main;

    @Override
    public void onLoad() {
        main = this;

        Yaml = new YamlFileUtil();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            ColorLog("&c找不到PlaceholderAPI,请安装PlaceholderAPI后才能使用本插件");
            Bukkit.getPluginManager().disablePlugins();
        }

        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            ColorLog("&c找不到ProtocolLib,请安装ProtocolLib后才能使用本插件");
            Bukkit.getPluginManager().disablePlugins();
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        ColorLog("&7=============&e橙式插件-橙工具&7=============");
        File PluginHome = new File(String.valueOf(this.getDataFolder()));

        File Config_File = new File(this.getDataFolder(), "config.yml");

        if (!PluginHome.exists()) {
            PluginHome.mkdirs();
        } //创建插件配置目录

        if (getConfig().getBoolean("HomeSystemSettings.Enable")) {
            File HomeData = new File(this.getDataFolder() + "/HomeData");
            if (!HomeData.exists()) {
                HomeData.mkdirs();
            }
        }

        if (!Config_File.exists()) {
            Yaml.saveYamlFile(this.getDataFolder().getPath(), "config.yml", "config.yml", true);
        } //生成config.yml文件

        if (getConfig().getBoolean("CustomJoinServerMessageSettings.Enable")) {
            Bukkit.getPluginManager().registerEvents(new PlayerJoin_Event(), this);
        }

        if (getConfig().getBoolean("CustomJoinServerMessageSettings.Enable")) {
            Bukkit.getPluginManager().registerEvents(new PlayerJoin_Event(), this);
        }

        if (getConfig().getBoolean("CustomQuitServerMessageSettings.Enable")) {
            Bukkit.getPluginManager().registerEvents(new PlayerQuit_Event(), this);
        }

        if (getConfig().getBoolean("EasyGamemodeCommandEnable")) {
            multi.registerCommand(this, new Gamemode_Command(), new Gamemode_Command(), "切换游戏模式", "Gamemode");
            multi.registerCommand(this, new Gamemode_Command(), new Gamemode_Command(), "切换游戏模式", "gm");
        }

        if (getConfig().getBoolean("TimeMessageSettings.Enable")) {
            BukkitTask TimeMessage = new TimeMessage_Task(this).runTaskTimer(this, 0L, getConfig().getInt("TimeMessageSettings.Delay") * 20L);
            IntHashMap.Set("TimeMessageTaskId", TimeMessage.getTaskId());
        }

        if (getConfig().getBoolean("FlyEnable")) {
            multi.registerCommand(this, new Fly_Command(), "飞行系统", "Fly");
            Bukkit.getPluginManager().registerEvents(new PlayerChangedWorld_Event(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerRespawn_Event(), this);
        }

        if (getConfig().getBoolean("BackEnable")) {
            multi.registerCommand(this, new Back_Command(), "Back系统", "Back");
            multi.registerCommand(this, new UnBack_Command(), "Back系统", "Unback");
        }

        if (getConfig().getBoolean("VanishEnable")) {
            multi.registerCommand(this, new Vanish_Command(), "Vanish系统", "Vanish");
            multi.registerCommand(this, new Vanish_Command(), "Vanish系统", "v");
        }

        if (getConfig().getBoolean("CrashPlayerEnable")) {
            multi.registerCommand(this, new CrashPlayerClient_Command(), "崩端系统", "CrashPlayerClient");
            multi.registerCommand(this, new CrashPlayerClient_Command(), "崩端系统", "CrashClient");
            multi.registerCommand(this, new CrashPlayerClient_Command(), "崩端系统", "Crash");
        }

        if (getConfig().getBoolean("TpaSettings.Enable")) {
            multi.registerCommand(this, new Tpa_Command(), "Tpa系统", "Tpa");
            multi.registerCommand(this, new TpaAccept_Command(), "接受TPA", "TpaAccept");
            multi.registerCommand(this, new TpaDefuse_Command(), "拒绝TPA", "TpaDefuse");
            BukkitTask TpaTime = new Tpa_Time(this).runTaskTimer(this, 0L, 20);
            IntHashMap.Set("TpaTimeTaskId", TpaTime.getTaskId());
            BukkitTask TpaDetect = new Tpa_Detect(this).runTaskTimer(this, 0L, 60 * 20);
            IntHashMap.Set("TpaDetectTaskId", TpaDetect.getTaskId());
        }

        if (getConfig().getBoolean("HomeSystemSettings.Enable")) {
            multi.registerCommand(this, new SetHome_Command(), "设置家", "sethome");
            multi.registerCommand(this, new DelHome_Command(), "删除家", "delhome");
            multi.registerCommand(this, new Home_Command(), "传送至家", "home");
        }

        if (getConfig().getBoolean("PluginManageEnable")) {
            multi.registerCommand(this, new PluginManage_Command(), "插件管理系统", "PluginManage");
            multi.registerCommand(this, new PluginManage_Command(), "插件管理系统", "pm");
        }

        if (getConfig().getBoolean("FastSunCommandEnable")) {
            multi.registerCommand(this, new Sun_Command(), "快速晴天命令", "Sun");
        }

        if (getConfig().getBoolean("FastSetTimeCommandEnable")) {
            multi.registerCommand(this, new Day_Command(), "快速天亮命令", "Day");
            multi.registerCommand(this, new Night_Command(), "快速天黑命令", "Night");
        }

        if (getConfig().getBoolean("ChatDelayEnable")) {
            BukkitTask ChatDelayTime = new ChatDelay_Time(this).runTaskTimer(this, 0L, 20);
            IntHashMap.Set("ChatDelayTaskId", ChatDelayTime.getTaskId());
        }

        if (getConfig().getBoolean("SuperListSettings.Enable")) {
            multi.registerCommand(this, new List_Command(), "高级list命令", "SuperList");
            multi.registerCommand(this, new List_Command(), "高级list命令", "List");
        }

        multi.registerCommand(this, new Reload_Command(), "重载插件", "ChengToolsReload");
        multi.registerCommand(this, new Reload_Command(), "重载插件", "CTReload");
        multi.registerCommand(this, new Reload_Command(), "重载插件", "CTR");
        Bukkit.getPluginManager().registerEvents(new PlayerChat_Event(), this);
        ColorLog("&a插件加载完成! 作者:292200693");
        ColorLog("&7=============&e橙式插件-橙工具&7=============");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        main = null;
        ColorLog("&7=============&e橙式插件-橙工具&7=============");
        ColorLog("&c插件卸载完成! 作者:292200693");
        ColorLog("&7=============&e橙式插件-橙工具&7=============");
    }
}
