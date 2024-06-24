package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.ifLogin;

public final class PlayerLoginListener implements Listener {
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketEntityEvent(PlayerBucketEntityEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFishEvent(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupArrowEvent(PlayerPickupArrowEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerShearEntityEvent(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerEditBookEvent(PlayerEditBookEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWindowClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!ifLogin(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!ifLogin(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (!ifLogin(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (plugin.getConfig().getBoolean("LoginSystemSettings.AutoLogin")) {
            Player player = event.getPlayer();
            if (MapUtil.getStringHashMap().get(player.getName() + "_LoginIP") != null && MapUtil.getStringHashMap().get(player.getName() + "_LoginIP").equals(Objects.requireNonNull(player.getAddress()).getHostString())) {
                MapUtil.getStringHashMap().put(player.getName() + "_Login", "t");
                player.sendMessage(SpigotUtil.i18n("Login.AutoLogin"));
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        if (plugin.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            Player player = event.getPlayer();
            MapUtil.getStringHashMap().put(player.getName() + "_Login", null);
        }
    }
}
