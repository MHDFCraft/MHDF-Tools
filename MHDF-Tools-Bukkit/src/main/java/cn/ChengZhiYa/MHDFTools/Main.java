package cn.ChengZhiYa.MHDFTools;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance;
    @Getter
    public static ServerManager serverManager;

    @Override
    public void onLoad() {
        instance = this;
        serverManager = PacketEvents.getAPI().getServerManager();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
