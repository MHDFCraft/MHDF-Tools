package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.*;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.Reload")) {
            if (IntHashMap.Get("ChatDelayTaskId") != null) {
                Bukkit.getScheduler().cancelTask(IntHashMap.Get("ChatDelayTaskId"));
                IntHashMap.Set("ChatDelayTaskId", null);
            }
            if (IntHashMap.Get("TimeMessageTaskId") != null) {
                Bukkit.getScheduler().cancelTask(IntHashMap.Get("TimeMessageTaskId"));
                IntHashMap.Set("TimeMessageTaskId", null);
            }
            if (IntHashMap.Get("ScoreboardTaskID") != null) {
                Bukkit.getScheduler().cancelTask(IntHashMap.Get("ScoreboardTaskID"));
                IntHashMap.Set("ScoreboardTaskID", null);
            }
            if (IntHashMap.Get("OpWhiteListTaskID") != null) {
                Bukkit.getScheduler().cancelTask(IntHashMap.Get("OpWhiteListTaskID"));
                IntHashMap.Set("OpWhiteListTaskID", null);
            }


            if (main.main.getConfig().getBoolean("ChatSettings.ChatDelayEnable")) {
                BukkitTask ChatDelayTime = new ChatDelay_Time().runTaskTimer(main.main, 0L, 20);
                IntHashMap.Set("ChatDelayTaskId", ChatDelayTime.getTaskId());
            }
            if (main.main.getConfig().getBoolean("TimeMessageSettings.Enable")) {
                BukkitTask TimeMessage = new TimeMessage_Task().runTaskTimer(main.main, 0L, main.main.getConfig().getInt("TimeMessageSettings.Delay") * 20L);
                IntHashMap.Set("TimeMessageTaskId", TimeMessage.getTaskId());
            }
            if (main.main.getConfig().getBoolean("ScoreboardSettings.Enable")) {
                BukkitTask Scoreboard = new Scoreboard_Task().runTaskTimer(main.main, 0L, 20L);
                IntHashMap.Set("ScoreboardTaskID", Scoreboard.getTaskId());
            }
            if (main.main.getConfig().getBoolean("VanillaOpWhitelist.Enable")) {
                BukkitTask WhiteListTask = new VanillaOpWhitelist_Task().runTaskTimer(main.main, 0L, 20L);
                IntHashMap.Set("OpWhiteListTaskID", WhiteListTask.getTaskId());
            }
            main.main.reloadConfig();
            sender.sendMessage(getLang("RelaodDone"));
        } else {
            sender.sendMessage(getLang("NoPermission"));
        }
        return false;
    }
}
