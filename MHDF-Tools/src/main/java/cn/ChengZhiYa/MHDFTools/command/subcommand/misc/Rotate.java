package cn.ChengZhiYa.MHDFTools.command.subcommand.misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Rotate implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(i18n("Usage.Rotate"));
            return false;
        }

        Player target = Bukkit.getServer().getPlayerExact(args[0]); // 获取玩家方法
        if (target == null) {
            sender.sendMessage(i18n("Rotate.NotFound"));
            return false;
        }


        Location targetLocation = target.getLocation();

        randomiseAim(target, targetLocation);

        sender.sendMessage(i18n("Rotate.Done"));
        return true;
    }

    public void randomiseAim(Player player, Location location) {
        location.setPitch((float) Math.max(-90, ThreadLocalRandom.current().nextInt(90)));
        location.setYaw((float) Math.max(-180, ThreadLocalRandom.current().nextInt(180)));
        player.teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }
}