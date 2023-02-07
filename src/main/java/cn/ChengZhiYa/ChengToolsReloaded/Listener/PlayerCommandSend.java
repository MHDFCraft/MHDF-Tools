package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class PlayerCommandSend implements Listener {
    @EventHandler

    public void On_Event(PlayerCommandSendEvent event) {
        if (main.main.getConfig().getBoolean("BanCommandSettings.Enable")) {
            if (event.getPlayer().hasPermission("ChengTools.BanCommand.Bypass")) {
                if (!main.main.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                    for (String BanCommand : main.main.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                        event.getCommands().remove(BanCommand);
                    }
                }
            } else {
                for (String BanCommand : main.main.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                    event.getCommands().remove(BanCommand);
                }
            }
        }
    }
}
