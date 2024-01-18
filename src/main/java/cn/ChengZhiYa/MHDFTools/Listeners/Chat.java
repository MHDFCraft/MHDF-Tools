package cn.ChengZhiYa.MHDFTools.Listeners;

import cn.ChengZhiYa.MHDFTools.HashMap.IntHasMap;
import cn.ChengZhiYa.MHDFTools.HashMap.StringHasMap;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.Utils.Util.*;

public final class Chat implements Listener {
    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        String Message = event.getMessage();
        Player player = event.getPlayer();

        String Format = Objects.requireNonNull(MHDFTools.instance.getConfig().getString("ChatSettings.ChatFormat")).replaceAll("%Player%", player.getName());
        if (MHDFTools.instance.getConfig().getBoolean("ChatSettings.ChatFormatEnable")) {
            if (player.hasPermission("MHDFTools.Chat.Color")) {
                event.setFormat(ChatColor(player, Format) + ChatColor(Message.replaceAll("%", "%%")));
            } else {
                event.setFormat(ChatColor(player, Format) + Message.replaceAll("%", "%%"));
            }
        }
        if (MHDFTools.instance.getConfig().getBoolean("ChatSettings.ChatBlackWorkEnable")) {
            if (!player.hasPermission("MHDFTools.Chat.BlackWorkBypass")) {
                for (String BlackWork : MHDFTools.instance.getConfig().getStringList("ChatSettings.ChatBlackWork")) {
                    if (Message.contains(BlackWork)) {
                        event.setCancelled(true);
                        player.sendMessage(i18n("Chat.SendBlackWord"));
                        OpSendMessage(i18n("Chat.SendBlackWordOpMessage", player.getName(), BlackWork));
                        break;
                    }
                }
            }
        }

        if (IntHasMap.getHasMap().get(player.getName() + "_ChatDelayTime") != null) {
            player.sendMessage(ChatColor((i18n("Chat.SendBlackWordOpMessage", String.valueOf(IntHasMap.getHasMap().get(player.getName() + "_ChatDelayTime"))))));
            event.setCancelled(true);
            return;
        }

        if (MHDFTools.instance.getConfig().getBoolean("ChatSettings.ChatDelayEnable")) {
            if (!player.hasPermission("MHDFTools.Chat.DelayBypass")) {
                IntHasMap.getHasMap().put(player.getName() + "_ChatDelayTime", MHDFTools.instance.getConfig().getInt("ChatSettings.ChatDelay"));
            }
        }

        if (MHDFTools.instance.getConfig().getBoolean("ChatSettings.AntiRepeatChat")) {
            String NoColorMessage = Message.toLowerCase()
                    .replaceAll("&1", "")
                    .replaceAll("&2", "")
                    .replaceAll("&3", "")
                    .replaceAll("&4", "")
                    .replaceAll("&5", "")
                    .replaceAll("&6", "")
                    .replaceAll("&7", "")
                    .replaceAll("&8", "")
                    .replaceAll("&9", "")
                    .replaceAll("&a", "")
                    .replaceAll("&e", "")
                    .replaceAll("&c", "")
                    .replaceAll("&d", "")
                    .replaceAll("&u", "")
                    .replaceAll("&l", "")
                    .replaceAll("&m", "")
                    .replaceAll("&n", "");
            if (StringHasMap.getHasMap().get(player + "_ChatMessage") != null) {
                if (NoColorMessage.equals(StringHasMap.getHasMap().get(player + "_ChatMessage"))) {
                    event.setCancelled(true);
                    player.sendMessage(i18n("Chat.RepectDelay"));
                    return;
                }
            }
            StringHasMap.getHasMap().put(player + "_ChatMessage", NoColorMessage);
        }
    }
}
