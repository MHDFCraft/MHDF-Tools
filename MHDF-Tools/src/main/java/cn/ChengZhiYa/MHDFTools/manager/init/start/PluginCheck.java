package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import cn.ChengZhiYa.MHDFTools.utils.message.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class PluginCheck implements Invitable {
    @Override
    public void start() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        boolean hasPlaceholderAPI = pluginManager.getPlugin("PlaceholderAPI") != null;
        boolean hasVault = pluginManager.getPlugin("Vault") != null;

        if (!hasPlaceholderAPI) {
            LogUtil.color("&f[MHDF-Tools] &c找不到PlaceholderAPI,已关闭PAPI变量解析系统!");
            PluginLoader.hasPlaceholderAPI = false;
        }
        if (!hasVault) {
            LogUtil.color("&f[MHDF-Tools] &c找不到Vault,无法启用经济系统!");
            PluginLoader.hasVault = false;
        }
    }
}