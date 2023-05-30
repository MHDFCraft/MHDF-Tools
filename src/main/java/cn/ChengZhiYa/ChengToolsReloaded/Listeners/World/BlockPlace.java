package cn.ChengZhiYa.ChengToolsReloaded.Listeners.World;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class BlockPlace implements Listener {
    @EventHandler
    public void onEvent(BlockPlaceEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("AntiTiaoLue")) {
            if (event.getBlock().getType() == Material.REDSTONE_WIRE) {
                if (event.getBlock().getRelative(BlockFace.DOWN).getType().name().endsWith("TRAPDOOR")) {
                    event.setCancelled(true);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.isOp()) {
                            player.sendMessage(ChatColor(getLang("AntiTiaoLue", event.getPlayer().getName(), String.valueOf(event.getBlock().getX()), String.valueOf(event.getBlock().getY()), String.valueOf(event.getBlock().getZ()))));
                        }
                    }
                }
            }
        }
    }
}
