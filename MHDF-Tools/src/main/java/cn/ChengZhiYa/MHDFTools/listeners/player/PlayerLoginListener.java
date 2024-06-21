package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.ifLogin;

public final class PlayerLoginListener implements Listener {
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
        if (MHDFTools.instance.getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
            Player player = event.getPlayer();
            if (MapUtil.getStringHashMap().get(player.getName() + "_LoginIP") != null) {
                if (MapUtil.getStringHashMap().get(player.getName() + "_LoginIP").equals(Objects.requireNonNull(player.getAddress()).getHostName())) {
                    MapUtil.getStringHashMap().put(player.getName() + "_Login", "t");
                    player.sendMessage(i18n("Login.AutoLogin"));
                }
            }
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            Player player = event.getPlayer();
            MapUtil.getStringHashMap().put(player.getName() + "_Login", null);
        }
    }
}
