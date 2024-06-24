package cn.ChengZhiYa.MHDFTools.manager.init.stop;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerChannelListener;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getServer;

public class stopChannel implements Invitable {
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();
    @Override
    public void start() {
        if (MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");
            getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord", new ServerChannelListener());
        }
    }
}
