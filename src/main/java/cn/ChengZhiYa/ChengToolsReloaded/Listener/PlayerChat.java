package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class PlayerChat implements Listener {
    @EventHandler
    public void On_Event(AsyncPlayerChatEvent event) {
        String Message = event.getMessage();
        Player player = event.getPlayer();
        if (main.main.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!getLogin(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
        String Format = Objects.requireNonNull(main.main.getConfig().getString("ChatSettings.ChatFormat")).replaceAll("%Player%", player.getName());
        if (main.main.getConfig().getBoolean("ChatSettings.ChatFormatEnable")) {
            if (player.hasPermission("ChengTools.Chat.Color")) {
                event.setFormat(ChatColor(player, Format) + ChatColor(Message));
            } else {
                event.setFormat(ChatColor(player, Format) + Message);
            }
        }
        if (main.main.getConfig().getBoolean("ChatSettings.ChatBlackWorkEnable")) {
            if (!player.hasPermission("ChengTools.Chat.BlackWorkBypass")) {
                for (String BlackWork : main.main.getConfig().getStringList("ChatSettings.ChatBlackWork")) {
                    if (Message.contains(BlackWork)) {
                        event.setCancelled(true);
                        player.sendMessage(getLang("Chat.SendBlackWord"));
                        OpSendMessage(getLang("Chat.SendBlackWordOpMessage", player.getName(), BlackWork));
                        break;
                    }
                }
            }
        }

        if (IntHashMap.Get(player.getName() + "_ChatDelayTime") != null) {
            player.sendMessage(ChatColor((getLang("Chat.SendBlackWordOpMessage", String.valueOf(IntHashMap.Get(player.getName() + "_ChatDelayTime"))))));
            event.setCancelled(true);
            return;
        }

        if (main.main.getConfig().getBoolean("ChatSettings.ChatDelayEnable")) {
            if (!player.hasPermission("ChengTools.Chat.DelayBypass")) {
                IntHashMap.Set(player.getName() + "_ChatDelayTime", main.main.getConfig().getInt("ChatSettings.ChatDelay"));
            }
        }

        if (main.main.getConfig().getBoolean("ChatSettings.AntiRepeatChat")) {
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
            if (StringHashMap.Get(player + "_ChatMessage") != null) {
                if (NoColorMessage.equals(StringHashMap.Get(player + "_ChatMessage"))) {
                    event.setCancelled(true);
                    player.sendMessage(getLang("Chat.RepectDelay"));
                    return;
                }
            }
            StringHashMap.Set(player + "_ChatMessage", NoColorMessage);
        }
    }
}
