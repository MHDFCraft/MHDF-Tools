package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class TabCompletePacket {
    public void reister() {
        if (main.main.getConfig().getBoolean("BanCommandSettings.Enable")) {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            protocolManager.addPacketListener(new PacketAdapter(main.main, ListenerPriority.LOWEST, PacketType.Play.Client.TAB_COMPLETE) {
                public void onPacketReceiving(PacketEvent event) {
                    if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                        Player player = event.getPlayer();
                        PacketContainer Packet = event.getPacket();
                        String Message = Packet.getSpecificModifier(String.class).read(0).toLowerCase();

                        if (event.getPlayer().hasPermission("ChengTools.BanCommand.Bypass") && main.main.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                            return;
                        }
                        for (String BanCommand : main.main.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                            if (Message.contains("/" + BanCommand)) {
                                deny(player);
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

    private void deny(Player player) {
        PacketContainer closeTab = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CLOSE_WINDOW);
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, closeTab);
        } catch (InvocationTargetException ignored) {}
    }
}
