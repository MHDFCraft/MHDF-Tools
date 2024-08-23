package cn.ChengZhiYa.MHDFTools.listeners.server;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.message.ColorLogs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.menu.MenuUtil.runAction;

public final class ServerEventActionListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        runActions(event.getPlayer(), "玩家加入服务器");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        runActions(event.getPlayer(), "玩家退出服务器");
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        runActions(event.getPlayer(), "玩家切换世界", event.getPlayer().getWorld().getName());
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        runActions(event.getPlayer(), "玩家聊天");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        runActions(event.getPlayer(), "玩家死亡");
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        runActions(event.getPlayer(), "玩家复活");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        runActions(event.getPlayer(), "玩家移动");
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        runActions(event.getPlayer(), "玩家传送");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        runActions(event.getPlayer(), "玩家破坏方块");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        runActions(event.getPlayer(), "玩家放置方块");
    }

    private void runActions(Player player, String event) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("EventActionSettings.Enable")) {
            for (String action : getActionList(event)) {
                for (String actions : PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("EventActionSettings.EventList." + action + ".ActionList")) {
                    ColorLogs.debug("事件操作", "事件名称: " + action, "操作: " + actions);
                    runAction(player, null, actions.split("\\|"));
                }
            }
        }
    }

    private void runActions(Player player, String event, String worldName) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("EventActionSettings.Enable")) {
            for (String action : getActionList(event)) {
                if (!PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("EventActionSettings.EventList." + action + ".WorldList").isEmpty()) {
                    if (!PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("EventActionSettings.EventList." + action + ".WorldList").contains(worldName)) {
                        ColorLogs.debug("事件未操作", "事件名称: " + action, "原因: 该世界不执行该操作", "世界名称: " + worldName);
                        continue;
                    }
                }
                for (String actions : PluginLoader.INSTANCE.getPlugin().getConfig().getStringList("EventActionSettings.EventList." + action + ".ActionList")) {
                    ColorLogs.debug("事件操作", "事件名称: " + action, "操作: " + actions);
                    runAction(player, null, actions.split("\\|"));
                }
            }
        }
    }

    private List<String> getActionList(String event) {
        List<String> list = new ArrayList<>();
        for (String action : Objects.requireNonNull(PluginLoader.INSTANCE.getPlugin().getConfig().getConfigurationSection("EventActionSettings.EventList")).getKeys(false)) {
            String type = PluginLoader.INSTANCE.getPlugin().getConfig().getString("EventActionSettings.EventList." + action + ".Event");
            ColorLogs.debug("事件操作类型", "事件名称: " + action, "事件类型:" + type);
            if (Objects.equals(type, event)) {
                list.add(action);
            }
        }
        ColorLogs.debug("对应事件类型操作列表", "事件类型: " + event, "操作列表:" + list);
        return list;
    }
}
