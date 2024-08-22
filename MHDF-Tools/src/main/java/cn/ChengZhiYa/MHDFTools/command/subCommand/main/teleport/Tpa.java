package cn.ChengZhiYa.MHDFTools.command.subCommand.main.teleport;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.entity.TpaData;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import cn.ChengZhiYa.MHDFTools.utils.command.TpaUtil;
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

public final class Tpa implements TabExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length >= 1) {
                Player player = (Player) sender;
                switch (args[0]) {
                    case "accept": {
                        if (args.length == 2) {
                            TpaData tpaData = TpaUtil.getTpaHashMap().get(args[1]);
                            if (tpaData != null && tpaData.getTargetPlayerName().equals(player.getName())) {
                                if (BungeeCordUtil.ifPlayerOnline(args[1])) {
                                    if (tpaData.getTpaOutTime() > 0) {
                                        BungeeCordUtil.tpPlayer(args[1], player.getName());
                                        BungeeCordUtil.sendMessage(args[1], i18n("Tpa.TeleportDone", args[1]));
                                        BungeeCordUtil.sendMessage(player.getName(), i18n("Tpa.AcceptDone", args[1]));

                                        BungeeCordUtil.cancelTpa(args[1]);
                                    } else {
                                        BungeeCordUtil.sendMessage(args[1], SpigotUtil.i18n("Tpa.TimeOutDone", player.getName()));
                                        BungeeCordUtil.sendMessage(player.getName(), SpigotUtil.i18n("Tpa.TimeOut", args[1]));
                                        BungeeCordUtil.cancelTpa(args[1]);
                                    }
                                } else {
                                    player.sendMessage(i18n("PlayerNotOnline"));
                                    BungeeCordUtil.cancelTpa(args[1]);
                                }
                            } else {
                                player.sendMessage(i18n("Tpa.NotSendTeleport"));
                            }
                            return false;
                        }
                        break;
                    }
                    case "defuse": {
                        if (args.length == 2) {
                            TpaData tpaData = TpaUtil.getTpaHashMap().get(args[1]);
                            if (tpaData != null && tpaData.getTargetPlayerName().equals(player.getName())) {
                                if (BungeeCordUtil.ifPlayerOnline(args[1])) {
                                    if (tpaData.getTpaOutTime() > 0) {
                                        BungeeCordUtil.sendMessage(args[1], i18n("Tpa.Defuse", player.getName()));
                                        BungeeCordUtil.sendMessage(player.getName(), i18n("Tpa.DefuseDone", args[1]));

                                        BungeeCordUtil.cancelTpa(args[1]);
                                    } else {
                                        BungeeCordUtil.sendMessage(args[1], SpigotUtil.i18n("Tpa.TimeOutDone", player.getName()));
                                        BungeeCordUtil.sendMessage(player.getName(), SpigotUtil.i18n("Tpa.TimeOut", args[1]));
                                        BungeeCordUtil.cancelTpa(args[1]);
                                    }
                                } else {
                                    player.sendMessage(i18n("PlayerNotOnline"));
                                    BungeeCordUtil.cancelTpa(args[1]);
                                }
                            } else {
                                player.sendMessage(i18n("Tpa.NotSendTeleport"));
                            }
                            return false;
                        }
                        break;
                    }
                    default: {
                        if (BungeeCordUtil.ifPlayerOnline(args[0])) {
                            if (!args[0].equals(sender.getName())) {
                                if (TpaUtil.getTpaHashMap().get(player.getName()) != null && TpaUtil.getTpaHashMap().get(player.getName()).getTpaOutTime() > 0) {
                                    sender.sendMessage(i18n("Tpa.RepectSend"));
                                    return false;
                                }
                                BungeeCordUtil.sendTpa(player.getName(), args[0]);
                                sender.sendMessage(MessageUtil.colorMessage(i18n("Tpa.SendDone")));
                            } else {
                                sender.sendMessage(i18n("Tpa.SendMe"));
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
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            BungeeCordUtil.getPlayerList();
            return new ArrayList<>(Arrays.asList(BungeeCordUtil.PlayerList));
        } else {
            return null;
        }
    }
}
