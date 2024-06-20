package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.MHDFPluginLoader;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;

import static cn.ChengZhiYa.MHDFTools.hooks.Vault.hookVault;

public class Vault implements Invitable {
    @Override
    public void start() {
        if (MHDFTools.instance.getConfig().getBoolean("EconomySettings.Enable")
                && MHDFPluginLoader.hasVault) {
            hookVault();
        }
    }
}
