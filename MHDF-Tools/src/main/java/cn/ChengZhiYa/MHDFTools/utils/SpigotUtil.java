package cn.ChengZhiYa.MHDFTools.utils;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.database.NickUtil;
import cn.ChengZhiYa.MHDFTools.utils.database.VanishUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import cn.ChengZhiYa.MHDFTools.utils.message.ColorLogs;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import java.util.stream.Collectors;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.PlayerList;

public final class SpigotUtil {
    public static final Class<?> pluginClassLoader;
    public static final Field pluginClassLoaderPlugin;
    public static final List<String> CommandLinkList = new ArrayList<>();
    public static volatile BossBar VanishBossBar;
    public static YamlConfiguration LangFileData;
    public static YamlConfiguration SoundFileData;

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
                JSONObject data = JSON.parseObject(response);
                String newVersionString = data.getString("data");

                if (!newVersionString.equals(PluginLoader.INSTANCE.getVersion())) {
                    ColorLogs.colorMessage("&f[MHDF-Tools] &c当前插件版本不是最新版!");
                    ColorLogs.colorMessage("&f[MHDF-Tools] &c下载链接: &7https://github.com/Love-MHDF/MHDF-Tools/releases/");
                    MapUtil.getBooleanHashMap().put("IsLast", true);
                } else {
                    ColorLogs.colorMessage("&f[MHDF-Tools] &a当前插件版本是最新版!");
                }
                MapUtil.getBooleanHashMap().put("CheckVersionError", false);
            } catch (IOException e) {
                ColorLogs.colorMessage("&f[MHDF-Tools] &c获取检测更新时出错!请检查网络连接!");
                MapUtil.getBooleanHashMap().put("IsLast", false);
                MapUtil.getBooleanHashMap().put("CheckVersionError", true);
            } finally {
                conn.disconnect();
            }
        } catch (IOException e) {
            ColorLogs.colorMessage("&f[MHDF-Tools] &c获取检测更新时出错!请检查网络连接!");
            MapUtil.getBooleanHashMap().put("IsLast", false);
            MapUtil.getBooleanHashMap().put("CheckVersionError", true);
        }
    }

    public static String getIpLocation(String ip) {
        try {
            if (ip.startsWith("127.")) {
                return "local";
            }

            URL url = new URL("https://opendata.baidu.com/api.php?query=" + ip +
                    "&co=&resource_id=6006&t=1433920989928&ie=utf8&oe=utf-8&format=json");
            URLConnection conn = url.openConnection();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                JSONObject json = JSONObject.parseObject(reader.readLine());
                JSONArray dataArray = json.getJSONArray("data");
                JSONObject dataJson = dataArray.getJSONObject(0);
                return dataJson.getString("location");
            } catch (IOException e) {
                return "Failed to get data";
            }
        } catch (IOException e) {
            return "Failed to connect";
        }
    }

    public static boolean canTPS() {
        try {
            Bukkit.class.getMethod("getTPS");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static String Placeholder(OfflinePlayer Player, String Message) {
        if (PluginLoader.INSTANCE.isHasPlaceholderAPI()) {
            Message = PlaceholderAPI.setPlaceholders(Player, Message);
        }
        return MessageUtil.colorMessage(Message);
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

    public static void opperSenderMessage(String Message, String PlayerName) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                if (!player.getName().equals(PlayerName)) {
                    player.sendMessage(MessageUtil.colorMessage(Message));
                }
            }
        }
    }

    public static GameMode getGamemode(int GameModeID) {
        if (GameModeID == 0) {
            return GameMode.SURVIVAL;
        }
        if (GameModeID == 1) {
            return GameMode.CREATIVE;
        }
        if (GameModeID == 2) {
            return GameMode.ADVENTURE;
        }
        if (GameModeID == 3) {
            return GameMode.SPECTATOR;
        }
        return null;
    }

    public static String getGamemodeString(int GameModeID) {
        if (GameModeID == 0) {
            return i18n("GameMode.Survival");
        }
        if (GameModeID == 1) {
            return i18n("GameMode.Creative");
        }
        if (GameModeID == 2) {
            return i18n("GameMode.Adventure");
        }
        if (GameModeID == 3) {
            return i18n("GameMode.Spectator");
        }
        return null;
    }

    public static void registerCommand(Plugin plugin, CommandExecutor commandExecutor, String description, String permission, String commandString) {
        PluginCommand command = getCommand(commandString, plugin);
        command.setDescription(description);

        if (permission != null) {
            command.setPermission(permission);
            command.setPermissionMessage(i18n("NoPermission"));
        }
        getCommandMap().register(plugin.getDescription().getName(), command);
        command.setExecutor(commandExecutor);
        if (commandExecutor instanceof TabExecutor) {
            command.setTabCompleter((TabCompleter) commandExecutor);
        }
    }

    private static PluginCommand getCommand(String name, Plugin plugin) {
        PluginCommand command = null;
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            command = c.newInstance(name, plugin);
        } catch (Exception ignored) {
        }
        return command;
    }

    private static CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (Exception ignored) {
        }
        return commandMap;
    }

    public static String getTps(int Time) {
        double[] tpsList = getTpsList();
        double TPS1 = BigDecimal.valueOf(tpsList[0]).setScale(1, RoundingMode.HALF_UP).doubleValue();
        double TPS5 = BigDecimal.valueOf(tpsList[1]).setScale(1, RoundingMode.HALF_UP).doubleValue();
        double TPS15 = BigDecimal.valueOf(tpsList[2]).setScale(1, RoundingMode.HALF_UP).doubleValue();

        if (TPS1 > 20.0D) TPS1 = 20.0D;
        if (TPS5 > 20.0D) TPS5 = 20.0D;
        if (TPS15 > 20.0D) TPS15 = 20.0D; //这里做限制 - 20

        switch (Time) {
            case 1:
                if (TPS1 > 18.0D) {
                    return "&a" + TPS1;
                } else if (TPS1 > 16.0D) {
                    return "&6" + TPS1;
                } else {
                    return "&c" + TPS1;
                }
            case 5:
                if (TPS5 > 18.0D) {
                    return "&a" + TPS5;
                } else if (TPS5 > 16.0D) {
                    return "&6" + TPS5;
                } else {
                    return "&c" + TPS5;
                }
            case 15:
                if (TPS15 > 18.0D) {
                    return "&a" + TPS15;
                } else if (TPS15 > 16.0D) {
                    return "&6" + TPS15;
                } else {
                    return "&c" + TPS15;
                }
            default:
                return "获取失败";
        }
    }

    public static double[] getTpsList() {
        return Bukkit.getTPS();
    }
    public static String sound(String soundKey) {
        return SoundFileData.getString(soundKey);
    }

    public static String i18n(String langKey, String... values) {
        String message = Objects.requireNonNull(LangFileData.getString(langKey));
        for (int i = 0; i < values.length; i++) {
            message = message.replaceAll("%" + (i + 1), values[i]);
        }
        return MessageUtil.colorMessage(message);
    }

    public static String getJoinMessage(Player player) {
        return getCustomMessage(player, "CustomJoinServerMessageSettings", "JoinMessage");
    }

    public static String getQuitMessage(Player player) {
        return getCustomMessage(player, "CustomQuitServerMessageSettings", "QuitMessage");
    }

    public static String getCustomMessage(Player player, String settingType, String messageType) {
        Map<Integer, String> messageList = new HashMap<>();
        List<Integer> weightList = new ArrayList<>();

        for (PermissionAttachmentInfo permInfo : player.getEffectivePermissions()) {
            String perm = permInfo.getPermission();
            if (perm.startsWith("mhdftools." + messageType)) {
                String group = perm.substring(("mhdftools." + messageType).length());
                int weight = PluginLoader.INSTANCE.getPlugin().getConfig().getInt(settingType + "." + group + ".Weight");
                messageList.put(weight, group);
                weightList.add(weight);
            }
        }

        if (!messageList.isEmpty()) {
            weightList.sort(Collections.reverseOrder());
            return Placeholder(player, PluginLoader.INSTANCE.getPlugin().getConfig().getString(settingType + "." + messageList.get(weightList.get(0)) + "." + messageType))
                    .replaceAll("%PlayerName%", NickUtil.getNickName(player.getName()));
        }

        return Placeholder(player, PluginLoader.INSTANCE.getPlugin().getConfig().getString(settingType + ".Default." + messageType))
                .replaceAll("%PlayerName%", NickUtil.getNickName(player.getName()));
    }

    public static BossBar getVanishBossBar() {
        if (VanishBossBar == null) {
            synchronized (SpigotUtil.class) {
                if (VanishBossBar == null) {
                    VanishBossBar = BossBar.bossBar(Component.text(i18n("Vanish.Bossbar")), 1f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
                }
            }
        }
        return VanishBossBar;
    }

    public static List<String> getPlayerList(boolean useBungeecordAPI) {
        List<String> onlinePlayerList;

        if (useBungeecordAPI && PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("BungeecordSettings.Enable")) {
            onlinePlayerList = new ArrayList<>(Arrays.asList(PlayerList));
        } else {
            onlinePlayerList = Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> !VanishUtil.getVanishList().contains(name))
                    .collect(Collectors.toList());
        }

        return onlinePlayerList;
    }

    public static void sendTitle(Player player, String titleString) {
        String[] title = titleString.split("\\|");
        player.sendTitle(Placeholder(player, title[0]), Placeholder(player, title[1]), Integer.parseInt(title[2]), Integer.parseInt(title[3]), Integer.parseInt(title[4]));
    }

    public static void playSound(Player player, String soundString) {
        String[] sound = soundString.split("\\|");
        player.playSound(player, org.bukkit.Sound.valueOf(sound[0]), Float.parseFloat(sound[1]), Float.parseFloat(sound[2]));
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