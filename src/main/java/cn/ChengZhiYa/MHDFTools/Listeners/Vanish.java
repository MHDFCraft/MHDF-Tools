package cn.ChengZhiYa.MHDFTools.Listeners;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.Utils.Util.ChatColor;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.VanishList;

public final class Vanish implements Listener {
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            if (MHDFTools.instance.getConfig().getBoolean("VanishEnable")) {
                if (!VanishList.isEmpty()) {
                    Player player = event.getPlayer();
                    if (VanishList.contains(player.getName())) {
                        Block block = event.getClickedBlock();
                        if (block.getType().toString().contains("CHEST") || block.getType().toString().contains("SHULKER_BOX")) {
                            event.setCancelled(true);
                            Inventory ChestInventory = ((Container) event.getClickedBlock().getState()).getInventory();
                            Inventory inventory;
                            if (block instanceof ShulkerBox) {
                                inventory = Bukkit.createInventory(player, InventoryType.SHULKER_BOX, ChatColor("&b无动画打开|" + block.getX() + "|" + block.getY() + "|" + block.getZ()));
                            } else {
                                inventory = Bukkit.createInventory(player, ChestInventory.getSize(), ChatColor("&b无动画打开|" + block.getX() + "|" + block.getY() + "|" + block.getZ()));
                            }
                            inventory.setContents(ChestInventory.getContents());
                            player.openInventory(inventory);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent event) {
        if (event.getView().getTitle().contains(ChatColor("&b无动画打开"))) {
            Player player = (Player) event.getPlayer();
            String[] Title = event.getView().getTitle().split("\\|");
            Block Block = new Location(player.getWorld(), Integer.parseInt(Title[1]), Integer.parseInt(Title[2]), Integer.parseInt(Title[3])).getBlock();
            BlockState BlockState = Block.getState();
            Inventory inventory = ((Container) BlockState).getInventory();
            if (inventory.getHolder() instanceof DoubleChest) {
                DoubleChest DoubleChest = (DoubleChest) inventory.getHolder();
                ((Container) ((Chest) Objects.requireNonNull(DoubleChest.getLeftSide())).getBlock().getState()).getInventory().setContents(event.getInventory().getContents());
                ((Container) ((Chest) Objects.requireNonNull(DoubleChest.getRightSide())).getBlock().getState()).getInventory().setContents(event.getInventory().getContents());
            } else if (inventory.getHolder() instanceof ShulkerBox) {
                ShulkerBox ShulkerBox = (ShulkerBox) inventory.getHolder();
                ((Container) ShulkerBox.getBlock().getState()).getInventory().setContents(event.getInventory().getContents());
            } else {
                inventory.setContents(event.getInventory().getContents());
                BlockState.update();
                System.out.println(2);
            }
        }
    }
}
