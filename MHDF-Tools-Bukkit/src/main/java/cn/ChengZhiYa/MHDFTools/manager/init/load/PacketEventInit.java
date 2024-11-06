package cn.ChengZhiYa.MHDFTools.manager.init.load;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.manager.init.Starter;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.TimeStampMode;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public class PacketEventInit implements Starter {

    @Override
    public void init() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(PluginLoader.INSTANCE.getPlugin()));
        PacketEvents.getAPI().getSettings()
                .bStats(false)
                .fullStackTrace(true)
                .kickOnPacketException(true)
                .checkForUpdates(false)
                .reEncodeByDefault(false)
                .debug(false)
                .timeStampMode(TimeStampMode.NANO);
        PacketEvents.getAPI().load();
        PluginLoader.INSTANCE.setServerManager(PacketEvents.getAPI().getServerManager());
    }
}