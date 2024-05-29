package cn.ChengZhiYa.MHDFTools.listener.Menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static cn.ChengZhiYa.MHDFTools.util.menu.MenuUtil.*;

public final class ClickCustomMenu implements Listener {
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        if (ifCustomMenu((Player) event.getWhoClicked(), event.getView().getTitle())) {
            if (event.getCurrentItem() != null) {
                event.setCancelled(true);
                if (getMenuFromItem(event.getCurrentItem()) != null && getItemNameFromItem(event.getCurrentItem()) != null) {
                    Player player = (Player) event.getWhoClicked();
                    String meneFileName = getMenuFromItem(event.getCurrentItem());
                    String item = getItemNameFromItem(event.getCurrentItem());

                    if (event.getClick() == ClickType.LEFT && event.getClick() == ClickType.RIGHT) {
                        List<String> DenyActionList = ifAllowClick(player, meneFileName, item, false);
                        if (DenyActionList.isEmpty()) {
                            runAction(player, meneFileName, getMenu(meneFileName).getStringList("Menu.ItemList." + item + ".ClickAction"));
                        } else {
                            runAction(player, meneFileName, DenyActionList);
                        }
                    }
                    if (event.getClick() == ClickType.SHIFT_LEFT && event.getClick() == ClickType.SHIFT_RIGHT) {
                        List<String> DenyActionList = ifAllowClick(player, meneFileName, item, true);
                        if (DenyActionList.isEmpty()) {
                            runAction(player, meneFileName, getMenu(meneFileName).getStringList("Menu.ItemList." + item + ".ShiftClickAction"));
                        } else {
                            runAction(player, meneFileName, DenyActionList);
                        }
                    }
                }
            }
        }
    }
}
