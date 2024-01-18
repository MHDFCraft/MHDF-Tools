package cn.ChengZhiYa.MHDFTools.Commands;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.HashMap.IntHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.StringHasMap;
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

import static cn.ChengZhiYa.MHDFTools.Utils.Util.ChatColor;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class TpaHere implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                String PlayerName = args[0];
                if (PlayerName.equals(sender.getName())) {
                    sender.sendMessage(i18n("TpaHere.SendMe"));
                    return false;
                }
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(i18n("PlayerNotOnline"));
                    return false;
                }
                if (IntHasMap.getHasMap().get(sender.getName() + "_TPAHereTime") != null && IntHasMap.getHasMap().get(sender.getName() + "_TPAHereTime") >= 0) {
                    sender.sendMessage(i18n("TpaHere.RepectSend"));
                    return false;
                }
                TextComponent Message = new TextComponent();
                for (String Messages : i18n("TpaHere.Message").split("\\?")) {
                    if (Messages.equals("Accent")) {
                        TextComponent MessageButton = new TextComponent(ChatColor(i18n("TpaHere.AccentMessage")));
                        MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpahere accent " + sender.getName()));
                        MessageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor("&a接受" + sender.getName() + "的传送请求"))));
                        Message.addExtra(MessageButton);
                    } else {
                        if (Messages.equals("Defuse")) {
                            TextComponent MessageButton = new TextComponent(ChatColor(i18n("TpaHere.DefuseMessage")));
                            MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpahere defuse " + sender.getName()));
                            MessageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor("&c拒绝" + sender.getName() + "的传送请求"))));
                            Message.addExtra(MessageButton);
                        } else {
                            Message.addExtra(new TextComponent(ChatColor(Messages.replaceAll("%1", sender.getName()))));
                        }
                    }
                }
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).spigot().sendMessage(Message);
                sender.sendMessage(ChatColor(i18n("TpaHere.SendDone")));
                IntHasMap.getHasMap().put(sender.getName() + "_TPAHereTime", MHDFTools.instance.getConfig().getInt("Tpahere.OutTime"));
                StringHasMap.getHasMap().put(sender.getName() + "_TPAHerePlayerName", Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).getName());
                return false;
            }
            if (args.length == 2) {
                if (args[0].equals("accent")) {
                    if (StringHasMap.getHasMap().get(args[1] + "_TPAHerePlayerName") != null && StringHasMap.getHasMap().get(args[1] + "_TPAHerePlayerName").equals(sender.getName())) {
                        if (IntHasMap.getHasMap().get(args[1] + "_TPAHereTime") != null && IntHasMap.getHasMap().get(args[1] + "_TPAHereTime") >= 0) {
                            IntHasMap.getHasMap().remove(args[1] + "_TPAHereTime");
                            StringHasMap.getHasMap().remove(args[1] + "_TPAHerePlayerName");
                            if (Bukkit.getPlayer(args[1]) == null) {
                                sender.sendMessage(i18n("TpaHere.Offline", args[1]));
                                return false;
                            }
                            ((Player) sender).teleport(Objects.requireNonNull(Bukkit.getPlayer(args[1])).getLocation());
                            Objects.requireNonNull(Bukkit.getPlayer(args[1])).sendMessage(i18n("TpaHere.TeleportDone", sender.getName()));
                            sender.sendMessage(i18n("TpaHere.AcceptDone", args[1]));

                        } else {
                            IntHasMap.getHasMap().remove(args[1] + "_TPAHereTime");
                            StringHasMap.getHasMap().remove(args[1] + "_TPAHerePlayerName");
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
                            IntHasMap.getHasMap().remove(args[1] + "_TPAHereTime");
                            StringHasMap.getHasMap().remove(args[1] + "_TPAHerePlayerName");
                            if (Bukkit.getPlayer(args[1]) == null) {
                                sender.sendMessage(i18n("TpaHere.Offline", args[1]));
                                return false;
                            }
                            Objects.requireNonNull(Bukkit.getPlayer(args[1])).sendMessage(i18n("TpaHere.Defuse", sender.getName()));
                            sender.sendMessage(i18n("TpaHere.DefuseDone", args[1]));
                        } else {
                            IntHasMap.getHasMap().remove(args[1] + "_TPAHereTime");
                            StringHasMap.getHasMap().remove(args[1] + "_TPAHerePlayerName");
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
}
