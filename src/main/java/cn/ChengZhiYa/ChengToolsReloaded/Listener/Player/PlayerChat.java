package cn.ChengZhiYa.ChengToolsReloaded.Listener.Player;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class PlayerChat implements Listener {
    @EventHandler
    public void On_Event(AsyncPlayerChatEvent event) {
        java.lang.String Message = event.getMessage();
        Player player = event.getPlayer();
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!getLogin(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
        java.lang.String Format = Objects.requireNonNull(ChengToolsReloaded.instance.getConfig().getString("ChatSettings.ChatFormat")).replaceAll("%Player%", player.getName());
        if (ChengToolsReloaded.instance.getConfig().getBoolean("ChatSettings.ChatFormatEnable")) {
            if (player.hasPermission("ChengTools.Chat.Color")) {
                event.setFormat(ChatColor(player, Format) + ChatColor(Message.replaceAll("%","%%")));
            } else {
                event.setFormat(ChatColor(player, Format) + Message.replaceAll("%","%%"));
            }
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("ChatSettings.ChatBlackWorkEnable")) {
            if (!player.hasPermission("ChengTools.Chat.BlackWorkBypass")) {
                for (java.lang.String BlackWork : ChengToolsReloaded.instance.getConfig().getStringList("ChatSettings.ChatBlackWork")) {
                    if (Message.contains(BlackWork)) {
                        event.setCancelled(true);
                        player.sendMessage(getLang("Chat.SendBlackWord"));
                        OpSendMessage(getLang("Chat.SendBlackWordOpMessage", player.getName(), BlackWork));
                        break;
                    }
                }
            }
        }

        if (IntHasMap.getHasMap().get(player.getName() + "_ChatDelayTime") != null) {
            player.sendMessage(ChatColor((getLang("Chat.SendBlackWordOpMessage", java.lang.String.valueOf(IntHasMap.getHasMap().get(player.getName() + "_ChatDelayTime"))))));
            event.setCancelled(true);
            return;
        }

        if (ChengToolsReloaded.instance.getConfig().getBoolean("ChatSettings.ChatDelayEnable")) {
            if (!player.hasPermission("ChengTools.Chat.DelayBypass")) {
                IntHasMap.getHasMap().put(player.getName() + "_ChatDelayTime", ChengToolsReloaded.instance.getConfig().getInt("ChatSettings.ChatDelay"));
            }
        }

        if (ChengToolsReloaded.instance.getConfig().getBoolean("ChatSettings.AntiRepeatChat")) {
            java.lang.String NoColorMessage = Message.toLowerCase()
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
                    player.sendMessage(getLang("Chat.RepectDelay"));
                    return;
                }
            }
            StringHasMap.getHasMap().put(player + "_ChatMessage", NoColorMessage);
        }
    }
}
