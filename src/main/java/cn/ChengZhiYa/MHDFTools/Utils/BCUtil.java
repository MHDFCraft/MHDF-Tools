package cn.ChengZhiYa.MHDFTools.Utils;

import cn.ChengZhiYa.MHDFTools.HashMap.IntHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.LocationHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.StringHasMap;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.Utils.Database.HomeUtil.getHomeLocation;
import static cn.ChengZhiYa.MHDFTools.Utils.Database.HomeUtil.getHomeServer;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.ChatColor;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;


public final class BCUtil {
    public static String[] PlayerList;
    public static String ServerName = "无";

    public static void getPlayerList() {
        final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player == null) {
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF("ALL");

        player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
    }

    public static void getServerName() {
        if (MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ServerName");

            player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
        } else {
            ServerName = MHDFTools.instance.getConfig().getString("BungeecordSettings.HomeDefaultServer");
        }
    }

    public static String getHomeServerName() {
        if (MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            getServerName();
        }
        return ServerName;
    }

    public static void SendTpa(String PlayerName, String SendPlayerName) {
        if (Bukkit.getPlayer(PlayerName) == null && MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("SendTpa");
            out.writeUTF(PlayerName);
            out.writeUTF(SendPlayerName);

            player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
        } else {
            TextComponent Message = new TextComponent();
            for (String Messages : i18n("Tpa.Message").split("\\?")) {
                if (Messages.equals("Accent")) {
                    TextComponent MessageButton = new TextComponent(ChatColor(i18n("Tpa.AccentMessage")));
                    MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accent " + SendPlayerName));
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
        IntHasMap.getHasMap().put(SendPlayerName + "_TPATime", MHDFTools.instance.getConfig().getInt("Tpa.OutTime"));
        StringHasMap.getHasMap().put(SendPlayerName + "_TPAPlayerName", PlayerName);
    }

    public static void SendTpaHere(String PlayerName, String SendPlayerName) {
        if (Bukkit.getPlayer(PlayerName) == null && MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("SendTpaHere");
            out.writeUTF(PlayerName);
            out.writeUTF(SendPlayerName);

            player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
        } else {
            TextComponent Message = new TextComponent();
            for (String Messages : i18n("TpaHere.Message").split("\\?")) {
                if (Messages.equals("Accent")) {
                    TextComponent MessageButton = new TextComponent(ChatColor(i18n("TpaHere.AccentMessage")));
                    MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpahere accent " + SendPlayerName));
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
        IntHasMap.getHasMap().put(SendPlayerName + "_TPAHereTime", MHDFTools.instance.getConfig().getInt("Tpa.OutTime"));
        StringHasMap.getHasMap().put(SendPlayerName + "_TPAHerePlayerName", PlayerName);
    }

    public static void SendMessage(String PlayerName, String Message) {
        if (Bukkit.getPlayer(PlayerName) == null && MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("SendMessage");
            out.writeUTF(PlayerName);
            out.writeUTF(Message);

            player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
        } else {
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(Message);
        }
    }

    public static void TpPlayer(String PlayerName, String TargetPlayerName, boolean TpaHere) {
        if (TpaHere) {
            if (Bukkit.getPlayer(TargetPlayerName) == null && MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
                final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
                if (player == null) {
                    return;
                }

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("TpPlayer");
                out.writeUTF(PlayerName);
                out.writeUTF(TargetPlayerName);

                player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
            } else {
                Objects.requireNonNull(Bukkit.getPlayer(TargetPlayerName)).teleport(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).getLocation());
            }
        } else if (Bukkit.getPlayer(PlayerName) == null && MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("TpPlayer");
            out.writeUTF(PlayerName);
            out.writeUTF(TargetPlayerName);

            player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
        } else {
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Objects.requireNonNull(Bukkit.getPlayer(TargetPlayerName)).getLocation());
        }
    }

    public static void TpPlayerHome(String PlayerName, String HomeName) {
        if (!getHomeServer(PlayerName, HomeName).equals(ServerName) && MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("TpPlayerHome");
            out.writeUTF(PlayerName);
            out.writeUTF(getHomeServer(PlayerName, HomeName));
            out.writeUTF(HomeName);

            player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
        } else {
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Objects.requireNonNull(getHomeLocation(PlayerName, HomeName)));
        }
    }

    public static void TpPlayerTo(String PlayerName, String Server, Location Location) {
        if (!ServerName.equals(Server) && MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("TpPlayerTo");
            out.writeUTF(PlayerName);
            out.writeUTF(Server);
            out.writeUTF(Location.getWorld().getName());
            out.writeDouble(Location.getX());
            out.writeDouble(Location.getY());
            out.writeDouble(Location.getZ());

            player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
        } else {
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Location);
        }
    }

    public static void CancelTpa(String PlayerName) {
        if (MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("CancelTpa");
            out.writeUTF(PlayerName);

            player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
        }
        IntHasMap.getHasMap().remove(PlayerName + "_TPATime");
        StringHasMap.getHasMap().remove(PlayerName + "_TPAPlayerName");
    }

    public static void CancelTpaHere(String PlayerName) {
        if (MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("CancelTpaHere");
            out.writeUTF(PlayerName);

            player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
        }
        IntHasMap.getHasMap().remove(PlayerName + "_TPAHereTime");
        StringHasMap.getHasMap().remove(PlayerName + "_TPAHerePlayerName");
    }

    public static void SaveLocation(String Key, String Server, Location Location) {
        if (MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("SaveLocation");
            out.writeUTF(Key);
            out.writeUTF(Server);
            out.writeUTF(Location.getWorld().getName());
            out.writeDouble(Location.getX());
            out.writeDouble(Location.getY());
            out.writeDouble(Location.getZ());

            player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
        }else {
            LocationHasMap.getHasMap().put(Key, Location);
            StringHasMap.getHasMap().put(Key + "_Server", Server);
        }
    }

    public static void SetSpawn(Location Location) {
        if (MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("SetSpawn");
            out.writeUTF(ServerName);
            out.writeUTF(Location.getWorld().getName());
            out.writeDouble(Location.getX());
            out.writeDouble(Location.getY());
            out.writeDouble(Location.getZ());
            out.writeFloat(Location.getYaw());
            out.writeFloat(Location.getPitch());

            player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
        }else {
            MHDFTools.instance.getConfig().set("SpawnSettings.Server", ServerName);
            MHDFTools.instance.getConfig().set("SpawnSettings.world", Location.getWorld().getName());
            MHDFTools.instance.getConfig().set("SpawnSettings.X", Location.getX());
            MHDFTools.instance.getConfig().set("SpawnSettings.Y", Location.getY());
            MHDFTools.instance.getConfig().set("SpawnSettings.Z", Location.getZ());
            MHDFTools.instance.getConfig().set("SpawnSettings.Yaw", Location.getYaw());
            MHDFTools.instance.getConfig().set("SpawnSettings.Pitch", Location.getPitch());
            MHDFTools.instance.saveConfig();
            MHDFTools.instance.reloadConfig();
        }
    }

    public static boolean ifPlayerOnline(String PlayerName) {
        if (MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            getPlayerList();
            return Arrays.asList(PlayerList).contains(PlayerName);
        } else {
            return Bukkit.getPlayer(PlayerName) == null;
        }
    }
}
