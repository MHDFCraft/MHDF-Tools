package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class KnockBack extends AbstractCommand {

    public KnockBack() {
        super(
                "knockBackSettings.enable",
                "崩溃玩家客户端",
                "mhdftools.commands.knockback",
                false,
                ConfigUtil.getConfig().getStringList("knockBackSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(LangUtil.i18n("commands.knockback.usage"));
            return;
        }

        final Player player = Bukkit.getPlayer(args[1]);

        if (player == null) {
            sender.sendMessage(LangUtil.i18n("playerOffline"));
            return;
        }

        final double x = ConfigUtil.getConfig().getDouble("vector.x");
        final double y = ConfigUtil.getConfig().getDouble("vector.y");
        final double z = ConfigUtil.getConfig().getDouble("vector.z");

        final Vector vector = new Vector(x, y, z);
        player.setVelocity(vector);
        sender.sendMessage(LangUtil.i18n("commands.knockback.sendDone").replace("{player}", player.getName()));
    }
}
