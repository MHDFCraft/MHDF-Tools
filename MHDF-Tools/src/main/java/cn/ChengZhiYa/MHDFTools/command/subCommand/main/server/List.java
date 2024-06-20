package cn.ChengZhiYa.MHDFTools.command.subCommand.main.server;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.Util.*;

public final class List implements CommandExecutor {
    private final Runtime runtime = Runtime.getRuntime();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        for (String Message : MHDFTools.instance.getConfig().getStringList("SuperListSettings.Message")) {
            Message = Message.replaceAll("%mem_used%", String.valueOf((runtime.totalMemory() - runtime.freeMemory()) / 1048576L)).
                    replaceAll("%mem_max%", String.valueOf(runtime.maxMemory() / 1048576L)).
                    replaceAll("%PlayerList%", String.valueOf(getPlayerList(false))).
                    replaceAll("%Online%", String.valueOf(getPlayerList(false).size())).
                    replaceAll("%Max_Online%", String.valueOf(Bukkit.getMaxPlayers()));
            if (canTPS()) {
                Message = Message.replaceAll("%TPS_1%", getTps(1)).
                        replaceAll("%TPS_5%", getTps(5)).
                        replaceAll("%TPS_15%", getTps(15));
            }
            sender.sendMessage(PAPI(null, Message));
        }
        return false;
    }
}
