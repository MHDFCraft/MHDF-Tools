package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.listeners.player.*;
import cn.ChengZhiYa.MHDFTools.listeners.player.fastuse.CraftingTable;
import cn.ChengZhiYa.MHDFTools.listeners.player.fastuse.EnderChest;
import cn.ChengZhiYa.MHDFTools.listeners.player.fastuse.ShulkerBox;
import cn.ChengZhiYa.MHDFTools.listeners.server.*;
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
        //Player Start
        registerEvent(new PlayerJoinListener(), null);
        registerEvent(new PlayerSpawnListener(), null);
        registerEvent(new PlayerQuitListener(), "FreezeSettings.PunishEnable");
        registerEvent(new PlayerLoginListener(), "LoginSystemSettings.Enable");
        registerEvent(new PlayerReSpawnListener(), "SpawnSettings.Enable");
        registerEvent(new PlayerAllowedFlightListener(), "FlySettings.Enable");
        registerEvent(new PlayerBackListener(), "BackSettings.Enable");
        registerEvent(new PlayerTpBackListener(), "TpBackSettings.Enable");
        registerEvent(new PlayerVanishListener(), "VanishSettings.Enable");
        registerEvent(new HomeMenu(), "HomeSystemSettings.Enable");
        registerEvent(new EnderChest(), "FastUseEnderChestSettings.Enable");
        registerEvent(new ShulkerBox(), "FastUseShulkerBoxSettings.Enable");
        registerEvent(new CraftingTable(), "FastUseCraftingTableSettings.Enable");
        registerEvent(new PlayerChatColorListener(), "ChatColorSettings.Enable");
        //Player End

        //Server Start
        registerEvent(new ServerJoinLeaveMessageListener(), null);
        registerEvent(new ServerEventActionListener(), "EventActionSettings.Enable");
        registerEvent(new ServerMOTDListener(), "MOTDSettings.Enable");
        registerEvent(new ServerCommandListener(), "BanCommandSettings.Enable");
        registerEvent(new ServerFreezeListener(), "FreezeSettings.Enable");
        //Server End
    }

    private void registerEvent(Listener listener, String configPath) {
        if (configPath == null || plugin.getConfig().getBoolean(configPath)) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }
}