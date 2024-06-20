<<<<<<<< HEAD:MHDF-Tools/src/main/java/cn/ChengZhiYa/MHDFTools/listeners/server/menu/OpenMenu.java
package cn.ChengZhiYa.MHDFTools.listeners.server.menu;
========
package cn.ChengZhiYa.MHDFTools.listener.menu;
>>>>>>>> master:MHDF-Tools/src/main/java/cn/ChengZhiYa/MHDFTools/listener/menu/OpenMenu.java

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.menu.MenuUtil.*;

public final class OpenMenu implements Listener {
    @EventHandler
    public void onEvent(PlayerCommandPreprocessEvent event) {
<<<<<<<< HEAD:MHDF-Tools/src/main/java/cn/ChengZhiYa/MHDFTools/listeners/server/menu/OpenMenu.java
        String Command = event.getMessage().replaceAll("/", "").split(" ")[0];

        if (MHDFTools.instance.getConfig().getBoolean("MenuEnable")) {

========
        if (MHDFTools.instance.getConfig().getBoolean("MenuSettings.Enable")) {
            String Command = event.getMessage().replaceAll("/", "").split(" ")[0];
>>>>>>>> master:MHDF-Tools/src/main/java/cn/ChengZhiYa/MHDFTools/listener/menu/OpenMenu.java
            for (String MenuFileName : getCustomMenuList()) {

                YamlConfiguration Menu = getMenu(MenuFileName);

              if (Menu.getString("menu.Command") != null) return;

              if (Objects.equals(Menu.getString("menu.Command"), Command)) {
                    event.setCancelled(true);
                    openMenu(event.getPlayer(), MenuFileName);
                    return;
                }
            }
        }
    }
}
