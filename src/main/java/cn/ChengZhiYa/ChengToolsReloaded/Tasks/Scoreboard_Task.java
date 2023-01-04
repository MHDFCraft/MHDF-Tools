package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class Scoreboard_Task extends BukkitRunnable {
    main pluginmain;

    public Scoreboard_Task(main main) {
        this.pluginmain = main;
    }

    public void run() {
        if (main.main.getConfig().getBoolean("ScoreboardSettings.Enable")) {
            if (Bukkit.getOnlinePlayers().size() == 0) {
                return;
            }
            if (main.objective != null) {
                main.objective.unregister();
            }
            main.objective = main.scoreboard.registerNewObjective(ChatColor(Objects.requireNonNull(main.main.getConfig().getString("ScoreboardSettings.Title"))), "dummy");
            main.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (int i=0 ; i<main.main.getConfig().getStringList("ScoreboardSettings.Lines").size() ; i++) {
                    Score ScoreMessage;
                    if (main.main.getConfig().getStringList("ScoreboardSettings.Lines").get(i) == null || main.main.getConfig().getStringList("ScoreboardSettings.Lines").get(i).equals("")) {
                        StringBuilder NullMessage = new StringBuilder(" ");
                        for (int i1=0 ; i1<i ; i1++ ) {
                            NullMessage.append(" ");
                        }
                        ScoreMessage = main.objective.getScore(NullMessage.toString());
                    }else {
                        ScoreMessage = main.objective.getScore(ChatColor(PlaceholderAPI.setPlaceholders(player,main.main.getConfig().getStringList("ScoreboardSettings.Lines").get(i))));
                    }
                    ScoreMessage.setScore(main.main.getConfig().getStringList("ScoreboardSettings.Lines").size()-i);
                }
                player.setScoreboard(main.scoreboard);
            }
        }
    }
}