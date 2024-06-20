package cn.ChengZhiYa.MHDFTools.hooks;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.Util.Version;
import static cn.ChengZhiYa.MHDFTools.utils.database.EconomyUtil.getMoney;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.getMaxHome;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.getPlayerHomeList;

public final class PlaceholderAPI extends PlaceholderExpansion {

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
        return Version;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (MHDFTools.instance.getConfig().getBoolean("EconomySettings.Enable")) {
            if (params.equalsIgnoreCase("money")) {
                return String.format("%.2f", getMoney(player.getName()));
            }
        }
        if (MHDFTools.instance.getConfig().getBoolean("HomeSystemSettings.Enable")) {
            if (params.equalsIgnoreCase("HomeAmount")) {
                return String.valueOf(getPlayerHomeList(player.getName()).size());
            }
            if (params.equalsIgnoreCase("MaxHomeAmount")) {
                return String.valueOf(getMaxHome((Player) player));
            }
        }
        return null;
    }
}
