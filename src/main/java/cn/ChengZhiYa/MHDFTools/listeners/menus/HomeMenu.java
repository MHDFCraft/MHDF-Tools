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

public final class HomeMenu implements Listener {
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        if (event.getView().getTitle().contains(ChatColor(Objects.requireNonNull(getMenu("HomeMenu.yml").getString("menus.Title")).split("\\{Page\\}")[0]))) {
            if (event.getCurrentItem() != null) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                int Page = Integer.parseInt(getPlaceholder(event.getView().getTitle(), getMenu("HomeMenu.yml").getString("menus.Title"), "{Page}"));
                String Item = getMenuItemHashMap().get(event.getView().getTitle() + "|" + event.getCurrentItem().toString());
                if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT) {
                    List<String> DenyActionList = allowClickAction(player, getMenu("HomeMenu.yml"), Item, false);
                    if (DenyActionList.isEmpty()) {
                        RunAction("HomeMenu.yml", player, getMenu("HomeMenu.yml").getStringList("menus.ItemList." + Item + ".ClickAction"), Page, event.getCurrentItem(), event.getView().getTitle());
                    } else {
                        RunAction("HomeMenu.yml", player, DenyActionList, Page, event.getCurrentItem(), event.getView().getTitle());
                    }
                }
                if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                    List<String> DenyActionList = allowClickAction(player, getMenu("HomeMenu.yml"), Item, true);
                    if (DenyActionList.isEmpty()) {
                        RunAction("HomeMenu.yml", player, getMenu("HomeMenu.yml").getStringList("menus.ItemList." + Item + ".ShiftClickAction"), Page, event.getCurrentItem(), event.getView().getTitle());
                    } else {
                        RunAction("HomeMenu.yml", player, DenyActionList, Page, event.getCurrentItem(), event.getView().getTitle());
                    }
                }
            }
        }
    }
}
