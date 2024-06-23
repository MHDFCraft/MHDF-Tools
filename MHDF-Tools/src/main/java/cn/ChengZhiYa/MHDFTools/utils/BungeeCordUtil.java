package cn.ChengZhiYa.MHDFTools.utils;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
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

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.*;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.getHomeLocation;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.getHomeServer;


public final class BungeeCordUtil {
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
                    TextComponent MessageButton = new TextComponent(MessageUtil.colorMessage(i18n("Tpa.AccentMessage")));
                    MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accept " + SendPlayerName));
                    MessageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(MessageUtil.colorMessage("&a接受" + SendPlayerName + "的传送请求"))));
                    Message.addExtra(MessageButton);
                } else {
                    if (Messages.equals("Defuse")) {
                        TextComponent MessageButton = new TextComponent(MessageUtil.colorMessage(i18n("Tpa.DefuseMessage")));
                        MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa defuse " + SendPlayerName));
                        MessageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(MessageUtil.colorMessage("&c拒绝" + SendPlayerName + "的传送请求"))));
                        Message.addExtra(MessageButton);
                    } else {
                        Message.addExtra(new TextComponent(MessageUtil.colorMessage(Messages.replaceAll("%1", SendPlayerName))));
                    }
                }
            }
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).spigot().sendMessage(Message);
        }
        MapUtil.getIntHashMap().put(SendPlayerName + "_TPATime", MHDFTools.instance.getConfig().getInt("TpaSettings.OutTime"));
        MapUtil.getStringHashMap().put(SendPlayerName + "_TPAPlayerName", PlayerName);
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
                    TextComponent MessageButton = new TextComponent(MessageUtil.colorMessage(i18n("TpaHere.AccentMessage")));
                    MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpahere accept " + SendPlayerName));
                    MessageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(MessageUtil.colorMessage("&a接受" + SendPlayerName + "的传送请求"))));
                    Message.addExtra(MessageButton);
                } else {
                    if (Messages.equals("Defuse")) {
                        TextComponent MessageButton = new TextComponent(MessageUtil.colorMessage(i18n("TpaHere.DefuseMessage")));
                        MessageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpahere defuse " + SendPlayerName));
                        MessageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(MessageUtil.colorMessage("&c拒绝" + SendPlayerName + "的传送请求"))));
                        Message.addExtra(MessageButton);
                    } else {
                        Message.addExtra(new TextComponent(MessageUtil.colorMessage(Messages.replaceAll("%1", SendPlayerName))));
                    }
                }
            }
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).spigot().sendMessage(Message);
        }
        MapUtil.getStringHashMap().put(SendPlayerName + "_TPAHerePlayerName", PlayerName);
        MapUtil.getIntHashMap().put(SendPlayerName + "_TPAHereTime", MHDFTools.instance.getConfig().getInt("TpaHereSettings.OutTime"));
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

    public static void TpPlayer(String PlayerName, String TargetPlayerName) {
        if (Bukkit.getPlayer(PlayerName) == null && MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
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
            Bukkit.getScheduler().runTask(MHDFTools.instance, () -> {
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Objects.requireNonNull(Objects.requireNonNull(Bukkit.getPlayer(TargetPlayerName)).getLocation()));
                playSound(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), sound("TeleportSound"));
            });
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

            Bukkit.getScheduler().runTask(MHDFTools.instance, () -> {
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Objects.requireNonNull(getHomeLocation(PlayerName, HomeName)));
                playSound(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), sound("TeleportSound"));
            });
        }
    }

    public static void TpPlayerTo(String PlayerName, String Server2, Location Location2) {
        if (!ServerName.equals(Server2) && MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("TpPlayerTo");
            out.writeUTF(PlayerName);
            out.writeUTF(Server2);
            out.writeUTF(Location2.getWorld().getName());
            out.writeDouble(Location2.getX());
            out.writeDouble(Location2.getY());
            out.writeDouble(Location2.getZ());
            out.writeDouble(Location2.getYaw());
            out.writeDouble(Location2.getPitch());
            player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
        } else {
            Bukkit.getScheduler().runTask(MHDFTools.instance, () -> {
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Location2);
                SpigotUtil.playSound(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), SpigotUtil.sound("TeleportSound"));
            });
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
        MapUtil.getIntHashMap().remove(PlayerName + "_TPATime");
        MapUtil.getStringHashMap().remove(PlayerName + "_TPAPlayerName");
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
        MapUtil.getIntHashMap().remove(PlayerName + "_TPAHereTime");
        MapUtil.getStringHashMap().remove(PlayerName + "_TPAHerePlayerName");
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
            out.writeDouble(Location.getYaw());
            out.writeDouble(Location.getPitch());

            player.sendPluginMessage(MHDFTools.instance, "BungeeCord", out.toByteArray());
        } else {
            MHDFTools.instance.getConfig().set("SpawnSettings.Server", ServerName);
            MHDFTools.instance.getConfig().set("SpawnSettings.World", Location.getWorld().getName());
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
            return Bukkit.getPlayer(PlayerName) != null;
        }
    }
}
