package cn.ChengZhiYa.MHDFTools.command;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;
import static cn.ChengZhiYa.MHDFTools.util.Util.i18n;

public final class Trash implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Inventory menu;
            if (Objects.equals(MHDFTools.instance.getConfig().getString("TrashSettings.GUISettings.Type"), "DISPENSER")) {
                menu = Bukkit.createInventory(
                        player,
                        InventoryType.DISPENSER,
                        ChatColor(MHDFTools.instance.getConfig().getString("TrashSettings.GUISettings.Title"))
                );
            } else {
                menu = Bukkit.createInventory(
                        player,
                        MHDFTools.instance.getConfig().getInt("TrashSettings.GUISettings.Size"),
                        ChatColor(MHDFTools.instance.getConfig().getString("TrashSettings.GUISettings.Title"))
                );
            }
            player.openInventory(menu);
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
