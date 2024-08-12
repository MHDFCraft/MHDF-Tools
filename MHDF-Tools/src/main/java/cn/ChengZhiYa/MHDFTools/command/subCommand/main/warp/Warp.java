package cn.ChengZhiYa.MHDFTools.command.subCommand.main.warp;

import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
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
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                String warpName = args[0];
                String serverName = getWarpServer(warpName);
                if (ifWarpExists(warpName)) {
                    SuperLocation warpLocation = getWarp(warpName);
                    BungeeCordUtil.tpPlayerTo(player.getName(), serverName, warpLocation);
                    player.sendMessage(SpigotUtil.i18n("Warp.TeleportDone", warpName));
                } else {
                    player.sendMessage(SpigotUtil.i18n("Warp.NotFound", warpName));
                }
            } else {
                sender.sendMessage(i18n("Usage.Warp", label));
            }
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
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
