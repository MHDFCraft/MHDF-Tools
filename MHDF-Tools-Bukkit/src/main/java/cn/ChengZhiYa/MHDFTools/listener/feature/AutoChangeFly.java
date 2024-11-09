package cn.ChengZhiYa.MHDFTools.listener.feature;

import cn.ChengZhiYa.MHDFTools.entity.data.FlyStatus;
import cn.ChengZhiYa.MHDFTools.listener.AbstractListener;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.feature.FlyUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class AutoChangeFly extends AbstractListener {
    public AutoChangeFly() {
        super(
                "flySettings.enable"
        );
    }

    /**
     * 加入服务器 自动启用飞行
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 不处理功能未开启的情况
        if (!ConfigUtil.getConfig().getBoolean("flySettings.autoEnable.joinServer")) {
            return;
        }

        if (allowChange(player)) {
            FlyUtil.enableFly(player);
        }
    }

    /**
     * 切换世界 自动启用飞行
     * 但目标世界在 自动关闭飞行世界列表 中则关闭飞行
     */
    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        // 不处理功能未开启的情况
        if (!ConfigUtil.getConfig().getBoolean("flySettings.autoEnable.chaneWorld")) {
            return;
        }

        if (allowChange(player)) {
            FlyUtil.enableFly(player);
        }
    }

    /**
     * 重生 自动启用飞行
     * 但目标世界在 自动关闭飞行世界列表 中则关闭飞行
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        // 不处理功能未开启的情况
        if (!ConfigUtil.getConfig().getBoolean("flySettings.autoEnable.respawn")) {
            return;
        }

        if (allowChange(player)) {
            FlyUtil.enableFly(player);
        }
    }

    /**
     * 受伤 自动关闭飞行
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            // 不处理功能未开启的情况
            if (!ConfigUtil.getConfig().getBoolean("flySettings.autoEnable.takeHealth")) {
                return;
            }

            if (allowChange(player)) {
                FlyUtil.disableFly(player);
            }
        }
    }

    /**
     * 检测是否可以切换飞行
     *
     * @param player 玩家实例
     */
    private boolean allowChange(Player player) {
        FlyStatus flyStatus = FlyUtil.getFlyStatusDao(player.getUniqueId());

        // 不处理没有开启飞行的玩家
        if (!flyStatus.isEnable()) {
            return false;
        }

        // 不处理游戏模式可以飞行的玩家
        if (FlyUtil.ifGamemodeAllowFlight(player)) {
            return false;
        }

        // 目标世界在 自动关闭飞行世界列表 当中
        if (ConfigUtil.getConfig().getStringList("flySettings.autoDisable.worldList").contains(player.getWorld().getName())) {
            FlyUtil.disableFly(player);
            return false;
        }
        return true;
    }
}
