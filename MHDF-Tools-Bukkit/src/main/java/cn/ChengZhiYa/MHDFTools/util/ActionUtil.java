package cn.ChengZhiYa.MHDFTools.util;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.util.message.ColorUtil;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public final class ActionUtil {
    /**
     * 给指定玩家播放指定音效ID
     *
     * @param player 玩家实例
     * @param sound  音效(<音效ID>|<音量>|<音调>)
     */
    public static void playSound(Player player, String sound) {
        FoliaScheduler.getAsyncScheduler().runNow(Main.instance, task -> {
            String[] data = sound.split("\\|");
            player.playSound(player, data[0], Float.parseFloat(data[1]), Float.parseFloat(data[2]));
        });
    }

    /**
     * 给指定玩家发送标题消息
     *
     * @param player 玩家实例
     * @param title  标题消息(<大标题>|<小标题>|<淡入时间>|<停留时间>|<淡出时间>)
     */
    public static void sendTitle(Player player, String title) {
        FoliaScheduler.getRegionScheduler().run(Main.instance, player.getLocation(), task -> {
            String[] data = title.split("\\|");
            player.sendTitle(
                    ColorUtil.color(player, data[0]),
                    ColorUtil.color(player, data[1]),
                    Integer.parseInt(data[2]),
                    Integer.parseInt(data[3]),
                    Integer.parseInt(data[4]));
        });
    }


    /**
     * 给指定玩家发送操作栏消息
     *
     * @param player  玩家实例
     * @param message 消息
     */
    public static void sendActionBar(Player player, String message) {
        FoliaScheduler.getAsyncScheduler().runNow(Main.instance, task ->
                Main.adventure.player(player).sendActionBar(Component.text(ColorUtil.color(message))));
    }

    /**
     * 给指定玩家发送BOSS血条
     *
     * @param player  玩家实例
     * @param bossBar BOSS血条实例
     */
    public static void sendBossbar(Player player, BossBar bossBar) {
        FoliaScheduler.getAsyncScheduler().runNow(Main.instance, task -> Main.adventure.player(player).showBossBar(bossBar));
    }

    /**
     * 给指定玩家发送BOSS血条
     *
     * @param player  玩家实例
     * @param bossBar BOSS血条实例
     * @param time    停留时间
     */
    public static void sendTimeBossbar(Player player, BossBar bossBar, Long time) {
        Main.adventure.player(player).showBossBar(bossBar);
        FoliaScheduler.getAsyncScheduler().runAtFixedRate(Main.instance, task ->
                Main.adventure.player(player).hideBossBar(bossBar), 1, time, TimeUnit.SECONDS);
    }
}