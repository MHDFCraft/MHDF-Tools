package cn.ChengZhiYa.MHDFTools.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cn.ChengZhiYa.MHDFTools.Utils.Database.HomeUtil.getHome;
import static cn.ChengZhiYa.MHDFTools.Utils.Database.HomeUtil.getPlayerHomeList;
import static cn.ChengZhiYa.MHDFTools.Utils.MenuUtil.OpenHomeMenu;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class Home implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                OpenHomeMenu(player, 1);
                return false;
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                String HomeName = args[0];
                Location HomeLocation = getHome(player.getName(), HomeName);
                if (HomeLocation != null) {
                    player.teleport(HomeLocation);
                    player.sendMessage(i18n("Home.TeleportDone"));
                } else {
                    player.sendMessage(i18n("Home.NotFound", HomeName));
                }
                return false;
            }
            sender.sendMessage(i18n("Usage.Home", label));
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
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
