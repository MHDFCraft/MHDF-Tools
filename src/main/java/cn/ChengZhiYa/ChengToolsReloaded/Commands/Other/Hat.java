package cn.ChengZhiYa.ChengToolsReloaded.Commands.Other;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.i18n;

public final class Hat implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerInventory PlayerInventory = player.getInventory();
            ItemStack OldHelmet = PlayerInventory.getHelmet();
            ItemStack NewHelmet = player.getItemInHand();
            if (NewHelmet.getType() == Material.AIR) {
                sender.sendMessage(i18n("HatNoItem"));
                return false;
            }
            PlayerInventory.setHelmet(NewHelmet);
            player.setItemInHand(OldHelmet);
            player.updateInventory();
        } else {
            sender.sendMessage(i18n("OnlyPlayer"));
        }
        return false;
    }
}
