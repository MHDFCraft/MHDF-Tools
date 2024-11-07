package cn.ChengZhiYa.MHDFTools.manager.hook;

import cn.ChengZhiYa.MHDFTools.manager.Hooker;
import lombok.Getter;

@Getter
public final class PluginHookManager implements Hooker {
    @Getter
    private static final PacketEventsHook packetEventsHook = new PacketEventsHook();

    @Override
    public void hook() {
        packetEventsHook.hook();
    }

    @Override
    public void unhook() {
        packetEventsHook.unhook();
    }
}
