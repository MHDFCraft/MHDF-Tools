package cn.ChengZhiYa.MHDFTools.command.subCommand.main.flight;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil.*;

public final class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            handlePlayerCommand((Player) sender, args, sender.hasPermission("MHDFTools.Command.Fly.Infinite"));
        } else {
            handleConsoleCommand(sender, args);
        }
        return true;
    }

    private void handlePlayerCommand(Player player, String[] args, boolean hasInfinitePermission) {
        if (shouldExecuteFlyCommand(player)) {
            if (hasInfinitePermission) {
                handleInfiniteFly(player);
            } else {
                handleLimitedFly(player);
            }
        }
    }

    private void handleConsoleCommand(CommandSender sender, String[] args) {
        if (args.length == 1) {
            handleFlyGiveCommand(sender, args[0]);
        } else {
            sender.sendMessage(i18n("Usage.Fly"));
        }
    }

    private boolean shouldExecuteFlyCommand(Player player) {
        return !MHDFTools.instance.getConfig().getStringList("FlySettings.AntiFlyWorldList").contains(player.getLocation().getWorld().getName());
    }

    private void handleInfiniteFly(Player player) {
        if (InFlyList.contains(player.getName())) {
            removeFlyAndDisable(player);
        } else {
            addFlyAndEnable(player);
        }
    }

    private void handleLimitedFly(Player player) {
        if (getFlyTime(player.getName()) > 0) {
            if (InFlyList.contains(player.getName())) {
                removeFlyAndDisable(player);
            } else {
                addFlyAndEnable(player);
            }
        } else {
            player.sendMessage(i18n("NoPermission"));
        }
    }

    private void handleFlyGiveCommand(CommandSender sender, String playerName) {
        if (sender.hasPermission("MHDFTools.Command.Fly.Give")) {
            Player player = Bukkit.getPlayer(playerName);
            if (player != null) {
                if (InFlyList.contains(player.getName())) {
                    removeFlyAndDisable(player);
                    sender.sendMessage(i18n("Fly.SetDone", player.getName(), i18n("Fly.Disabled")));
                } else {
                    addFlyAndEnable(player);
                    sender.sendMessage(i18n("Fly.SetDone", player.getName(), i18n("Fly.Enable")));
                }
            } else {
                sender.sendMessage(i18n("PlayerNotOnline"));
            }
        } else {
            sender.sendMessage(i18n("NoPermission"));
        }
    }

    private void removeFlyAndDisable(Player player) {
        removeFlyTime(player.getName());
        InFlyList.remove(player.getName());
        player.setAllowFlight(false);
        player.sendMessage(i18n("Fly.Done", i18n("Fly.Disabled")));
        MapUtil.getStringHasMap().put(player.getName() + "_Fly", null);
    }

    private void addFlyAndEnable(Player player) {
        addFlyTime(player.getName(), -999);
        InFlyList.add(player.getName());
        player.setAllowFlight(true);
        player.sendMessage(i18n("Fly.Done", i18n("Fly.Enable")));
        MapUtil.getStringHasMap().put(player.getName() + "_Fly", "已启用");
    }
}