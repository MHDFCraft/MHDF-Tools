package cn.ChengZhiYa.MHDFTools.manager.init.stop;

import cn.ChengZhiYa.MHDFTools.MHDFPluginLoader;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;

import static cn.ChengZhiYa.MHDFTools.hooks.Vault.unHookVault;

public class stopVault implements Invitable {
    @Override
    public void start() {
        if (MHDFPluginLoader.hasVault) {
            unHookVault();
        }
    }
}
