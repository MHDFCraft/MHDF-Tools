package cn.ChengZhiYa.MHDFTools.utils;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import cn.ChengZhiYa.MHDFTools.entity.TpaData;
import cn.ChengZhiYa.MHDFTools.utils.command.TpaHereUtil;
import cn.ChengZhiYa.MHDFTools.utils.command.TpaUtil;
import cn.ChengZhiYa.MHDFTools.utils.message.ColorLogs;
import com.github.Anon8281.universalScheduler.foliaScheduler.FoliaScheduler;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.crypto.spec.PSource;
import java.util.Arrays;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.playSound;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.sound;
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

        player.sendPluginMessage(PluginLoader.INSTANCE.getPlugin(), "BungeeCord", out.toByteArray());
    }

    public static void getServerName() {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ServerName");

            player.sendPluginMessage(PluginLoader.INSTANCE.getPlugin(), "BungeeCord", out.toByteArray());
        } else {
            ServerName = PluginLoader.INSTANCE.getPlugin().getConfig().getString("BungeecordSettings.HomeDefaultServer");
        }
    }

    public static void sendTpa(String playerName, String targetPlayerName) {
        if (Bukkit.getPlayer(playerName) != null) {
            Player player = Bukkit.getPlayer(playerName);

            if (Bukkit.getPlayer(targetPlayerName) == null && PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("SendTpa");
                out.writeUTF(playerName);
                out.writeUTF(targetPlayerName);

                Objects.requireNonNull(player).sendPluginMessage(PluginLoader.INSTANCE.getPlugin(), "BungeeCord", out.toByteArray());
            } else {
                Objects.requireNonNull(Bukkit.getPlayer(targetPlayerName)).spigot().sendMessage(TpaUtil.getTpaRequestMessage(playerName));
            }
            TpaUtil.getTpaHashMap().put(playerName, new TpaData(targetPlayerName, PluginLoader.INSTANCE.getPlugin().getConfig().getInt("TpaSettings.OutTime")));
        }
    }

    public static void sendTpaHere(String playerName, String targetPlayerName) {
        if (Bukkit.getPlayer(playerName) != null) {
            Player player = Bukkit.getPlayer(playerName);

            if (Bukkit.getPlayer(targetPlayerName) == null && PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("SendTpaHere");
                out.writeUTF(playerName);
                out.writeUTF(targetPlayerName);

                Objects.requireNonNull(player).sendPluginMessage(PluginLoader.INSTANCE.getPlugin(), "BungeeCord", out.toByteArray());
            } else {
                Objects.requireNonNull(Bukkit.getPlayer(targetPlayerName)).spigot().sendMessage(TpaHereUtil.getTpaHereRequestMessage(playerName));
            }
            TpaHereUtil.getTpahereHashMap().put(playerName, new TpaData(targetPlayerName, PluginLoader.INSTANCE.getPlugin().getConfig().getInt("TpaHereSettings.OutTime")));
        }
    }

    public static void sendMessage(String playerName, String Message) {
        if (Bukkit.getPlayer(playerName) == null && PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("SendMessage");
            out.writeUTF(playerName);
            out.writeUTF(Message);

            player.sendPluginMessage(PluginLoader.INSTANCE.getPlugin(), "BungeeCord", out.toByteArray());
        } else {
            Objects.requireNonNull(Bukkit.getPlayer(playerName)).sendMessage(Message);
        }
    }

    public static void tpPlayer(String playerName, String targetPlayerName) {
        if ((Bukkit.getPlayer(playerName) == null || Bukkit.getPlayer(targetPlayerName) == null) && PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("TpPlayer");
            out.writeUTF(playerName);
            out.writeUTF(targetPlayerName);

            Objects.requireNonNull(player).sendPluginMessage(PluginLoader.INSTANCE.getPlugin(), "BungeeCord", out.toByteArray());
        } else {
            new FoliaScheduler(PluginLoader.INSTANCE.getPlugin()).runTask(Objects.requireNonNull(Bukkit.getPlayer(targetPlayerName)).getLocation(), () -> {
                Objects.requireNonNull(Bukkit.getPlayer(playerName)).teleport(Objects.requireNonNull(Bukkit.getPlayer(targetPlayerName)));
                playSound(Objects.requireNonNull(Bukkit.getPlayer(playerName)), sound("TeleportSound"));
            });
        }
    }

    public static void tpPlayerHome(String playerName, String homeName) {
        if (!getHomeServer(playerName, homeName).equals(ServerName) && PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("TpPlayerHome");
            out.writeUTF(playerName);
            out.writeUTF(getHomeServer(playerName, homeName));
            out.writeUTF(homeName);

            player.sendPluginMessage(PluginLoader.INSTANCE.getPlugin(), "BungeeCord", out.toByteArray());
        } else {
            new FoliaScheduler(PluginLoader.INSTANCE.getPlugin()).runTask(getHomeLocation(playerName, homeName).getLocation(), () -> {
                Objects.requireNonNull(Bukkit.getPlayer(playerName)).teleport(Objects.requireNonNull(getHomeLocation(playerName, homeName)).getLocation());
                playSound(Objects.requireNonNull(Bukkit.getPlayer(playerName)), sound("TeleportSound"));
            });
        }
    }

    public static void tpPlayerTo(String playerName, String serverName, SuperLocation location) {
        if (playerName == null || location == null) {
            return;
        }

        if (!ServerName.equals(serverName) && PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("TpPlayerTo");
            out.writeUTF(playerName);
            out.writeUTF(serverName);
            out.writeUTF(location.getWorldName());
            out.writeDouble(location.getX());
            out.writeDouble(location.getY());
            out.writeDouble(location.getZ());
            out.writeDouble(location.getYaw());
            out.writeDouble(location.getPitch());

            player.sendPluginMessage(PluginLoader.INSTANCE.getPlugin(), "BungeeCord", out.toByteArray());
        } else {
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                throw new IllegalStateException("Player " + playerName + " is not online");
            }

            try {
                if (PluginLoader.INSTANCE.getServerManager().is1_20orAbove()) {
                    PaperLib.teleportAsync(player, location.getLocation());
                } else {
                    player.teleport(location.getLocation());
                }
                SpigotUtil.playSound(player, SpigotUtil.sound("TeleportSound"));
            } catch (Exception e) {
                ColorLogs.debug("Failed to teleport to " + playerName);
            }
        }
    }

    public static void cancelTpa(String playerName) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("CancelTpa");
            out.writeUTF(playerName);

            player.sendPluginMessage(PluginLoader.INSTANCE.getPlugin(), "BungeeCord", out.toByteArray());
        }
        TpaUtil.getTpaHashMap().remove(playerName);
    }

    public static void cancelTpaHere(String playerName) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("CancelTpaHere");
            out.writeUTF(playerName);

            player.sendPluginMessage(PluginLoader.INSTANCE.getPlugin(), "BungeeCord", out.toByteArray());
        }
        TpaHereUtil.getTpahereHashMap().remove(playerName);
    }

    public static void setSpawn(SuperLocation location) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("SetSpawn");
            out.writeUTF(ServerName);
            out.writeUTF(location.getWorldName());
            out.writeDouble(location.getX());
            out.writeDouble(location.getY());
            out.writeDouble(location.getZ());
            out.writeDouble(location.getYaw());
            out.writeDouble(location.getPitch());

            player.sendPluginMessage(PluginLoader.INSTANCE.getPlugin(), "BungeeCord", out.toByteArray());
        } else {
            PluginLoader.INSTANCE.getPlugin().getConfig().set("SpawnSettings.Server", ServerName);
            PluginLoader.INSTANCE.getPlugin().getConfig().set("SpawnSettings.World", location.getWorldName());
            PluginLoader.INSTANCE.getPlugin().getConfig().set("SpawnSettings.X", location.getX());
            PluginLoader.INSTANCE.getPlugin().getConfig().set("SpawnSettings.Y", location.getY());
            PluginLoader.INSTANCE.getPlugin().getConfig().set("SpawnSettings.Z", location.getZ());
            PluginLoader.INSTANCE.getPlugin().getConfig().set("SpawnSettings.Yaw", location.getYaw());
            PluginLoader.INSTANCE.getPlugin().getConfig().set("SpawnSettings.Pitch", location.getPitch());
            PluginLoader.INSTANCE.getPlugin().saveConfig();
            PluginLoader.INSTANCE.getPlugin().reloadConfig();
        }
    }

    public static boolean ifPlayerOnline(String playerName) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            getPlayerList();
            return Arrays.asList(PlayerList).contains(playerName);
        } else {
            return Bukkit.getPlayer(playerName) != null;
        }
    }
}
