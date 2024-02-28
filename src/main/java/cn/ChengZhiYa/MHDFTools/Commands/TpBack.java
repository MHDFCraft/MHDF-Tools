package cn.ChengZhiYa.MHDFTools.Commands;

import cn.ChengZhiYa.MHDFTools.HashMap.IntHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.LocationHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.StringHasMap;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.Utils.BCUtil.TpPlayerTo;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class TpBack implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LocationHasMap.getHasMap().get(player.getName() + "_TpBackLocation") != null) {
                if (MHDFTools.instance.getConfig().getStringList("TpBackSettings.DisableWorldList").contains(LocationHasMap.getHasMap().get(player.getName() + "_TpBackLocation").getWorld().getName()) || MHDFTools.instance.getConfig().getStringList("TpBackSettings.DisableWorldList").contains(player.getLocation().getWorld().getName())) {
                    return false;
                }
                if (IntHasMap.getHasMap().get(player.getName() + "_BackTpDelay") != null) {
                    player.sendMessage(i18n("TpBack.Delay"));
                    return false;
                }
                IntHasMap.getHasMap().put(player.getName() + "_BackTpDelay", MHDFTools.instance.getConfig().getInt("TpBackSettings.Delay"));
                Bukkit.getScheduler().runTaskLaterAsynchronously(MHDFTools.instance, () -> IntHasMap.getHasMap().remove(player.getName() + "_BackTpDelay"), 20L * MHDFTools.instance.getConfig().getInt("TpBackSettings.Delay"));
                LocationHasMap.getHasMap().put(player.getName() + "_UnBackLocation", player.getLocation());
                TpPlayerTo(player.getName(),
                        StringHasMap.getHasMap().get(player.getName() + "_TpBackLocation_Server"),
                        LocationHasMap.getHasMap().get(player.getName() + "_TpBackLocation")
                );
                player.sendMessage(i18n("TpBack.Done"));
            } else {
                sender.sendMessage(i18n("TpBack.NotFound"));
            }
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
