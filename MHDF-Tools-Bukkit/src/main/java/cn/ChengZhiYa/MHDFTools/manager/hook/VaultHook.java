package cn.ChengZhiYa.MHDFTools.manager.hook;

import cn.ChengZhiYa.MHDFTools.manager.AbstractHook;
import org.bukkit.Bukkit;

public class VaultHook extends AbstractHook {
    @Override
    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            super.enable = true;
        }
    }

    @Override
    public void unhook() {
        super.enable = false;
    }
}
