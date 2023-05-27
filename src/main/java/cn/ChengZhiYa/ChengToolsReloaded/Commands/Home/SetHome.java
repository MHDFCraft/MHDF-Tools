package cn.ChengZhiYa.ChengToolsReloaded.Commands.Home;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class SetHome implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String NewHomeName = args[0];
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
                if (ChengToolsReloaded.instance.getConfig().getInt("HomeSystemSettings.MaxHomeTime") <= HomeList.size()) {
                    sender.sendMessage(getLang("Home.HomeListFull", label));
                    return false;
                }
                if (!HomeList.contains(NewHomeName)) {
                    HomeList.add(NewHomeName);
                    PlayerHomeData.set(player.getName() + "_HomeList", HomeList);
                }
                PlayerHomeData.set(NewHomeName + ".World", player.getWorld().getName());
                PlayerHomeData.set(NewHomeName + ".X", player.getLocation().getX());
                PlayerHomeData.set(NewHomeName + ".Y", player.getLocation().getY());
                PlayerHomeData.set(NewHomeName + ".Z", player.getLocation().getZ());
                try {
                    PlayerHomeData.save(HomeData_File);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                sender.sendMessage(getLang("Home.SetDone", label));
            } else {
                sender.sendMessage(getLang("Usage.Home", label));
            }
        } else {
            sender.sendMessage(getLang("OnlyPlayer"));
        }
        return false;
    }
}
