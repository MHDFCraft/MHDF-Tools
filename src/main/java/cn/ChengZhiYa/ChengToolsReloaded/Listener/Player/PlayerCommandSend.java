package cn.ChengZhiYa.ChengToolsReloaded.Listener.Player;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

public final class PlayerCommandSend implements Listener {
    @EventHandler

    public void On_Event(PlayerCommandSendEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("BanCommandSettings.Enable")) {
            if (event.getPlayer().hasPermission("ChengTools.BanCommand.Bypass")) {
                if (!ChengToolsReloaded.instance.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                    for (String BanCommand : ChengToolsReloaded.instance.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                        event.getCommands().remove(BanCommand);
                    }
                }
            } else {
                for (String BanCommand : ChengToolsReloaded.instance.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                    event.getCommands().remove(BanCommand);
                }
            }
        }
    }
}
