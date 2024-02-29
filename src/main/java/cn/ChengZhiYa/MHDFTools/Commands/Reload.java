package cn.ChengZhiYa.MHDFTools.Commands;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static cn.ChengZhiYa.MHDFTools.Utils.Util.*;

public final class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("MHDFTools.Command.Command.Reload")) {
            MHDFTools.instance.reloadConfig();
            if (MHDFTools.instance.getConfig().getBoolean("InvseeSettings.Enable")) {
                VanishBossBar = BossBar.bossBar(Component.text(i18n("Vanish.Bossbar")), 1f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
            }
            LangFileData = YamlConfiguration.loadConfiguration(new File(MHDFTools.instance.getDataFolder(), "lang.yml"));
            SoundFileData = YamlConfiguration.loadConfiguration(new File(MHDFTools.instance.getDataFolder(), "sound.yml"));
            sender.sendMessage(i18n("RelaodDone"));
        } else {
            sender.sendMessage(i18n("NoPermission"));
        }
        return false;
    }
}
