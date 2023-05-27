package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.ChatDelay;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.Scoreboard;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.TimeMessage;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.VanillaOpWhitelist;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("ChengTools.Reload")) {
            if (IntHasMap.getHasMap().get("ChatDelayTaskId") != null) {
                Bukkit.getScheduler().cancelTask(IntHasMap.getHasMap().get("ChatDelayTaskId"));
                IntHasMap.getHasMap().put("ChatDelayTaskId", null);
            }
            if (IntHasMap.getHasMap().get("TimeMessageTaskId") != null) {
                Bukkit.getScheduler().cancelTask(IntHasMap.getHasMap().get("TimeMessageTaskId"));
                IntHasMap.getHasMap().put("TimeMessageTaskId", null);
            }
            if (IntHasMap.getHasMap().get("ScoreboardTaskID") != null) {
                Bukkit.getScheduler().cancelTask(IntHasMap.getHasMap().get("ScoreboardTaskID"));
                IntHasMap.getHasMap().put("ScoreboardTaskID", null);
            }
            if (IntHasMap.getHasMap().get("OpWhiteListTaskID") != null) {
                Bukkit.getScheduler().cancelTask(IntHasMap.getHasMap().get("OpWhiteListTaskID"));
                IntHasMap.getHasMap().put("OpWhiteListTaskID", null);
            }


            if (ChengToolsReloaded.instance.getConfig().getBoolean("ChatSettings.ChatDelayEnable")) {
                BukkitTask ChatDelayTime = new ChatDelay().runTaskTimer(ChengToolsReloaded.instance, 0L, 20);
                IntHasMap.getHasMap().put("ChatDelayTaskId", ChatDelayTime.getTaskId());
            }
            if (ChengToolsReloaded.instance.getConfig().getBoolean("TimeMessageSettings.Enable")) {
                BukkitTask TimeMessage = new TimeMessage().runTaskTimer(ChengToolsReloaded.instance, 0L, ChengToolsReloaded.instance.getConfig().getInt("TimeMessageSettings.Delay") * 20L);
                IntHasMap.getHasMap().put("TimeMessageTaskId", TimeMessage.getTaskId());
            }
            if (ChengToolsReloaded.instance.getConfig().getBoolean("ScoreboardSettings.Enable")) {
                BukkitTask Scoreboard = new Scoreboard().runTaskTimer(ChengToolsReloaded.instance, 0L, 20L);
                IntHasMap.getHasMap().put("ScoreboardTaskID", Scoreboard.getTaskId());
            }
            if (ChengToolsReloaded.instance.getConfig().getBoolean("VanillaOpWhitelist.Enable")) {
                BukkitTask WhiteListTask = new VanillaOpWhitelist().runTaskTimer(ChengToolsReloaded.instance, 0L, 20L);
                IntHasMap.getHasMap().put("OpWhiteListTaskID", WhiteListTask.getTaskId());
            }
            ChengToolsReloaded.instance.reloadConfig();
            sender.sendMessage(getLang("RelaodDone"));
        } else {
            sender.sendMessage(getLang("NoPermission"));
        }
        return false;
    }
}
