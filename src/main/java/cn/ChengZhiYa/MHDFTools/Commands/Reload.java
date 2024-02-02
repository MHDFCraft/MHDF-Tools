package cn.ChengZhiYa.MHDFTools.Commands;

import cn.ChengZhiYa.MHDFTools.HashMap.IntHasMap;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.Tasks.Scoreboard;
import cn.ChengZhiYa.MHDFTools.Tasks.TimeMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static cn.ChengZhiYa.MHDFTools.Utils.Util.LangFileData;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("MHDFTools.Command.Command.Reload")) {
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
            if (MHDFTools.instance.getConfig().getBoolean("TimeMessageSettings.Enable")) {
                BukkitTask TimeMessage = new TimeMessage().runTaskTimer(MHDFTools.instance, 0L, MHDFTools.instance.getConfig().getInt("TimeMessageSettings.Delay") * 20L);
                IntHasMap.getHasMap().put("TimeMessageTaskId", TimeMessage.getTaskId());
            }
            if (MHDFTools.instance.getConfig().getBoolean("ScoreboardSettings.Enable")) {
                BukkitTask Scoreboard = new Scoreboard().runTaskTimer(MHDFTools.instance, 0L, 20L);
                IntHasMap.getHasMap().put("ScoreboardTaskID", Scoreboard.getTaskId());
            }
            MHDFTools.instance.reloadConfig();
            File LangData = new File(MHDFTools.instance.getDataFolder() + "/lang.yml");
            LangFileData = YamlConfiguration.loadConfiguration(LangData);
            sender.sendMessage(i18n("RelaodDone"));
        } else {
            sender.sendMessage(i18n("NoPermission"));
        }
        return false;
    }
}
