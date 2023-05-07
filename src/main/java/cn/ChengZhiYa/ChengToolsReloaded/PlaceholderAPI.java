package cn.ChengZhiYa.ChengToolsReloaded;

import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class PlaceholderAPI extends PlaceholderExpansion {

    public PlaceholderAPI() {
    }

    @Override
    public @NotNull String getAuthor() {
        return "ChengZhiYa";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "ChengTools";
    }

    @Override
    public @NotNull String getVersion() {
        return ChengToolsReloaded.getDescriptionFile().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("PlayerTitleSettings.Enable")) {
            if (params.equalsIgnoreCase("Prefix")) {
                return GetPlt(player)[0];
            }
            if (params.equalsIgnoreCase("Suffix")) {
                return GetPlt(player)[1];
            }
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("EconomySettings.Enable")) {
            if (params.equalsIgnoreCase("money")) {
                return String.format("%.2f", new EconomyAPI().checkMoney(player.getName()));
            }
        }
        return null;
    }
}
