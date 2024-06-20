package cn.ChengZhiYa.MHDFTools.command;

import cn.ChengZhiYa.MHDFTools.MHDFPluginLoader;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.auth.Login;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.auth.Register;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.flight.Fly;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.flight.FlyTime;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.gamemode.GameMode;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.server.List;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.server.Stop;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.teleport.Tpa;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.teleport.TpaHere;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.*;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.back.Back;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.back.TpBack;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.home.DelHome;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.home.Home;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.home.SetHome;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.money.Money;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.money.MoneyAdmin;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.money.Pay;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.spawn.SetSpawn;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.spawn.Spawn;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.time.Day;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.time.Night;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.weather.Sun;
import cn.ChengZhiYa.MHDFTools.listeners.player.*;
import cn.ChengZhiYa.MHDFTools.listeners.player.fastuse.EnderChest;
import cn.ChengZhiYa.MHDFTools.listeners.player.fastuse.ShulkerBox;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerCommandListener;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerMOTDListener;
import cn.ChengZhiYa.MHDFTools.listeners.server.menu.HomeMenu;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import cn.ChengZhiYa.MHDFTools.utils.message.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.Util.*;
import static cn.ChengZhiYa.MHDFTools.utils.menu.MenuUtil.runAction;

public class CommandRegister implements Invitable {
    FileConfiguration getConfig = MHDFTools.instance.getConfig();
    JavaPlugin plugin = MHDFPluginLoader.INSTANCE.getPlugin();

    @Override
    public void start() {
        registerCommand(plugin, new cn.ChengZhiYa.MHDFTools.command.subCommand.main.MHDFTools(), "插件主命令", "MHDFTools.Command.MHDFTools", "mhdftools");
        for (String configKey : getConfig.getKeys(false)) {
            boolean isEnabled = getConfig.getBoolean(configKey + ".Enable", true);
            switch (configKey) {
                case "HomeSystemSettings":
                    if (isEnabled) {
                        registerHomeCommands();
                    }
                    break;
                case "SuperListSettings":
                    if (isEnabled) {
                        registerSuperListCommands();
                    }
                    break;
                case "LoginSystemSettings":
                    if (isEnabled) {
                        registerLoginCommands();
                    }
                    break;
                case "BanCommandSettings":
                    if (isEnabled) {
                        registerBanCommand();
                    }
                    break;
                case "SpawnSettings":
                    if (isEnabled) {
                        registerSpawnCommands();
                    }
                    break;
                case "SuperStopSettings":
                    if (isEnabled) {
                        registerStopCommand();
                    }
                    break;
                case "MOTDSettings":
                    if (isEnabled) {
                        registerMOTDListener();
                    }
                    break;
                case "FlySettings":
                    if (isEnabled) {
                        registerFlyCommands();
                    }
                    break;
                case "BackSettings":
                    if (isEnabled) {
                        registerBackCommands();
                    }
                    break;
                case "TpBackSettings":
                    if (isEnabled) {
                        registerTpBackCommands();
                    }
                    break;
                case "VanishSettings":
                    if (isEnabled) {
                        registerVanishCommands();
                    }
                    break;
                case "IpCommandSettings":
                    if (isEnabled) {
                        registerIPCommand();
                    }
                    break;
                case "EasyGamemodeSettings":
                    if (isEnabled) {
                        registerGamemodeCommands();
                        LogUtil.color("&aDone9");
                    }
                    break;
                case "TpaSettings":
                    if (isEnabled) {
                        registerTpaCommand();
                    }
                    break;
                case "InvseeSettings":
                    if (isEnabled) {
                        registerInvseeCommand();
                    }
                    break;
                case "HatSettings": {
                    registerHatCommand();
                }
                break;
                case "TpahereSettings":
                    if (isEnabled) {
                        registerTpaHereCommand();
                    }
                    break;
                case "FastSunCommandSettings":
                    if (isEnabled) {
                        registerFastSunCommands();
                    }
                    break;
                case "FastSetTimeCommandSettings":
                    if (isEnabled) {
                        registerFastTimeCommands();
                    }
                    break;
                case "CrashPlayerSettings":
                    if (MHDFPluginLoader.hasProtocolLib) {
                        registerCrashCommand();
                    }
                    break;
                case "EconomySettings":
                    if (isEnabled && MHDFPluginLoader.hasVault) {
                        registerEconomy();
                    }
                case "CommandLinkSettings":
                    if (isEnabled) {
                        registerLinkCommands();
                    }
                    break;
                case "TrashSettings":
                    if (isEnabled) {
                        registerTrashCommands();
                    }
                case "FastUseSettings.EnderChest": {
                    registerEnderChestCommands();
                }
                    break;
                case "FastUseSettings.ShulkerBox": {
                    registerShulkerBoxCommands();
                }
                break;
            }
        }
    }
    private void registerEnderChestCommands() {
        Bukkit.getPluginManager().registerEvents(new EnderChest(), plugin);
    }
    private void registerShulkerBoxCommands() {
        Bukkit.getPluginManager().registerEvents(new ShulkerBox(), plugin);
    }
    private void registerTrashCommands() {
        registerCommand(plugin, new Trash(),"垃圾桶","MHDFTools.Command.Trash","trash");
    }
    private void registerHomeCommands() {
        registerCommand(plugin, new SetHome(), "设置家", "MHDFTools.Command.SetHome", "sethome");
        registerCommand(plugin, new DelHome(), "删除家", "MHDFTools.Command.DelHome", "delhome");
        registerCommand(plugin, new Home(), "传送至家", "MHDFTools.Command.Home", "home");
        Bukkit.getPluginManager().registerEvents(new HomeMenu(), plugin);
    }

