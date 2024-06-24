package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.database.VanishUtil;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.getVanishBossBar;

public final class PlayerVanishListener implements Listener {

    static String VANISH_OPEN_PREFIX = "&b无动画打开|";
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getConfig().getBoolean("VanishSettings.Enable")) {
            Player player = event.getPlayer();
            if (VanishUtil.getVanishList().contains(player.getName())) {
                hidePlayerFromOthers(player);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (plugin.getConfig().getBoolean("VanishSettings.Enable")) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
                Player player = event.getPlayer();
                if (VanishUtil.getVanishList().contains(player.getName())) {
                    handleVanishInteract(event, player);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (plugin.getConfig().getBoolean("VanishSettings.Enable")) {
            if (event.getView().getTitle().contains(VANISH_OPEN_PREFIX)) {
                handleVanishInventoryClose(event);
            }
        }
    }

    private void hidePlayerFromOthers(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            try {
                Player.class.getDeclaredMethod("hidePlayer", Plugin.class, Player.class);
                onlinePlayer.hidePlayer(plugin, player);
            } catch (NoSuchMethodException e) {
                onlinePlayer.hidePlayer(player);
            }
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, true));
        player.showBossBar(getVanishBossBar());
    }

    private void handleVanishInteract(PlayerInteractEvent event, Player player) {
        Block block = event.getClickedBlock();
        if (block != null && (block.getType().toString().contains("CHEST") || block.getType().toString().contains("SHULKER_BOX"))) {
            event.setCancelled(true);
            openVanishInventory(player, block);
        }
    }

    private void openVanishInventory(Player player, Block block) {
        Inventory inventory;
        if (block instanceof ShulkerBox) {
            inventory = Bukkit.createInventory(player, InventoryType.SHULKER_BOX, MessageUtil.colorMessage(VANISH_OPEN_PREFIX + block.getX() + "|" + block.getY() + "|" + block.getZ()));
        } else {
            inventory = Bukkit.createInventory(player, ((Container) block.getState()).getInventory().getSize(), MessageUtil.colorMessage(VANISH_OPEN_PREFIX + block.getX() + "|" + block.getY() + "|" + block.getZ()));
        }
        inventory.setContents(((Container) block.getState()).getInventory().getContents());
        player.openInventory(inventory);
    }

    private void handleVanishInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        String[] title = event.getView().getTitle().split("\\|");
        Block block = new Location(player.getWorld(), Integer.parseInt(title[1]), Integer.parseInt(title[2]), Integer.parseInt(title[3])).getBlock();
        Inventory inventory = ((Container) block.getState()).getInventory();

        if (inventory.getHolder() instanceof DoubleChest) {
            DoubleChest doubleChest = (DoubleChest) inventory.getHolder();
            ((Container) ((Chest) Objects.requireNonNull(doubleChest.getLeftSide())).getBlock().getState()).getInventory().setContents(event.getInventory().getContents());
            ((Container) ((Chest) Objects.requireNonNull(doubleChest.getRightSide())).getBlock().getState()).getInventory().setContents(event.getInventory().getContents());
        } else {
            inventory.setContents(event.getInventory().getContents());
            ((BlockState) Objects.requireNonNull(inventory.getHolder())).update();
        }
    }
}