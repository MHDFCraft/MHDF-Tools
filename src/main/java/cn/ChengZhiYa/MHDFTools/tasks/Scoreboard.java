package cn.ChengZhiYa.MHDFTools.tasks;

import cn.ChengZhiYa.MHDFTools.map.ObjectiveHasMap;
import cn.ChengZhiYa.MHDFTools.map.ScoreboardHasMap;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.Util.PAPIChatColor;

public final class Scoreboard extends BukkitRunnable {

    public void run() {
        if (MHDFTools.instance.getConfig().getBoolean("ScoreboardSettings.Enable")) {
            if (!Bukkit.getOnlinePlayers().isEmpty()) {
                try {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (ObjectiveHasMap.getHasMap().get(player.getName() + "_Objective") != null) {
                            ObjectiveHasMap.getHasMap().get(player.getName() + "_Objective").unregister();
                        }
                        if (ScoreboardHasMap.getHasMap().get(player.getName() + "_Scoreboard") == null) {
                            ScoreboardHasMap.getHasMap().put(player.getName() + "_Scoreboard", Bukkit.getScoreboardManager().getNewScoreboard());
                        }
                        ObjectiveHasMap.getHasMap().put(player.getName() + "_Objective", ScoreboardHasMap.getHasMap().get(player.getName() + "_Scoreboard").registerNewObjective(PAPIChatColor(player, Objects.requireNonNull(MHDFTools.instance.getConfig().getString("ScoreboardSettings.Title"))), "dummy"));
                        ObjectiveHasMap.getHasMap().get(player.getName() + "_Objective").setDisplaySlot(DisplaySlot.SIDEBAR);
                        for (int i = 0; i < MHDFTools.instance.getConfig().getStringList("ScoreboardSettings.Lines").size(); i++) {
                            Score ScoreMessage;
                            if (MHDFTools.instance.getConfig().getStringList("ScoreboardSettings.Lines").get(i) == null || MHDFTools.instance.getConfig().getStringList("ScoreboardSettings.Lines").get(i).isEmpty()) {
                                StringBuilder NullMessage = new StringBuilder(" ");
                                for (int i1 = 0; i1 < i; i1++) {
                                    NullMessage.append(" ");
                                }
                                ScoreMessage = ObjectiveHasMap.getHasMap().get(player.getName() + "_Objective").getScore(NullMessage.toString());
                            } else {
                                ScoreMessage = ObjectiveHasMap.getHasMap().get(player.getName() + "_Objective").getScore(PAPIChatColor(player, MHDFTools.instance.getConfig().getStringList("ScoreboardSettings.Lines").get(i)));
                            }
                            ScoreMessage.setScore(MHDFTools.instance.getConfig().getStringList("ScoreboardSettings.Lines").size() - i);
                        }
                        player.setScoreboard(ScoreboardHasMap.getHasMap().get(player.getName() + "_Scoreboard"));
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }
}