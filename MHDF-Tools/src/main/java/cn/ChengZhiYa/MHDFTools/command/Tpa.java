package cn.ChengZhiYa.MHDFTools.command;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.hashmap.IntHasMap;
import cn.ChengZhiYa.MHDFTools.hashmap.StringHasMap;
import cn.ChengZhiYa.MHDFTools.util.message.LogUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.util.BCUtil.*;
import static cn.ChengZhiYa.MHDFTools.util.Util.i18n;

public final class Tpa implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                String PlayerName = args[0];
                if (PlayerName.equals(sender.getName())) {
                    sender.sendMessage(i18n("Tpa.SendMe"));
                    return false;
                }
                if (!ifPlayerOnline(PlayerName)) {
                    sender.sendMessage(i18n("PlayerNotOnline"));
                    return false;
                }
                if (IntHasMap.getHasMap().get(sender.getName() + "_TPATime") != null && IntHasMap.getHasMap().get(sender.getName() + "_TPATime") >= 0) {
                    sender.sendMessage(i18n("Tpa.RepectSend"));
                    return false;
                }
                SendTpa(PlayerName, sender.getName());
                sender.sendMessage(LogUtil.ChatColor(i18n("Tpa.SendDone")));
                return false;
            }
            if (args.length == 2) {
                if (args[0].equals("accept")) {
                    if (StringHasMap.getHasMap().get(args[1] + "_TPAPlayerName") != null && StringHasMap.getHasMap().get(args[1] + "_TPAPlayerName").equals(sender.getName())) {
                        if (IntHasMap.getHasMap().get(args[1] + "_TPATime") != null && IntHasMap.getHasMap().get(args[1] + "_TPATime") >= 0) {
                            CancelTpa(args[1]);
                            if (!ifPlayerOnline(args[1])) {
                                sender.sendMessage(i18n("Tpa.Offline", args[1]));
                                return false;
                            }
                            TpPlayer(args[1], sender.getName());
                            SendMessage(args[1], i18n("Tpa.TeleportDone", sender.getName()));
                            sender.sendMessage(i18n("Tpa.AcceptDone", args[1]));
                        } else {
                            CancelTpa(args[1]);
                            sender.sendMessage(i18n("Tpa.NotSendTeleport"));
                        }
                    } else {
                        sender.sendMessage(i18n("Tpa.NotSendTeleport"));
                    }
                    return false;
                }
                if (args[0].equals("defuse")) {
                    if (StringHasMap.getHasMap().get(args[1] + "_TPAPlayerName") != null && StringHasMap.getHasMap().get(args[1] + "_TPAPlayerName").equals(sender.getName())) {
                        if (IntHasMap.getHasMap().get(args[1] + "_TPATime") != null && IntHasMap.getHasMap().get(args[1] + "_TPATime") >= 0) {
                            CancelTpa(args[1]);
                            if (!ifPlayerOnline(args[1])) {
                                sender.sendMessage(i18n("Tpa.Offline", args[1]));
                                return false;
                            }
                            SendMessage(args[1], i18n("Tpa.Defuse", sender.getName()));
                            sender.sendMessage(i18n("Tpa.DefuseDone", args[1]));
                        } else {
                            CancelTpa(args[1]);
                            sender.sendMessage(i18n("Tpa.NotSendTeleport"));
                        }
                    } else {
                        sender.sendMessage(i18n("Tpa.NotSendTeleport"));
                    }
                    return false;
                }
            }
            sender.sendMessage(i18n("Usage.Tpa"));
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            getPlayerList();
            return Arrays.asList(PlayerList);
        }
        return null;
    }
}
