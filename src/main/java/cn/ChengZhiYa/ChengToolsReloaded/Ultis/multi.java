package cn.ChengZhiYa.ChengToolsReloaded.Ultis;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.*;
import cn.ChengZhiYa.ChengToolsReloaded.Plugman.GentleUnload;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.json.JSONArray;

public class multi {
    protected static final HashMap<Object, GentleUnload> gentleUnloads = new HashMap<>();
    private static final Class<?> pluginClassLoader;
    private static final Field pluginClassLoaderPlugin;
    private static Field commandMapField;
    private static Field knownCommandsField;
    private static String nmsVersion = null;
    public static String Version = "1.0.8";

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
            StringHashMap.Clear();
            IntHashMap.Clear();
            BooleanHashMap.Clear();
            LocationHashMap.Clear();
            ScoreboardHashMap.Clear();
            ObjectiveHashMap.Clear();
        }catch (Exception ignored) {}
    }

    public static boolean CheckVersion() {
        try {
            URL url1 = new URL("https://chengzhinb.github.io/Cheng-Tools-Reloaded-CheckVersion");
            URLConnection urlConnection = url1.openConnection();
            urlConnection.addRequestProperty("User-Agent", "Mozilla");
            urlConnection.setReadTimeout(30000);
            urlConnection.setConnectTimeout(30000);
            InputStream in = url1.openStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader bufr = new BufferedReader(isr);
            String str;
            StringBuilder stringBuilder = new StringBuilder();
            while ((str = bufr.readLine()) != null) {
                stringBuilder.append(str);
            }
            String NewVersion = stringBuilder.toString().replace("<!--", "").replace("-->", "");
            in.close();
            isr.close();
            bufr.close();
            return Version.equals(NewVersion);
        } catch (Exception i) {
            i.printStackTrace();
            ColorLog("[Cheng-Tools-Reloaded]获取检测更新时出错!请检查网络连接!");
            return false;
        }
    }

    public static String getIpLocation(String Ip) {
        try {
            URL url = new URL("http://opendata.baidu.com/api.php?query=" + Ip +"&co=&resource_id=6006&t=1433920989928&ie=utf8&oe=utf-8&format=json");;
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            JSONObject jsStr = new JSONObject(result.toString());
            JSONArray jsData = (JSONArray) jsStr.get("data");
            JSONObject data= (JSONObject) jsData.get(0);
            return (String) data.get("location");
        } catch (IOException e) {
            return "获取失败";
        }
    }

    public static boolean isPaper() {
        boolean IsPaper = true;
        try {
            Bukkit.class.getMethod("getTPS");
        } catch (NoSuchMethodException e) {
            IsPaper = false;
        }
        return IsPaper;
    }

    public static String ChatColor(String Message) {
        Message = ChatColor.translateAlternateColorCodes('&', Message);
        return Message;
    }

    public static String ChatColor(OfflinePlayer Player, String Message) {
        Message = ChatColor.translateAlternateColorCodes('&', Message);
        if (main.PAPI) {
            Message = PlaceholderAPI.setPlaceholders(Player, Message);
        }
        return Message;
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
        return StringHashMap.Get(player.getName() + "_Login") != null;
    }

    public static void ColorLog(String Message) {
        CommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(ChatColor(Message));
    }

    public static String consolidateStrings(String[] args, int StartInt) {
        StringBuilder ret = new StringBuilder(args[StartInt]);
        if (args.length > (StartInt + 1)) {
            for (int i = (StartInt + 1); i < args.length; i++)
                ret.append(" ").append(args[i]);
        }
        return ret.toString();
    }

    public static Plugin getPluginName(String[] args, int StartInt) {
        return getPluginName(consolidateStrings(args, StartInt));
    }

    public static Plugin getPluginName(String name) {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
            if (name.equalsIgnoreCase(plugin.getName())) return plugin;
        return null;
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

        File pluginDir = new File("plugins");

        if (!pluginDir.isDirectory())
            return name;

        File pluginFile = new File(pluginDir, name + ".jar");

        if (!pluginFile.isFile()) for (File f : Objects.requireNonNull(pluginDir.listFiles()))
            if (f.getName().endsWith(".jar")) try {
                PluginDescriptionFile desc = main.main.getPluginLoader().getPluginDescription(f);
                if (desc.getName().equalsIgnoreCase(name)) {
                    pluginFile = f;
                    break;
                }
            } catch (InvalidDescriptionException e) {
                return name;
            }

        try {
            target = Bukkit.getPluginManager().loadPlugin(pluginFile);
        } catch (InvalidDescriptionException e) {
            return "这个插件的描述文件无效!";
        } catch (InvalidPluginException e) {
            return "这个插件不存在!";
        }

        Objects.requireNonNull(target).onLoad();
        Bukkit.getPluginManager().enablePlugin(target);

        Plugin finalTarget = target;
        Bukkit.getScheduler().runTaskLater(main.main, () -> {
            Map<String, Command> knownCommands = getKnownCommands();
            List<Map.Entry<String, Command>> commands = Objects.requireNonNull(knownCommands).entrySet().stream()
                    .filter(s -> {
                        if (s.getKey().contains(":")) {
                            return s.getKey().split(":")[0].equalsIgnoreCase(finalTarget.getName());
                        } else {
                            ClassLoader cl = s.getValue().getClass().getClassLoader();
                            try {
                                return cl.getClass() == pluginClassLoader && pluginClassLoaderPlugin.get(cl) == finalTarget;
                            } catch (IllegalAccessException e) {
                                return false;
                            }
                        }
                    })
                    .collect(Collectors.toList());

            if (Bukkit.getOnlinePlayers().size() >= 1)
                for (Player player : Bukkit.getOnlinePlayers()) player.updateCommands();
        }, 10L);

        return null;
    }

    private static String getNmsVersion() {
        if (nmsVersion == null) try {
            nmsVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            nmsVersion = null;
        }
        return nmsVersion;
    }

    public static HashMap<Object, GentleUnload> getGentleUnloads() {
        return new HashMap<>(gentleUnloads);
    }

    public static Map<String, Command> getKnownCommands() {
        if (commandMapField == null) try {
            commandMapField = Class.forName("org.bukkit.craftbukkit." + getNmsVersion() + ".CraftServer").getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        SimpleCommandMap commandMap;
        try {
            commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (knownCommandsField == null) try {
            knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }

        Map<String, Command> knownCommands;

        try {
            knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return knownCommands;
    }

    public static void unload(Plugin plugin) {
        String name = plugin.getName();

        if (!getGentleUnloads().containsKey(plugin)) {
            PluginManager pluginManager = Bukkit.getPluginManager();

            SimpleCommandMap commandMap = null;

            java.util.List<Plugin> plugins = null;

            Map<String, Plugin> names = null;
            Map<String, Command> commands = null;
            Map<org.bukkit.event.Event, SortedSet<RegisteredListener>> listeners = null;

            boolean reloadlisteners = true;

            if (pluginManager != null) {

                pluginManager.disablePlugin(plugin);

                try {

                    Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
                    pluginsField.setAccessible(true);
                    plugins = (List<Plugin>) pluginsField.get(pluginManager);

                    Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
                    lookupNamesField.setAccessible(true);
                    names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);

                    try {
                        Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
                        listenersField.setAccessible(true);
                        listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
                    } catch (Exception e) {
                        reloadlisteners = false;
                    }

                    Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
                    commandMapField.setAccessible(true);
                    commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);

                    Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                    knownCommandsField.setAccessible(true);
                    commands = (Map<String, Command>) knownCommandsField.get(commandMap);

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                }

            }

            pluginManager.disablePlugin(plugin);

            if (listeners != null && reloadlisteners)
                for (SortedSet<RegisteredListener> set : listeners.values())
                    set.removeIf(value -> value.getPlugin() == plugin);

            if (commandMap != null)
                for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<String, Command> entry = it.next();
                    if (entry.getValue() instanceof PluginCommand) {
                        PluginCommand c = (PluginCommand) entry.getValue();
                        if (c.getPlugin() == plugin) {
                            c.unregister(commandMap);
                            it.remove();
                        }
                    } else try {
                        Field pluginField = Arrays.stream(entry.getValue().getClass().getDeclaredFields()).filter(field -> Plugin.class.isAssignableFrom(field.getType())).findFirst().orElse(null);
                        if (pluginField != null) {
                            Plugin owningPlugin;
                            try {
                                pluginField.setAccessible(true);
                                owningPlugin = (Plugin) pluginField.get(entry.getValue());
                                if (owningPlugin.getName().equalsIgnoreCase(plugin.getName())) {
                                    entry.getValue().unregister(commandMap);
                                    it.remove();
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }

            if (plugins != null)
                plugins.remove(plugin);

            if (names != null)
                names.remove(name);
        } else {
            GentleUnload gentleUnload = getGentleUnloads().get(plugin);
            if (!gentleUnload.askingForGentleUnload())
                return;
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

            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(multi.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {

                ((URLClassLoader) cl).close();
            } catch (IOException ex) {
                Logger.getLogger(multi.class.getName()).log(Level.SEVERE, null, ex);
            }

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

    public static void registerCommand(Plugin plugin, CommandExecutor commandExecutor, String description, String... aliases) {
        PluginCommand command = multi.getCommand(aliases[0], plugin);
        command.setAliases(Arrays.asList(aliases));
        command.setDescription(description);
        multi.getCommandMap().register(plugin.getDescription().getName(), command);
        command.setExecutor(commandExecutor);
    }

    public static void registerCommand(Plugin plugin, CommandExecutor commandExecutor, TabExecutor tabExecutor, String description, String... aliases) {
        PluginCommand command = multi.getCommand(aliases[0], plugin);
        command.setAliases(Arrays.asList(aliases));
        command.setDescription(description);
        multi.getCommandMap().register(plugin.getDescription().getName(), command);
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
        File TitleData = new File(main.main.getDataFolder() + "/TitleData.yml");
        YamlConfiguration TitleFileData = YamlConfiguration.loadConfiguration(TitleData);
        String DefaultPrefix = main.main.getConfig().getString("PlayerTitleSettings.DefaultPrefix");
        String DefaultSuffix = main.main.getConfig().getString("PlayerTitleSettings.DefaultSuffix");
        String Prefix = main.main.getConfig().getString("PlayerTitleSettings.Prefix");
        String Suffix = main.main.getConfig().getString("PlayerTitleSettings.Suffix");
        String PlayerPrefix;
        String PlayerSuffix;
        if (TitleFileData.getString(player.getName() + "_Prefix") == null) {
            if (!Objects.equals(DefaultPrefix, "")) {
                PlayerPrefix =  Prefix + DefaultPrefix + Suffix;
            }
            PlayerPrefix =  "";
        } else {
            PlayerPrefix = Prefix + TitleFileData.getString(player.getName() + "_Prefix") + Suffix;
        }
        if (TitleFileData.getString(player.getName() + "_Suffix") == null) {
            if (!Objects.equals(DefaultSuffix, "")) {
                PlayerSuffix = Prefix + DefaultSuffix + Suffix;
            }
            PlayerSuffix = "";
        } else {
            PlayerSuffix = Prefix + TitleFileData.getString(player.getName() + "_Suffix") + Suffix;
        }
        return new String[]{PlayerPrefix, PlayerSuffix};
    }
}
