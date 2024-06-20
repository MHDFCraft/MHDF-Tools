package cn.ChengZhiYa.MHDFTools.listeners.server.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.menu.MenuUtil.*;

public final class ClickCustomMenu implements Listener {
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        if (ifCustomMenu((Player) event.getWhoClicked(), event.getView().getTitle())) {
            if (event.getCurrentItem() != null) {
                event.setCancelled(true);
                if (getMenuFromItem(event.getCurrentItem()) != null && getItemNameFromItem(event.getCurrentItem()) != null) {
                    Player player = (Player) event.getWhoClicked();
                    String menuFileName = getMenuFromItem(event.getCurrentItem());
                    String item = getItemNameFromItem(event.getCurrentItem());

                    if (event.getClick() == ClickType.LEFT && event.getClick() == ClickType.RIGHT) {
                        List<String> DenyActionList = ifAllowClick(player, menuFileName, item, false);
                        if (DenyActionList.isEmpty()) {
                            runAction(player, menuFileName, getMenu(menuFileName).getStringList("menu.ItemList." + item + ".ClickAction"));
                        } else {
                            runAction(player, menuFileName, DenyActionList);
                        }
                    }
                    if (event.getClick() == ClickType.SHIFT_LEFT && event.getClick() == ClickType.SHIFT_RIGHT) {
                        List<String> DenyActionList = ifAllowClick(player, menuFileName, item, true);
                        if (DenyActionList.isEmpty()) {
                            runAction(player, menuFileName, getMenu(menuFileName).getStringList("menu.ItemList." + item + ".ShiftClickAction"));
                        } else {
                            runAction(player, menuFileName, DenyActionList);
                        }
                    }
                }
            }
        }
    }
}
