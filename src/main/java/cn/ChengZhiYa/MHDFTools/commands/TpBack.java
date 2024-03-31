package cn.ChengZhiYa.MHDFTools.commands;

import cn.ChengZhiYa.MHDFTools.map.IntHasMap;
import cn.ChengZhiYa.MHDFTools.map.LocationHasMap;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;

public final class TpBack implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (LocationHasMap.getHasMap().get(player.getName() + "_TpBackLocation") != null) {
                if (MHDFTools.instance.getConfig().getStringList("TpBackSettings.DisableWorldList").contains(LocationHasMap.getHasMap().get(player.getName() + "_TpBackLocation").getWorld().getName()) || MHDFTools.instance.getConfig().getStringList("TpBackSettings.DisableWorldList").contains(player.getLocation().getWorld().getName())) {
                    return false;
                }
                if (IntHasMap.getHasMap().get(player.getName() + "_BackDelay") != null || IntHasMap.getHasMap().get(player.getName() + "_TpBackDelay") != null) {
                    player.sendMessage(i18n("TpBack.RepectSend"));
                    return false;
                }
                IntHasMap.getHasMap().put(player.getName() + "_TpBackDelay", MHDFTools.instance.getConfig().getInt("TpBackSettings.Delay"));
            } else {
                sender.sendMessage(i18n("TpBack.NotFound"));
            }
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
