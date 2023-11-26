package cn.ChengZhiYa.ChengToolsReloaded.Commands.Home;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.HomeUtil.*;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLang;

public final class DelHome implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String HomeName = args[0];
                if (HomeExists(player.getName(),HomeName)) {
                    RemoveHome(player.getName(),HomeName);
                    sender.sendMessage(getLang("Home.SetDone", label));
                    player.sendMessage(getLang("Home.RemoveDone"));
                }else {
                    player.sendMessage(getLang("Home.NotFound", HomeName));
                }
            } else {
                sender.sendMessage(getLang("Usage.Home", label));
            }
        } else {
            sender.sendMessage(getLang("OnlyPlayer"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                File HomeData = new File(ChengToolsReloaded.instance.getDataFolder() + "/HomeData");
                File HomeData_File = new File(HomeData, player.getName() + ".yml");
                YamlConfiguration PlayerHomeData = YamlConfiguration.loadConfiguration(HomeData_File);
                return PlayerHomeData.getStringList(player.getName() + "_HomeList");
            }
        }
        return null;
    }
}
