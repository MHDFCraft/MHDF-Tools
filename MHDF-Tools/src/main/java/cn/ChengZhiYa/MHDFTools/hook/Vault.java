package cn.ChengZhiYa.MHDFTools.hook;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public final class Vault {
    public static void hookVault() {
        Bukkit.getServicesManager().register(Economy.class, new EconomyImplementer(), MHDFTools.instance, ServicePriority.Normal);
    }

    public static void unHookVault() {
        Bukkit.getServicesManager().unregister(Economy.class, new EconomyImplementer());
    }
}
