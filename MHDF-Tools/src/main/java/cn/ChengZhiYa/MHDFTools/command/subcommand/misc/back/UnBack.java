package cn.ChengZhiYa.MHDFTools.command.subcommand.misc.back;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.ServerName;
import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.tpPlayerTo;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class UnBack implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (MapUtil.getLocationHashMap().get(player.getName() + "_UnBackLocation") != null) {
                if (PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("BackSettings.DisableWorldList").contains(MapUtil.getLocationHashMap().get(player.getName() + "_UnBackLocation").getWorldName()) || PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("TpBackSettings.DisableWorldList").contains(player.getLocation().getWorld().getName())) {
                    return false;
                }
                if (PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("TpBackSettings.DisableWorldList").contains(MapUtil.getLocationHashMap().get(player.getName() + "_UnBackLocation").getWorldName()) || PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("TpBackSettings.DisableWorldList").contains(player.getLocation().getWorld().getName())) {
                    return false;
                }
                tpPlayerTo(player.getName(), ServerName, MapUtil.getLocationHashMap().get(player.getName() + "_UnBackLocation"));
                player.sendMessage(i18n("UnBack.Done"));
            } else {
                sender.sendMessage(i18n("UnBack.NotFound"));
            }
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
