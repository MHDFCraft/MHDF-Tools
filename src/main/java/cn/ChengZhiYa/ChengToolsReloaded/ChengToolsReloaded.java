package cn.ChengZhiYa.ChengToolsReloaded;

import cn.ChengZhiYa.ChengToolsReloaded.Commands.Back.*;
import cn.ChengZhiYa.ChengToolsReloaded.Commands.FastSet.*;
import cn.ChengZhiYa.ChengToolsReloaded.Commands.Home.*;
import cn.ChengZhiYa.ChengToolsReloaded.Commands.Login.*;
import cn.ChengZhiYa.ChengToolsReloaded.Commands.Other.*;
import cn.ChengZhiYa.ChengToolsReloaded.Commands.Spawn.*;
import cn.ChengZhiYa.ChengToolsReloaded.Commands.Tpa.*;
import cn.ChengZhiYa.ChengToolsReloaded.Commands.TpaHere.*;
import cn.ChengZhiYa.ChengToolsReloaded.Commands.Vault.*;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.*;
import cn.ChengZhiYa.ChengToolsReloaded.Listener.Entity.*;
import cn.ChengZhiYa.ChengToolsReloaded.Listener.Inventory.*;
import cn.ChengZhiYa.ChengToolsReloaded.Listener.Player.*;
import cn.ChengZhiYa.ChengToolsReloaded.Listener.Server.*;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.*;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.Money;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.Tpa;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.TpaHere;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import cn.ChengZhiYa.ChengToolsReloaded.Metrics.Metrics;
import com.alibaba.fastjson.parser.ParserConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class ChengToolsReloaded extends JavaPlugin implements Listener {
    public static YamlFileUtil Yaml;
    public static ChengToolsReloaded instance;
    public static java.lang.Boolean PAPI = true;
    public static java.lang.Boolean PLIB = true;
    public static java.lang.Boolean Vault = true;
    public static PluginDescriptionFile descriptionFile;

    public static PluginDescriptionFile getDescriptionFile() {
        return descriptionFile;
    }

    @Override
    public void onLoad() {
        instance = this;
        descriptionFile = this.getDescription();
        Yaml = new YamlFileUtil();
    }

    @Override
    public void onEnable() {
        ParserConfig.getGlobalInstance().setSafeMode(true);
        ColorLog("&7=============&e橙式插件-橙工具&7=============");

        if (getConfig().getBoolean("bStats")) {
            new Metrics(this, 17154);
        }

        if (!isNewPaper()) {
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

        try {
            if (getConfig().getBoolean("CheckVersion")) {
                BooleanHasMap.getHasMap().put("IsLast", CheckVersion());
                BooleanHasMap.getHasMap().put("CheckVersionError", false);
                if (!BooleanHasMap.getHasMap().get("IsLast")) {
                    ColorLog("&c当前插件版本不是最新版! 下载链接:https://github.com/ChengZhiNB/Cheng-Tools-Reloaded/releases/");
                } else {
                    ColorLog("&a当前插件版本是最新版!");
                }
            }
        } catch (Exception exception) {
            ColorLog("&c检测更新时出错!\n" + exception.getCause().toString());
            BooleanHasMap.getHasMap().put("IsLast", false);
            BooleanHasMap.getHasMap().put("CheckVersionError", true);
        }

        File PluginHome = new File(String.valueOf(this.getDataFolder()));

        File Config_File = new File(this.getDataFolder(), "config.yml");
        File Lang_File = new File(this.getDataFolder(), "lang.yml");
        File Login_File = new File(this.getDataFolder(), "LoginData.yml");
        File Title_File = new File(this.getDataFolder(), "TitleData.yml");
        File Point_File = new File(this.getDataFolder(), "PointData.yml");

        if (!PluginHome.exists()) {
            PluginHome.mkdirs();
        }

        if (getConfig().getBoolean("HomeSystemSettings.Enable")) {
            File HomeFile = new File(this.getDataFolder() + "/HomeData");
            HomeFile.mkdirs();
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (getConfig().getBoolean("PointEnable")) {
            if (!Point_File.exists()) {
                try {
                    Point_File.createNewFile();
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
        }

        if (!Lang_File.exists()) {
            Yaml.saveYamlFile(this.getDataFolder().getPath(), "lang.yml", "lang.yml", true);
        }

        if (getConfig().getBoolean("ScoreboardSettings.Enable")) {
            BukkitTask Scoreboard = new Scoreboard().runTaskTimerAsynchronously(this, 0L, 20L);
            IntHasMap.getHasMap().put("ScoreboardTaskID", Scoreboard.getTaskId());
        }
        if (getConfig().getBoolean("HomeSystemSettings.Enable")) {
            registerCommand(this, new SetHome(), "设置家", "sethome");
            registerCommand(this, new DelHome(), "删除家", "delhome");
            registerCommand(this, new Home(), "传送至家", "home");
        }
        if (getConfig().getBoolean("PlayerTitleSettings.Enable")) {
            registerCommand(this, new PlayerTitle(), "称号系统", "playertitle");
            registerCommand(this, new PlayerTitle(), "称号系统", "plt");
            registerCommand(this, new PlayerTitle(), "称号系统", "pt");
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
        if (getConfig().getBoolean("SpawnSettings.Enable")) {
            registerCommand(this, new Spawn(), "Spawn命令", "spawn");
            registerCommand(this, new SetSpawn(), "SetSpawn命令", "setspawn");
        }
        if (getConfig().getBoolean("SuperStopSettings.Enable")) {
            registerCommand(this, new Stop(), "关闭服务器", "stop");
        }
        if (getConfig().getBoolean("FreezeCommandSettings.Enable")) {
            registerCommand(this, new Freeze(), "冻结玩家", "freeze");
        }
        if (getConfig().getBoolean("MOTDSettings.Enable")) {
            if (isNewPaper()) {
                Bukkit.getPluginManager().registerEvents(new PaperServerListPing(), this);
            } else {
                Bukkit.getPluginManager().registerEvents(new ServerListPing(), this);
            }
        }


        if (getConfig().getBoolean("FlyEnable")) {
            registerCommand(this, new Fly(), "飞行系统", "fly");
            Bukkit.getPluginManager().registerEvents(new PlayerChangedWorld(), this);
        }
        if (getConfig().getBoolean("BackEnable")) {
            registerCommand(this, new Back(), "Back系统", "back");
            registerCommand(this, new UnBack(), "Back系统", "unback");
            Bukkit.getPluginManager().registerEvents(new PlayerDeath(), this);
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
        if (getConfig().getBoolean("TpaEnable")) {
            registerCommand(this, new cn.ChengZhiYa.ChengToolsReloaded.Commands.Tpa.Tpa(), "Tpa系统", "tpa");
            registerCommand(this, new TpaAccept(), "接受Tpa", "tpaaccept");
            registerCommand(this, new TpaDefuse(), "拒绝Tpa", "tpadefuse");
            new Tpa().runTaskTimerAsynchronously(this, 0L, 20);
        }
        if (getConfig().getBoolean("PointEnable")) {
            registerCommand(this, new Point(), "点券系统", "point");
            registerCommand(this, new Point(), "点券系统", "p");
        }
        if (getConfig().getBoolean("InvseeEnable")) {
            registerCommand(this, new Invsee(), "Invsee系统", "invsee");
        }
        if (getConfig().getBoolean("HatEnable")) {
            registerCommand(this, new Hat(), "Hat系统", "hat");
        }
        if (getConfig().getBoolean("TpahereEnable")) {
            registerCommand(this, new cn.ChengZhiYa.ChengToolsReloaded.Commands.TpaHere.TpaHere(), "Tpahere系统", "tpahere");
            registerCommand(this, new TpaHereAccept(), "接受Tpahere", "tpahereaccept");
            registerCommand(this, new TpaHereDefuse(), "拒绝Tpahere", "tpaheredefuse");
            new TpaHere().runTaskTimerAsynchronously(this, 0L, 20);
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

        if (PLIB) {
            if (getConfig().getBoolean("CrashPlayerEnable")) {
                registerCommand(this, new CrashPlayerClient(), "崩端系统", "crashplayerclient");
                registerCommand(this, new CrashPlayerClient(), "崩端系统", "crashclient");
                registerCommand(this, new CrashPlayerClient(), "崩端系统", "crash");
            }
        }

        registerCommand(this, new Reload(), "重载插件", "chengtoolsreload");
        registerCommand(this, new Reload(), "重载插件", "ctreload");
        registerCommand(this, new Reload(), "重载插件", "ctr");
        Bukkit.getPluginManager().registerEvents(new PlayerChat(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocess(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawn(), this);
        if (PAPI) {
            Bukkit.getPluginManager().registerEvents(this, this);
            new PlaceholderAPI().register();
        }
        if (Vault) {
            if (getConfig().getBoolean("EconomySettings.Enable")) {
                new Money().runTaskTimerAsynchronously(this, 0L, 20);
                registerCommand(this, new BalTop(), "经济排行榜", "baltop");
                registerCommand(this, new cn.ChengZhiYa.ChengToolsReloaded.Commands.Vault.Money(), "查询", "money");
                registerCommand(this, new Pay(), "转账", "pay");
                registerCommand(this, new MoneyAdmin(), "管理员管理", "moneyadmin");
                registerCommand(this, new MoneyAdmin(), "管理员管理", "ma");
            }
        }
        ColorLog("&a插件加载完成! 作者:292200693");
        ColorLog("&7=============&e橙式插件-橙工具&7=============");
    }

    @Override
    public void onDisable() {
        instance = null;
        ColorLog("&7=============&e橙式插件-橙工具&7=============");
        ClearAllHashMap();
        ColorLog("&c插件卸载完成! 作者:292200693");
        ColorLog("&7=============&e橙式插件-橙工具&7=============");
    }
}
