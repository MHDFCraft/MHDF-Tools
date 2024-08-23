package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;

import static cn.ChengZhiYa.MHDFTools.hooks.Vault.hookVault;

public class Vault implements Invitable {
    @Override
    public void start() {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("EconomySettings.Enable")
                && PluginLoader.INSTANCE.isHasVault()) {
            hookVault();
        }
    }
}
