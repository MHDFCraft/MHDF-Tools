package cn.ChengZhiYa.MHDFTools.manager.init.stop;

import cn.ChengZhiYa.MHDFTools.manager.init.Starter;
import com.github.retrooper.packetevents.PacketEvents;

public class PacketEventUnload implements Starter {

    @Override
    public void init() {
        PacketEvents.getAPI().terminate();
    }
}
