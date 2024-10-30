package cn.ChengZhiYa.MHDFTools.listeners.player.fastuse;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public final class ShulkerBox implements Listener {
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("FastUseShulkerBoxSettings.Enable")) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                Player player = event.getPlayer();
                if (player.hasPermission("MHDFTools.FastUse.ShulkerBox")) {
                    if (player.getInventory().getItemInMainHand().getType().toString().contains("SHULKER_BOX")) {
                        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
                        BlockStateMeta blockMate = (BlockStateMeta) meta;
                        org.bukkit.block.ShulkerBox box = (org.bukkit.block.ShulkerBox) blockMate.getBlockState();
                        Inventory inv = Bukkit.createInventory(player, InventoryType.SHULKER_BOX, MessageUtil.colorMessage(PluginLoader.INSTANCE.getPlugin().getConfig().getString("FastUseShulkerBoxSettings.MenuTitle")));
                        inv.setContents(box.getInventory().getContents());
                        player.openInventory(inv);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClickInventoryEvent(InventoryClickEvent event) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("FastUseShulkerBoxSettings.Enable")) {
            if (event.getView().getTitle().equals(MessageUtil.colorMessage(PluginLoader.INSTANCE.getPlugin().getConfig().getString("FastUseShulkerBoxSettings.MenuTitle")))) {
                if (event.getCurrentItem() != null) {
                    event.setCancelled(event.getCurrentItem().getType().toString().contains("SHULKER_BOX"));
                }
                Player player = (Player) event.getWhoClicked();
                updateShulker(player, event.getInventory());
            }
        }
    }

    @EventHandler
    public void onCloseInventoryEvent(InventoryCloseEvent event) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("FastUseShulkerBoxSettings.Enable")) {
            if (event.getView().getTitle().equals(MessageUtil.colorMessage(PluginLoader.INSTANCE.getPlugin().getConfig().getString("FastUseShulkerBoxSettings.MenuTitle")))) {
                Player player = (Player) event.getPlayer();
                updateShulker(player, event.getInventory());
            }
        }
    }

    private void updateShulker(Player player, Inventory inventory) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getItemMeta() instanceof BlockStateMeta) {
            BlockStateMeta blockStateMeta = (BlockStateMeta) item.getItemMeta();
            org.bukkit.block.ShulkerBox box = (org.bukkit.block.ShulkerBox) blockStateMeta.getBlockState();

            box.getInventory().setContents(inventory.getContents());

            blockStateMeta.setBlockState(box);
            item.setItemMeta(blockStateMeta);
            player.getInventory().setItemInMainHand(item);
        } else {
            player.sendMessage("Invalid");
        }
    }
}
