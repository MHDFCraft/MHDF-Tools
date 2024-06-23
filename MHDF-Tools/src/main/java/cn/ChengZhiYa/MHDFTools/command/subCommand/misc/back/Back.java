package cn.ChengZhiYa.MHDFTools.command.subCommand.misc.back;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public final class Back implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (MapUtil.getLocationHashMap().get(player.getName() + "_DeathLocation") != null) {
                if (MHDFTools.instance.getConfig().getStringList("BackSettings.DisableWorldList").contains(MapUtil.getLocationHashMap().get(player.getName() + "_DeathLocation").getWorld().getName()) || MHDFTools.instance.getConfig().getStringList("BackSettings.DisableWorldList").contains(player.getLocation().getWorld().getName())) {
                    return false;
                }
                if (MapUtil.getIntHashMap().get(player.getName() + "_BackDelay") != null || MapUtil.getIntHashMap().get(player.getName() + "_TpBackDelay") != null) {
                    player.sendMessage(SpigotUtil.i18n("Back.RepectSend"));
                    return false;
                }
                MapUtil.getIntHashMap().put(player.getName() + "_BackDelay", MHDFTools.instance.getConfig().getInt("BackSettings.Delay"));
            } else {
                sender.sendMessage(SpigotUtil.i18n("Back.NotFound"));
            }
        } else {
            sender.sendMessage(SpigotUtil.i18n("OnlyPlayer"));
        }
        return false;
    }
}
