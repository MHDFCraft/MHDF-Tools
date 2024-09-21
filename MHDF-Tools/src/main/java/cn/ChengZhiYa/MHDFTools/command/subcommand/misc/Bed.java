package cn.ChengZhiYa.MHDFTools.command.subcommand.misc;

import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Bed implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n("OnlyPlayer"));
            return false;
        }

        Player player = (Player) sender;
        if (player.getBedSpawnLocation() != null) {
            SpigotUtil.diffTeleport(player, player.getBedSpawnLocation());
            player.sendMessage(i18n("Bed.TeleportDone"));
        } else {
            player.sendMessage(i18n("Bed.NotFound"));
        }
        return true;
    }
}