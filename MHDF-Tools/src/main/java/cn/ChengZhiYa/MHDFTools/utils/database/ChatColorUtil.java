package cn.ChengZhiYa.MHDFTools.utils.database;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.Placeholder;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.getCustomMessage;
import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.dataExists;
import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.getData;

public final class ChatColorUtil {
    public static Boolean ifPlayerChatColorExists(Player player) {
        if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
            return dataExists("mhdftools_chatcolor", "PlayerName", player.getName());
        } else {
            File file = new File(MHDFTools.instance.getDataFolder(), "ChatColorData.yml");
            YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
            return data.getString(player.getName()) != null;
        }
    }

    public static String getChatColor(Player player) {
        if (ifPlayerChatColorExists(player)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                return Placeholder(player,(String) getData("mhdftools_chatcolor", "PlayerName", player.getName(), "ChatColor"));
            } else {
                File file = new File(MHDFTools.instance.getDataFolder(), "ChatColorData.yml");
                YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
                return Placeholder(player,data.getString(player.getName()));
            }
        } else {
            return getCustomMessage(player, "ChatColorSettings.Group", "ChatColor");
        }
    }
}
