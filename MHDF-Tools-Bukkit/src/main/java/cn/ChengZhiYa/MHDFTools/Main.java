package cn.ChengZhiYa.MHDFTools;

import cn.ChengZhiYa.MHDFTools.manager.init.*;
import com.github.Anon8281.universalScheduler.UniversalScheduler;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public static Main instance;
    public static BukkitAudiences adventure;
    private final ConfigManager configManager = new ConfigManager();
    private final LibrariesManager librariesManager = new LibrariesManager();
    @Getter
    private DatabaseManager databaseManager;
    @Getter
    private CommandManager commandManager;
    @Getter
    private ListenerManager listenerManager;
    @Getter
    private PluginHookManager pluginHookManager;
    @Getter
    private TaskManager taskManager;

    @Override
    public void onLoad() {
        instance = this;

        this.configManager.init();
        this.librariesManager.init();

        this.databaseManager = new DatabaseManager();
        this.commandManager = new CommandManager();
        this.listenerManager = new ListenerManager();
        this.pluginHookManager = new PluginHookManager();
        this.taskManager = new TaskManager();
    }

    @Override
    public void onEnable() {
        adventure = BukkitAudiences.create(this);

        this.pluginHookManager.hook();

        this.databaseManager.init();

        this.commandManager.init();
        this.listenerManager.init();
        this.taskManager.init();
    }

    @Override
    public void onDisable() {
        this.pluginHookManager.unhook();
        this.databaseManager.close();

        UniversalScheduler.getScheduler(this).cancelTasks();

        instance = null;
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
    }
}
