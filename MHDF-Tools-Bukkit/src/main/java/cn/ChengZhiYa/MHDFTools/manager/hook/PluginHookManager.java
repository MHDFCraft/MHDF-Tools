package cn.ChengZhiYa.MHDFTools.manager.hook;

import cn.ChengZhiYa.MHDFTools.manager.Hooker;
import lombok.Getter;

@Getter
public final class PluginHookManager implements Hooker {
    @Getter
    private static final PacketEventsHook packetEventsHook = new PacketEventsHook();

    /**
     * 初始化所有对接的API
     */
    @Override
    public void hook() {
        packetEventsHook.hook();
    }

    /**
     * 卸载所有对接的API
     */
    @Override
    public void unhook() {
        packetEventsHook.unhook();
    }
}
