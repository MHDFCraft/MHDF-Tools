package cn.ChengZhiYa.MHDFTools.manager.init.start;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.manager.init.Invitable;
import cn.ChengZhiYa.MHDFTools.utils.message.ColorLogs;
import org.bukkit.Bukkit;

public class PluginChecker implements Invitable {

    @Override
    public void start() {
        checkVault();
        checkPlaceholderAPI();
    }

    private void checkVault() {
        boolean hasVault = Bukkit.getPluginManager().getPlugin("Vault") != null;
        if (hasVault) {
            ColorLogs.consoleMessage("&f[MHDF-Tools] &a找到了Vault，启用经济系统。");
        } else {
            ColorLogs.consoleMessage("&f[MHDF-Tools] &c找不到Vault, 无法启用经济系统!");
        }
        PluginLoader.INSTANCE.setHasVault(hasVault);
    }

    private void checkPlaceholderAPI() {
        boolean hasPlaceholderAPI = isPlaceholderAPIAvailable();
        if (hasPlaceholderAPI) {
            ColorLogs.consoleMessage("&f[MHDF-Tools] &a找到了PlaceholderAPI，启用PAPI变量解析系统。");
        } else {
            ColorLogs.consoleMessage("&f[MHDF-Tools] &c找不到PlaceholderAPI, 已关闭PAPI变量解析系统!");
        }
        PluginLoader.INSTANCE.setHasPlaceholderAPI(hasPlaceholderAPI);
    }

    private boolean isPlaceholderAPIAvailable() {
        try {
            Class.forName("me.clip.placeholderapi.PlaceholderAPI");
            return true;
        } catch (ClassNotFoundException ignore) {
            return false;
        }
    }
}