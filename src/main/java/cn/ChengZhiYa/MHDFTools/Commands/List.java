package cn.ChengZhiYa.MHDFTools.Commands;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static cn.ChengZhiYa.MHDFTools.Utils.Util.*;

public final class List implements CommandExecutor {
    private final Runtime runtime = Runtime.getRuntime();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        for (String Message : MHDFTools.instance.getConfig().getStringList("SuperListSettings.Message")) {
            java.util.List<String> PlayerList = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerList.add(player.getName());
            }
            Message = Message.replaceAll("%mem_used%", String.valueOf((runtime.totalMemory() - runtime.freeMemory()) / 1048576L)).
                    replaceAll("%mem_max%", String.valueOf(runtime.maxMemory() / 1048576L)).
                    replaceAll("%PlayerList%", String.valueOf(PlayerList)).
                    replaceAll("%Online%", String.valueOf(Bukkit.getOnlinePlayers().size())).
                    replaceAll("%Max_Online%", String.valueOf(Bukkit.getMaxPlayers()));
            if (ifSupportGetTps()) {
                Message = Message.replaceAll("%TPS_1%", getTps(1)).
                        replaceAll("%TPS_5%", getTps(5)).
                        replaceAll("%TPS_15%", getTps(15));
            }
            sender.sendMessage(ChatColor(null, Message));
        }
        return false;
    }
}
