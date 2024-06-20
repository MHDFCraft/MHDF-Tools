package cn.ChengZhiYa.MHDFTools.command;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.util.BCUtil.TpPlayerHome;
import static cn.ChengZhiYa.MHDFTools.util.Util.i18n;
import static cn.ChengZhiYa.MHDFTools.util.database.HomeUtil.getHomeLocation;
import static cn.ChengZhiYa.MHDFTools.util.database.HomeUtil.getPlayerHomeList;
import static cn.ChengZhiYa.MHDFTools.util.menu.HomeMenuUtil.openHomeMenu;

public final class Home implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                if (Objects.equals(MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                    Player player = (Player) sender;
                    openHomeMenu(player, 1);
                    return false;
                }
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                String HomeName = args[0];
                Location HomeLocation = getHomeLocation(player.getName(), HomeName);
                if (HomeLocation != null) {
                    TpPlayerHome(player.getName(), HomeName);
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
