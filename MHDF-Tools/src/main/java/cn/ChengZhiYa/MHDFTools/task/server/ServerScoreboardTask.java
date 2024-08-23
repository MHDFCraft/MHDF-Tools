package cn.ChengZhiYa.MHDFTools.task.server;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Objects;
import java.util.function.Consumer;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.Placeholder;

public final class ServerScoreboardTask implements Consumer<ScheduledTask> {
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    @Override
    public void accept(ScheduledTask task) {
        if (!plugin.getConfig().getBoolean("ScoreboardSettings.Enable")) {
            return;
        }

        Bukkit.getOnlinePlayers().forEach(this::updateScoreboard);
    }

    private void updateScoreboard(Player player) {
        try {
            String playerName = player.getName();
            String scoreboardKey = playerName + "_Scoreboard";
            MapUtil.getObjectiveHashMap().remove(playerName + "_Objective");

            if (!MapUtil.getScoreboardHashMap().containsKey(scoreboardKey)) {
                MapUtil.getScoreboardHashMap().put(scoreboardKey, Bukkit.getScoreboardManager().getNewScoreboard());
            }

            Objective objective = MapUtil.getScoreboardHashMap().get(scoreboardKey).registerNewObjective("sidebar", "dummy");
            objective.setDisplayName(Placeholder(player, Objects.requireNonNull(plugin.getConfig().getString("ScoreboardSettings.Title"))));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            MapUtil.getObjectiveHashMap().put(playerName + "_Objective", objective);

            updateScoreboardLines(player, objective);

            player.setScoreboard(MapUtil.getScoreboardHashMap().get(scoreboardKey));
        } catch (Exception ignored) {
        }
    }

    private void updateScoreboardLines(Player player, Objective objective) {
        int size = plugin.getConfig().getStringList("ScoreboardSettings.Lines").size();
        for (int i = 0; i < size; i++) {
            String line = plugin.getConfig().getStringList("ScoreboardSettings.Lines").get(i);

            if (line == null || line.isEmpty()) {
                line = " ";
            }

            String finalLine = Placeholder(player, line);

            Score score = objective.getScore(finalLine);
            score.setScore(size - i);
        }
    }
}