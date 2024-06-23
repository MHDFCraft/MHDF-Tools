package cn.ChengZhiYa.MHDFTools.command.subCommand.main.teleport;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

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
                if (!BungeeCordUtil.ifPlayerOnline(PlayerName)) {
                    sender.sendMessage(i18n("PlayerNotOnline"));
                    return false;
                }
                if (MapUtil.getIntHashMap().get(sender.getName() + "_TPATime") != null && MapUtil.getIntHashMap().get(sender.getName() + "_TPATime") >= 0) {
                    sender.sendMessage(i18n("Tpa.RepectSend"));
                    return false;
                }
                BungeeCordUtil.SendTpa(PlayerName, sender.getName());
                sender.sendMessage(MessageUtil.colorMessage(i18n("Tpa.SendDone")));
                return false;
            }
            if (args.length == 2) {
                if (args[0].equals("accept")) {
                    if (MapUtil.getStringHashMap().get(args[1] + "_TPAPlayerName") != null && MapUtil.getStringHashMap().get(args[1] + "_TPAPlayerName").equals(sender.getName())) {
                        if (MapUtil.getIntHashMap().get(args[1] + "_TPATime") != null && MapUtil.getIntHashMap().get(args[1] + "_TPATime") >= 0) {
                            BungeeCordUtil.CancelTpa(args[1]);
                            if (!BungeeCordUtil.ifPlayerOnline(args[1])) {
                                sender.sendMessage(i18n("Tpa.Offline", args[1]));
                                return false;
                            }
                            BungeeCordUtil.TpPlayer(args[1], sender.getName());
                            BungeeCordUtil.SendMessage(args[1], i18n("Tpa.TeleportDone", sender.getName()));
                            sender.sendMessage(i18n("Tpa.AcceptDone", args[1]));
                        } else {
                            BungeeCordUtil.CancelTpa(args[1]);
                            sender.sendMessage(i18n("Tpa.NotSendTeleport"));
                        }
                    } else {
                        sender.sendMessage(i18n("Tpa.NotSendTeleport"));
                    }
                    return false;
                }
                if (args[0].equals("defuse")) {
                    if (MapUtil.getStringHashMap().get(args[1] + "_TPAPlayerName") != null && MapUtil.getStringHashMap().get(args[1] + "_TPAPlayerName").equals(sender.getName())) {
                        if (MapUtil.getIntHashMap().get(args[1] + "_TPATime") != null && MapUtil.getIntHashMap().get(args[1] + "_TPATime") >= 0) {
                            BungeeCordUtil.CancelTpa(args[1]);
                            if (!BungeeCordUtil.ifPlayerOnline(args[1])) {
                                sender.sendMessage(i18n("Tpa.Offline", args[1]));
                                return false;
                            }
                            BungeeCordUtil.SendMessage(args[1], i18n("Tpa.Defuse", sender.getName()));
                            sender.sendMessage(i18n("Tpa.DefuseDone", args[1]));
                        } else {
                            BungeeCordUtil.CancelTpa(args[1]);
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

    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            BungeeCordUtil.getPlayerList();
            return Arrays.asList(BungeeCordUtil.PlayerList);
        }
        return null;
    }
}