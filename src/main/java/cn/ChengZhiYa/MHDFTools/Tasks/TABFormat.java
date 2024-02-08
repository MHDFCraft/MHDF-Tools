/*
package cn.ChengZhiYa.MHDFTools.Tasks;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.Utils.Util.ChatColor;

public final class TABFormat extends BukkitRunnable {
    @Override
    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("TabSettings.Enable")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

                packet.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor(player, MHDFTools.instance.getConfig().getString("TabSettings.Header"))));
                packet.getChatComponents().write(1, WrappedChatComponent.fromText(ChatColor(player, MHDFTools.instance.getConfig().getString("TabSettings.Footer"))));

                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            }
        }
    }
}
 */