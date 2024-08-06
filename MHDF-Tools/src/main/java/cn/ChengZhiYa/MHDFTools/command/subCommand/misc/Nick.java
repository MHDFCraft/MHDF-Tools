package cn.ChengZhiYa.MHDFTools.command.subCommand.misc;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.database.NickUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Nick implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        switch (args.length) {
            case 1: {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    boolean setNick = args[0].equals("off");
                    String nickName = setNick ? player.getName() : args[0];
                    if (player.hasPermission("MHDFTools.Command.Nick.BypassCheck")) {
                        for (String blackWord : MHDFTools.instance.getConfig().getStringList("NickSettings.BlackWordList")) {
                            if (nickName.contains(blackWord)) {
                                player.sendMessage(i18n("Nick.BlackWord", blackWord));
                                return false;
                            }
                        }
                        if (nickName.length() < MHDFTools.instance.getConfig().getInt("NickSettings.MinLength")) {
                            player.sendMessage(i18n("Nick.MinLength", String.valueOf(MHDFTools.instance.getConfig().getInt("NickSettings.MinLength"))));
                            return false;
                        }
                        if (nickName.length() > MHDFTools.instance.getConfig().getInt("NickSettings.MaxLength")) {
                            player.sendMessage(i18n("Nick.MaxLength", String.valueOf(MHDFTools.instance.getConfig().getInt("NickSettings.MaxLength"))));
                            return false;
                        }
                    }
                    if (setNick) {
                        NickUtil.setNickName(player.getName(), nickName);
                    } else {
                        NickUtil.removeNickName(player.getName());
                    }
                    player.setDisplayName(nickName);
                    player.setCustomName(nickName);
                    player.setPlayerListName(nickName);
                    player.setCustomNameVisible(!setNick);
                    player.sendMessage(i18n("Nick.SetDone", nickName));
                } else {
                    sender.sendMessage(i18n("OnlyPlayer"));
                }
                break;
            }
            case 2: {
                if (sender.hasPermission("MHDFTools.Command.Nick.SetOther")) {
                    boolean setNick = args[0].equals("off");
                    String nickName = setNick ? args[1] : args[0];
                    if (setNick) {
                        NickUtil.setNickName(args[1], nickName);
                    } else {
                        NickUtil.removeNickName(args[1]);
                    }
                    if (Bukkit.getPlayer(args[1]) != null) {
                        Player player = (Player) sender;
                        player.setDisplayName(nickName);
                        player.setCustomName(nickName);
                        player.setPlayerListName(nickName);
                        player.setCustomNameVisible(!setNick);
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

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1 && sender.hasPermission("MHDFTools.Command.Nick")) {
            return Collections.singletonList("off");
        }
        if (args.length == 2 && sender.hasPermission("MHDFTools.Command.Nick.SetOther")) {
            if (MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
                BungeeCordUtil.getPlayerList();
                return new ArrayList<>(Arrays.asList(BungeeCordUtil.PlayerList));
            } else {
                return null;
            }
        }
        return new ArrayList<>();
    }
}