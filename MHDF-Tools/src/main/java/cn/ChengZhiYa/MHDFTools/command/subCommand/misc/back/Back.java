package cn.ChengZhiYa.MHDFTools.command.subCommand.misc.back;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Back implements CommandExecutor {

    private final List<String> disableWorldList;

    public Back() {
        this.disableWorldList = MHDFTools.instance.getConfig().getStringList("BackSettings.DisableWorldList");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n("OnlyPlayer"));
            return false;
        }

        Player player = (Player) sender;
        if (MapUtil.getBooleanHashMap().containsKey(player.getName() + "_DeathLocation")) {
            if (isWorldDisabled(player)) {
                return false;
            }
            if (isBackDelayActive(player)) {
                player.sendMessage(i18n("Back.RepectSend"));
                return false;
            }
            MapUtil.getIntHashMap().put(player.getName() + "_BackDelay", MHDFTools.instance.getConfig().getInt("BackSettings.Delay"));
        } else {
            sender.sendMessage(i18n("Back.NotFound"));
        }

        return true;
    }

    private boolean isWorldDisabled(Player player) {
        String deathWorldName = MapUtil.getLocationHashMap().get(player.getName() + "_DeathLocation").getWorld().getName();
        String currentWorldName = player.getLocation().getWorld().getName();
        return disableWorldList.contains(deathWorldName) || disableWorldList.contains(currentWorldName);
    }

    private boolean isBackDelayActive(Player player) {
        return MapUtil.getIntHashMap().containsKey(player.getName() + "_BackDelay") || MapUtil.getIntHashMap().containsKey(player.getName() + "_TpBackDelay");
    }
}