package cn.ChengZhiYa.ChengToolsReloaded.Commands.Home;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class DelHome implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String HomeName = args[0];
                File HomeData = new File(ChengToolsReloaded.instance.getDataFolder() + "/HomeData");
                File HomeData_File = new File(HomeData, player.getName() + ".yml");
                if (!HomeData_File.exists()) {
                    try {
                        HomeData_File.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                YamlConfiguration PlayerHomeData = YamlConfiguration.loadConfiguration(HomeData_File);
                List<String> HomeList = PlayerHomeData.getStringList(player.getName() + "_HomeList");
                if (!HomeList.contains(HomeName)) {
                    player.sendMessage(getLang("Home.NotFound", HomeName));
                    return false;
                }
                HomeList.remove(HomeName);
                PlayerHomeData.set(player.getName() + "_HomeList", HomeList);
                PlayerHomeData.set(HomeName + ".World", null);
                PlayerHomeData.set(HomeName + ".X", null);
                PlayerHomeData.set(HomeName + ".Y", null);
                PlayerHomeData.set(HomeName + ".Z", null);
                PlayerHomeData.set(HomeName, null);
                try {
                    PlayerHomeData.save(HomeData_File);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                player.sendMessage(getLang("Home.RemoveDone"));
            } else {
                sender.sendMessage(getLang("Usage.Home", label));
            }
        } else {
            sender.sendMessage(getLang("OnlyPlayer"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                File HomeData = new File(ChengToolsReloaded.instance.getDataFolder() + "/HomeData");
                File HomeData_File = new File(HomeData, player.getName() + ".yml");
                YamlConfiguration PlayerHomeData = YamlConfiguration.loadConfiguration(HomeData_File);
                List<String> HomeList = PlayerHomeData.getStringList(player.getName() + "_HomeList");
                if (HomeList.get(0) != null) {
                    return HomeList;
                }
                return null;
            }
        }
        return null;
    }
}
