package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.ChatDelay_Time;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.TimeMessage_Task;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.Tpa_Detect;
import cn.ChengZhiYa.ChengToolsReloaded.Tasks.Tpa_Time;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class Reload_Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (IntHashMap.Get("TpaTimeTaskId") != null) {
            Bukkit.getScheduler().cancelTask(IntHashMap.Get("TpaTimeTaskId"));
            IntHashMap.Set("TpaTimeTaskId", null);
        }
        if (IntHashMap.Get("TpaDetectTaskId") != null) {
            Bukkit.getScheduler().cancelTask(IntHashMap.Get("TpaDetectTaskId"));
            IntHashMap.Set("TpaTimeTaskId", null);
        }
        if (IntHashMap.Get("ChatDelayTaskId") != null) {
            Bukkit.getScheduler().cancelTask(IntHashMap.Get("ChatDelayTaskId"));
            IntHashMap.Set("ChatDelayTaskId", null);
        }
        if (IntHashMap.Get("TimeMessageTaskId") != null) {
            Bukkit.getScheduler().cancelTask(IntHashMap.Get("TimeMessageTaskId"));
            IntHashMap.Set("TimeMessageTaskId", null);
        }

        if (main.main.getConfig().getBoolean("TpaSettings.Enable")) {
            BukkitTask TpaTime = new Tpa_Time(main.main).runTaskTimer(main.main, 0L, 20);
            IntHashMap.Set("TpaTimeTaskId", TpaTime.getTaskId());
            BukkitTask TpaDetect = new Tpa_Detect(main.main).runTaskTimer(main.main, 0L, 60 * 20);
            IntHashMap.Set("TpaDetectTaskId", TpaDetect.getTaskId());
        }
        if (main.main.getConfig().getBoolean("ChatDelayEnable")) {
            BukkitTask ChatDelayTime = new ChatDelay_Time(main.main).runTaskTimer(main.main, 0L, 20);
            IntHashMap.Set("ChatDelayTaskId", ChatDelayTime.getTaskId());
        }
        if (main.main.getConfig().getBoolean("TimeMessageSettings.Enable")) {
            BukkitTask TimeMessage = new TimeMessage_Task(main.main).runTaskTimer(main.main, 0L, main.main.getConfig().getInt("TimeMessageSettings.Delay") * 20L);
            IntHashMap.Set("TimeMessageTaskId", TimeMessage.getTaskId());
        }
        main.main.reloadConfig();
        sender.sendMessage(ChatColor("&a&l重载完成!"));
        return false;
    }
}
