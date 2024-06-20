package cn.ChengZhiYa.MHDFTools.listeners.player.fastuse;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public final class EnderChest implements Listener {
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("FastUseSettings.EnderChest")) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                Player player = event.getPlayer();
                if (player.hasPermission("MHDFTools.FastUse.EnderChest")) {
                    if (player.getInventory().getItemInMainHand().getType() == Material.ENDER_CHEST) {
                        player.openInventory(player.getEnderChest());
                    }
                }
            }
        }
    }
}