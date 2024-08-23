package cn.ChengZhiYa.MHDFTools.command.subCommand.misc;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.database.NickUtil;
import cn.ChengZhiYa.MHDFTools.utils.message.ColorLogs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Nick implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        switch (args.length) {
            case 1: {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    boolean setNick = !args[0].equals("off");
                    String nickName = setNick ? args[0] : player.getName();
                    if (!player.hasPermission("MHDFTools.Command.Nick.BypassCheck") && setNick) {
                        for (String blackWord : PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("NickSettings.BlackWordList")) {
                            if (nickName.toLowerCase(Locale.ROOT).contains(blackWord.toLowerCase(Locale.ROOT))) {
                                ColorLogs.debug("设置昵称失败", "设定昵称: " + nickName, "敏感词: " + blackWord, "原因: 不能包含敏感词");
                                player.sendMessage(i18n("Nick.BlackWord", blackWord));
                                return false;
                            }
                        }
                        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("NickSettings.AntiSetPlayerName")) {
                            for (OfflinePlayer players : Bukkit.getOfflinePlayers()) {
                                if (Objects.equals(players.getName(), nickName)) {
                                    ColorLogs.debug("设置昵称失败", "设定昵称: " + nickName, "其他玩家的昵称: " + nickName, "原因: 不能使用其他玩家的ID");
                                    player.sendMessage(i18n("Nick.AntiSetPlayerName"));
                                    return false;
                                }
                            }
                        }
                        if (nickName.length() < PluginLoader.INSTANCE.getPlugin().getConfig().getInt("NickSettings.MinLength")) {
                            ColorLogs.debug("设置昵称失败", "设定昵称: " + nickName, "最小长度: " + PluginLoader.INSTANCE.getPlugin().getConfig().getInt("NickSettings.MinLength"), "原因: 名字过短");
                            player.sendMessage(i18n("Nick.MinLength", String.valueOf(PluginLoader.INSTANCE.getPlugin().getConfig().getInt("NickSettings.MinLength"))));
                            return false;
                        }
                        if (nickName.length() > PluginLoader.INSTANCE.getPlugin().getConfig().getInt("NickSettings.MaxLength")) {
                            ColorLogs.debug("设置昵称失败", "设定昵称: " + nickName, "最长长度: " + PluginLoader.INSTANCE.getPlugin().getConfig().getInt("NickSettings.MinLength"), "原因: 名字过长");
                            player.sendMessage(i18n("Nick.MaxLength", String.valueOf(PluginLoader.INSTANCE.getPlugin().getConfig().getInt("NickSettings.MaxLength"))));
                            return false;
                        }
                    }
                    if (setNick) {
                        ColorLogs.debug("设置昵称成功", "设定昵称: " + nickName, "玩家ID: " + player.getName());
                        NickUtil.setNickName(player.getName(), nickName);
                    } else {
                        ColorLogs.debug("移除昵称成功", "设定昵称: " + nickName, "玩家ID: " + player.getName());
                        NickUtil.removeNickName(player.getName());
                    }
                    setNickForPlayer(player, nickName, args);
                    player.sendMessage(i18n("Nick.SetDone", nickName));
                } else {
                    sender.sendMessage(i18n("OnlyPlayer"));
                }
                break;
            }
            case 2: {
                if (sender.hasPermission("MHDFTools.Command.Nick.SetOther")) {
                    boolean setNick = !args[0].equals("off");
                    String nickName = setNick ? args[1] : args[0];
                    if (setNick) {
                        ColorLogs.debug("设置昵称成功", "设定昵称: " + nickName, "玩家ID: " + args[1]);
                        NickUtil.setNickName(args[1], nickName);
                    } else {
                        ColorLogs.debug("移除昵称成功", "设定昵称: " + nickName, "玩家ID: " + args[1]);
                        NickUtil.removeNickName(args[1]);
                    }
                    if (Bukkit.getPlayer(args[1]) != null) {
                        Player player = (Player) sender;
                        setNickForPlayer(player, nickName, args);
                        player.sendMessage(i18n("Nick.SetDone", nickName));
                    }
                    sender.sendMessage(i18n("Nick.SetOtherDone", args[1], nickName));
                } else {
                    sender.sendMessage(i18n("NoPermission"));
                }
                break;
            }
            default:
                sender.sendMessage(i18n("Usage.Nick"));
                break;
        }
        return true;
    }
    private void setNickForPlayer(Player player, String name, String[] args) {
        boolean setNick = !args[0].equals("off");
        Bukkit.getScheduler().runTaskAsynchronously(PluginLoader.INSTANCE.getPlugin(), () -> {
            player.setDisplayName(name);
            player.setCustomName(name);
            player.setPlayerListName(name);
            player.setCustomNameVisible(!setNick);
        });
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1 && sender.hasPermission("MHDFTools.Command.Nick")) {
            return Collections.singletonList("off");
        }
        if (args.length == 2 && sender.hasPermission("MHDFTools.Command.Nick.SetOther")) {
            if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
                BungeeCordUtil.getPlayerList();
                return new ArrayList<>(Arrays.asList(BungeeCordUtil.PlayerList));
            } else {
                return null;
            }
        }
        return new ArrayList<>();
    }
}