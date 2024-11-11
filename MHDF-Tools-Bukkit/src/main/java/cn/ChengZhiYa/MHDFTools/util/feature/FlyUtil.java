package cn.ChengZhiYa.MHDFTools.util.feature;

import cn.ChengZhiYa.MHDFTools.Main;
import cn.ChengZhiYa.MHDFTools.entity.data.FlyStatus;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public final class FlyUtil {
    private static final Dao<FlyStatus, UUID> flyStatusDao;

    static {
        try {
            flyStatusDao = DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), FlyStatus.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定玩家的飞行状态数据
     *
     * @param uuid 玩家UUID
     * @return 飞行状态实例
     */
    public static FlyStatus getFlyStatusDao(UUID uuid) {
        try {
            FlyStatus flyStatus = flyStatusDao.queryForId(uuid);
            if (flyStatus == null) {
                flyStatus = new FlyStatus();
                flyStatus.setPlayer(uuid);
            }
            return flyStatus;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置指定玩家的飞行状态数据
     *
     * @param flyStatus 飞行状态实例
     */
    public static void setFlyStatusDao(FlyStatus flyStatus) {
        try {
            flyStatusDao.createOrUpdate(flyStatus);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除指定玩家的飞行状态数据
     *
     * @param uuid 玩家UUID
     */
    public static void deleteFlyStatusDao(UUID uuid) {
        try {
            flyStatusDao.deleteById(uuid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 开启指定玩家的飞行模式
     *
     * @param player 玩家实例
     */
    public static void enableFly(Player player) {
        FlyStatus flyStatus = getFlyStatusDao(player.getUniqueId());
        flyStatus.setEnable(true);
        setFlyStatusDao(flyStatus);

        player.setAllowFlight(true);
    }

    /**
     * 关闭指定玩家的飞行模式
     *
     * @param player 玩家实例
     */
    public static void disableFly(Player player) {
        FlyStatus flyStatus = getFlyStatusDao(player.getUniqueId());
        flyStatus.setEnable(false);
        setFlyStatusDao(flyStatus);
        player.setAllowFlight(false);
    }

    /**
     * 设置指定玩家的限时飞行时间
     *
     * @param player 玩家实例
     * @param time   飞行时间
     */
    public static void setFlyTime(Player player, Long time) {
        FlyStatus flyStatus = getFlyStatusDao(player.getUniqueId());
        flyStatus.setTime(time);
        setFlyStatusDao(flyStatus);
    }

    /**
     * 给指定玩家发送切换飞行的提示
     *
     * @param sender 接收信息的玩家
     * @param player 开启飞行的玩家
     * @param enable 是否开启飞行
     */
    public static void sendChangeFlyMessage(CommandSender sender, Player player, boolean enable) {
        sender.sendMessage(
                LangUtil.i18n("commands.fly.message")
                        .replace("{player}", player.getName())
                        .replace("{change}",
                                enable ? LangUtil.i18n("enable") : LangUtil.i18n("disable")
                        )
        );
    }

    /**
     * 检测指定玩家的游戏模式是否可以飞行
     *
     * @param player 玩家实例
     */
    public static boolean isAllowedFlyingGameMode(Player player) {
        return player.getGameMode() == GameMode.CREATIVE ||
                player.getGameMode() == GameMode.SPECTATOR;
    }
}
