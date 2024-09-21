package cn.ChengZhiYa.MHDFTools.manager.init.stop;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerChannelListener;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getServer;

public class stopChannel implements Invitable {

    final JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    @Override
    public void init() {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");
            getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord", new ServerChannelListener());
        }
    }
}
