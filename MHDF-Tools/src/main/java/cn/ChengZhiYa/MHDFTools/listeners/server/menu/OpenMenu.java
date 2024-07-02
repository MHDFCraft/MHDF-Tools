package cn.ChengZhiYa.MHDFTools.listeners.server.menu;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.menu.MenuUtil.*;

public final class OpenMenu implements Listener {
    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("MenuSettings.Enable")) {
            String Command = event.getMessage().replaceAll("/", "").split(" ")[0];
            for (String MenuFileName : getCustomMenuList()) {

                YamlConfiguration Menu = getMenu(MenuFileName);

                if (Menu.getString("menu.Command") == null) return;

                if (Objects.equals(Menu.getString("menu.Command"), Command)) {
                    event.setCancelled(true);
                    openMenu(event.getPlayer(), MenuFileName);
                    return;
                }
            }
        }
    }
}
