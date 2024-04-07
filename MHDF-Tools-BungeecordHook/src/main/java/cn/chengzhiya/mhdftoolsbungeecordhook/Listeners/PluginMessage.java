package cn.chengzhiya.mhdftoolsbungeecordhook.Listeners;

import cn.chengzhiya.mhdftoolsbungeecordhook.main;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public final class PluginMessage implements Listener {
    public ServerInfo getServerInfo(PluginMessageEvent event) {
        ServerInfo server = null;
        for (Map.Entry one : main.main.getProxy().getServers().entrySet()) {
            if (!(((ServerInfo) one.getValue()).getAddress().getAddress().getHostAddress() + ":" + ((ServerInfo) one.getValue()).getAddress().getPort())
                    .equalsIgnoreCase(
                            event.getSender().getAddress().getAddress().getHostAddress() + ":" + event.getSender().getAddress().getPort()
                    )) {
                continue;
            }
            server = (ServerInfo) one.getValue();
        }
        if (server == null || server.getPlayers().isEmpty()) {
            return null;
        }
        return server;
    }

    @EventHandler
    public void onEvent(PluginMessageEvent event) {
        if (!event.getTag().contains("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String subchannel = in.readUTF();

            if (subchannel.equals("ServerName")) {
                ServerInfo Server = getServerInfo(event);

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("ServerName");
                out.writeUTF(Objects.requireNonNull(Server).getName());

                Server.sendData("BungeeCord", out.toByteArray());
            }
            if (subchannel.equals("SendTpa")) {
                String PlayerName = in.readUTF();
                String SendPlayerName = in.readUTF();
                ServerInfo Server = main.main.getProxy().getPlayer(PlayerName).getServer().getInfo();

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("SendTpa");
                out.writeUTF(PlayerName);
                out.writeUTF(SendPlayerName);

                Server.sendData("BungeeCord", out.toByteArray());

            }
            if (subchannel.equals("SendTpaHere")) {
                String PlayerName = in.readUTF();
                String SendPlayerName = in.readUTF();
                ServerInfo Server = main.main.getProxy().getPlayer(PlayerName).getServer().getInfo();

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("SendTpaHere");
                out.writeUTF(PlayerName);
                out.writeUTF(SendPlayerName);

                Server.sendData("BungeeCord", out.toByteArray());

            }
            if (subchannel.equals("SendMessage")) {
                String PlayerName = in.readUTF();
                String Message = in.readUTF();
                ServerInfo Server = main.main.getProxy().getPlayer(PlayerName).getServer().getInfo();

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("SendMessage");
                out.writeUTF(PlayerName);
                out.writeUTF(Message);

                Server.sendData("BungeeCord", out.toByteArray());
            }
            if (subchannel.equals("TpPlayer")) {
                String PlayerName = in.readUTF();
                String TargetPlayerName = in.readUTF();
                ServerInfo Server = main.main.getProxy().getPlayer(TargetPlayerName).getServer().getInfo();
                main.main.getProxy().getPlayer(PlayerName).connect(Server);

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("TpPlayer");
                out.writeUTF(PlayerName);
                out.writeUTF(TargetPlayerName);

                Server.sendData("BungeeCord", out.toByteArray());
            }
            if (subchannel.equals("TpPlayerHome")) {
                String PlayerName = in.readUTF();
                ServerInfo Server = main.main.getProxy().getServerInfo(in.readUTF());
                main.main.getProxy().getPlayer(PlayerName).connect(Server);

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("TpPlayerHome");
                out.writeUTF(PlayerName);
                out.writeUTF(in.readUTF());
                Server.sendData("BungeeCord", out.toByteArray());
            }
            if (subchannel.equals("TpPlayerTo")) {
                String PlayerName = in.readUTF();
                ServerInfo Server = main.main.getProxy().getServerInfo(in.readUTF());
                main.main.getProxy().getPlayer(PlayerName).connect(Server);

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("TpPlayerTo");
                out.writeUTF(PlayerName);
                out.writeUTF(in.readUTF());
                out.writeDouble(in.readDouble());
                out.writeDouble(in.readDouble());
                out.writeDouble(in.readDouble());
                out.writeDouble(in.readDouble());
                out.writeDouble(in.readDouble());
                Server.sendData("BungeeCord", out.toByteArray());
            }
            if (subchannel.equals("SaveLocation")) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("SaveLocation");
                out.writeUTF(in.readUTF());
                out.writeUTF(in.readUTF());
                out.writeUTF(in.readUTF());
                out.writeDouble(in.readDouble());
                out.writeDouble(in.readDouble());
                out.writeDouble(in.readDouble());
                for (Map.Entry one : main.main.getProxy().getServers().entrySet()) {
                    ServerInfo Server = (ServerInfo) one.getValue();
                    Server.sendData("BungeeCord", out.toByteArray());
                }
            }
            if (subchannel.equals("SetSpawn")) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("SetSpawn");
                out.writeUTF(in.readUTF());
                out.writeUTF(in.readUTF());
                out.writeDouble(in.readDouble());
                out.writeDouble(in.readDouble());
                out.writeDouble(in.readDouble());
                out.writeDouble(in.readDouble());
                out.writeDouble(in.readDouble());
                for (Map.Entry one : main.main.getProxy().getServers().entrySet()) {
                    ServerInfo Server = (ServerInfo) one.getValue();
                    Server.sendData("BungeeCord", out.toByteArray());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
