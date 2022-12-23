package cn.ChengZhiYa.ChengToolsReloaded;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class PlaceholderAPI  extends PlaceholderExpansion {

    public PlaceholderAPI(main main) {}

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
            File TitleData = new File(main.main.getDataFolder() + "/TitleData.yml");
            YamlConfiguration TitleFileData = YamlConfiguration.loadConfiguration(TitleData);
            String DefaultPrefix = main.main.getConfig().getString("PlayerTitleSettings.DefaultPrefix");
            String DefaultSuffix = main.main.getConfig().getString("PlayerTitleSettings.DefaultSuffix");
            String Prefix = main.main.getConfig().getString("PlayerTitleSettings.Prefix");
            String Suffix = main.main.getConfig().getString("PlayerTitleSettings.Suffix");
            if (params.equalsIgnoreCase("Prefix")) {
                if (TitleFileData.getString(player.getName() + "_Prefix") == null) {
                    if (!Objects.equals(DefaultPrefix, "")) {
                        return Prefix + DefaultPrefix + Suffix;
                    }
                    return "";
                }else {
                    return Prefix + TitleFileData.getString(player.getName() + "_Prefix") + Suffix;
                }
            }
            if (params.equalsIgnoreCase("Suffix")) {
                if (TitleFileData.getString(player.getName() + "_Suffix") == null) {
                    if (!Objects.equals(DefaultSuffix, "")) {
                        return Prefix + DefaultSuffix + Suffix;
                    }
                    return "";
                }else {
                    return Prefix + TitleFileData.getString(player.getName() + "_Suffix") + Suffix;
                }
            }
        }
        return null;
    }
}
