package cn.ChengZhiYa.ChengToolsReloaded.Listeners.Player;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

public final class PlayerChatTabComplete implements Listener {
    @EventHandler
    public void On_Event(PlayerChatTabCompleteEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("BanCommandSettings.Enable")) {
            if (event.getPlayer().hasPermission("ChengTools.BanCommand.Bypass")) {
                if (!ChengToolsReloaded.instance.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                    for (String BanCommand : ChengToolsReloaded.instance.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                        event.getTabCompletions().remove(BanCommand);
                    }
                }
            } else {
                for (String BanCommand : ChengToolsReloaded.instance.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                    event.getTabCompletions().remove(BanCommand);
                }
            }
        }
    }
}
