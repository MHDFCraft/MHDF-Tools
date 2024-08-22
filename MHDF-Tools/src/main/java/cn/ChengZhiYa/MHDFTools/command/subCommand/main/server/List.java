package cn.ChengZhiYa.MHDFTools.command.subCommand.main.server;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.*;

public final class List implements CommandExecutor {
    Runtime runtime = Runtime.getRuntime();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        for (String messageTemplate : PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("SuperListSettings.Message")) {
            String message = messageTemplate.replaceAll("%mem_used%", String.valueOf((runtime.totalMemory() - runtime.freeMemory()) / 1048576L))
                    .replaceAll("%mem_max%", String.valueOf(runtime.maxMemory() / 1048576L))
                    .replaceAll("%playerlist%", String.valueOf(getPlayerList(false)))
                    .replaceAll("%online%", String.valueOf(getPlayerList(false).size()))
                    .replaceAll("%max_online%", String.valueOf(Bukkit.getMaxPlayers()));

            if (canTPS()) {
                message = message.replaceAll("%TPS_1%", getTps(1))
                        .replaceAll("%TPS_5%", getTps(5))
                        .replaceAll("%TPS_15%", getTps(15));
            }

            sender.sendMessage(Placeholder(null, message));
        }
        return false;
    }
}
