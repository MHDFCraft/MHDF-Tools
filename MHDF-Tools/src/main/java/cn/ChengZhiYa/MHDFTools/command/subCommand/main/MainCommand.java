package cn.ChengZhiYa.MHDFTools.command.subCommand.main;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
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

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.*;
import static cn.ChengZhiYa.MHDFTools.utils.database.ConvertData.MySQLToYAML;
import static cn.ChengZhiYa.MHDFTools.utils.database.ConvertData.YAMLToMySQL;
import static cn.ChengZhiYa.MHDFTools.utils.database.ImportUtil.importCMIData;
import static cn.ChengZhiYa.MHDFTools.utils.database.ImportUtil.importHuskHomesData;

public final class MainCommand implements TabExecutor {
    private static final String YAML_TYPE = "YAML";
    private static final String MYSQL_TYPE = "MySQL";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1 && "reload".equals(args[0])) {
            loadConfigurations();
            sender.sendMessage(i18n("AdminCommands.reload.ReloadDone"));
            return true;
        }

        if (args.length == 1 && "version".equals(args[0])) {
            sender.sendMessage(
                    i18n("AdminCommands.version.Message")
                            .replaceAll("\\{Version}", PluginLoader.INSTANCE.getVersion())
                            .replaceAll("\\{ServerVersion}", PluginLoader.INSTANCE.getServerManager().getVersion())
            );
            return true;
        }

        if (args.length >= 2 && "convert".equals(args[0])) {
            switch (args[1]) {
                case "YAML":
                    handleYAMLConversion(sender);
                    break;
                case "MySQL":
                    handleMySQLConversion(sender, args);
                    break;
                default:
                    sender.sendMessage(i18n("AdminCommands.convert.NotFoundType"));
                    break;
            }
            return true;
        }

        if (args.length == 2 && "import".equals(args[0])) {
            switch (args[1]) {
                case "HuskHomes":
                    importHuskHomesData(sender);
                    break;
                case "CMI":
                    importCMIData(sender);
                    break;
                default:
                    sender.sendMessage(i18n("AdminCommands.import.NotFoundPlugin"));
                    break;
            }
            return true;
        }

        sendHelpMessage(sender, label);
        return false;
    }

    private void handleYAMLConversion(CommandSender sender) {
        if (!Objects.equals(cn.ChengZhiYa.MHDFTools.MHDFTools.instance.getConfig().getString("DataSettings.Type"), YAML_TYPE)) {
            MySQLToYAML(sender);
        } else {
            sender.sendMessage(i18n("AdminCommands.convert.ConvertInvalid", YAML_TYPE));
        }
    }

    private void handleMySQLConversion(CommandSender sender, String[] args) {
        if (args.length == 6) {
            if (!Objects.equals(cn.ChengZhiYa.MHDFTools.MHDFTools.instance.getConfig().getString("DataSettings.Type"), MYSQL_TYPE)) {
                YAMLToMySQL(sender, args[2], args[3], args[4], args[5]);
            } else {
                sender.sendMessage(i18n("AdminCommands.convert.ConvertInvalid", MYSQL_TYPE));
            }
        }
    }

    private void sendHelpMessage(CommandSender sender, String label) {
        sender.sendMessage(i18n("AdminCommands.help.Message")
                .replaceAll("\\{help}", CommandHelp("help", label))
                .replaceAll("\\{version}", CommandHelp("version", label))
                .replaceAll("\\{convert}", CommandHelp("convert", label))
                .replaceAll("\\{import}", CommandHelp("import", label))
                .replaceAll("\\{reload}", CommandHelp("reload", label)));
    }

    private void loadConfigurations() {
        cn.ChengZhiYa.MHDFTools.MHDFTools instance = cn.ChengZhiYa.MHDFTools.MHDFTools.instance;
        instance.reloadConfig();

        if (instance.getConfig().getBoolean("InvseeSettings.Enable")) {
            VanishBossBar = BossBar.bossBar(Component.text(i18n("Vanish.Bossbar")), 1f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
        }

        LangFileData = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), "lang.yml"));
        SoundFileData = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), "sound.yml"));
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("help", "version", "convert", "import", "reload");
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
