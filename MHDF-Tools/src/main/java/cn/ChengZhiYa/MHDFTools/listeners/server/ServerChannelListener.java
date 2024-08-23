package cn.ChengZhiYa.MHDFTools.listeners.server;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import cn.ChengZhiYa.MHDFTools.entity.TpaData;
import cn.ChengZhiYa.MHDFTools.utils.command.TpaHereUtil;
import cn.ChengZhiYa.MHDFTools.utils.command.TpaUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.PlayerList;
import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.ServerName;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.playSound;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.sound;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.getHomeLocation;

@SuppressWarnings("ALL")
public final class ServerChannelListener implements PluginMessageListener {
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

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
                String playerName = in.readUTF();
                String targetPlayerName = in.readUTF();

                TpaUtil.getTpaHashMap().put(playerName, new TpaData(targetPlayerName, PluginLoader.INSTANCE.getPlugin().getConfig().getInt("TpaSettings.OutTime")));
                Objects.requireNonNull(Bukkit.getPlayer(targetPlayerName)).spigot().sendMessage(TpaUtil.getTpaRequestMessage(playerName));
            }
            if (subchannel.equals("SendTpaHere")) {
                String playerName = in.readUTF();
                String targetPlayerName = in.readUTF();

                TpaHereUtil.getTpahereHashMap().put(playerName, new TpaData(targetPlayerName, PluginLoader.INSTANCE.getPlugin().getConfig().getInt("TpaHereSettings.OutTime")));
                Objects.requireNonNull(Bukkit.getPlayer(targetPlayerName)).spigot().sendMessage(TpaHereUtil.getTpaHereRequestMessage(playerName));
            }
            if (subchannel.equals("SendMessage")) {
                String PlayerName = in.readUTF();
                String Message = in.readUTF();
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(Message);
            }
            if (subchannel.equals("TpPlayer")) {
                String PlayerName = in.readUTF();
                String TargetPlayerName = in.readUTF();
                Bukkit.getAsyncScheduler().runAtFixedRate(PluginLoader.INSTANCE.getPlugin(), task -> {
                    int i = 0;
                    if (Bukkit.getPlayer(PlayerName) != null && Bukkit.getPlayer(TargetPlayerName) != null) {
                        Bukkit.getRegionScheduler().run(PluginLoader.INSTANCE.getPlugin(), Bukkit.getPlayer(TargetPlayerName).getLocation(), t -> {
                            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Bukkit.getPlayer(TargetPlayerName));
                            playSound(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), sound("TeleportSound"));
                        });
                        task.cancel();
                    } else {
                        i++;
                        if (i > 10) {
                            task.cancel();
                        }
                    }
                }, 0, 500, TimeUnit.MILLISECONDS);
            }
            if (subchannel.equals("TpPlayerHome")) {
                String PlayerName = in.readUTF();
                String HomeName = in.readUTF();
                Bukkit.getRegionScheduler().runDelayed(plugin, Bukkit.getPlayer(PlayerName).getLocation(), task -> {
                    Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(getHomeLocation(PlayerName, HomeName).getLocation());
                    playSound(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), sound("TeleportSound"));
                }, 20);
            }
            if (subchannel.equals("tpPlayerTo")) {
                String PlayerName = in.readUTF();
                Location Location = new Location(Bukkit.getWorld(in.readUTF()), in.readDouble(), in.readDouble(), in.readDouble(), (float) in.readDouble(), (float) in.readDouble());
                Bukkit.getRegionScheduler().runDelayed(plugin, Bukkit.getPlayer(PlayerName).getLocation(), task -> {
                    Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Location);
                    playSound(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), sound("TeleportSound"));
                }, 20);
            }
            if (subchannel.equals("CancelTpa")) {
                String playerName = in.readUTF();
                TpaUtil.getTpaHashMap().remove(playerName);
            }
            if (subchannel.equals("CancelTpaHere")) {
                String playerName = in.readUTF();
                TpaHereUtil.getTpahereHashMap().remove(playerName);
            }
            if (subchannel.equals("SaveLocation")) {
                String Key = in.readUTF();
                String Server = in.readUTF();
                MapUtil.getLocationHashMap().put(Key, new SuperLocation(in.readUTF(), in.readDouble(), in.readDouble(), in.readDouble()));
                MapUtil.getStringHashMap().put(Key + "_Server", Server);
            }
            if (subchannel.equals("ServerName")) {
                ServerName = in.readUTF();
            }
            if (subchannel.equals("SetSpawn")) {
                plugin.getConfig().set("SpawnSettings.Server", in.readUTF());
                plugin.getConfig().set("SpawnSettings.World", in.readUTF());
                plugin.getConfig().set("SpawnSettings.X", in.readDouble());
                plugin.getConfig().set("SpawnSettings.Y", in.readDouble());
                plugin.getConfig().set("SpawnSettings.Z", in.readDouble());
                plugin.getConfig().set("SpawnSettings.Yaw", in.readDouble());
                plugin.getConfig().set("SpawnSettings.Pitch", in.readDouble());
                plugin.saveConfig();
                plugin.reloadConfig();
            }
        } catch (IOException ignored) {
        }
    }
}
