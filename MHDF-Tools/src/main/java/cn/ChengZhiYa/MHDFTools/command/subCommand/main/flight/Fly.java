package cn.ChengZhiYa.MHDFTools.command.subCommand.main.flight;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil.addFly;
import static cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil.flyList;

public final class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = null;
        if (args.length == 0) {
            if (sender instanceof Player) {
                player = (Player) sender;
            }
        }
        if (args.length == 1) {
            if (Bukkit.getPlayer(args[0]) != null) {
                player = Bukkit.getPlayer(args[0]);
            }
        }
        if (player != null){
            if (!MHDFTools.instance.getConfig().getStringList("FlySettings.AntiFlyWorldList").contains(player.getLocation().getWorld().getName())) {
                if (sender.hasPermission("MHDFTools.Command.Fly.Infinite")) {
                    if (flyList.contains(player.getName())) {
                        FlyUtil.removeFly(player.getName());
                        disableFly(player);
                    } else {
                        addFly(player.getName(), -999);
                        enableFly(player);
                    }
                    return false;
                } else {
                    if (FlyUtil.getFlyTime(player.getName()) > 0) {
                        if (flyList.contains(player.getName())) {
                            disableFly(player);
                        } else {
                            enableFly(player);
                        }
                        return false;
                    }
                }
                sender.sendMessage(i18n("NoPermission"));
            }
        }else {
            sender.sendMessage(i18n("Usage.Fly"));
        }
        return false;
    }

    private void disableFly(Player player) {
        flyList.remove(player.getName());
        player.setAllowFlight(false);
        player.sendMessage(i18n("Fly.Done", i18n("Fly.Disabled")));
    }

    private void enableFly(Player player) {
        flyList.add(player.getName());
        player.setAllowFlight(true);
        player.sendMessage(i18n("Fly.Done", i18n("Fly.Enable")));
    }
}