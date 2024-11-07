package cn.ChengZhiYa.MHDFTools.manager.init;

import cn.ChengZhiYa.MHDFTools.manager.hook.PacketEventsHook;
import cn.ChengZhiYa.MHDFTools.manager.hook.PlaceholderAPIHook;
import cn.ChengZhiYa.MHDFTools.manager.interfaces.Hooker;
import lombok.Getter;

@Getter
public final class PluginHookManager implements Hooker {
    @Getter
    private static final PacketEventsHook packetEventsHook = new PacketEventsHook();
    @Getter
    private static final PlaceholderAPIHook placeholderAPIHook = new PlaceholderAPIHook();

    /**
     * 初始化所有对接的API
     */
    @Override
    public void hook() {
        packetEventsHook.hook();
        placeholderAPIHook.hook();
    }

    /**
     * 卸载所有对接的API
     */
    @Override
    public void unhook() {
        packetEventsHook.unhook();
        placeholderAPIHook.unhook();
    }
}
