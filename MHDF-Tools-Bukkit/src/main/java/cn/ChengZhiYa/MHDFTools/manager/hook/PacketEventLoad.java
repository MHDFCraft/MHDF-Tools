package cn.ChengZhiYa.MHDFTools.manager.hook;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.manager.Hooker;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.TimeStampMode;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public final class PacketEventLoad implements Hooker {
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
    }

    @Override
    public void unhook() {
        PacketEvents.getAPI().terminate();
    }
}