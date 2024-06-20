package cn.ChengZhiYa.MHDFToolsBungeeCordHook;

import cn.ChengZhiYa.MHDFToolsBungeeCordHook.Listeners.PluginMessage;
import net.md_5.bungee.api.plugin.Plugin;

public final class main extends Plugin {
    public static main main;

    @Override
    public void onEnable() {
        // Plugin startup logic
        main = this;
        getProxy().getPluginManager().registerListener(this, new PluginMessage());
        getProxy().registerChannel("BungeeCord");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        main = null;
    }
}
