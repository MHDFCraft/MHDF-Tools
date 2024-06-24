package cn.ChengZhiYa.MHDFTools.listeners.server;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.PlayerList;
import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.ServerName;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.*;
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
                String PlayerName = in.readUTF();
                String SendPlayerName = in.readUTF();

                MapUtil.getIntHashMap().put(SendPlayerName + "_TPATime", plugin.getConfig().getInt("TpaSettings.OutTime"));
                MapUtil.getStringHashMap().put(SendPlayerName + "_TPAPlayerName", PlayerName);

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
            if (subchannel.equals("SendTpaHere")) {
                String PlayerName = in.readUTF();
                String SendPlayerName = in.readUTF();

                MapUtil.getIntHashMap().put(SendPlayerName + "_TPAHereTime", plugin.getConfig().getInt("TpaHereSettings.OutTime"));
                MapUtil.getStringHashMap().put(SendPlayerName + "_TPAHerePlayerName", PlayerName);

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
            if (subchannel.equals("SendMessage")) {
                String PlayerName = in.readUTF();
                String Message = in.readUTF();
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(Message);
            }
            if (subchannel.equals("TpPlayer")) {
                String PlayerName = in.readUTF();
                String TargetPlayerName = in.readUTF();
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Objects.requireNonNull(Bukkit.getPlayer(TargetPlayerName)).getLocation());
                    playSound(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), sound("TeleportSound"));
                }, 20);
            }
            if (subchannel.equals("TpPlayerHome")) {
                String PlayerName = in.readUTF();
                String HomeName = in.readUTF();
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Objects.requireNonNull(getHomeLocation(PlayerName, HomeName)));
                    playSound(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), sound("TeleportSound"));
                }, 20);
            }
            if (subchannel.equals("TpPlayerTo")) {
                String PlayerName = in.readUTF();
                Location Location = new Location(Bukkit.getWorld(in.readUTF()), in.readDouble(), in.readDouble(), in.readDouble(), (float) in.readDouble(), (float) in.readDouble());
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(Location);
                    playSound(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), sound("TeleportSound"));
                }, 20);
            }
            if (subchannel.equals("CancelTpa")) {
                String PlayerName = in.readUTF();
                MapUtil.getIntHashMap().remove(PlayerName + "_TPATime");
                MapUtil.getStringHashMap().remove(PlayerName + "_TPAPlayerName");
            }
            if (subchannel.equals("CancelTpaHere")) {
                String PlayerName = in.readUTF();
                MapUtil.getIntHashMap().remove(PlayerName + "_TPAHereTime");
                MapUtil.getStringHashMap().remove(PlayerName + "_TPAHerePlayerName");
            }
            if (subchannel.equals("SaveLocation")) {
                String Key = in.readUTF();
                String Server = in.readUTF();
                Location Location = new Location(Bukkit.getWorld(in.readUTF()), in.readDouble(), in.readDouble(), in.readDouble());
                MapUtil.getLocationHashMap().put(Key, Location);
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
