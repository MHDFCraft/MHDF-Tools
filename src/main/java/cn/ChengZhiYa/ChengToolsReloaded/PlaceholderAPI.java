package cn.ChengZhiYa.ChengToolsReloaded;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class PlaceholderAPI extends PlaceholderExpansion {

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
        return main.getDescriptionFile().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (main.main.getConfig().getBoolean("PlayerTitleSettings.Enable")) {
            if (params.equalsIgnoreCase("Prefix")) {
                return GetPlt(player)[0];
            }
            if (params.equalsIgnoreCase("Suffix")) {
                return GetPlt(player)[1];
            }
        }
        return null;
    }
}
