package cn.ChengZhiYa.ChengToolsReloaded.Listeners;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.i18n;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.ifLogin;

public final class LoginSystem implements Listener {
    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerBucketEntityEvent(PlayerBucketEntityEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerBucketFillEvent(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerFishEvent(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerPickupArrowEvent(PlayerPickupArrowEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerShearEntityEvent(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerBedEnterEvent(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerEditBookEvent(PlayerEditBookEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void SignChangeEvent(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void EntityPickupItemEvent(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!ifLogin(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (!ifLogin(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
            Player player = event.getPlayer();
            if (StringHasMap.getHasMap().get(player.getName() + "_LoginIP") != null) {
                if (StringHasMap.getHasMap().get(player.getName() + "_LoginIP").equals(Objects.requireNonNull(player.getAddress()).getHostName())) {
                    StringHasMap.getHasMap().put(player.getName() + "_Login", "t");
                    player.sendMessage(i18n("Login.AutoLogin"));
                }
            }
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            Player player = event.getPlayer();
            StringHasMap.getHasMap().put(player.getName() + "_Login", null);
        }
    }
}
