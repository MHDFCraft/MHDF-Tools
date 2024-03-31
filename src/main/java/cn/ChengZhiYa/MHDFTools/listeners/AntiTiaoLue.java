package cn.ChengZhiYa.MHDFTools.listeners;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;
import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;

public final class AntiTiaoLue implements Listener {
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("AntiTiaoLue")) {
            if (event.getBlock().getType() == Material.REDSTONE_WIRE) {
                if (event.getBlock().getRelative(BlockFace.DOWN).getType().name().endsWith("TRAPDOOR")) {
                    event.setCancelled(true);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.isOp()) {
                            player.sendMessage(ChatColor(i18n("AntiTiaoLue", event.getPlayer().getName(), String.valueOf(event.getBlock().getX()), String.valueOf(event.getBlock().getY()), String.valueOf(event.getBlock().getZ()))));
                        }
                    }
                }
            }
        }
    }
}
