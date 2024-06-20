package cn.ChengZhiYa.MHDFTools.command.subCommand.misc.home;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.BCUtil.TpPlayerHome;
import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.getHomeLocation;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.getPlayerHomeList;
import static cn.ChengZhiYa.MHDFTools.utils.menu.HomeMenuUtil.openHomeMenu;

public final class Home implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n("OnlyPlayer"));
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                openHomeMenu(player, 1);
                return true;
            }
        } else if (args.length == 1) {
            String homeName = args[0];
            Location homeLocation = getHomeLocation(player.getName(), homeName);
            if (homeLocation != null) {
                TpPlayerHome(player.getName(), homeName);
                player.sendMessage(i18n("Home.TeleportDone"));
            } else {
                player.sendMessage(i18n("Home.NotFound", homeName));
            }
            return true;
        }

        player.sendMessage(i18n("Usage.Home", label));
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player && args.length == 1) {
            return getPlayerHomeList(sender.getName());
        }
        return null;
    }
}