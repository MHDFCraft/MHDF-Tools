package cn.ChengZhiYa.ChengToolsReloaded.Ultis;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.BooleanHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.IntHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.LocationHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class multi {
    public static final String Version = "1.1.1";
    public static final HashMap<Object, GentleUnload> gentleUnloads = new HashMap<>();
    public static final Class<?> pluginClassLoader;
    public static final Field pluginClassLoaderPlugin;
    public static YamlConfiguration LangFileData;
    public static Field commandMapField;
    public static Field OldCommands;

    static {
        try {
            pluginClassLoader = Class.forName("org.bukkit.plugin.java.PluginClassLoader");
            pluginClassLoaderPlugin = pluginClassLoader.getDeclaredField("plugin");
            pluginClassLoaderPlugin.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private multi() {
    }

    public static void ClearAllHashMap() {
        try {
            StringHasMap.getHasMap().clear();
            IntHasMap.getHasMap().clear();
            BooleanHasMap.getHasMap().clear();
            LocationHasMap.getHasMap().clear();
        } catch (Exception ignored) {
        }
    }

    public static String getIpLocation(String Ip) {
        try {
            URL url = new URL("https://opendata.baidu.com/api.php?query=" + Ip + "&co=&resource_id=6006&t=1433920989928&ie=utf8&oe=utf-8&format=json");
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            JSONObject Json = JSONObject.parseObject(reader.readLine());
            JSONObject DataJson = JSONObject.parseObject(JSON.parseArray(Json.getString("data"), String.class).get(0));
            return DataJson.getString("location");
        } catch (IOException e) {
            return "获取失败";
        }
    }

    public static boolean isNewPaper() {
        boolean IsPaper = true;
        try {
            Bukkit.class.getMethod("getTPS");
        } catch (NoSuchMethodException e) {
            IsPaper = false;
        }
        return IsPaper;
    }

    public static String translateHexCodes(String message) {
        Pattern hexPattern = Pattern.compile("&#" + "([A-Fa-f0-9]{6})");
        return translate(hexPattern, message);
    }

    private static String translate(Pattern hex, String message) {
        Matcher matcher = hex.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 32);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer,
                    "§x§" + group.charAt(0) + "§" + group.charAt(1) + "§" + group.charAt(2)
                            + "§" + group.charAt(3) + "§" + group.charAt(4) + "§" + group.charAt(5));
        }
        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static String ChatColor(String Message) {
        Message = ChatColor.translateAlternateColorCodes('&', translateHexCodes(Message));
        return Message;
    }

    public static String ChatColor(OfflinePlayer Player, String Message) {
        if (ChengToolsReloaded.PAPI) {
            Message = PlaceholderAPI.setPlaceholders(Player, Message);
        }
        return ChatColor(Message);
    }

    public static String Sha256(String Message) {
        String encodeStr = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(Message.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuffer = new StringBuilder();
            for (final byte aByte : messageDigest.digest()) {
                final String temp = Integer.toHexString(aByte & 0xFF);
                if (temp.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(temp);
            }
            encodeStr = stringBuffer.toString().toUpperCase();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    public static boolean getLogin(Player player) {
        return StringHasMap.getHasMap().get(player.getName() + "_Login") != null;
    }

    public static void ColorLog(String Message) {
        CommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(ChatColor(Message));
    }

    public static Plugin getPluginName(String name) {
        return Bukkit.getPluginManager().getPlugin(name);
    }

    public static void reload(Plugin plugin) {
        if (plugin != null) {
            unload(plugin);
            load(plugin);
        }
    }

    private static void load(Plugin plugin) {
        load(plugin.getName());
    }

    public static String load(String name) {
        Plugin target = null;
        boolean paperLoaded = false;

        File pluginDir = new File("plugins");

        if (!pluginDir.isDirectory()) {
            return name;
        }

        File PluginFile = new File(pluginDir, name + ".jar");

        if (!PluginFile.isFile())
            for (File Files : Objects.requireNonNull(pluginDir.listFiles()))
                if (Files.getName().endsWith(".jar")) try {
                    PluginDescriptionFile desc = ChengToolsReloaded.instance.getPluginLoader().getPluginDescription(Files);
                    if (desc.getName().equalsIgnoreCase(name)) {
                        PluginFile = Files;
                        break;
                    }
                } catch (InvalidDescriptionException e) {
                    return name;
                }

        try {
            Object paperPluginManagerImpl = Class.forName("io.papermc.paper.plugin.manager.PaperPluginManagerImpl").getMethod("getInstance").invoke(null);

            Field instanceManagerF = paperPluginManagerImpl.getClass().getDeclaredField("instanceManager");
            instanceManagerF.setAccessible(true);
            Object instanceManager = instanceManagerF.get(paperPluginManagerImpl);

            Method loadMethod = instanceManager.getClass().getMethod("loadPlugin", Path.class);
            loadMethod.setAccessible(true);
            target = (Plugin) loadMethod.invoke(instanceManager, PluginFile.toPath());

            Method enableMethod = instanceManager.getClass().getMethod("enablePlugin", Plugin.class);
            enableMethod.setAccessible(true);
            enableMethod.invoke(instanceManager, target);

            paperLoaded = true;
        } catch (Exception ignore) {
        }

        if (!paperLoaded) {
            try {
                target = Bukkit.getPluginManager().loadPlugin(PluginFile);
            } catch (InvalidDescriptionException e) {
                return "这个插件的描述文件无效!";
            } catch (InvalidPluginException e) {
                return "这个插件不存在!";
            }

            Objects.requireNonNull(target).onLoad();
            Bukkit.getPluginManager().enablePlugin(Objects.requireNonNull(target));
        }

        Plugin finalTarget = target;
        //Bukkit.getScheduler().runTaskLater(ChengToolsReloaded.instance, () -> {
        //loadCommands(finalTarget);
        //}, 10L);
        return null;
    }

    public static void loadCommands(Plugin plugin) {
        Map<String, Command> knownCommands = getOldCommands();
        List<Map.Entry<String, Command>> commands = Objects.requireNonNull(knownCommands).entrySet().stream()
                .filter(s -> {
                    if (s.getKey().contains(":"))
                        return s.getKey().split(":")[0].equalsIgnoreCase(plugin.getName());
                    else {
                        ClassLoader cl = s.getValue().getClass().getClassLoader();
                        try {
                            return cl.getClass() == pluginClassLoader && pluginClassLoaderPlugin.get(cl) == plugin;
                        } catch (IllegalAccessException e) {
                            return false;
                        }
                    }
                }).collect(Collectors.toList());

        for (Map.Entry<String, Command> entry : commands) {
            String alias = entry.getKey();
            Command command = entry.getValue();
            new BukkitCommandWrap().wrap(command, alias);
        }

        new BukkitCommandWrap().sync();

        if (Bukkit.getOnlinePlayers().size() >= 1)
            for (Player player : Bukkit.getOnlinePlayers())
                player.updateCommands();
    }

    public static Map<String, Command> getOldCommands() {
        if (commandMapField == null) try {
            commandMapField = Class.forName("org.bukkit.craftbukkit." + getNMSVersion() + ".CraftServer").getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            return null;
        }
        SimpleCommandMap commandMap;
        try {
            commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            return null;
        }
        if (OldCommands == null) try {
            OldCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            OldCommands.setAccessible(true);
        } catch (NoSuchFieldException e) {
            return null;
        }
        try {
            return (Map<String, Command>) OldCommands.get(commandMap);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static String getNMSVersion() {
        try {
            return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static HashMap<Object, GentleUnload> getGentleUnloads() {
        return new HashMap<>(gentleUnloads);
    }

    public static void unload(Plugin plugin) {
        String name = plugin.getName();
        if (!getGentleUnloads().containsKey(plugin)) {
            //loadCommands(plugin);
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.disablePlugin(plugin);
            pluginManager.disablePlugin(plugin);
        } else {
            GentleUnload gentleUnload = getGentleUnloads().get(plugin);
            if (!gentleUnload.askingForGentleUnload()) {
                return;
            }
        }

        ClassLoader cl = plugin.getClass().getClassLoader();
        if (cl instanceof URLClassLoader) {
            try {
                Field pluginField = cl.getClass().getDeclaredField("plugin");
                pluginField.setAccessible(true);
                pluginField.set(cl, null);
                Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
                pluginInitField.setAccessible(true);
                pluginInitField.set(cl, null);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            try {
                ((URLClassLoader) cl).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Object paperPluginManagerImpl = Class.forName("io.papermc.paper.plugin.manager.PaperPluginManagerImpl").getMethod("getInstance").invoke(null);
            Field instanceManagerField = paperPluginManagerImpl.getClass().getDeclaredField("instanceManager");
            instanceManagerField.setAccessible(true);
            Object instanceManager = instanceManagerField.get(paperPluginManagerImpl);
            Field lookupNamesField = instanceManager.getClass().getDeclaredField("lookupNames");
            lookupNamesField.setAccessible(true);
            Map<String, Object> lookupNames = (Map<String, Object>) lookupNamesField.get(instanceManager);
            Method disableMethod = instanceManager.getClass().getMethod("disablePlugin", Plugin.class);
            disableMethod.setAccessible(true);
            disableMethod.invoke(instanceManager, plugin);
            lookupNames.remove(plugin.getName().toLowerCase());
            Field pluginListField = instanceManager.getClass().getDeclaredField("plugins");
            pluginListField.setAccessible(true);
            List<Plugin> pluginList = (List<Plugin>) pluginListField.get(instanceManager);
            pluginList.remove(plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();
    }
    public static void OpSendMessage(String Message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                player.sendMessage(ChatColor(Message));
            }
        }
    }

    public static void OpSendMessage(String Message, String PlayerName) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                if (!player.getName().equals(PlayerName)) {
                    player.sendMessage(ChatColor(Message));
                }
            }
        }
    }

    public static List<String> getPluginNames(boolean fullName) {
        List<String> plugins = new ArrayList<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
            plugins.add(fullName ? plugin.getDescription().getFullName() : plugin.getName());
        return plugins;
    }

    public static GameMode GetGamemode(int GameModeID) {
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

    public static String GetGamemodeString(int GameModeID) {
        if (GameModeID == 0) {
            return getLang("Gamemode.Survival");
        }
        if (GameModeID == 1) {
            return getLang("Gamemode.Creative");
        }
        if (GameModeID == 2) {
            return getLang("Gamemode.Adventure");
        }
        if (GameModeID == 3) {
            return getLang("Gamemode.Spectator");
        }
        return null;
    }

    public static void registerCommand(Plugin plugin, CommandExecutor commandExecutor, String description, String... aliases) {
        PluginCommand command = getCommand(aliases[0], plugin);
        command.setAliases(Arrays.asList(aliases));
        command.setDescription(description);
        getCommandMap().register(plugin.getDescription().getName(), command);
        command.setExecutor(commandExecutor);
    }

    public static void registerCommand(Plugin plugin, CommandExecutor commandExecutor, TabExecutor tabExecutor, String description, String... aliases) {
        PluginCommand command = getCommand(aliases[0], plugin);
        command.setAliases(Arrays.asList(aliases));
        command.setDescription(description);
        getCommandMap().register(plugin.getDescription().getName(), command);
        command.setExecutor(commandExecutor);
        command.setTabCompleter(tabExecutor);
    }

    private static PluginCommand getCommand(String name, Plugin plugin) {
        PluginCommand command = null;
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            command = c.newInstance(name, plugin);
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commandMap;
    }

    public static String getTps(int Time) {
        double TPS1 = BigDecimal.valueOf(getTpsList()[0]).setScale(1, RoundingMode.HALF_UP).doubleValue();
        double TPS5 = BigDecimal.valueOf(getTpsList()[1]).setScale(1, RoundingMode.HALF_UP).doubleValue();
        double TPS15 = BigDecimal.valueOf(getTpsList()[2]).setScale(1, RoundingMode.HALF_UP).doubleValue();
        if (TPS1 > 20.0D) {
            TPS1 = 20.0D;
        }
        if (TPS5 > 20.0D) {
            TPS5 = 20.0D;
        }
        if (TPS15 > 20.0D) {
            TPS15 = 20.0D;
        }
        if (Time == 1) {
            if (TPS1 > 18.0D) {
                return "&a" + TPS1;
            }
            if (TPS1 > 16.0D) {
                return "&6" + TPS1;
            }
            if (TPS1 < 16.0D) {
                return "&c" + TPS1;
            }
        }
        if (Time == 5) {
            if (TPS5 > 18.0D) {
                return "&a" + TPS5;
            }
            if (TPS5 > 16.0D) {
                return "&6" + TPS5;
            }
            if (TPS5 < 16.0D) {
                return "&c" + TPS5;
            }
        }
        if (Time == 15) {
            if (TPS15 > 18.0D) {
                return "&a" + TPS15;
            }
            if (TPS15 > 16.0D) {
                return "&6" + TPS15;
            }
            if (TPS15 < 16.0D) {
                return "&c" + TPS15;
            }
        }
        return "获取失败";
    }

    public static double[] getTpsList() {
        return Bukkit.getTPS();
    }

    public static String[] GetPlt(OfflinePlayer player) {
        File TitleData = new File(ChengToolsReloaded.instance.getDataFolder() + "/TitleData.yml");
        YamlConfiguration TitleFileData = YamlConfiguration.loadConfiguration(TitleData);
        String DefaultPrefix = ChengToolsReloaded.instance.getConfig().getString("PlayerTitleSettings.DefaultPrefix");
        String DefaultSuffix = ChengToolsReloaded.instance.getConfig().getString("PlayerTitleSettings.DefaultSuffix");
        String Prefix = ChengToolsReloaded.instance.getConfig().getString("PlayerTitleSettings.Prefix");
        String Suffix = ChengToolsReloaded.instance.getConfig().getString("PlayerTitleSettings.Suffix");
        String PlayerPrefix;
        String PlayerSuffix;
        if (TitleFileData.getString(player.getName() + "_Prefix") == null) {
            if (!Objects.equals(DefaultPrefix, "")) {
                PlayerPrefix = Prefix + DefaultPrefix + Suffix;
            } else {
                PlayerPrefix = "";
            }
        } else {
            PlayerPrefix = Prefix + TitleFileData.getString(player.getName() + "_Prefix") + Suffix;
        }
        if (TitleFileData.getString(player.getName() + "_Suffix") == null) {
            if (!Objects.equals(DefaultSuffix, "")) {
                PlayerSuffix = Prefix + DefaultSuffix + Suffix;
            } else {
                PlayerSuffix = "";
            }
        } else {
            PlayerSuffix = Prefix + TitleFileData.getString(player.getName() + "_Suffix") + Suffix;
        }
        return new String[]{PlayerPrefix, PlayerSuffix};
    }

    public static String getLang(String LangVaule) {
        return ChatColor(Objects.requireNonNull(LangFileData.getString(LangVaule)));
    }

    public static String getLang(String LangVaule, String Vaule1) {
        return ChatColor(Objects.requireNonNull(LangFileData.getString(LangVaule))
                .replaceAll("%1", Vaule1));
    }

    public static String getLang(String LangVaule, String Vaule1, String Vaule2) {
        return ChatColor(Objects.requireNonNull(LangFileData.getString(LangVaule))
                .replaceAll("%1", Vaule1)
                .replaceAll("%2", Vaule2));
    }

    public static String getLang(String LangVaule, String Vaule1, String Vaule2, String Vaule3) {
        return ChatColor(Objects.requireNonNull(LangFileData.getString(LangVaule))
                .replaceAll("%1", Vaule1)
                .replaceAll("%2", Vaule2)
                .replaceAll("%3", Vaule3));
    }

    public static String getLang(String LangVaule, String Vaule1, String Vaule2, String Vaule3, String Vaule4) {
        return ChatColor(Objects.requireNonNull(LangFileData.getString(LangVaule))
                .replaceAll("%1", Vaule1)
                .replaceAll("%2", Vaule2)
                .replaceAll("%3", Vaule3)
                .replaceAll("%4", Vaule4));
    }
}
