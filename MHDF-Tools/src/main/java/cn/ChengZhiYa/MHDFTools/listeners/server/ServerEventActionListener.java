package cn.ChengZhiYa.MHDFTools.listeners.server;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.menu.MenuUtil.runAction;

public final class ServerEventActionListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("EventActionSettings.Enable")) {
            for (String action : getActionList("玩家加入服务器")) {
                for (String actions : MHDFTools.instance.getConfig().getStringList("EventActionSettings.EventList." + action + ".ActionList")) {
                    runAction(event.getPlayer(), null, actions.split("\\|"));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("EventActionSettings.Enable")) {
            for (String action : getActionList("玩家退出服务器")) {
                for (String actions : MHDFTools.instance.getConfig().getStringList("EventActionSettings.EventList." + action + ".ActionList")) {
                    runAction(event.getPlayer(), null, actions.split("\\|"));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("EventActionSettings.Enable")) {
            for (String action : getActionList("玩家切换世界")) {
                if (!MHDFTools.instance.getConfig().getStringList("EventActionSettings.EventList." + action + ".WorldList").isEmpty()) {
                    if (!MHDFTools.instance.getConfig().getStringList("EventActionSettings.EventList." + action + ".WorldList").contains(event.getPlayer().getWorld().getName())) {
                        continue;
                    }
                }
                for (String actions : MHDFTools.instance.getConfig().getStringList("EventActionSettings.EventList." + action + ".ActionList")) {
                    runAction(event.getPlayer(), null, actions.split("\\|"));
                }
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("EventActionSettings.Enable")) {
            for (String action : getActionList("玩家聊天")) {
                for (String actions : MHDFTools.instance.getConfig().getStringList("EventActionSettings.EventList." + action + ".ActionList")) {
                    runAction(event.getPlayer(), null, actions.split("\\|"));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("EventActionSettings.Enable")) {
            for (String action : getActionList("玩家死亡")) {
                for (String actions : MHDFTools.instance.getConfig().getStringList("EventActionSettings.EventList." + action + ".ActionList")) {
                    runAction(event.getPlayer(), null, actions.split("\\|"));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("EventActionSettings.Enable")) {
            for (String action : getActionList("玩家复活")) {
                for (String actions : MHDFTools.instance.getConfig().getStringList("EventActionSettings.EventList." + action + ".ActionList")) {
                    runAction(event.getPlayer(), null, actions.split("\\|"));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("EventActionSettings.Enable")) {
            for (String action : getActionList("玩家移动")) {
                for (String actions : MHDFTools.instance.getConfig().getStringList("EventActionSettings.EventList." + action + ".ActionList")) {
                    runAction(event.getPlayer(), null, actions.split("\\|"));
                }
            }
        }
    }

    private List<String> getActionList(String event) {
        List<String> list = new ArrayList<>();
        for (String action : Objects.requireNonNull(MHDFTools.instance.getConfig().getConfigurationSection("EventActionSettings.EventList")).getKeys(false)) {
            if (Objects.equals(MHDFTools.instance.getConfig().getString("EventActionSettings.EventList." + action + ".Event"), event)) {
                list.add(action);
            }
        }
        return list;
    }
}
