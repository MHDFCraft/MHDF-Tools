package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.Placeholder;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.getCustomMessage;
import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.dataExists;
import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.getData;

public final class ChatColorUtil {

    private static boolean isMySQL() {
        return Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL");
    }

    private static File getChatColorFile() {
        return new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "ChatColorData.yml");
    }

    public static boolean doesPlayerChatColorExist(Player player) {
        if (isMySQL()) {
            return dataExists("mhdftools_chatcolor", "PlayerName", player.getName());
        } else {
            YamlConfiguration data = YamlConfiguration.loadConfiguration(getChatColorFile());
            return data.contains(player.getName());
        }
    }

    public static String getPlayerChatColor(Player player) {
        if (doesPlayerChatColorExist(player)) {
            if (isMySQL()) {
                return Placeholder(player, (String) getData("mhdftools_chatcolor", "PlayerName", player.getName(), "ChatColor"));
            } else {
                YamlConfiguration data = YamlConfiguration.loadConfiguration(getChatColorFile());
                return Placeholder(player, data.getString(player.getName()));
            }
        } else {
            return getCustomMessage(player, "ChatColorSettings.Group", "ChatColor");
        }
    }
}