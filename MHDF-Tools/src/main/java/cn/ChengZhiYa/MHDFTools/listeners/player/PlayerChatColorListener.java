package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.database.ChatColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class PlayerChatColorListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("ChatColorSettings.Enable")) {
            if (!event.isCancelled()) {
                Player player = event.getPlayer();
                String chatColor = ChatColorUtil.getPlayerChatColor(player);
                String message = event.getMessage();
                message = chatColor + message.replaceAll("§r", "§r" + chatColor);
                event.setMessage(message);
            }
        }
    }
}
