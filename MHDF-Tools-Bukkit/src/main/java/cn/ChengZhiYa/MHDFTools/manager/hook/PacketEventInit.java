package cn.ChengZhiYa.MHDFTools.manager.hook;

import cn.ChengZhiYa.MHDFTools.manager.Hooker;
import com.github.retrooper.packetevents.PacketEvents;

public class PacketEventInit implements Hooker {


    @Override
    public void hook() {
        PacketEvents.getAPI().init();
    }

    @Override
    public void unhook() {

    }
}
