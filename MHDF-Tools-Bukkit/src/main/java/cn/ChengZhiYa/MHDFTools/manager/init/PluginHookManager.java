package cn.ChengZhiYa.MHDFTools.manager.init;

import cn.ChengZhiYa.MHDFTools.manager.hook.PacketEventsHook;
import cn.ChengZhiYa.MHDFTools.manager.hook.PlaceholderAPIHook;
import cn.ChengZhiYa.MHDFTools.manager.hook.VaultHook;
import cn.ChengZhiYa.MHDFTools.manager.interfaces.Hook;
import lombok.Getter;

@Getter
public final class PluginHookManager implements Hook {
    @Getter
    private static final PacketEventsHook packetEventsHook = new PacketEventsHook();
    @Getter
    private static final PlaceholderAPIHook placeholderAPIHook = new PlaceholderAPIHook();
    @Getter
    private static final VaultHook vaultHook = new VaultHook();

    /**
     * 初始化所有对接的API
     */
    @Override
    public void hook() {
        packetEventsHook.hook();
        placeholderAPIHook.hook();
        vaultHook.hook();
    }

    /**
     * 卸载所有对接的API
     */
    @Override
    public void unhook() {
        packetEventsHook.unhook();
        placeholderAPIHook.unhook();
        vaultHook.unhook();
    }
}
