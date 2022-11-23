package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class SetHome_Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String NewHomeName = args[0];
                File HomeData = new File(main.main.getDataFolder() + "/HomeData");
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
                if (main.main.getConfig().getInt("HomeSystemSettings.MaxHomeTime") <= HomeList.size()) {
                    player.sendMessage(ChatColor("&c&l你已经创建了" + HomeList.size() + "个家了!不能再创建了!"));
                    return false;
                }
                HomeList.add(NewHomeName);
                PlayerHomeData.set(player.getName() + "_HomeList", HomeList);
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
                player.sendMessage(ChatColor("&a&l设置成功!"));
            } else {
                sender.sendMessage(ChatColor("&c用法错误,用法:/sethome <家名称>"));
            }
        } else {
            sender.sendMessage(ChatColor("&c这个命令只有玩家才能执行"));
        }
        return false;
    }
}
