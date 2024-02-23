package cn.ChengZhiYa.MHDFTools.Commands;

import cn.ChengZhiYa.MHDFTools.HashMap.IntHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.StringHasMap;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.Utils.BCUtil.*;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.ChatColor;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class TpaHere implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                String PlayerName = args[0];
                if (PlayerName.equals(sender.getName())) {
                    sender.sendMessage(i18n("TpaHere.SendMe"));
                    return false;
                }
                if (!ifPlayerOnline(PlayerName)) {
                    sender.sendMessage(i18n("PlayerNotOnline"));
                    return false;
                }
                if (IntHasMap.getHasMap().get(sender.getName() + "_TPAHereTime") != null && IntHasMap.getHasMap().get(sender.getName() + "_TPAHereTime") >= 0) {
                    sender.sendMessage(i18n("TpaHere.RepectSend"));
                    return false;
                }
                SendTpaHere(PlayerName, sender.getName());
                sender.sendMessage(ChatColor(i18n("TpaHere.SendDone")));
                return false;
            }
            if (args.length == 2) {
                if (args[0].equals("accept")) {
                    if (StringHasMap.getHasMap().get(args[1] + "_TPAHerePlayerName") != null && StringHasMap.getHasMap().get(args[1] + "_TPAHerePlayerName").equals(sender.getName())) {
                        if (IntHasMap.getHasMap().get(args[1] + "_TPAHereTime") != null && IntHasMap.getHasMap().get(args[1] + "_TPAHereTime") >= 0) {
                            CancelTpaHere(args[1]);
                            if (!ifPlayerOnline(args[1])) {
                                sender.sendMessage(i18n("TpaHere.Offline", args[1]));
                                return false;
                            }
                            TpPlayer(sender.getName(), args[1]);
                            SendMessage(args[1], i18n("TpaHere.TeleportDone", sender.getName()));
                            sender.sendMessage(i18n("TpaHere.AcceptDone", args[1]));
                        } else {
                            CancelTpaHere(args[1]);
                            sender.sendMessage(i18n("TpaHere.NotSendTeleport"));
                        }
                    } else {
                        sender.sendMessage(i18n("TpaHere.NotSendTeleport"));
                    }
                    return false;
                }
                if (args[0].equals("defuse")) {
                    if (StringHasMap.getHasMap().get(args[1] + "_TPAHerePlayerName") != null && StringHasMap.getHasMap().get(args[1] + "_TPAHerePlayerName").equals(sender.getName())) {
                        if (IntHasMap.getHasMap().get(args[1] + "_TPAHereTime") != null && IntHasMap.getHasMap().get(args[1] + "_TPAHereTime") >= 0) {
                            CancelTpaHere(args[1]);
                            if (!ifPlayerOnline(args[1])) {
                                sender.sendMessage(i18n("TpaHere.Offline", args[1]));
                                return false;
                            }
                            SendMessage(args[1], i18n("TpaHere.Defuse", sender.getName()));
                            sender.sendMessage(i18n("TpaHere.DefuseDone", args[1]));
                        } else {
                            CancelTpaHere(args[1]);
                            sender.sendMessage(i18n("TpaHere.NotSendTeleport"));
                        }
                    } else {
                        sender.sendMessage(i18n("TpaHere.NotSendTeleport"));
                    }
                    return false;
                }
            }
            sender.sendMessage(i18n("Usage.TpaHere"));
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
