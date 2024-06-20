package cn.ChengZhiYa.MHDFTools.listeners.player.fastuse.util;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public final class CraftingTable implements Listener {
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("FastUseCraftingTableSettings.Enable")) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                Player player = event.getPlayer();
                if (player.hasPermission("MHDFTools.FastUse.CraftingTable")) {
                    if (player.getInventory().getItemInMainHand().getType() == Material.CRAFTING_TABLE) {
                        player.openWorkbench(null, true);
                    }
                }
            }
        }
    }
}