    private void registerSuperListCommands() {
        registerCommand(plugin, new List(), "高级list命令", "MHDFTools.Command.List", "superlist");
        registerCommand(plugin, new List(), "高级list命令", "MHDFTools.Command.List", "list");
    }

    private void registerLoginCommands() {
        registerCommand(plugin, new Register(), "注册命令", "MHDFTools.Command.Register", "register");
        registerCommand(plugin, new Register(), "注册命令", "MHDFTools.Command.Register", "reg");
        registerCommand(plugin, new Login(), "登录命令", "MHDFTools.Command.Login", "l");
        registerCommand(plugin, new Login(), "登录命令", "MHDFTools.Command.Login", "login");
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(), plugin);
    }

    private void registerBanCommand() {
        Bukkit.getPluginManager().registerEvents(new ServerCommandListener(), plugin);
    }

    private void registerSpawnCommands() {
        registerCommand(plugin, new Spawn(), "Spawn命令", "MHDFTools.Command.Spawn", "spawn");
        registerCommand(plugin, new SetSpawn(), "SetSpawn命令", "MHDFTools.Command.SetSpawn", "setspawn");
        Bukkit.getPluginManager().registerEvents(new PlayerReSpawnListener(), plugin);
    }

    private void registerStopCommand() {
        registerCommand(plugin, new Stop(), "关闭服务器", "MHDFTools.Command.Stop", "stop");
    }

    private void registerMOTDListener() {
        Bukkit.getPluginManager().registerEvents(new ServerMOTDListener(), plugin);
    }

    private void registerFlyCommands() {
        registerCommand(plugin, new Fly(), "飞行系统", "MHDFTools.Command.Fly", "fly");
        registerCommand(plugin, new FlyTime(), "限时飞行系统", "MHDFTools.Command.FlyTime", "flytime");
        Bukkit.getPluginManager().registerEvents(new PlayerAllowedFlightListener(), plugin);
    }

    private void registerBackCommands() {
        registerCommand(plugin, new Back(), "Back系统", "MHDFTools.Command.Back", "back");
        Bukkit.getPluginManager().registerEvents(new PlayerBackListener(), plugin);
    }

    private void registerTpBackCommands() {
        registerCommand(plugin, new TpBack(), "TpBack系统", "MHDFTools.Command.TpBack", "tpback");
        Bukkit.getPluginManager().registerEvents(new PlayerTPBackListener(), plugin);
    }

    private void registerVanishCommands() {
        registerCommand(plugin, new Vanish(), "Vanish系统", "MHDFTools.Command.Vanish", "vanish");
        registerCommand(plugin, new Vanish(), "Vanish系统", "MHDFTools.Command.Vanish", "v");
        Bukkit.getPluginManager().registerEvents(new PlayerVanishListener(), plugin);
    }

    private void registerIPCommand() {
        registerCommand(plugin, new IP(), "查询玩家IP与IP归属地命令", "MHDFTools.Command.Ip", "ip");
    }

    private void registerGamemodeCommands() {
        registerCommand(plugin, new GameMode(), "切换游戏模式", "MHDFTools.Command.Gamemode", "gamemode");
        registerCommand(plugin, new GameMode(), "切换游戏模式", "MHDFTools.Command.Gamemode", "gm");
    }

    private void registerTpaCommand() {
        registerCommand(plugin, new Tpa(), "Tpa系统", "MHDFTools.Command.Tpa", "tpa");
    }

    private void registerInvseeCommand() {
        registerCommand(plugin, new InvSee(), "Invsee系统", "MHDFTools.Command.Invsee", "invsee");
    }

    private void registerHatCommand() {
        registerCommand(plugin, new Hat(), "Hat系统", "MHDFTools.Command.Hat", "hat");
    }

    private void registerTpaHereCommand() {
        registerCommand(plugin, new TpaHere(), "Tpahere系统", "MHDFTools.Command.TpaHere", "tpahere");
    }

    private void registerFastSunCommands() {
        registerCommand(plugin, new Sun(), "快速晴天命令", "MHDFTools.Command.Sun", "sun");
    }

    private void registerFastTimeCommands() {
        registerCommand(plugin, new Day(), "快速天亮命令", "MHDFTools.Command.Sun", "day");
        registerCommand(plugin, new Night(), "快速天黑命令", "MHDFTools.Command.Night", "night");
    }
    private void registerCrashCommand() {
        registerCommand(plugin, new Crash(), "崩端系统", "MHDFTools.Command.Crash", "crash");
    }
    private void registerEconomy() {
        registerCommand(plugin, new Money(), "查询", "MHDFTools.Command.Money", "money");
        registerCommand(plugin, new Pay(), "转账", "MHDFTools.Command.Pay", "pay");
        registerCommand(plugin, new MoneyAdmin(), "管理员管理", "MHDFTools.Command.MoneyAdmin", "moneyadmin");
    }

    private void registerLinkCommands() {
        for (String Command : Objects.requireNonNull(getConfig.getConfigurationSection("CommandLinkSettings.CommandList")).getKeys(false)) {
            CommandLinkList.add(Command);
            registerCommand(plugin, new TabExecutor() {

                @Override
                public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
                    if (getConfig.getBoolean("CommandLinkSettings.CommandList." + Command + ".OnlyPlayer")) {
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(i18n("OnlyPlayer"));
                            return false;
                        }
                    }
                    java.util.List<String> actionList = new ArrayList<>();
                    if (getConfig.getString("CommandLinkSettings.CommandList." + Command + ".ActionList." + args.length) != null) {
                        getConfig.getStringList("CommandLinkSettings.CommandList." + Command + ".ActionList." + args.length).forEach(action -> {
                            for (int i = 0; i < args.length; i++) {
                                action = action.replaceAll("%" + (i + 1), args[i]);
                            }
                            actionList.add(action);
                        });
                    } else {
                        actionList.addAll(getConfig.getStringList("CommandLinkSettings.CommandList." + Command + ".ActionList.Default"));
                    }
                    runAction(sender, null, actionList);
                    return false;
                }

                @Override
                public java.util.@Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
                    java.util.List<String> playerList = getPlayerList(getConfig.getBoolean("CommandLinkSettings.CommandList." + Command + ".BungeeCordGetPlayerList"));
                    java.util.List<String> tabList = new ArrayList<>();
                    if (getConfig.getString("CommandLinkSettings.CommandList." + Command + ".TabList." + args.length) != null) {
                        for (String tab : getConfig.getStringList("CommandLinkSettings.CommandList." + Command + ".TabList." + args.length)) {
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
            }, Command, getConfig.getString("CommandLinkSettings.CommandList." + Command + "." + "Permission"), Command);
        }
    }
}
