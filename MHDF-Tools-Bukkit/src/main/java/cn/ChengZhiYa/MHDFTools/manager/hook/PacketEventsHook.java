package cn.ChengZhiYa.MHDFTools.manager.hook;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.manager.Hooker;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.util.TimeStampMode;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;

@Getter
public final class PacketEventsHook implements Hooker {
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
        this.serverManager = PacketEvents.getAPI().getServerManager();
    }

    /**
     * 卸载PacketEvents的API
     */
    @Override
    public void unhook() {
        PacketEvents.getAPI().terminate();
    }
}