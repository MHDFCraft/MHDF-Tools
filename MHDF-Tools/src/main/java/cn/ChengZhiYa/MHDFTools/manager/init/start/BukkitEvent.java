package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.listeners.player.*;
import cn.ChengZhiYa.MHDFTools.listeners.player.fastuse.EnderChest;
import cn.ChengZhiYa.MHDFTools.listeners.player.fastuse.ShulkerBox;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerCommandListener;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerInteractListener;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerJoinLeaveMessageListener;
import cn.ChengZhiYa.MHDFTools.listeners.server.ServerMOTDListener;
import cn.ChengZhiYa.MHDFTools.listeners.server.menu.HomeMenu;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import cn.ChengZhiYa.MHDFTools.utils.message.LogUtil;
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
        LogUtil.color("&e[MHDFTools] &f事件注册ing...");
        registerEvent(new PlayerJoinListener());
        registerEvent(new ServerJoinLeaveMessageListener());
        registerEvent(new PlayerSpawnListener());

        if (MHDFTools.instance.getConfig().getBoolean("FastUseCraftingTableSettings.Enable")) {
            registerEvent(new ServerInteractListener());
        }
        if (MHDFTools.instance.getConfig().getBoolean("BanCommandSettings.Enable")) {
            registerEvent(new ServerCommandListener());
        }
        if (MHDFTools.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            registerEvent(new PlayerLoginListener());
        }
        if (MHDFTools.instance.getConfig().getBoolean("SpawnSettings.Enable")) {
            registerEvent(new PlayerReSpawnListener());
        }
        if (MHDFTools.instance.getConfig().getBoolean("FlySettings.Enable")) {
            registerEvent(new PlayerAllowedFlightListener());
        }
        if (MHDFTools.instance.getConfig().getBoolean("BackSettings.Enable")) {
            registerEvent(new PlayerBackListener());
        }
        if (MHDFTools.instance.getConfig().getBoolean("TpBackSettings.Enable")) {
            registerEvent(new PlayerTPBackListener());
        }
        if (MHDFTools.instance.getConfig().getBoolean("VanishSettings.Enable")) {
            registerEvent(new PlayerVanishListener());
        }
        if (MHDFTools.instance.getConfig().getBoolean("MOTDSettings.Enable")) {
            registerEvent(new ServerMOTDListener());
        }
        if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
            registerEvent(new HomeMenu());
        }
        if (MHDFTools.instance.getConfig().getBoolean("FastUseEnderChestSettings.Enable")) {
            registerEvent(new EnderChest());
        }
        if (MHDFTools.instance.getConfig().getBoolean("FastUseShulkerBoxSettings.Enable")) {
            registerEvent(new ShulkerBox());
        }
        LogUtil.color("&e[MHDFTools] &a事件注册完毕!");
    }

    private void registerEvent(Object listener) {
        Bukkit.getPluginManager().registerEvents((Listener) listener, plugin);
    }
}