package cn.ChengZhiYa.MHDFTools.Listeners.Menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.Utils.MenuUtil.*;
import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;

public final class ClickCustomMenu implements Listener {
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        if (getMenuHashMap().get(event.getView().getTitle()) != null) {
            String MenuFileName = getMenuHashMap().get(event.getView().getTitle());
            if (event.getView().getTitle().contains(ChatColor(Objects.requireNonNull(getMenu(MenuFileName).getString("Menu.Title"))))) {
                if (event.getCurrentItem() != null) {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    String Item = getMenuItemHashMap().get(event.getView().getTitle() + "|" + event.getCurrentItem().toString());
                    List<String> DenyActionList = AllowClickAction(player, getMenu(MenuFileName), Item);
                    if (DenyActionList.isEmpty()) {
                        RunAction(MenuFileName, player, getMenu(MenuFileName).getStringList("Menu.ItemList." + Item + ".ClickAction"), null, null);
                    } else {
                        RunAction(MenuFileName, player, DenyActionList, null, null);
                    }
                }
            }
        }
    }
}
