package cn.ChengZhiYa.MHDFTools.listener;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.util.message.LogUtil;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.util.Util.VanishList;
import static cn.ChengZhiYa.MHDFTools.util.Util.getVanishBossBar;

public final class Vanish implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("VanishSettings.Enable")) {
            Player player = event.getPlayer();
            if (VanishList.contains(player.getName())) {
                for (Player OnlinePlayer : Bukkit.getOnlinePlayers()) {
                    try {
                        Player.class.getDeclaredMethod("hidePlayer", Plugin.class, Player.class);
                        OnlinePlayer.hidePlayer(MHDFTools.instance, player);
                    } catch (NoSuchMethodException e) {
                        OnlinePlayer.hidePlayer(player);
                    }
                }
                PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, true);
                player.addPotionEffect(INVISIBILITY);
                player.showBossBar(getVanishBossBar());
            }
        }
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("VanishSettings.Enable")) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
                if (!VanishList.isEmpty()) {
                    Player player = event.getPlayer();
                    if (VanishList.contains(player.getName())) {
                        Block block = event.getClickedBlock();
                        if (block.getType().toString().contains("CHEST") || block.getType().toString().contains("SHULKER_BOX")) {
                            event.setCancelled(true);
                            Inventory ChestInventory = ((Container) event.getClickedBlock().getState()).getInventory();
                            Inventory inventory;
                            if (block instanceof ShulkerBox) {
                                inventory = Bukkit.createInventory(player, InventoryType.SHULKER_BOX, LogUtil.ChatColor("&b无动画打开|" + block.getX() + "|" + block.getY() + "|" + block.getZ()));
                            } else {
                                inventory = Bukkit.createInventory(player, ChestInventory.getSize(), LogUtil.ChatColor("&b无动画打开|" + block.getX() + "|" + block.getY() + "|" + block.getZ()));
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
        if (MHDFTools.instance.getConfig().getBoolean("VanishSettings.Enable")) {
            if (event.getView().getTitle().contains(LogUtil.ChatColor("&b无动画打开"))) {
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
                }
            }
        }
    }
}
