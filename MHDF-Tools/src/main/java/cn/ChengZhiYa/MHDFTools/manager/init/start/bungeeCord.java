package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.MHDFPluginLoader;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerChannelListener;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import org.bukkit.plugin.java.JavaPlugin;

import static cn.ChengZhiYa.MHDFTools.utils.BCUtil.getServerName;
import static org.bukkit.Bukkit.getServer;

public class bungeeCord implements Invitable {
    JavaPlugin plugin = MHDFPluginLoader.INSTANCE.getPlugin();

    @Override
    public void start() {
        if (MHDFTools.instance.getConfig().getBoolean("BungeecordSettings.Enable")) {
            getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
            getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", new ServerChannelListener());
            getServerName();
        }
    }
}
