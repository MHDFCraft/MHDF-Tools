package cn.ChengZhiYa.MHDFTools.task.feature;

import cn.ChengZhiYa.MHDFTools.entity.data.FlyStatus;
import cn.ChengZhiYa.MHDFTools.task.AbstractTask;
import cn.ChengZhiYa.MHDFTools.util.ActionUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import cn.ChengZhiYa.MHDFTools.util.feature.FlyUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static cn.ChengZhiYa.MHDFTools.util.config.SoundUtil.getSound;

@SuppressWarnings("unused")
public final class FlyTime extends AbstractTask {
    public FlyTime() {
        super(
                "flySettings.enable",
                20L
        );
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getAllowFlight() && !FlyUtil.ifGamemodeAllowFlight(player)) {
                FlyStatus flyStatus = FlyUtil.getFlyStatusDao(player.getUniqueId());
                // 不处理永久飞行的玩家
                if (!player.hasPermission("mhdftools.commands.fly.infinite")) {
                    // 发送迫题提示
                    String title = LangUtil.i18n("commands.fly.fallMessage." + flyStatus.getTime());
                    if (!title.isEmpty()) {
                        ActionUtil.sendTitle(player, title);
                    }

                    // 播放音效
                    String sound = getSound("flyFall." + flyStatus.getTime());
                    if (!sound.isEmpty()) {
                        ActionUtil.playSound(player, sound);
                    }

                    // 减少飞行时长
                    FlyUtil.setFlyTime(player, flyStatus.getTime() - 1);

                    if (flyStatus.getTime() <= 0) {
                        FlyUtil.disableFly(player);
                        FlyUtil.setFlyTime(player, 0L);
                    }
                }
            }
        }
    }
}
