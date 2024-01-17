package cn.ChengZhiYa.ChengToolsReloaded.Listeners.Menu;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Database.HomeUtil.getHome;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.MenuUtil.*;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.*;

public final class HomeMenu implements Listener {
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        if (event.getView().getTitle().contains(ChatColor(Objects.requireNonNull(getHomeMenu().getString("Menu.Title")).split("\\{Page\\}")[0]))) {
            if (event.getCurrentItem() != null) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                int Page = Integer.parseInt(getPlaceholder(event.getView().getTitle(), getHomeMenu().getString("Menu.Title"), "{Page}"));
                List<String> ClickActionList = getMenuHashMap().get(event.getCurrentItem().getType() + "|" + event.getCurrentItem().getItemMeta().getDisplayName());
                for (String ClickActions : ClickActionList) {
                    String[] ClickAction = ClickActions.split("\\|");
                    if (ClickAction[0].equals("[player]")) {
                        Bukkit.getScheduler().runTask(ChengToolsReloaded.instance, () -> player.chat("/" + PlaceholderAPI.setPlaceholders(player, ClickAction[1])));
                        continue;
                    }
                    if (ClickAction[0].equals("[console]")) {
                        Bukkit.getScheduler().runTask(ChengToolsReloaded.instance, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, ClickAction[1])));
                        continue;
                    }
                    if (ClickAction[0].equals("[playsound]")) {
                        try {
                            player.playSound(player, Sound.valueOf(ClickAction[1]), Float.parseFloat(ClickAction[2]), Float.parseFloat(ClickAction[3]));
                        } catch (Exception e) {
                            ColorLog(i18n("Message.AudioNoExists"));
                        }
                        continue;
                    }
                    if (ClickAction[0].equals("[message]")) {
                        player.sendMessage(ChatColor(PlaceholderAPI.setPlaceholders(player, ClickActions.replaceAll(ClickAction[0] + "\\|", "").replaceAll("\\|", "\n"))));
                        continue;
                    }
                    if (ClickAction[0].equals("[PageUp]")) {
                        OpenHomeMenu(player, Page - 1);
                        continue;
                    }
                    if (ClickAction[0].equals("[PageNext]")) {
                        OpenHomeMenu(player, Page + 1);
                        continue;
                    }
                    if (ClickAction[0].equals("[Home]")) {
                        String DisplayName = event.getCurrentItem().getItemMeta().getDisplayName();
                        String HomeName = getPlaceholder(DisplayName, getMenuItemLangHashMap().get(DisplayName), "{HomeName}");
                        player.teleport(Objects.requireNonNull(getHome(player.getName(), HomeName)));
                        continue;
                    }
                    ColorLog(i18n("Message.ActionNoExists"));
                }
            }
        }
    }
}
