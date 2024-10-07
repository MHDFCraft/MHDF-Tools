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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.playSound;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.sound;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.getHomeLocation;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.getHomeServer;

public final class BungeeCordUtil {

    public static String[] PlayerList;
    public static String ServerName = "无";
    private static final FileConfiguration config = PluginLoader.INSTANCE.getPlugin().getConfig();

    private static Optional<Player> getFirstPlayer() {
        return Optional.ofNullable(Iterables.getFirst(Bukkit.getOnlinePlayers(), null));
    }

    public static void getPlayerList() {
        getFirstPlayer().ifPresent(player -> sendPluginMessage(player, "PlayerList", "ALL"));
    }

    public static void getServerName() {
        if (config.getBoolean("BungeecordSettings.Enable")) {
            getFirstPlayer().ifPresent(player -> sendPluginMessage(player, "ServerName"));
        } else {
            ServerName = config.getString("BungeecordSettings.HomeDefaultServer");
        }
    }

    public static void sendTpa(String playerName, String targetPlayerName) {
        Optional<Player> playerOpt = Optional.ofNullable(Bukkit.getPlayer(playerName));
        playerOpt.ifPresent(player -> {
            if (Bukkit.getPlayer(targetPlayerName) == null && config.getBoolean("BungeecordSettings.Enable")) {
                sendPluginMessage(player, "SendTpa", playerName, targetPlayerName);
            } else {
                sendTpaMessage(targetPlayerName, playerName);
            }
            TpaUtil.getTpaHashMap().put(playerName, new TpaData(targetPlayerName, config.getInt("TpaSettings.OutTime")));
        });
    }

    public static void sendTpaHere(String playerName, String targetPlayerName) {
        Optional<Player> playerOpt = Optional.ofNullable(Bukkit.getPlayer(playerName));
        playerOpt.ifPresent(player -> {
            if (Bukkit.getPlayer(targetPlayerName) == null && config.getBoolean("BungeecordSettings.Enable")) {
                sendPluginMessage(player, "SendTpaHere", playerName, targetPlayerName);
            } else {
                sendTpaHereMessage(targetPlayerName, playerName);
            }
            TpaHereUtil.getTpahereHashMap().put(playerName, new TpaData(targetPlayerName, config.getInt("TpaHereSettings.OutTime")));
        });
    }

    public static void sendMessage(String playerName, String message) {
        Optional<Player> playerOpt = Optional.ofNullable(Bukkit.getPlayer(playerName));
        if (playerOpt.isPresent()) {
            playerOpt.get().sendMessage(message);
        } else if (config.getBoolean("BungeecordSettings.Enable")) {
            getFirstPlayer().ifPresent(player -> sendPluginMessage(player, "SendMessage", playerName, message));
        }
    }

    public static void tpPlayer(String playerName, String targetPlayerName) {
        Optional<Player> playerOpt = Optional.ofNullable(Bukkit.getPlayer(playerName));
        Optional<Player> targetPlayerOpt = Optional.ofNullable(Bukkit.getPlayer(targetPlayerName));

        if ((playerOpt.isEmpty() || targetPlayerOpt.isEmpty()) && config.getBoolean("BungeecordSettings.Enable")) {
            getFirstPlayer().ifPresent(player -> sendPluginMessage(player, "TpPlayer", playerName, targetPlayerName));
        } else {
            targetPlayerOpt.ifPresent(targetPlayer -> playerOpt.ifPresent(player -> {
                new FoliaScheduler(PluginLoader.INSTANCE.getPlugin()).runTask(targetPlayer.getLocation(), () -> {
                    player.teleport(targetPlayer);
                    playSound(player, sound("TeleportSound"));
                });
            }));
        }
    }

    public static void tpPlayerHome(String playerName, String homeName) {
        String homeServer = getHomeServer(playerName, homeName);
        if (!homeServer.equals(ServerName) && config.getBoolean("BungeecordSettings.Enable")) {
            getFirstPlayer().ifPresent(player -> sendPluginMessage(player, "TpPlayerHome", playerName, homeServer, homeName));
        } else {
            SuperLocation homeLocation = getHomeLocation(playerName, homeName);
            new FoliaScheduler(PluginLoader.INSTANCE.getPlugin()).runTask(homeLocation.getLocation(), () -> {
                Player player = Objects.requireNonNull(Bukkit.getPlayer(playerName));
                player.teleport(homeLocation.getLocation());
                playSound(player, sound("TeleportSound"));
            });
        }
    }

    public static void tpPlayerTo(String playerName, String serverName, SuperLocation location) {
        if (playerName == null || location == null) {
            return;
        }

        if (!ServerName.equals(serverName) && config.getBoolean("BungeecordSettings.Enable")) {
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
        if (config.getBoolean("BungeecordSettings.Enable")) {
            getFirstPlayer().ifPresent(player -> sendPluginMessage(player, "CancelTpa", playerName));
        }
        TpaUtil.getTpaHashMap().remove(playerName);
    }

    public static void cancelTpaHere(String playerName) {
        if (config.getBoolean("BungeecordSettings.Enable")) {
            getFirstPlayer().ifPresent(player -> sendPluginMessage(player, "CancelTpaHere", playerName));
        }
        TpaHereUtil.getTpahereHashMap().remove(playerName);
    }

    public static void setSpawn(SuperLocation location) {
        if (config.getBoolean("BungeecordSettings.Enable")) {
            getFirstPlayer().ifPresent(player -> {
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
            });
        } else {
            config.set("SpawnSettings.Server", ServerName);
            config.set("SpawnSettings.World", location.getWorldName());
            config.set("SpawnSettings.X", location.getX());
            config.set("SpawnSettings.Y", location.getY());
            config.set("SpawnSettings.Z", location.getZ());
            config.set("SpawnSettings.Yaw", location.getYaw());
            config.set("SpawnSettings.Pitch", location.getPitch());
            PluginLoader.INSTANCE.getPlugin().saveConfig();
            PluginLoader.INSTANCE.getPlugin().reloadConfig();
        }
    }

    public static boolean ifPlayerOnline(String playerName) {
        if (config.getBoolean("BungeecordSettings.Enable")) {
            getPlayerList();
            return Arrays.asList(PlayerList).contains(playerName);
        } else {
            return Bukkit.getPlayer(playerName) != null;
        }
    }

    private static void sendPluginMessage(Player player, String... messages) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String message : messages) {
            out.writeUTF(message);
        }
        player.sendPluginMessage(PluginLoader.INSTANCE.getPlugin(), "BungeeCord", out.toByteArray());
    }

    private static void sendTpaMessage(String targetPlayerName, String playerName) {
        Optional.ofNullable(Bukkit.getPlayer(targetPlayerName))
                .ifPresent(targetPlayer -> targetPlayer.spigot().sendMessage(TpaUtil.getTpaRequestMessage(playerName)));
    }

    private static void sendTpaHereMessage(String targetPlayerName, String playerName) {
        Optional.ofNullable(Bukkit.getPlayer(targetPlayerName))
                .ifPresent(targetPlayer -> targetPlayer.spigot().sendMessage(TpaHereUtil.getTpaHereRequestMessage(playerName)));
    }
}
