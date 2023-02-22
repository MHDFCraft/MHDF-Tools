package cn.ChengZhiYa.ChengToolsReloaded.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class Hat implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerInventory PlayerInventory = player.getInventory();
            ItemStack OldHelmet = PlayerInventory.getHelmet();
            ItemStack NewHelmet = player.getItemInHand();
            if (NewHelmet.getType() == Material.AIR) {
                sender.sendMessage(ChatColor("&c&l你需要拿着一个物品才能使用这个命令!"));
                return false;
            }
            PlayerInventory.setHelmet(NewHelmet);
            player.setItemInHand(OldHelmet);
            player.updateInventory();
        } else {
            sender.sendMessage(ChatColor("&c&l这个命令只能玩家使用!"));
        }
        return false;
    }
}
