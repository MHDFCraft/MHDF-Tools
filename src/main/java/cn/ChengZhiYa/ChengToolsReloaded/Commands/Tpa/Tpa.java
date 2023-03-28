package cn.ChengZhiYa.ChengToolsReloaded.Commands.Tpa;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
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

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class Tpa implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                String TpaPlayerName = args[0];
                if (Bukkit.getPlayer(TpaPlayerName) == null) {
                    sender.sendMessage(getLang("PlayerNotOnline"));
                    return false;
                }

                Player player = (Player) sender;
                Player TpaPlayer = Bukkit.getPlayer(TpaPlayerName);
                String PlayerName = player.getName();
                if (StringHashMap.Get(PlayerName + "_Temp_TpaPlayerName") != null) {
                    player.sendMessage(getLang("Tpa.RepectSend"));
                    return false;
                }
                player.sendMessage(getLang("Tpa.SendDone"));
                TextComponent Message = new TextComponent();
                TextComponent AccentMessage = new TextComponent(getLang("Tpa.AccentMessage"));
                AccentMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor("   "))));
                AccentMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaaccept " + PlayerName));
                TextComponent DefuseMessage = new TextComponent(getLang("Tpa.DefuseMessage"));
                DefuseMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor("   "))));
                DefuseMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadefuse " + PlayerName));
                TextComponent Null = new TextComponent(getLang("Tpa.NullMessage"));
                Message.addExtra(AccentMessage);
                Message.addExtra(Null);
                Message.addExtra(DefuseMessage);
                Objects.requireNonNull(TpaPlayer).sendMessage(getLang("Tpa.MessageUp",player.getName()));
                Objects.requireNonNull(TpaPlayer).spigot().sendMessage(Message);
                TpaPlayer.sendMessage(getLang("Tpa.MessageDown"));
                StringHashMap.Set(PlayerName + "_Temp_TpaPlayerName", TpaPlayer.getName());
                BooleanHashMap.Set(PlayerName + "_Temp_Tpa", false);
                IntHashMap.Set(PlayerName + "_Temp_TpaTime", 0);
            } else {
                sender.sendMessage(getLang("Usage.Tpa"));
            }
        } else {
            sender.sendMessage(getLang("OnlyPlayer"));
        }
        return false;
    }
}
