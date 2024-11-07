package cn.ChengZhiYa.MHDFTools;

import cn.ChengZhiYa.MHDFTools.manager.init.*;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    @Getter
    private static final CommandManager commandManager = new CommandManager();
    @Getter
    private static final ListenerManager listenerManager = new ListenerManager();
    public static Main instance;
    @Getter
    private static PluginHookManager pluginHookManager;
    private final ConfigManager configManager = new ConfigManager();
    private final LibrariesManager librariesManager = new LibrariesManager();

    @Override
    public void onLoad() {
        instance = this;

        configManager.init();
        librariesManager.init();

        pluginHookManager = new PluginHookManager();
    }

    @Override
    public void onEnable() {
        pluginHookManager.hook();

        commandManager.init();
        listenerManager.init();
    }

    @Override
    public void onDisable() {
        pluginHookManager.unhook();

        instance = null;
    }
}
