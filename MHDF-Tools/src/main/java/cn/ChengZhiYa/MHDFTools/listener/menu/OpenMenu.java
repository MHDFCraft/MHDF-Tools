package cn.ChengZhiYa.MHDFTools.listener.menu;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.util.menu.MenuUtil.*;

public final class OpenMenu implements Listener {
    @EventHandler
    public void onEvent(PlayerCommandPreprocessEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("MenuSettings.Enable")) {
            String Command = event.getMessage().replaceAll("/", "").split(" ")[0];
            for (String MenuFileName : getCustomMenuList()) {
                YamlConfiguration Menu = getMenu(MenuFileName);
                if (Menu.getString("Menu.Command") != null && Objects.equals(Menu.getString("Menu.Command"), Command)) {
                    event.setCancelled(true);
                    openMenu(event.getPlayer(), MenuFileName);
                    return;
                }
            }
        }
    }
}
