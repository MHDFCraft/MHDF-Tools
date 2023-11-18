package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.ChatColor;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLang;

public final class Stop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("ChengTools.Stop")) {
            if (Bukkit.getOnlinePlayers().size() == 0) {
                Bukkit.shutdown();
                return false;
            }
            if (args.length >= 1) {
                StringBuilder StopMessage = new StringBuilder();
                for (String arg : args) {
                    StopMessage.append(" ").append(arg);
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.kickPlayer(ChatColor(player, ChengToolsReloaded.instance.getConfig().getString("SuperStopSettings.ServerName") + "\n" + StopMessage));
                }
                Bukkit.shutdown();
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.kickPlayer(ChatColor(player, ChengToolsReloaded.instance.getConfig().getString("SuperStopSettings.ServerName") + "\n" + Objects.requireNonNull(ChengToolsReloaded.instance.getConfig().getString("SuperStopSettings.DefaultStopMessage"))));
                }
                Bukkit.shutdown();
            }
        } else {
            sender.sendMessage(getLang("NoPermission"));
        }
        return false;
    }
}
