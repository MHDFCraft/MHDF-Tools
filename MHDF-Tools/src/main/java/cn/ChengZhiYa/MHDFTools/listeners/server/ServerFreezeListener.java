package cn.ChengZhiYa.MHDFTools.listeners.server;

import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.freeze.Freeze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public class ServerFreezeListener implements Listener {

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(i18n("Freeze.EventCanceledMessage", name));
        }
    }

    @EventHandler
    public void onPlayerPickupArrowEvent(PlayerPickupArrowEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(i18n("Freeze.EventCanceledMessage", name));
        }
    }

    @EventHandler
    public void onPlayerVelocityEvent(PlayerVelocityEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(i18n("Freeze.EventCanceledMessage", name));
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(i18n("Freeze.EventCanceledMessage", name));
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(i18n("Freeze.EventCanceledMessage", name));
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(i18n("Freeze.EventCanceledMessage", name));
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(i18n("Freeze.EventCanceledMessage", name));
        }
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(i18n("Freeze.EventCanceledMessage", name));
        }
    }

    @EventHandler
    public void onPlayerAnimationEvent(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(i18n("Freeze.EventCanceledMessage", name));
        }
    }

    @EventHandler
    public void onPlayerAnimationEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(i18n("Freeze.EventCanceledMessage", name));
        }
    }

    @EventHandler
    public void onPlayerAnimationEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(i18n("Freeze.EventCanceledMessage", name));
        }
    }

    @EventHandler
    public void onPlayerAnimationEvent(PlayerFishEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(i18n("Freeze.EventCanceledMessage", name));
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();

        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            player.sendMessage(i18n("Freeze.EventCanceledMessage", player.getName()));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (Freeze.freezeUUID.contains(player.getUniqueId())) {
            player.sendMessage(i18n("Freeze.EventCanceledMessage", player.getName()));
            event.setCancelled(true);
        }
    }
}