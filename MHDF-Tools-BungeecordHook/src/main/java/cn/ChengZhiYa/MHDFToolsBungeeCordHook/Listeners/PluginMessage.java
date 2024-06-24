package cn.ChengZhiYa.MHDFToolsBungeeCordHook.Listeners;

import cn.ChengZhiYa.MHDFToolsBungeeCordHook.Main;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

public final class PluginMessage implements Listener {

    public ServerInfo getServerInfo(PluginMessageEvent event) {
        InetAddress senderAddress = event.getSender().getAddress().getAddress();
        int senderPort = event.getSender().getAddress().getPort();

        for (Map.Entry<String, ServerInfo> entry
                : Main.main.getProxy().getServers().entrySet()) {

            ServerInfo server = entry.getValue();
            InetAddress serverAddress = server.getAddress().getAddress();

            int serverPort = server.getAddress().getPort();

            if (serverAddress.equals(senderAddress) && serverPort == senderPort) {
                if (!server.getPlayers().isEmpty()) {
                    return server;
                } else if (server.getPlayers().isEmpty()) { //也许是无意义的
                    return null;
                }
            }
        }
        return null;
    }

    @EventHandler
    public void onEvent(PluginMessageEvent event) {
        if (!event.getTag().contains("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String subchannel = in.readUTF();
            System.out.println(subchannel);

            switch (subchannel) {
                case "ServerName":
                    handleServerName(event);
                    break;
                case "SendTpa":
                    handleSendTpa(in, event);
                    break;
                case "SendTpaHere":
                    handleSendTpaHere(in, event);
                    break;
                case "SendMessage":
                    handleSendMessage(in, event);
                    break;
                case "TpPlayer":
                    handleTpPlayer(in, event);
                    break;
                case "TpPlayerHome":
                    handleTpPlayerHome(in, event);
                    break;
                case "TpPlayerTo":
                    handleTpPlayerTo(in, event);
                    break;
                case "SaveLocation":
                    handleSaveLocation(in, event);
                    break;
                case "SetSpawn":
                    handleSetSpawn(in, event);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleServerName(PluginMessageEvent event) {
        ServerInfo server = getServerInfo(event);
        if (server != null) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ServerName");
            out.writeUTF(server.getName());
            server.sendData("BungeeCord", out.toByteArray());
        }
    }

    private void handleSendTpa(DataInputStream in, PluginMessageEvent event) throws IOException {
        String playerName = in.readUTF();
        String sendPlayerName = in.readUTF();
        ServerInfo server = Main.main.getProxy().getPlayer(playerName).getServer().getInfo();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("SendTpa");
        out.writeUTF(playerName);
        out.writeUTF(sendPlayerName);

        server.sendData("BungeeCord", out.toByteArray());
    }

    private void handleSendTpaHere(DataInputStream in, PluginMessageEvent event) throws IOException {
        String playerName = in.readUTF();
        String sendPlayerName = in.readUTF();
        ServerInfo server = Main.main.getProxy().getPlayer(playerName).getServer().getInfo();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("SendTpaHere");
        out.writeUTF(playerName);
        out.writeUTF(sendPlayerName);

        server.sendData("BungeeCord", out.toByteArray());
        System.out.println(1);
    }

    private void handleSendMessage(DataInputStream in, PluginMessageEvent event) throws IOException {
        String playerName = in.readUTF();
        String message = in.readUTF();
        ServerInfo server = Main.main.getProxy().getPlayer(playerName).getServer().getInfo();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("SendMessage");
        out.writeUTF(playerName);
        out.writeUTF(message);

        server.sendData("BungeeCord", out.toByteArray());
    }

    private void handleTpPlayer(DataInputStream in, PluginMessageEvent event) throws IOException {
        String playerName = in.readUTF();
        String targetPlayerName = in.readUTF();
        ServerInfo server = Main.main.getProxy().getPlayer(targetPlayerName).getServer().getInfo();
        Main.main.getProxy().getPlayer(playerName).connect(server);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("TpPlayer");
        out.writeUTF(playerName);
        out.writeUTF(targetPlayerName);

        server.sendData("BungeeCord", out.toByteArray());
    }

    private void handleTpPlayerHome(DataInputStream in, PluginMessageEvent event) throws IOException {
        String playerName = in.readUTF();
        ServerInfo server = Main.main.getProxy().getServerInfo(in.readUTF());
        Main.main.getProxy().getPlayer(playerName).connect(server);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("TpPlayerHome");
        out.writeUTF(playerName);
        out.writeUTF(in.readUTF());

        server.sendData("BungeeCord", out.toByteArray());
    }

    private void handleTpPlayerTo(DataInputStream in, PluginMessageEvent event) throws IOException {
        String playerName = in.readUTF();
        ServerInfo server = Main.main.getProxy().getServerInfo(in.readUTF());
        Main.main.getProxy().getPlayer(playerName).connect(server);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("TpPlayerTo");
        out.writeUTF(playerName);
        out.writeUTF(in.readUTF());
        out.writeDouble(in.readDouble());
        out.writeDouble(in.readDouble());
        out.writeDouble(in.readDouble());
        out.writeDouble(in.readDouble());
        out.writeDouble(in.readDouble());

        server.sendData("BungeeCord", out.toByteArray());
    }

    private void handleSaveLocation(DataInputStream in, PluginMessageEvent event) throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("SaveLocation");
        out.writeUTF(in.readUTF());
        out.writeUTF(in.readUTF());
        out.writeUTF(in.readUTF());
        out.writeDouble(in.readDouble());
        out.writeDouble(in.readDouble());
        out.writeDouble(in.readDouble());

        for (Map.Entry<String, ServerInfo> entry : Main.main.getProxy().getServers().entrySet()) {
            ServerInfo server = entry.getValue();
            server.sendData("BungeeCord", out.toByteArray());
        }
    }

    private void handleSetSpawn(DataInputStream in, PluginMessageEvent event) throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("SetSpawn");
        out.writeUTF(in.readUTF());
        out.writeUTF(in.readUTF());
        out.writeDouble(in.readDouble());
        out.writeDouble(in.readDouble());
        out.writeDouble(in.readDouble());
        out.writeDouble(in.readDouble());
        out.writeDouble(in.readDouble());

        for (Map.Entry<String, ServerInfo> entry : Main.main.getProxy().getServers().entrySet()) {
            ServerInfo server = entry.getValue();
            server.sendData("BungeeCord", out.toByteArray());
        }
    }
}