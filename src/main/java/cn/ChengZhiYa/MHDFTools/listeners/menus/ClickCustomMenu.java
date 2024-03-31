package cn.ChengZhiYa.MHDFTools.listeners.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.MenuUtil.*;
import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;

public final class ClickCustomMenu implements Listener {
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        if (getMenuHashMap().get(event.getView().getTitle()) != null) {
            String MenuFileName = getMenuHashMap().get(event.getView().getTitle());
            if (event.getView().getTitle().contains(ChatColor(Objects.requireNonNull(getMenu(MenuFileName).getString("menus.Title"))))) {
                if (event.getCurrentItem() != null) {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    String Item = getMenuItemHashMap().get(event.getView().getTitle() + "|" + event.getCurrentItem().toString());

                    if (event.getClick() == ClickType.LEFT && event.getClick() == ClickType.RIGHT) {
                        List<String> DenyActionList = allowClickAction(player, getMenu(MenuFileName), Item, false);
                        if (DenyActionList.isEmpty()) {
                            RunAction(MenuFileName, player, getMenu(MenuFileName).getStringList("menus.ItemList." + Item + ".ClickAction"), null, event.getCurrentItem(), event.getView().getTitle());
                        } else {
                            RunAction(MenuFileName, player, DenyActionList, null, event.getCurrentItem(), event.getView().getTitle());
                        }
                    }
                    if (event.getClick() == ClickType.SHIFT_LEFT && event.getClick() == ClickType.SHIFT_RIGHT) {
                        List<String> DenyActionList = allowClickAction(player, getMenu(MenuFileName), Item, true);
                        if (DenyActionList.isEmpty()) {
                            RunAction(MenuFileName, player, getMenu(MenuFileName).getStringList("menus.ItemList." + Item + ".ShiftClickAction"), null, event.getCurrentItem(), event.getView().getTitle());
                        } else {
                            RunAction(MenuFileName, player, DenyActionList, null, event.getCurrentItem(), event.getView().getTitle());
                        }
                    }
                }
            }
        }
    }
}
