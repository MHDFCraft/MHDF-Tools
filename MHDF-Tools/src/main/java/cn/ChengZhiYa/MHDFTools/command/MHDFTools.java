package cn.ChengZhiYa.MHDFTools.command;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.util.Util.*;
import static cn.ChengZhiYa.MHDFTools.util.database.ConvertData.MySQLToYAML;
import static cn.ChengZhiYa.MHDFTools.util.database.ConvertData.YAMLToMySQL;
import static cn.ChengZhiYa.MHDFTools.util.database.ImportUtil.ImportCMIData;
import static cn.ChengZhiYa.MHDFTools.util.database.ImportUtil.ImportHuskHomesData;

public final class MHDFTools implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("MHDFTools.Admin")) {
            if (args.length == 1) {
                if (args[0].equals("reload")) {
                    cn.ChengZhiYa.MHDFTools.MHDFTools.instance.reloadConfig();
                    if (cn.ChengZhiYa.MHDFTools.MHDFTools.instance.getConfig().getBoolean("InvseeSettings.Enable")) {
                        VanishBossBar = BossBar.bossBar(Component.text(i18n("Vanish.Bossbar")), 1f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
                    }
                    LangFileData = YamlConfiguration.loadConfiguration(new File(cn.ChengZhiYa.MHDFTools.MHDFTools.instance.getDataFolder(), "lang.yml"));
                    SoundFileData = YamlConfiguration.loadConfiguration(new File(cn.ChengZhiYa.MHDFTools.MHDFTools.instance.getDataFolder(), "sound.yml"));
                    sender.sendMessage(i18n("AdminCommands.reload.ReloadDone"));

                    return true;
                }
            }
            if (args.length >= 2) {
                if (args[0].equals("convert")) {
                    switch (args[1]) {
                        case "YAML":
                            if (!Objects.equals(cn.ChengZhiYa.MHDFTools.MHDFTools.instance.getConfig().getString("DataSettings.Type"), "YAML")) {
                                MySQLToYAML(sender);
                            } else {
                                sender.sendMessage(i18n("AdminCommands.convert.ConvertInvalid", "YAML"));
                            }
                            break;
                        case "MySQL":
                            if (args.length == 6) {
                                if (!Objects.equals(cn.ChengZhiYa.MHDFTools.MHDFTools.instance.getConfig().getString("DataSettings.Type"), "MySQL")) {
                                    YAMLToMySQL(sender, args[2], args[3], args[4], args[5]);
                                } else {
                                    sender.sendMessage(i18n("AdminCommands.convert.ConvertInvalid", "MySQL"));
                                }
                                break;
                            }
                        default:
                            sender.sendMessage(i18n("AdminCommands.convert.NotFoundType"));
                            break;
                    }
                    return true;
                }
            }
            if (args.length == 2) {
                if (args[0].equals("import")) {
                    switch (args[1]) {
                        case "HuskHomes":
                            ImportHuskHomesData(sender);
                            break;
                        case "CMI":
                            ImportCMIData(sender);
                            break;
                        default:
                            sender.sendMessage(i18n("AdminCommands.import.NotFoundPlugin"));
                            break;
                    }
                    return true;
                }
            }
        } else {
            sender.sendMessage(i18n("NoPermission"));
        }

        {
            sender.sendMessage(i18n("AdminCommands.help.Message")
                    .replaceAll("\\{help\\}", CommandHelp("help", label))
                    .replaceAll("\\{convert\\}", CommandHelp("convert", label))
                    .replaceAll("\\{import\\}", CommandHelp("import", label))
                    .replaceAll("\\{reload\\}", CommandHelp("reload", label)));
        }
        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("help", "convert", "import", "reload");
        }
        if (args.length == 2) {
            switch (args[0]) {
                case "convert":
                    return Arrays.asList("YAML", "MySQL");
                case "import":
                    return Arrays.asList("HuskHomes", "CMI");
            }
        }
        return new ArrayList<>();
    }

    public String CommandHelp(String Command, String label) {
        return i18n("AdminCommands." + Command + ".Usage").replaceAll("\\{Command\\}", label) + i18n("AdminCommands.Center") + i18n("AdminCommands." + Command + ".Description").replaceAll("\\{Command\\}", label);
    }
}
