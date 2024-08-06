package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.getChatColor;

public final class PlayerChatColorListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("ChatColorSettings.Enable")) {
            if (!event.isCancelled()) {
                Player player = event.getPlayer();
                String chatColor = getChatColor(player);
                String message = event.getMessage();
                message = chatColor + message.replaceAll("§r", "§r" + chatColor);
                event.setMessage(message);
            }
        }
    }
}
