package cn.ChengZhiYa.MHDFTools.hooks;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public final class Vault {
    public static void hookVault() {
        Bukkit.getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new Economy(), PluginLoader.INSTANCE.getPlugin(), ServicePriority.Normal);
    }

    public static void unHookVault() {
        Bukkit.getServicesManager().unregister(net.milkbowl.vault.economy.Economy.class, new Economy());
    }
}
