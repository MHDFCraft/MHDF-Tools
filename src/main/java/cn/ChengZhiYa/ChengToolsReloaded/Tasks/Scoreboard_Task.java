package cn.ChengZhiYa.ChengToolsReloaded.Tasks;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.ObjectiveHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.ScoreboardHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.main;
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
            try {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (ObjectiveHashMap.Get(player.getName() + "_Objective") != null) {
                        ObjectiveHashMap.Get(player.getName() + "_Objective").unregister();
                    }
                    if (ScoreboardHashMap.Get(player.getName() + "_Scoreboard") == null) {
                        ScoreboardHashMap.Set(player.getName() + "_Scoreboard", Bukkit.getScoreboardManager().getNewScoreboard());
                    }
                    ObjectiveHashMap.Set(player.getName() + "_Objective", ScoreboardHashMap.Get(player.getName() + "_Scoreboard").registerNewObjective(ChatColor(player, Objects.requireNonNull(main.main.getConfig().getString("ScoreboardSettings.Title"))), "dummy"));
                    ObjectiveHashMap.Get(player.getName() + "_Objective").setDisplaySlot(DisplaySlot.SIDEBAR);
                    for (int i = 0; i < main.main.getConfig().getStringList("ScoreboardSettings.Lines").size(); i++) {
                        Score ScoreMessage;
                        if (main.main.getConfig().getStringList("ScoreboardSettings.Lines").get(i) == null || main.main.getConfig().getStringList("ScoreboardSettings.Lines").get(i).equals("")) {
                            StringBuilder NullMessage = new StringBuilder(" ");
                            for (int i1 = 0; i1 < i; i1++) {
                                NullMessage.append(" ");
                            }
                            ScoreMessage = ObjectiveHashMap.Get(player.getName() + "_Objective").getScore(NullMessage.toString());
                        } else {
                            ScoreMessage = ObjectiveHashMap.Get(player.getName() + "_Objective").getScore(ChatColor(player, main.main.getConfig().getStringList("ScoreboardSettings.Lines").get(i)));
                        }
                        ScoreMessage.setScore(main.main.getConfig().getStringList("ScoreboardSettings.Lines").size() - i);
                    }
                    player.setScoreboard(ScoreboardHashMap.Get(player.getName() + "_Scoreboard"));
                }
            }catch (Exception ignored) {}
        }
    }
}