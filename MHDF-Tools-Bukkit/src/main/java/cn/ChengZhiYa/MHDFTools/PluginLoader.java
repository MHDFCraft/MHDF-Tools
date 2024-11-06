package cn.ChengZhiYa.MHDFTools;

import cn.ChengZhiYa.MHDFTools.manager.InitManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public enum PluginLoader {
    INSTANCE;
    @Setter
    private boolean hasPlaceholderAPI;
    @Setter
    private boolean hasVault;

    private JavaPlugin plugin;
    @Setter
    private ServerManager serverManager;
    private InitManager initManager;

    public void initialize_load(JavaPlugin plugin) {
        this.plugin = plugin;
        initManagers();
        load();
    }

    public void initialize_start(JavaPlugin plugin) {
        this.plugin = plugin;
        start();
    }

    public void initialize_stop(JavaPlugin plugin) {
        this.plugin = plugin;
        stop();
    }

    private void initManagers() {
        this.initManager = new InitManager();
    }

    public void load() {
        initManager.load();
    }

    public void start() {
        initManager.start();
    }

    public void stop() {
        initManager.stop();
    }


    public String getVersion() {
        return getPlugin().getDescription().getVersion();
    }

}