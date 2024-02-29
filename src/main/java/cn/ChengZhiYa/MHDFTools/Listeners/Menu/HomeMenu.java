package cn.ChengZhiYa.MHDFTools.Listeners.Menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.Utils.MenuUtil.*;
import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;

public final class HomeMenu implements Listener {
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        if (event.getView().getTitle().contains(ChatColor(Objects.requireNonNull(getMenu("HomeMenu.yml").getString("Menu.Title")).split("\\{Page\\}")[0]))) {
            if (event.getCurrentItem() != null) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                int Page = Integer.parseInt(getPlaceholder(event.getView().getTitle(), getMenu("HomeMenu.yml").getString("Menu.Title"), "{Page}"));
                String Item = getMenuItemHashMap().get(event.getView().getTitle() + "|" + event.getCurrentItem().toString());
                List<String> DenyActionList = AllowClickAction(player, getMenu("HomeMenu.yml"), Item);
                if (DenyActionList.isEmpty()) {
                    RunAction("HomeMenu.yml", player, getMenu("HomeMenu.yml").getStringList("Menu.ItemList." + Item + ".ClickAction"), Page, event.getCurrentItem(), event.getView().getTitle());
                } else {
                    RunAction("HomeMenu.yml", player, DenyActionList, Page, event.getCurrentItem(), event.getView().getTitle());
                }
            }
        }
    }
}
