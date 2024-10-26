package cn.ChengZhiYa.MHDFTools.utils;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.database.NickUtil;
import cn.ChengZhiYa.MHDFTools.utils.database.VanishUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import cn.ChengZhiYa.MHDFTools.utils.message.ColorLogs;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.papermc.lib.PaperLib;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.PlayerList;

public final class SpigotUtil {

    public static final Class<?> pluginClassLoader;
    public static final Field pluginClassLoaderPlugin;
    public static final List<String> CommandLinkList = new ArrayList<>();
    public static YamlConfiguration LangFileData;
    public static YamlConfiguration SoundFileData;
    private static volatile BossBar vanishBossBar;

    static {
        try {
            pluginClassLoader = Class.forName("org.bukkit.plugin.java.PluginClassLoader");
            pluginClassLoaderPlugin = pluginClassLoader.getDeclaredField("plugin");
            pluginClassLoaderPlugin.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkUpdate() {
        try {
            URL url = new URL("https://mhdf.love:8888/plugin/version/MHDF-Tools");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String response = in.readLine();
                JsonObject data = JsonParser.parseString(response).getAsJsonObject();
                String newVersionString = data.get("data").getAsString();

                if (!newVersionString.equals(PluginLoader.INSTANCE.getVersion())) {
                    ColorLogs.colorMessage("&f[MHDF-Tools] &c当前插件版本不是最新版!");
                    ColorLogs.colorMessage("&f[MHDF-Tools] &c下载链接: &7https://github.com/Love-MHDF/MHDF-Tools/releases/");
                    MapUtil.getBooleanHashMap().put("IsLast", true);
                } else {
                    ColorLogs.colorMessage("&f[MHDF-Tools] &a当前插件版本是最新版!");
                }
                MapUtil.getBooleanHashMap().put("CheckVersionError", false);
            } catch (IOException e) {
                logUpdateError();
            } finally {
                conn.disconnect();
            }
        } catch (IOException e) {
            logUpdateError();
        }
    }

    private static void logUpdateError() {
        ColorLogs.colorMessage("&f[MHDF-Tools] &c获取检测更新时出错!请检查网络连接!");
        MapUtil.getBooleanHashMap().put("IsLast", false);
        MapUtil.getBooleanHashMap().put("CheckVersionError", true);
    }

    public static String getIpLocation(String ip) {
        if (ip.startsWith("127.")) {
            return "local";
        }

        try {
            URL url = new URL("https://opendata.baidu.com/api.php?query=" + ip + "&co=&resource_id=6006&t=1433920989928&ie=utf8&oe=utf-8&format=json");
            URLConnection conn = url.openConnection();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                JsonObject json = JsonParser.parseString(reader.readLine()).getAsJsonObject();
                JsonArray dataArray = json.getAsJsonArray("data");
                JsonObject dataJson = dataArray.get(0).getAsJsonObject();
                return dataJson.get("location").getAsString();
            } catch (IOException e) {
                return "Failed to get data";
            }
        } catch (IOException e) {
            return "Failed to connect";
        }
    }

    public static boolean canTPS() {
        return PluginLoader.INSTANCE.getServerManager().is1_20orAbove();
    }

    public static String Placeholder(OfflinePlayer player, String message) {
        if (PluginLoader.INSTANCE.isHasPlaceholderAPI()) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        return MessageUtil.colorMessage(message);
    }

    public static String sha256(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(message.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean ifLogin(Player player) {
        return MapUtil.getStringHashMap().get(player.getName() + "_Login") != null;
    }

    public static void adminSendMessage(String message, String playerName) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.isOp() && !player.getName().equals(playerName))
                .forEach(player -> player.sendMessage(MessageUtil.colorMessage(message)));
    }

    public static GameMode getGamemode(int gameModeID) {
        return switch (gameModeID) {
            case 0 -> GameMode.SURVIVAL;
            case 1 -> GameMode.CREATIVE;
            case 2 -> GameMode.ADVENTURE;
            case 3 -> GameMode.SPECTATOR;
            default -> null;
        };
    }

    public static String getGamemodeString(int gameModeID) {
        return switch (gameModeID) {
            case 0 -> i18n("GameMode.Survival");
            case 1 -> i18n("GameMode.Creative");
            case 2 -> i18n("GameMode.Adventure");
            case 3 -> i18n("GameMode.Spectator");
            default -> null;
        };
    }

    public static void registerCommand(Plugin plugin, CommandExecutor commandExecutor, String description, String permission, String commandString) {
        PluginCommand command = getCommand(commandString, plugin);
        if (command != null) {
            command.setDescription(description);

            if (permission != null) {
                command.setPermission(permission);
                command.setPermissionMessage(i18n("NoPermission"));
            }
            Objects.requireNonNull(getCommandMap()).register(plugin.getDescription().getName(), command);
            command.setExecutor(commandExecutor);
            if (commandExecutor instanceof TabExecutor) {
                command.setTabCompleter((TabCompleter) commandExecutor);
            }
        }
    }

