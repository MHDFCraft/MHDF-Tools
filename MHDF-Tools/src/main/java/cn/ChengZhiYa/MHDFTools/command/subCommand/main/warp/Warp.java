package cn.ChengZhiYa.MHDFTools.command.subCommand.main.warp;

import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.database.WarpUtil.*;

public final class Warp implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String playerName = sender instanceof Player ? sender.getName() : null;
        if (args.length == 2) {
            if (!BungeeCordUtil.ifPlayerOnline(args[1])) {
                sender.sendMessage(i18n("PlayerNotOnline"));
                return false;
            }
            playerName = args[1];
        }
        if (playerName != null && args.length >= 1) {
            String warpName = args[0];
            String serverName = getWarpServer(warpName);
            if (ifWarpExists(warpName)) {
                SuperLocation warpLocation = getWarp(warpName);
                BungeeCordUtil.tpPlayerTo(playerName, serverName, warpLocation);
                BungeeCordUtil.sendMessage(playerName,i18n("Warp.TeleportDone", warpName));
            } else {
                sender.sendMessage(i18n("Warp.NotFound", warpName));
            }
        } else {
            sender.sendMessage(i18n("Usage.Warp", label));
        }
        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                return getWarpList();
            }
        }
        return new ArrayList<>();
    }
}
