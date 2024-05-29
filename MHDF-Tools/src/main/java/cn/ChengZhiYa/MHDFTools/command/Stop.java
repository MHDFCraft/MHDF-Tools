package cn.ChengZhiYa.MHDFTools.command;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.util.Util.PAPIChatColor;
import static cn.ChengZhiYa.MHDFTools.util.Util.i18n;

public final class Stop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("MHDFTools.Command.Stop")) {
            if (Bukkit.getOnlinePlayers().isEmpty()) {
                Bukkit.shutdown();
                return false;
            }
            if (args.length >= 1) {
                StringBuilder StopMessage = new StringBuilder();
                for (String arg : args) {
                    StopMessage.append(" ").append(arg);
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.kickPlayer(PAPIChatColor(player, MHDFTools.instance.getConfig().getString("SuperStopSettings.ServerName") + "\n" + StopMessage));
                }
                Bukkit.shutdown();
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.kickPlayer(PAPIChatColor(player, MHDFTools.instance.getConfig().getString("SuperStopSettings.ServerName") + "\n" + Objects.requireNonNull(MHDFTools.instance.getConfig().getString("SuperStopSettings.DefaultStopMessage"))));
                }
                Bukkit.shutdown();
            }
        } else {
            sender.sendMessage(i18n("NoPermission"));
        }
        return false;
    }
}
