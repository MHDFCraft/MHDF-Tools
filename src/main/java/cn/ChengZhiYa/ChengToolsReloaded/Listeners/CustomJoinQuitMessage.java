package cn.ChengZhiYa.ChengToolsReloaded.Listeners;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.i18n;

public final class CustomJoinQuitMessage implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
            Player player = event.getPlayer();
            if (StringHasMap.getHasMap().get(player.getName() + "_LoginIP") != null) {
                if (StringHasMap.getHasMap().get(player.getName() + "_LoginIP").equals(Objects.requireNonNull(player.getAddress()).getHostName())) {
                    StringHasMap.getHasMap().put(player.getName() + "_Login", "t");
                    player.sendMessage(i18n("Login.AutoLogin"));
                }
            }
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            Player player = event.getPlayer();
            StringHasMap.getHasMap().put(player.getName() + "_Login", null);
        }
    }
}
