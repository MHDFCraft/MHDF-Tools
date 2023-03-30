package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class Ip implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.Ip")) {
            if (args.length == 1) {
                String PlayerName = args[0];
                if (Bukkit.getPlayer(PlayerName) == null) {
                    sender.sendMessage(getLang("PlayerNotOnline"));
                    return false;
                }
                Player player = Bukkit.getPlayer(PlayerName);
                String PlayerIp = Objects.requireNonNull(Objects.requireNonNull(player).getAddress()).getHostString();
                String PlayerLocation = getIpLocation(PlayerIp);
                sender.sendMessage(getLang("IPMessage", player.getName(), PlayerIp, PlayerLocation));
            } else {
                sender.sendMessage(getLang("Usage.Ip"));
            }
        } else {
            sender.sendMessage(getLang("NoPermission"));
        }
        return false;
    }
}
