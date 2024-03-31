package cn.ChengZhiYa.MHDFTools.hooks;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.databases.EconomyUtil.getMoney;

public final class PlaceholderAPI extends PlaceholderExpansion {

    public PlaceholderAPI() {
    }

    @Override
    public @NotNull String getAuthor() {
        return "ChengZhiYa";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "MHDFTools";
    }

    @Override
    public @NotNull String getVersion() {
        return MHDFTools.getDescriptionFile().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (MHDFTools.instance.getConfig().getBoolean("EconomySettings.Enable")) {
            if (params.equalsIgnoreCase("money")) {
                return String.format("%.2f", getMoney(player.getName()));
            }
        }
        return null;
    }
}
