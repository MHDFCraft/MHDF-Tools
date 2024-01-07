package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.ChatColor;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.i18n;

public final class Tpa implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                String PlayerName = args[0];
                if (PlayerName.equals(sender.getName())) {
                    sender.sendMessage(i18n("Tpa.SendMe"));
                    return false;
                }
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(i18n("PlayerNotOnline"));
                    return false;
                }
                if (IntHasMap.getHasMap().get(sender.getName() + "_TPATime") != null && IntHasMap.getHasMap().get(sender.getName() + "_TPATime") >= 0) {
                    sender.sendMessage(i18n("Tpa.RepectSend"));
                    return false;
                }
                TextComponent Message = new TextComponent();
                for (String Messages : i18n("Tpa.Message").split("\\?")) {
                    if (Messages.equals("Accent")) {
                        TextComponent MessageButton = new TextComponent(ChatColor(i18n("Tpa.AccentMessage")));
                        MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accent " + sender.getName()));
                        MessageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor("&a接受" + sender.getName() + "的传送请求"))));
                        Message.addExtra(MessageButton);
                    } else {
                        if (Messages.equals("Defuse")) {
                            TextComponent MessageButton = new TextComponent(ChatColor(i18n("Tpa.DefuseMessage")));
                            MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa defuse " + sender.getName()));
                            MessageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor("&c拒绝" + sender.getName() + "的传送请求"))));
                            Message.addExtra(MessageButton);
                        } else {
                            Message.addExtra(new TextComponent(ChatColor(Messages.replaceAll("%1", sender.getName()))));
                        }
                    }
                }
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).spigot().sendMessage(Message);
                sender.sendMessage(ChatColor(i18n("Tpa.SendDone")));
                IntHasMap.getHasMap().put(sender.getName() + "_TPATime", ChengToolsReloaded.instance.getConfig().getInt("Tpa.OutTime"));
                StringHasMap.getHasMap().put(sender.getName() + "_TPAPlayerName", Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).getName());
                return false;
            }
            if (args.length == 2) {
                if (args[0].equals("accent")) {
                    if (StringHasMap.getHasMap().get(args[1] + "_TPAPlayerName") != null && StringHasMap.getHasMap().get(args[1] + "_TPAPlayerName").equals(sender.getName())) {
                        if (IntHasMap.getHasMap().get(args[1] + "_TPATime") != null && IntHasMap.getHasMap().get(args[1] + "_TPATime") >= 0) {
                            IntHasMap.getHasMap().remove(args[1] + "_TPATime");
                            StringHasMap.getHasMap().remove(args[1] + "_TPAPlayerName");
                            if (Bukkit.getPlayer(args[1]) == null) {
                                sender.sendMessage(i18n("Tpa.Offline", args[1]));
                                return false;
                            }
                            Objects.requireNonNull(Bukkit.getPlayer(args[1])).teleport(((Player) sender).getLocation());
                            Objects.requireNonNull(Bukkit.getPlayer(args[1])).sendMessage(i18n("Tpa.TeleportDone", args[1]));
                            sender.sendMessage(i18n("Tpa.AcceptDone", sender.getName()));

                        } else {
                            IntHasMap.getHasMap().remove(args[1] + "_TPATime");
                            StringHasMap.getHasMap().remove(args[1] + "_TPAPlayerName");
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
                            IntHasMap.getHasMap().remove(args[1] + "_TPATime");
                            StringHasMap.getHasMap().remove(args[1] + "_TPAPlayerName");
                            if (Bukkit.getPlayer(args[1]) == null) {
                                sender.sendMessage(i18n("Tpa.Offline", args[1]));
                                return false;
                            }
                            Objects.requireNonNull(Bukkit.getPlayer(args[1])).sendMessage(i18n("Tpa.Defuse", sender.getName()));
                            sender.sendMessage(i18n("Tpa.DefuseDone", args[1]));
                        } else {
                            IntHasMap.getHasMap().remove(args[1] + "_TPATime");
                            StringHasMap.getHasMap().remove(args[1] + "_TPAPlayerName");
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
}
