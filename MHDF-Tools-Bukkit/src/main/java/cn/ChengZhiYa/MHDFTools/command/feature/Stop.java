package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.message.ColorUtil;
import com.github.Anon8281.universalScheduler.UniversalRunnable;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.util.config.LangUtil.i18n;

@SuppressWarnings("unused")
public final class Stop extends AbstractCommand {
    public Stop() {
        super(
                "stopSettings.enable",
                "更好的关服",
                "mhdftools.commands.stop",
                false,
                ConfigUtil.getConfig().getStringList("stopSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        int time = args.length >= 1 ? Integer.parseInt(args[0]) :
                ConfigUtil.getConfig().getInt("stopSettings.defaultCountdown");
        String message = args.length >= 2 ? ColorUtil.color(args[1]) :
                i18n("commands.stop.defaultMessage");

        new UniversalRunnable() {
            private int countdown = time;

            @Override
            public void run() {
                if (countdown <= 0) {
                    stopServer(message);
                } else {
                    Bukkit.broadcastMessage(
                            i18n("commands.stop.countdownMessage")
                                    .replace("{countdown}", String.valueOf(countdown))
                    );
                }
                countdown--;
            }
        }.runTaskTimerAsynchronously(Main.instance, 0L, 20L);
    }

    /**
     * 关闭服务器
     */
    private void stopServer(String message) {
        Bukkit.savePlayers();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(
                    i18n("commands.stop.kickMessage")
                            .replace("{message}", message)
            );
        }

        Bukkit.shutdown();
    }
}
