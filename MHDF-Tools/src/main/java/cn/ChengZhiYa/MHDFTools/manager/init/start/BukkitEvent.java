package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.listeners.player.*;
import cn.ChengZhiYa.MHDFTools.listeners.player.fastuse.CraftingTable;
import cn.ChengZhiYa.MHDFTools.listeners.player.fastuse.EnderChest;
import cn.ChengZhiYa.MHDFTools.listeners.player.fastuse.ShulkerBox;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerCommandListener;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerEventActionListener;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerJoinLeaveMessageListener;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerMOTDListener;
import cn.ChengZhiYa.MHDFTools.listeners.server.menu.HomeMenu;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitEvent implements Invitable {
    private final JavaPlugin plugin;

    public BukkitEvent() {
        this.plugin = PluginLoader.INSTANCE.getPlugin();
    }

    @Override
    public void start() {
        registerEvent(new PlayerJoinListener(), null);
        registerEvent(new ServerJoinLeaveMessageListener(), null);
        //       registerEvent(new PlayerActionListener(), null);
        registerEvent(new PlayerSpawnListener(), null);
        registerEvent(new ServerCommandListener(), "BanCommandSettings.Enable");
        registerEvent(new PlayerLoginListener(), "LoginSystemSettings.Enable");
        registerEvent(new PlayerReSpawnListener(), "SpawnSettings.Enable");
        registerEvent(new PlayerAllowedFlightListener(), "FlySettings.Enable");
        registerEvent(new PlayerBackListener(), "BackSettings.Enable");
        registerEvent(new PlayerTpBackListener(), "TpBackSettings.Enable");
        registerEvent(new PlayerVanishListener(), "VanishSettings.Enable");
        registerEvent(new ServerMOTDListener(), "MOTDSettings.Enable");
        registerEvent(new HomeMenu(), "HomeSystemSettings.Enable");
        registerEvent(new EnderChest(), "FastUseEnderChestSettings.Enable");
        registerEvent(new ShulkerBox(), "FastUseShulkerBoxSettings.Enable");
        registerEvent(new CraftingTable(), "FastUseCraftingTableSettings.Enable");
        registerEvent(new ServerEventActionListener(), "EventActionSettings.Enable");
        registerEvent(new PlayerChatColorListener(),"ChatColorSettings.Enable");
    }

    private void registerEvent(Listener listener, String configPath) {
        if (configPath == null || plugin.getConfig().getBoolean(configPath)) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }
}