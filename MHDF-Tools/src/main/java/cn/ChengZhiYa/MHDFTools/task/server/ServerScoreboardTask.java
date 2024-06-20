package cn.ChengZhiYa.MHDFTools.task.server;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.Util.Placeholder;

public final class ServerScoreboardTask extends BukkitRunnable {

    @Override
    public void run() {
        if (!MHDFTools.instance.getConfig().getBoolean("ScoreboardSettings.Enable")) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            updateScoreboard(player);
        }
    }

    private void updateScoreboard(Player player) {
        try {
            String playerName = player.getName();
            String scoreboardKey = playerName + "_Scoreboard";
            MapUtil.getObjectiveHasMap().remove(playerName + "_Objective");

            if (!MapUtil.getScoreboardHasMap().containsKey(scoreboardKey)) {
                MapUtil.getScoreboardHasMap().put(scoreboardKey, Bukkit.getScoreboardManager().getNewScoreboard());
            }

            Objective objective = MapUtil.getScoreboardHasMap().get(scoreboardKey).registerNewObjective("sidebar", "dummy");
            objective.setDisplayName(Placeholder(player, Objects.requireNonNull(MHDFTools.instance.getConfig().getString("ScoreboardSettings.Title"))));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            MapUtil.getObjectiveHasMap().put(playerName + "_Objective", objective);

            updateScoreboardLines(player, objective);

            player.setScoreboard(MapUtil.getScoreboardHasMap().get(scoreboardKey));
        } catch (Exception ignored) {
        }
    }

    private void updateScoreboardLines(Player player, Objective objective) {
        int size = MHDFTools.instance.getConfig().getStringList("ScoreboardSettings.Lines").size();
        for (int i = 0; i < size; i++) {
            String line = MHDFTools.instance.getConfig().getStringList("ScoreboardSettings.Lines").get(i);

            if (line == null || line.isEmpty()) {
                line = " ";
            }

            String finalLine = Placeholder(player, line);

            Score score = objective.getScore(finalLine);
            score.setScore(size - i);
        }
    }
}