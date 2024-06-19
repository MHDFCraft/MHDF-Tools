package cn.chengzhiya.mhdftools.listener.menu;

import cn.chengzhiya.mhdftools.MHDFTools;
import cn.chengzhiya.mhdftools.hashmap.StringHasMap;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static cn.chengzhiya.mhdftools.util.menu.MenuUtil.openMenu;

public final class MenuArgsCommand implements Listener {
    @EventHandler
    public void onEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (StringHasMap.getHasMap().get(player.getName() + "_ArgsRunCommmand") != null) {
            String[] ArgsRunCommmand = StringHasMap.getHasMap().get(player.getName() + "_ArgsRunCommmand").split("\\|");
            String[] Args = event.getMessage().split("\\|");
            for (int i = 0; i < Args.length; i++) {
                ArgsRunCommmand[3] = ArgsRunCommmand[3].replaceAll("%" + i, Args[i]);
            }
            if (ArgsRunCommmand[0].equals("player")) {
                Bukkit.getScheduler().runTask(MHDFTools.instance, () -> player.chat("/" + PlaceholderAPI.setPlaceholders(player, ArgsRunCommmand[3])));
            } else {
                Bukkit.getScheduler().runTask(MHDFTools.instance, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, ArgsRunCommmand[3])));
            }
            if (ArgsRunCommmand[2].equals("true")) {
                openMenu(player, ArgsRunCommmand[1]);
            }
            StringHasMap.getHasMap().remove(player.getName() + "_ArgsRunCommmand");
        }
    }
}
