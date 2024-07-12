package cn.ChengZhiYa.MHDFTools.command.subCommand.main.teleport;

import cn.ChengZhiYa.MHDFTools.entity.TpaData;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import cn.ChengZhiYa.MHDFTools.utils.command.TpaHereUtil;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class TpaHere implements TabExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length >= 1) {
                Player player = (Player) sender;
                switch (args[0]) {
                    case "accept": {
                        if (args.length == 2) {
                            TpaData tpaData = TpaHereUtil.getTpahereHashMap().get(args[1]);
                            if (tpaData != null && tpaData.getTargetPlayerName().equals(player.getName())) {
                                if (BungeeCordUtil.ifPlayerOnline(args[1])) {
                                    if (tpaData.getTpaOutTime() > 0) {
                                        BungeeCordUtil.tpPlayer(player.getName(), args[1]);
                                        BungeeCordUtil.sendMessage(args[1], i18n("TpaHere.TeleportDone", args[1]));
                                        BungeeCordUtil.sendMessage(player.getName(), i18n("TpaHere.AcceptDone", args[1]));

                                        BungeeCordUtil.cancelTpaHere(args[1]);
                                    } else {
                                        BungeeCordUtil.sendMessage(args[1], SpigotUtil.i18n("TpaHere.TimeOutDone", player.getName()));
                                        BungeeCordUtil.sendMessage(player.getName(), SpigotUtil.i18n("TpaHere.TimeOut", args[1]));
                                        BungeeCordUtil.cancelTpaHere(args[1]);
                                    }
                                } else {
                                    player.sendMessage(i18n("PlayerNotOnline"));
                                    BungeeCordUtil.cancelTpaHere(args[1]);
                                }
                            } else {
                                player.sendMessage(i18n("TpaHere.NotSendTeleport"));
                            }
                            return false;
                        }
                        break;
                    }
                    case "defuse": {
                        if (args.length == 2) {
                            TpaData tpaData = TpaHereUtil.getTpahereHashMap().get(args[1]);
                            if (tpaData != null && tpaData.getTargetPlayerName().equals(player.getName())) {
                                if (BungeeCordUtil.ifPlayerOnline(args[1])) {
                                    if (tpaData.getTpaOutTime() > 0) {
                                        BungeeCordUtil.sendMessage(args[1], i18n("TpaHere.Defuse", player.getName()));
                                        BungeeCordUtil.sendMessage(player.getName(), i18n("TpaHere.DefuseDone", args[1]));

                                        BungeeCordUtil.cancelTpaHere(args[1]);
                                    } else {
                                        BungeeCordUtil.sendMessage(args[1], SpigotUtil.i18n("TpaHere.TimeOutDone", player.getName()));
                                        BungeeCordUtil.sendMessage(player.getName(), SpigotUtil.i18n("TpaHere.TimeOut", args[1]));
                                        BungeeCordUtil.cancelTpaHere(args[1]);
                                    }
                                } else {
                                    player.sendMessage(i18n("PlayerNotOnline"));
                                    BungeeCordUtil.cancelTpaHere(args[1]);
                                }
                            } else {
                                player.sendMessage(i18n("TpaHere.NotSendTeleport"));
                            }
                            return false;
                        }
                        break;
                    }
                    default: {
                        if (BungeeCordUtil.ifPlayerOnline(args[0])) {
                            if (!args[0].equals(sender.getName())) {
                                if (TpaHereUtil.getTpahereHashMap().get(player.getName()) != null && TpaHereUtil.getTpahereHashMap().get(player.getName()).getTpaOutTime() > 0) {
                                    sender.sendMessage(i18n("TpaHere.RepectSend"));
                                    return false;
                                }
                                BungeeCordUtil.sendTpaHere(player.getName(), args[0]);
                                sender.sendMessage(MessageUtil.colorMessage(i18n("TpaHere.SendDone")));
                            } else {
                                sender.sendMessage(i18n("TpaHere.SendMe"));
                            }
                        } else {
                            sender.sendMessage(i18n("PlayerNotOnline"));
                        }
                        return false;
                    }
                }
            }
            sender.sendMessage(i18n("Usage.Tpa"));
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        BungeeCordUtil.getPlayerList();
        return new ArrayList<>(Arrays.asList(BungeeCordUtil.PlayerList));
    }
}