    private static PluginCommand getCommand(String name, Plugin plugin) {
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            return c.newInstance(name, plugin);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static CommandMap getCommandMap() {
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                return (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public static String getTps(int time) {
        double[] tpsList = getTpsList();
        double tpsValue = BigDecimal.valueOf(tpsList[switch (time) {
            case 1 -> 0;
            case 5 -> 1;
            case 15 -> 2;
            default -> throw new IllegalArgumentException("未知数: " + time);
        }]).setScale(1, RoundingMode.HALF_UP).doubleValue();

        return tpsValue > 18.0D ? "&a" + tpsValue :
                tpsValue > 16.0D ? "&6" + tpsValue :
                        "&c" + tpsValue;
    }

    public static double[] getTpsList() {
        return Bukkit.getTPS();
    }

    public static String sound(String soundKey) {
        return SoundFileData.getString(soundKey);
    }

    public static String i18n(String langKey, String... values) {
        String message = LangFileData.getString(langKey);
        if (message != null) {
            for (int i = 0; i < values.length; i++) {
                message = message.replace("%" + (i + 1), values[i]);
            }
        }
        return MessageUtil.colorMessage(Objects.requireNonNullElse(message, ""));
    }

    public static String getJoinMessage(Player player) {
        return getCustomMessage(player, "CustomJoinServerMessageSettings", "JoinMessage");
    }

    public static String getQuitMessage(Player player) {
        return getCustomMessage(player, "CustomQuitServerMessageSettings", "QuitMessage");
    }

    public static String getCustomMessage(Player player, String settingType, String messageType) {
        Map<Integer, String> messageList = new ConcurrentHashMap<>();
        List<Integer> weightList = new ArrayList<>();

        player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(perm -> perm.startsWith("mhdftools." + messageType))
                .forEach(perm -> {
                    String group = perm.substring(("mhdftools." + messageType).length());
                    int weight = PluginLoader.INSTANCE.getPlugin().getConfig().getInt(settingType + "." + group + ".Weight");
                    messageList.put(weight, group);
                    weightList.add(weight);
                });

        if (!messageList.isEmpty()) {
            weightList.sort(Collections.reverseOrder());
            return Placeholder(player, PluginLoader.INSTANCE.getPlugin().getConfig().getString(settingType + "." + messageList.get(weightList.get(0)) + "." + messageType))
                    .replace("%PlayerName%", NickUtil.getNickName(player.getName()));
        }

        return Placeholder(player, PluginLoader.INSTANCE.getPlugin().getConfig().getString(settingType + ".Default." + messageType))
                .replace("%PlayerName%", NickUtil.getNickName(player.getName()));
    }

    public static BossBar getVanishBossBar() {
        if (vanishBossBar == null) {
            synchronized (SpigotUtil.class) {
                if (vanishBossBar == null) {
                    vanishBossBar = BossBar.bossBar(Component.text(i18n("Vanish.Bossbar")), 1f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
                }
            }
        }
        return vanishBossBar;
    }

    public static List<String> getPlayerList(boolean isBungeeCord) {
        if (isBungeeCord && PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            return new ArrayList<>(Arrays.asList(PlayerList));
        } else {
            return Bukkit.getOnlinePlayers().stream() //麻烦不要老是套if,可以用stream优化可读性
                    .map(Player::getName)
                    .filter(name -> !VanishUtil.getVanishList().contains(name))
                    .collect(Collectors.toList());
        }
    }

    public static void sendTitle(Player player, String titleString) {
        String[] title = titleString.split("\\|");
        player.sendTitle(Placeholder(player, title[0]), Placeholder(player, title[1]), Integer.parseInt(title[2]), Integer.parseInt(title[3]), Integer.parseInt(title[4]));
    }

    public static void playSound(Player player, String soundString) {
        String[] sound = soundString.split("\\|");
        player.playSound(player, org.bukkit.Sound.valueOf(sound[0]), Float.parseFloat(sound[1]), Float.parseFloat(sound[2]));
    }

    public static void diffTeleport(Player player, Location location) {
        if (PluginLoader.INSTANCE.getServerManager().is1_20orAbove()) {
            PaperLib.teleportAsync(player, location);
        } else {
            player.teleport(location);
        }
    }

    public static String getTimeString(int time) {
        int seconds = time % 60;
        int totalMinutes = time / 60;
        int minutes = totalMinutes % 60;
        int totalHours = totalMinutes / 60;
        int hours = totalHours % 24;
        int totalDays = totalHours / 24;
        int days = totalDays % 30;
        int totalMonths = totalDays / 30;
        int months = totalMonths % 12;
        int years = totalMonths / 12;

        StringBuilder sb = new StringBuilder();

        if (years > 0) {
            sb.append(years).append("年");
        }

        if (months > 0) {
            sb.append(months).append("月");
        }

        if (days > 0) {
            sb.append(days).append("日");
        }

        if (hours > 0) {
            sb.append(hours).append("时");
        }

        if (minutes > 0) {
            sb.append(minutes).append("分");
        }

        if (time > 0) {
            sb.append(seconds).append("秒");
        } else if (sb.length() == 0) {
            sb.append("0秒");
        }

        return sb.toString();
    }
}
