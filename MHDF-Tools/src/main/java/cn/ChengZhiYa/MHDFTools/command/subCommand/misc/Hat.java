package cn.ChengZhiYa.MHDFTools.command.subCommand.misc;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Hat implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n("OnlyPlayer"));
            return false;
        }

        Player player = (Player) sender;
        PlayerInventory playerInventory = player.getInventory();
        ItemStack oldHelmet = playerInventory.getHelmet();
        ItemStack newHelmet = player.getInventory().getItemInMainHand();

        if (newHelmet.getType() == Material.AIR) {
            sender.sendMessage(i18n("HatNoItem"));
            return false;
        }

        playerInventory.setHelmet(newHelmet);
        player.getInventory().setItemInMainHand(oldHelmet);
        player.updateInventory();

        return true;
    }
}