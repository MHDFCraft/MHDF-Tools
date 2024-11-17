package cn.ChengZhiYa.MHDFTools.command.feature;

import cn.ChengZhiYa.MHDFTools.command.AbstractCommand;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.config.LangUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.util.config.LangUtil.i18n;

@SuppressWarnings("unused")
public final class List extends AbstractCommand {
    private final Runtime runtime = Runtime.getRuntime();

    public List() {
        super(
                "listSettings.enable",
                "查看在线列表",
                "mhdftools.commands.list",
                false,
                ConfigUtil.getConfig().getStringList("listSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(
                    i18n("commands.list.message")
                            .replace("{tps}", String.valueOf(getTps()))
                            .replace("{memory}", String.valueOf(getUsedMemory()))
                            .replace("{maxMemory}", String.valueOf(getTotalMemory()))
                            .replace("{playerCount}", BungeeCordUtil.getPlayerList().toString())
                            .replace("{maxPlayerCount}", String.valueOf(Bukkit.getMaxPlayers()))
                            .replace("{playerList}", BungeeCordUtil.getPlayerList().toString())
            );
            return;
        }

        // 输出帮助信息
        {
            sender.sendMessage(
                    LangUtil.i18n("usageError")
                            .replace("{usage}", LangUtil.i18n("commands.list.usage"))
                            .replace("{command}", label)
            );
        }
    }

    /**
     * 获取服务器当前TPS
     *
     * @return TPS数值
     */
    private double getTps() {
        double tps;
        if (FoliaScheduler.isFolia()) {
            tps = 0.0;
        } else {
            tps = SpigotReflectionUtil.getTPS();
        }
       return Double.parseDouble(String.format("%.2f", tps));
    }

    /**
     * 获取服务器当前总内存
     *
     * @return 总内存数(单位 MB)
     */
    private long getTotalMemory() {
        return runtime.totalMemory() / 1048576L;
    }

    /**
     * 获取服务器当前总空闲内存
     *
     * @return 总空闲内存(单位 MB)
     */
    private long getFreeMemory() {
        return runtime.freeMemory() / 1048576L;
    }

    /**
     * 获取服务器当前总占用内存
     *
     * @return 总占用内存(单位 MB)
     */
    private long getUsedMemory() {
        return getTotalMemory() - getFreeMemory();
    }
}
