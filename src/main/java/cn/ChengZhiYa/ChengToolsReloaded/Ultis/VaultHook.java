package cn.ChengZhiYa.ChengToolsReloaded.Ultis;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;

public class VaultHook {

    public void hook() {
        Bukkit.getServicesManager().register(Economy.class, new EconomyImplementer(), main.main, ServicePriority.Normal);
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Economy.class, new EconomyImplementer());
    }
}
