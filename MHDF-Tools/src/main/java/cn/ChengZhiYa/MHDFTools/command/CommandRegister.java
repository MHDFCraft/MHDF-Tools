package cn.ChengZhiYa.MHDFTools.command;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.MainCommand;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.auth.Login;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.auth.Register;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.flight.Fly;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.flight.FlyTime;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.gamemode.GameMode;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.server.List;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.server.Stop;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.teleport.Tpa;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.teleport.TpaHere;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.warp.DelWarp;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.warp.SetWarp;
import cn.ChengZhiYa.MHDFTools.command.subCommand.main.warp.Warp;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.*;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.back.Back;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.back.TpBack;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.back.UnBack;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.freeze.Freeze;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.freeze.UnFreeze;
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
import cn.ChengZhiYa.MHDFTools.listeners.player.PlayerNickListener;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
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

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.*;
import static cn.ChengZhiYa.MHDFTools.utils.menu.MenuUtil.runAction;

public final class CommandRegister implements Invitable {
    FileConfiguration config;
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();
    boolean canRegister;

    @Override
    public void start() {
        canRegister = false;
        config = PluginLoader.INSTANCE.getPlugin().getConfig();
        registerFreezeCommands();
        for (String configKey : config.getKeys(false)) {
            boolean isEnabled = config.getBoolean(configKey + ".Enable", true);
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
                case "FlySettings":
                    if (isEnabled) {
                        registerFlyCommands();
                    }
                    break;
                case "BackSettings":
                    if (isEnabled) {
                        canRegister = true;
                        registerBackCommands();
                    }
                    break;
                case "FreezeSettings":
                    if (isEnabled) {
                        registerFreezeCommands();
                    }
                    break;
                case "TpBackSettings":
                    if (isEnabled) {
                        canRegister = true;
                        registerTpBackCommands();
                    }
                    break;
                case "VanishSettings":
                    if (isEnabled) {
                        registerVanishCommands();
                    }
                    break;
                case "IpSettings":
                    if (isEnabled) {
                        registerIPCommand();
                    }
                    break;
                case "EasyGamemodeSettings":
                    if (isEnabled) {
                        registerGamemodeCommands();
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
                case "HatSettings":
                    if (isEnabled) {
                        registerHatCommand();
                    }
                    break;
                case "TpaHereSettings":
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
                case "EconomySettings":
                    if (isEnabled && PluginLoader.INSTANCE.isHasVault()) {
                        registerEconomy();
                    }
                    break;
                case "CommandLinkSettings":
                    if (isEnabled) {
                        registerLinkCommands();
                    }
                    break;
                case "TrashSettings":
                    if (isEnabled) {
                        registerTrashCommands();
                    }
                    break;
                case "RotateSettings":
                    if (isEnabled) {
                        registerRotateCommands();
                    }
                    break;
                case "BedSettings":
                    if (isEnabled) {
                        registerBedCommands();
                    }
                    break;
                case "NickSettings":
                    if (isEnabled) {
                        registerNickCommands();
                    }
                    break;
                case "WarpSettings":
                    if (isEnabled) {
                        registerWarpCommands();
                    }
                    break;
                case "FeedSettings":
                    if (isEnabled) {
                        registerFeedCommands();
                    }
                    break;
                case "HealSettings":
                    if (isEnabled) {
                        registerHealCommands();
                    }
                    break;
                case "RepairSettings":
                    if (isEnabled) {
                        registerRepairCommands();
                    }
                    break;
                case "SuicideSettings":
                    if (isEnabled) {
                        registerSuicideCommands();
                    }
                    break;
                case "SudoSettings":
                    if (isEnabled) {
                        registerSudoCommands();
                    }
                    break;
                case "SaySettings":
                    if (isEnabled) {
                        registerSayCommands();
                    }
                    break;
            }
        }
        if (canRegister) {
            registerUnBackCommands();
        }
    }

    public void registerFeedCommands() {
        registerCommand(plugin, new Feed(), "回复饱食度", "MHDFTools.Command.Feed", "feed");
    }
    public void registerFreezeCommands() {
        registerCommand(plugin, new Freeze(), "冻结玩家", "MHDFTools.Command.Freeze", "freeze");
        registerCommand(plugin, new UnFreeze(), "解除冻结", "MHDFTools.Command.UnFreeze", "unfreeze");
    }

    public void registerHealCommands() {
        registerCommand(plugin, new Heal(), "治疗", "MHDFTools.Command.Feed", "heal");
    }

    public void registerRepairCommands() {
        registerCommand(plugin, new Repair(), "修复手上物品", "MHDFTools.Command.Repair", "repair");
    }

    public void registerSuicideCommands() {
        registerCommand(plugin, new Suicide(), "自杀", "MHDFTools.Command.Suicide", "suicide");
    }

    public void registerSudoCommands() {
        registerCommand(plugin, new Sudo(), "强制执行命令", "MHDFTools.Command.Sudo", "sudo");
    }

    public void registerSayCommands() {
        registerCommand(plugin, new Say(), "全服消息", "MHDFTools.Command.Say", "say");
    }

