package cn.ChengZhiYa.MHDFTools.listener.Menu;

import cn.ChengZhiYa.MHDFTools.util.menu.HomeMenuUtil;
import cn.ChengZhiYa.MHDFTools.util.message.LogUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.util.menu.HomeMenuUtil.getPlaceholder;
import static cn.ChengZhiYa.MHDFTools.util.menu.HomeMenuUtil.homeMenuFile;
import static cn.ChengZhiYa.MHDFTools.util.menu.MenuUtil.*;

public final class HomeMenu implements Listener {
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        if (event.getView().getTitle().contains(LogUtil.ChatColor(Objects.requireNonNull(getMenu(homeMenuFile).getString("Menu.Title")).split("\\{Page}")[0]))) {
            if (event.getCurrentItem() != null) {
                event.setCancelled(true);
                if (getMenuFromItem(event.getCurrentItem()) != null && getItemNameFromItem(event.getCurrentItem()) != null) {
                    Player player = (Player) event.getWhoClicked();
                    int page = Integer.parseInt(getPlaceholder(event.getView().getTitle(), getMenu(homeMenuFile).getString("Menu.Title"), "{Page}"));
                    String Item = getItemNameFromItem(event.getCurrentItem());
                    if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT) {
                        List<String> DenyActionList = ifAllowClick(player, homeMenuFile, Item, false);
                        if (DenyActionList.isEmpty()) {
                            HomeMenuUtil.runAction(player, homeMenuFile, page, event.getCurrentItem(), getMenu(homeMenuFile).getStringList("Menu.ItemList." + Item + ".ClickAction"));
                        } else {
                            HomeMenuUtil.runAction(player, homeMenuFile, page, event.getCurrentItem(), DenyActionList);
                        }
                    }
                    if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                        List<String> DenyActionList = ifAllowClick(player, homeMenuFile, Item, true);
                        if (DenyActionList.isEmpty()) {
                            HomeMenuUtil.runAction(player, homeMenuFile, page, event.getCurrentItem(), getMenu(homeMenuFile).getStringList("Menu.ItemList." + Item + ".ShiftClickAction"));
                        } else {
                            HomeMenuUtil.runAction(player, homeMenuFile, page, event.getCurrentItem(), DenyActionList);
                        }
                    }
                }
            }
        }
    }
}
