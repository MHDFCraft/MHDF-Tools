package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class TpaHere implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String TpaHerePlayerName = args[0];
                String PlayerName = player.getName();
                if (Bukkit.getPlayer(TpaHerePlayerName) == null) {
                    sender.sendMessage(ChatColor("&c这个玩家不存在"));
                    return false;
                }
                Player TpaHerePlayer = Bukkit.getPlayer(TpaHerePlayerName);
                if (StringHashMap.Get(PlayerName + "_TempTpaherePlayerName") != null) {
                    player.sendMessage(ChatColor("&c请不要重复发送Tpahere！"));
                    return false;
                }
                player.sendMessage(ChatColor("&a传送请求已发送"));
                TextComponent Message = new TextComponent();
                TextComponent AccentMessage = new TextComponent(ChatColor("&7[&a&l同意&7]"));
                AccentMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor("&a接受" + PlayerName + "的传送请求"))));
                AccentMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpahereccept " + PlayerName));
                TextComponent RefuseMessage = new TextComponent(ChatColor("&7[&c&l拒绝&7]"));
                RefuseMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor("&a拒绝" + PlayerName + "的传送请求"))));
                RefuseMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaheredefuse " + PlayerName));
                TextComponent Null = new TextComponent("   ");
                Message.addExtra(AccentMessage);
                Message.addExtra(Null);
                Message.addExtra(RefuseMessage);
                Objects.requireNonNull(TpaHerePlayer).sendMessage(multi.ChatColor("&7--------------&9Tpagere系统&7--------------\n&a玩家:" + PlayerName + "想来让你去他那里看看"));
                Objects.requireNonNull(TpaHerePlayer).spigot().sendMessage(Message);
                TpaHerePlayer.sendMessage(ChatColor("&7--------------&9Tpahere系统&7--------------"));
                StringHashMap.Set(PlayerName + "_Temp_TpaherePlayerName", TpaHerePlayer.getName());
                BooleanHashMap.Set(PlayerName + "_Temp_Tpahere", false);
                IntHashMap.Set(PlayerName + "_Temp_TpaTimehere", 0);
            }else {
                sender.sendMessage(ChatColor("&c用法错误,用法:/tpahere <玩家ID>"));
            }
        }else {
            sender.sendMessage(ChatColor("&c这个命令只有玩家才能执行"));
        }
        return false;
    }
}
