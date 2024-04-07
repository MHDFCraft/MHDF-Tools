package cn.ChengZhiYa.MHDFTools.Listeners.Menu;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.Utils.MenuUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.Utils.MenuUtil.getCustomMenuList;
import static cn.ChengZhiYa.MHDFTools.Utils.MenuUtil.getMenu;

public final class OpenMenu implements Listener {
    @EventHandler
    public void onEvent(PlayerCommandPreprocessEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("MenuEnable")) {
            String Command = event.getMessage().replaceAll("/", "").split(" ")[0];
            for (String MenuList : getCustomMenuList()) {
                YamlConfiguration Menu = getMenu(MenuList);
                if (Menu.getString("Menu.Command") != null && Objects.equals(Menu.getString("Menu.Command"), Command)) {
                    event.setCancelled(true);
                    MenuUtil.OpenMenu(event.getPlayer(), MenuList);
                    return;
                }
            }
        }
    }
}
