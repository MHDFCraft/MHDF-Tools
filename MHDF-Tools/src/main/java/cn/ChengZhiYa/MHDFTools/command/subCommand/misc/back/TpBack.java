package cn.ChengZhiYa.MHDFTools.command.subCommand.misc.back;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class TpBack implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (MapUtil.getLocationHashMap().get(player.getName() + "_TpBackLocation") != null) {
                if (MHDFTools.instance.getConfig().getStringList("TpBackSettings.DisableWorldList").contains(MapUtil.getLocationHashMap().get(player.getName() + "_TpBackLocation").getWorld().getName()) || MHDFTools.instance.getConfig().getStringList("TpBackSettings.DisableWorldList").contains(player.getLocation().getWorld().getName())) {
                    return false;
                }
                if (MapUtil.getIntHashMap().get(player.getName() + "_BackDelay") != null
                        || MapUtil.getIntHashMap().get(player.getName() + "_TpBackDelay") != null) {
                    player.sendMessage(i18n("TpBack.RepectSend"));
                    return false;
                }
                MapUtil.getIntHashMap().put(player.getName() + "_TpBackDelay", MHDFTools.instance.getConfig().getInt("TpBackSettings.Delay"));
            } else {
                sender.sendMessage(i18n("TpBack.NotFound"));
            }
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
