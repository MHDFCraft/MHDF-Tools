package cn.ChengZhiYa.ChengToolsReloaded.Commands.Home;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Database.HomeUtil.getHome;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Database.HomeUtil.getPlayerHomeList;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLang;

public final class Home implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String HomeName = args[0];
                Location HomeLocation = getHome(player.getName(),HomeName);
                if (HomeLocation != null) {
                    player.teleport(HomeLocation);
                    player.sendMessage(getLang("Home.TeleportDone"));
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
                return getPlayerHomeList(sender.getName());
            }
        }
        return null;
    }
}
