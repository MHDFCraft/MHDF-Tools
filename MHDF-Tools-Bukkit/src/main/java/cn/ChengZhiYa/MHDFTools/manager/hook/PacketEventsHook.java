package cn.ChengZhiYa.MHDFTools.manager.hook;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.manager.AbstractHook;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.util.TimeStampMode;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public final class PacketEventsHook extends AbstractHook {
    private ServerManager serverManager;

    /**
     * 初始化PacketEvents的API
     */
    @Override
    public void hook() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(Main.instance));
        PacketEvents.getAPI().getSettings()
                .bStats(false)
                .fullStackTrace(true)
                .kickOnPacketException(true)
                .checkForUpdates(false)
                .reEncodeByDefault(false)
                .debug(false)
                .timeStampMode(TimeStampMode.NANO);
        PacketEvents.getAPI().load();

        PacketEvents.getAPI().init();
        serverManager = PacketEvents.getAPI().getServerManager();
        super.enable = true;
    }

    /**
     * 卸载PacketEvents的API
     */
    @Override
    public void unhook() {
        PacketEvents.getAPI().terminate();
        super.enable = false;
    }

    /**
     * 给指定玩家发送指定数据包
     *
     * @param player 接收数据包的玩家
     * @param packet 发送的数据包
     */
    public void sendPacket(Player player, PacketWrapper<?> packet) {
        if (enable) {
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
        }
    }
}