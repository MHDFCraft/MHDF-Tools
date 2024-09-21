package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerChannelListener;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import org.bukkit.plugin.java.JavaPlugin;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.getServerName;
import static org.bukkit.Bukkit.getServer;

public class bungeeCord implements Invitable {
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    @Override
    public void init() {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
            getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", new ServerChannelListener());
            getServerName();
        }
    }
}
