package cn.ChengZhiYa.MHDFTools.command.subcommand.misc.home;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil;
import cn.ChengZhiYa.MHDFTools.utils.menu.HomeMenuUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class Home implements TabExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (!PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("HomeSystemSettings.DisableWorldList").contains(((Player) sender).getLocation().getWorld().getName())) {
                if (args.length == 0 && Objects.equals(PluginLoader.INSTANCE.getPlugin().getConfig().getString("DataSettings.Type"), "MySQL")) {
                    Player player = (Player) sender;
                    HomeMenuUtil.openHomeMenu(player, 1);
                    return false;
                }
                if (args.length == 1) {
                    Player player = (Player) sender;
                    String HomeName = args[0];
                    SuperLocation HomeLocation = HomeUtil.getHomeLocation(player.getName(), HomeName);
                    if (HomeLocation != null) {
                        BungeeCordUtil.tpPlayerHome(player.getName(), HomeName);
                        player.sendMessage(SpigotUtil.i18n("Home.TeleportDone"));
                    } else {
                        player.sendMessage(SpigotUtil.i18n("Home.NotFound", HomeName));
                    }
                    return false;
                }
                sender.sendMessage(SpigotUtil.i18n("Usage.Home", label));
            }
        } else {
            sender.sendMessage(SpigotUtil.i18n("OnlyPlayer"));
        }
        return false;
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player && args.length == 1) {
            return HomeUtil.getPlayerHomeList(sender.getName());
        }
        return null;
    }
}
