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
import java.util.List;

@SuppressWarnings("unused")
public final class Fly extends AbstractCommand {
    public Fly() {
        super(
                "flySettings.enable",
                "飞行",
                "mhdftools.commands.fly",
                false,
                ConfigUtil.getConfig().getStringList("flySettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = null;
        boolean sendToSender = true;

        // 切换玩家自己的飞行模式
        if (args.length == 0 && sender instanceof Player) {
            sendToSender = false;
            player = (Player) sender;

            FlyStatus flyStatus = FlyUtil.getFlyStatusDao(player.getUniqueId());
            if (!sender.hasPermission("mhdftools.commands.fly.infinite") && flyStatus.getTime() <= 0) {
                sender.sendMessage(LangUtil.i18n("noPermission"));
                return;
            }
        }

        // 切换其他玩家的飞行模式
        if (args.length == 1) {
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(LangUtil.i18n("playerOffline"));
                return;
            }
            player = Bukkit.getPlayer(args[0]);

            if (!sender.hasPermission("mhdftools.commands.fly.give")) {
                sender.sendMessage(LangUtil.i18n("noPermission"));
                return;
            }
        }

        // 切换飞行
        if (player != null) {
            FlyStatus flyStatus = FlyUtil.getFlyStatusDao(player.getUniqueId());
            if (!flyStatus.isEnable()) {
                FlyUtil.enableFly(player);
                if (sendToSender) {
                    FlyUtil.sendChangeFlyMessage(sender, player, true);
                }
                FlyUtil.sendChangeFlyMessage(player, player, true);
            } else {
                FlyUtil.disableFly(player);
                if (sendToSender) {
                    FlyUtil.sendChangeFlyMessage(sender, player, false);
                }
                FlyUtil.sendChangeFlyMessage(player, player, false);
            }
            return;
        }

        // 输出帮助信息
        {
            sender.sendMessage(
                    LangUtil.i18n("usageError")
                            .replace("{usage}", LangUtil.i18n("commands.fly.usage"))
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return BungeeCordUtil.getPlayerList();
        }
        return new ArrayList<>();
    }
}
