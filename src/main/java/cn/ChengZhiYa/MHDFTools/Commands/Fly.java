package cn.ChengZhiYa.MHDFTools.Commands;

import cn.ChengZhiYa.MHDFTools.HashMap.StringHasMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.Utils.Database.FlyUtil.*;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("MHDFTools.Command.Fly")) {
                if (InFlyList.contains(player.getName())) {
                    removeFly(player.getName());
                    InFlyList.remove(player.getName());
                    player.setAllowFlight(false);
                    player.sendMessage(i18n("Fly.Done", i18n("Fly.Disabled")));
                    StringHasMap.getHasMap().put(player.getName() + "_Fly", null);
                } else {
                    addFly(player.getName(), -999);
                    InFlyList.add(player.getName());
                    player.setAllowFlight(true);
                    player.sendMessage(i18n("Fly.Done", i18n("Fly.Enable")));
                    StringHasMap.getHasMap().put(player.getName() + "_Fly", "已启用");
                }
            } else {
                if (getFlyTime(player.getName()) > 0) {
                    if (InFlyList.contains(player.getName())) {
                        InFlyList.remove(player.getName());
                        player.setAllowFlight(false);
                        player.sendMessage(i18n("Fly.Done", i18n("Fly.Disabled")));
                        StringHasMap.getHasMap().put(player.getName() + "_Fly", null);
                    } else {
                        InFlyList.add(player.getName());
                        player.setAllowFlight(true);
                        player.sendMessage(i18n("Fly.Done", i18n("Fly.Enable")));
                        StringHasMap.getHasMap().put(player.getName() + "_Fly", "已启用");
                    }
                } else {
                    sender.sendMessage(i18n("NoPermission"));
                }
            }
        }
        if (args.length == 1) {
            if (sender.hasPermission("MHDFTools.Fly.Give")) {
                String PlayerName = args[0];
                if (Bukkit.getPlayer(PlayerName) != null) {
                    Player player = Bukkit.getPlayer(PlayerName);
                    if (InFlyList.contains(Objects.requireNonNull(player).getName())) {
                        removeFly(player.getName());
                        InFlyList.remove(player.getName());
                        player.setAllowFlight(false);
                        player.sendMessage(i18n("Fly.Done", i18n("Fly.Disabled")));
                        sender.sendMessage(i18n("Fly.SetDone", player.getName(), i18n("Fly.Disabled")));
                        StringHasMap.getHasMap().put(player.getName() + "_Fly", null);
                    } else {
                        addFly(player.getName(), -999);
                        InFlyList.add(player.getName());
                        player.setAllowFlight(true);
                        player.sendMessage(i18n("Fly.Done", i18n("Fly.Enable")));
                        sender.sendMessage(i18n("Fly.SetDone", player.getName(), i18n("Fly.Enable")));
                        StringHasMap.getHasMap().put(player.getName() + "_Fly", "已启用");
                    }
                } else {
                    sender.sendMessage(i18n("PlayerNotOnline"));
                }
            } else {
                sender.sendMessage(i18n("NoPermission"));
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(i18n("Usage.Fly"));
            }
        }
        return false;
    }
}