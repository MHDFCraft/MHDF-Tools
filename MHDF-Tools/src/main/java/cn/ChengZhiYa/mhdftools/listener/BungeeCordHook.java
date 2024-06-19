package cn.chengzhiya.mhdftools.listener;

import cn.chengzhiya.mhdftools.MHDFTools;
import cn.chengzhiya.mhdftools.hashmap.IntHasMap;
import cn.chengzhiya.mhdftools.hashmap.LocationHasMap;
import cn.chengzhiya.mhdftools.hashmap.StringHasMap;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;
import static cn.chengzhiya.mhdftools.util.BCUtil.PlayerList;
import static cn.chengzhiya.mhdftools.util.BCUtil.ServerName;
import static cn.chengzhiya.mhdftools.util.Util.*;
import static cn.chengzhiya.mhdftools.util.database.HomeUtil.getHomeLocation;

public final class BungeeCordHook implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        try {
            String subchannel = in.readUTF();
            if (subchannel.equals("PlayerList")) {
                in.readUTF();
                PlayerList = in.readUTF().split(", ");
            }
            if (subchannel.equals("SendTpa")) {
                String PlayerName = in.readUTF();
                String SendPlayerName = in.readUTF();

                IntHasMap.getHasMap().put(SendPlayerName + "_TPATime", MHDFTools.instance.getConfig().getInt("Tpa.OutTime"));
                StringHasMap.getHasMap().put(SendPlayerName + "_TPAPlayerName", PlayerName);

                TextComponent Message = new TextComponent();
                for (String Messages : i18n("Tpa.Message").split("\\?")) {
                    if (Messages.equals("Accent")) {
                        TextComponent MessageButton = new TextComponent(ChatColor(i18n("Tpa.AccentMessage")));
                        MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accept " + SendPlayerName));
                        MessageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor("&a接受" + SendPlayerName + "的传送请求"))));
                        Message.addExtra(MessageButton);
                    } else {
                        if (Messages.equals("Defuse")) {
                            TextComponent MessageButton = new TextComponent(ChatColor(i18n("Tpa.DefuseMessage")));
                            MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa defuse " + SendPlayerName));
                            MessageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor("&c拒绝" + SendPlayerName + "的传送请求"))));
                            Message.addExtra(MessageButton);
                        } else {
                            Message.addExtra(new TextComponent(ChatColor(Messages.replaceAll("%1", SendPlayerName))));
                        }
                    }
                }
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).spigot().sendMessage(Message);
            }
            if (subchannel.equals("SendTpaHere")) {
                String PlayerName = in.readUTF();
                String SendPlayerName = in.readUTF();

                IntHasMap.getHasMap().put(SendPlayerName + "_TPAHereTime", MHDFTools.instance.getConfig().getInt("Tpa.OutTime"));
                StringHasMap.getHasMap().put(SendPlayerName + "_TPAHerePlayerName", PlayerName);

                TextComponent Message = new TextComponent();
                for (String Messages : i18n("TpaHere.Message").split("\\?")) {
                    if (Messages.equals("Accent")) {
                        TextComponent MessageButton = new TextComponent(ChatColor(i18n("TpaHere.AccentMessage")));
                        MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpahere accept " + SendPlayerName));
                        MessageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor("&a接受" + SendPlayerName + "的传送请求"))));
                        Message.addExtra(MessageButton);
                    } else {
                        if (Messages.equals("Defuse")) {
                            TextComponent MessageButton = new TextComponent(ChatColor(i18n("TpaHere.DefuseMessage")));
                            MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpahere defuse " + SendPlayerName));
                            MessageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor("&c拒绝" + SendPlayerName + "的传送请求"))));
                            Message.addExtra(MessageButton);
                        } else {
                            Message.addExtra(new TextComponent(ChatColor(Messages.replaceAll("%1", SendPlayerName))));
                        }
                    }
                }
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).spigot().sendMessage(Message);
            }
            if (subchannel.equals("SendMessage")) {
                String PlayerName = in.readUTF();
                String Message = in.readUTF();
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(Message);
            }
            if (subchannel.equals("TpPlayer")) {
                String PlayerName = in.readUTF();
                String TargetPlayerName = in.readUTF();
                Bukkit.getScheduler().runTaskLater(MHDFTools.instance, () -> {
                    Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Objects.requireNonNull(Bukkit.getPlayer(TargetPlayerName)).getLocation());
                    PlaySound(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), sound("TeleportSound"));
                }, 20);
            }
            if (subchannel.equals("TpPlayerHome")) {
                String PlayerName = in.readUTF();
                String HomeName = in.readUTF();
                Bukkit.getScheduler().runTaskLater(MHDFTools.instance, () -> {
                    Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Objects.requireNonNull(getHomeLocation(PlayerName, HomeName)));
                    PlaySound(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), sound("TeleportSound"));
                }, 20);
            }
            if (subchannel.equals("TpPlayerTo")) {
                String PlayerName = in.readUTF();
                Location Location = new Location(Bukkit.getWorld(in.readUTF()), in.readDouble(), in.readDouble(), in.readDouble(), (float) in.readDouble(), (float) in.readDouble());
                Bukkit.getScheduler().runTaskLater(MHDFTools.instance, () -> {
                    Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Location);
                    PlaySound(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), sound("TeleportSound"));
                }, 20);
            }
            if (subchannel.equals("CancelTpa")) {
                String PlayerName = in.readUTF();
                IntHasMap.getHasMap().remove(PlayerName + "_TPATime");
                StringHasMap.getHasMap().remove(PlayerName + "_TPAPlayerName");
            }
            if (subchannel.equals("CancelTpaHere")) {
                String PlayerName = in.readUTF();
                IntHasMap.getHasMap().remove(PlayerName + "_TPAHereTime");
                StringHasMap.getHasMap().remove(PlayerName + "_TPAHerePlayerName");
            }
            if (subchannel.equals("SaveLocation")) {
                String Key = in.readUTF();
                String Server = in.readUTF();
                Location Location = new Location(Bukkit.getWorld(in.readUTF()), in.readDouble(), in.readDouble(), in.readDouble());
                LocationHasMap.getHasMap().put(Key, Location);
                StringHasMap.getHasMap().put(Key + "_Server", Server);
            }
            if (subchannel.equals("ServerName")) {
                ServerName = in.readUTF();
            }
            if (subchannel.equals("SetSpawn")) {
                MHDFTools.instance.getConfig().set("SpawnSettings.Server", in.readUTF());
                MHDFTools.instance.getConfig().set("SpawnSettings.World", in.readUTF());
                MHDFTools.instance.getConfig().set("SpawnSettings.X", in.readDouble());
                MHDFTools.instance.getConfig().set("SpawnSettings.Y", in.readDouble());
                MHDFTools.instance.getConfig().set("SpawnSettings.Z", in.readDouble());
                MHDFTools.instance.getConfig().set("SpawnSettings.Yaw", in.readDouble());
                MHDFTools.instance.getConfig().set("SpawnSettings.Pitch", in.readDouble());
                MHDFTools.instance.saveConfig();
                MHDFTools.instance.reloadConfig();
            }
        } catch (IOException ignored) {
        }
    }
}