    public void registerWarpCommands() {
        registerCommand(plugin, new SetWarp(), "设置地标", "MHDFTools.Command.SetWarp", "setwarp");
        registerCommand(plugin, new DelWarp(), "删除地标", "MHDFTools.Command.DelWarp", "delwarp");
        registerCommand(plugin, new Warp(), "传送至地标", "MHDFTools.Command.Warp", "warp");
    }

    public void registerBedCommands() {
        registerCommand(plugin, new Bed(), "返回床位置", "MHDFTools.Command.Bed", "bed");
    }

    public void registerNickCommands() {
        registerCommand(plugin, new Nick(), "改名系统", "MHDFTools.Command.Nick", "nick");
        Bukkit.getPluginManager().registerEvents(new PlayerNickListener(), plugin);
    }

    private void registerTrashCommands() {
        registerCommand(plugin, new Trash(), "垃圾桶", "MHDFTools.Command.Trash", "trash");
    }

    private void registerUnBackCommands() {
        registerCommand(plugin, new UnBack(), "Back系统", "MHDFTools.Command.UnBack", "unback");
    }

    private void registerTpBackCommands() {
        registerCommand(plugin, new TpBack(), "TpBack系统", "MHDFTools.Command.TpBack", "tpback");
    }

    private void registerRotateCommands() {
        registerCommand(plugin, new Rotate(), "给玩家转头", "MHDFTools.Command.rotate", "rotate");
    }

    private void registerHomeCommands() {
        registerCommand(plugin, new SetHome(), "设置家", "MHDFTools.Command.SetHome", "sethome");
        registerCommand(plugin, new DelHome(), "删除家", "MHDFTools.Command.DelHome", "delhome");
        registerCommand(plugin, new Home(), "传送至家", "MHDFTools.Command.Home", "home");
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
    }

    private void registerSpawnCommands() {
        registerCommand(plugin, new Spawn(), "Spawn命令", "MHDFTools.Command.Spawn", "spawn");
        registerCommand(plugin, new SetSpawn(), "SetSpawn命令", "MHDFTools.Command.SetSpawn", "setspawn");

    }

    private void registerStopCommand() {
        registerCommand(plugin, new Stop(), "关闭服务器", "MHDFTools.Command.Stop", "stop");
    }

    private void registerFlyCommands() {
        registerCommand(plugin, new Fly(), "飞行系统", "MHDFTools.Command.Fly", "fly");
        registerCommand(plugin, new FlyTime(), "限时飞行系统", "MHDFTools.Command.FlyTime", "flytime");

    }

    private void registerBackCommands() {
        registerCommand(plugin, new Back(), "Back系统", "MHDFTools.Command.Back", "back");
    }

    private void registerVanishCommands() {
        registerCommand(plugin, new Vanish(), "Vanish系统", "MHDFTools.Command.Vanish", "vanish");
        registerCommand(plugin, new Vanish(), "Vanish系统", "MHDFTools.Command.Vanish", "v");
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

    private void registerEconomy() {
        registerCommand(plugin, new Money(), "查询", "MHDFTools.Command.Money", "money");
        registerCommand(plugin, new Pay(), "转账", "MHDFTools.Command.Pay", "pay");
        registerCommand(plugin, new MoneyAdmin(), "管理员管理", "MHDFTools.Command.MoneyAdmin", "moneyadmin");
    }

    private void registerLinkCommands() {
        for (String Command : Objects.requireNonNull(config.getConfigurationSection("CommandLinkSettings.CommandList")).getKeys(false)) {
            CommandLinkList.add(Command);
            registerCommand(plugin, new TabExecutor() {

                @Override
                public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
                    if (config.getBoolean("CommandLinkSettings.CommandList." + Command + ".OnlyPlayer")) {
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(i18n("OnlyPlayer"));
                            return false;
                        }
                    }
                    java.util.List<String> actionList = new ArrayList<>();
                    if (config.getString("CommandLinkSettings.CommandList." + Command + ".ActionList." + args.length) != null) {
                        config.getStringList("CommandLinkSettings.CommandList." + Command + ".ActionList." + args.length).forEach(action -> {
                            for (int i = 0; i < args.length; i++) {
                                action = action.replaceAll("%" + (i + 1), args[i]);
                            }
                            actionList.add(action);
                        });
                    } else {
                        actionList.addAll(config.getStringList("CommandLinkSettings.CommandList." + Command + ".ActionList.Default"));
                    }
                    runAction(sender, null, actionList);
                    return false;
                }

                @Override
                public java.util.@Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
                    java.util.List<String> playerList = getPlayerList(config.getBoolean("CommandLinkSettings.CommandList." + Command + ".BungeeCordGetPlayerList"));
                    java.util.List<String> tabList = new ArrayList<>();
                    if (config.getString("CommandLinkSettings.CommandList." + Command + ".TabList." + args.length) != null) {
                        for (String tab : config.getStringList("CommandLinkSettings.CommandList." + Command + ".TabList." + args.length)) {
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
            }, Command, config.getString("CommandLinkSettings.CommandList." + Command + "." + "Permission"), Command);
        }
    }
}
