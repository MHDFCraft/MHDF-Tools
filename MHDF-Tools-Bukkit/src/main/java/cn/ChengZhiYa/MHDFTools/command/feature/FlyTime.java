package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.entity.data.FlyStatus;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import cn.ChengZhiYa.MHDFTools.util.feature.FlyUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public final class FlyTime extends AbstractCommand {
    public FlyTime() {
        super(
                "flySettings.enable",
                "限时飞行",
                "mhdftools.commands.flytime",
                false,
                ConfigUtil.getConfig().getStringList("flySettings.flyTimeCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 3) {
            Player player = Bukkit.getPlayer(args[1]);
            long inputTime = Long.parseLong(args[2]);
            if (player == null) {
                sender.sendMessage(LangUtil.i18n("playerOffline"));
                return;
            }

            // 设置飞行时间
            if (args[0].equalsIgnoreCase("set")) {
                FlyUtil.setFlyTime(player, inputTime);

                sender.sendMessage(
                        LangUtil.i18n("commands.flytime.subCommands.set.message")
                                .replace("{player}", player.getName())
                                .replace("{time}", args[2])
                );
                return;
            }

            // 增加飞行时间
            if (args[0].equalsIgnoreCase("add")) {
                FlyStatus flyStatus = FlyUtil.getFlyStatusDao(player.getUniqueId());

                long time = flyStatus.getTime() + inputTime;
                FlyUtil.setFlyTime(player, time);

                sender.sendMessage(
                        LangUtil.i18n("commands.flytime.subCommands.add.message")
                                .replace("{player}", player.getName())
                                .replace("{add}", args[2])
                                .replace("{time}", String.valueOf(time))
                );
                return;
            }

            // 减少飞行时间
            if (args[0].equalsIgnoreCase("take")) {
                FlyStatus flyStatus = FlyUtil.getFlyStatusDao(player.getUniqueId());

                long time = flyStatus.getTime() - inputTime;
                FlyUtil.setFlyTime(player, flyStatus.getTime() - inputTime);

                sender.sendMessage(
                        LangUtil.i18n("commands.flytime.subCommands.take.message")
                                .replace("{player}", player.getName())
                                .replace("{take}", args[2])
                                .replace("{time}", String.valueOf(time))
                );
                return;
            }
        }

        {
            sender.sendMessage(
                    LangUtil.i18n("commands.flytime.subCommands.help.message")
                            .replace("{helpList}", LangUtil.getHelpList("flytime"))
                            .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 2 &&
                (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("take"))
        ) {
            return BungeeCordUtil.getPlayerList();
        }
        if (args.length == 1) {
            return Arrays.asList("help", "set", "add", "take");
        }
        return new ArrayList<>();
    }
}
