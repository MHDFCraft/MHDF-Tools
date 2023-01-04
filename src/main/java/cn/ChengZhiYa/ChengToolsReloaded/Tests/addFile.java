package cn.ChengZhiYa.ChengToolsReloaded.Tests;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class addFile implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            File PlayerData = new File(main.getPlugin(main.class).getDataFolder(), "data.yml");
            YamlConfiguration Player_Data = YamlConfiguration.loadConfiguration(PlayerData);
            Player_Data.set("qwe", "qwe");
            try {
                Player_Data.save(PlayerData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
