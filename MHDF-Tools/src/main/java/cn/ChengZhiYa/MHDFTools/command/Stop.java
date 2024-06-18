package cn.ChengZhiYa.MHDFTools.command;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.util.Util.PAPI;

public final class Stop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            String Message = Objects.requireNonNull(MHDFTools.instance.getConfig().getString("SuperStopSettings.DefaultStopMessage"));

            if (args.length >= 1) {
                StringBuilder StopMessage = new StringBuilder();
                for (String arg : args) {
                    StopMessage.append(" ").append(arg);
                }
                Message = StopMessage.toString();
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.kickPlayer(PAPI(player, Objects.requireNonNull(MHDFTools.instance.getConfig().getString("SuperStopSettings.StopMessageFormat")).replaceAll("\\{Message}", Message)));
            }
        }

        Bukkit.shutdown();
        return false;
    }
}
