package cn.ChengZhiYa.MHDFTools.utils.menu;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.Placeholder;

public final class MenuUtil {
    public static ItemStack getItemStack(String type, String displayName, List<String> lore, Integer customModelData, Integer amount) {
        if (type != null) {
            if (type.startsWith("{RandomBed}")) {
                return getRandomBed(displayName, lore, customModelData, amount);
            }
            if (type.startsWith("PlayerHead-")) {
                return getPlayerHead(type.replaceAll("PlayerHead-", ""), displayName, lore, customModelData, amount);
            }
            return getItemStack(Material.getMaterial(type.toUpperCase(Locale.ROOT)), displayName, lore, customModelData, amount);
        } else {
            return new ItemStack(Material.AIR);
        }
    }

    public static ItemStack getItemStack(Material material, String displayName, List<String> lore, Integer customModelData, Integer amount) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (displayName != null) {
            meta.setDisplayName(MessageUtil.colorMessage(displayName));
        }
        if (lore != null && !lore.isEmpty()) {
            List<String> lores = new ArrayList<>();
            lore.forEach(s -> lores.add(MessageUtil.colorMessage(s)));
            meta.setLore(lores);
        }
        if (customModelData != null) {
            meta.setCustomModelData(customModelData);
        }
        item.setItemMeta(meta);
        if (amount != null) {
            item.setAmount(amount);
        }
        return item;
    }

    public static ItemStack getRandomBed(String displayName, List<String> lore, Integer customModelData, Integer amount) {
        List<Material> bedList = Arrays.asList(
                Material.BLACK_BED,
                Material.BLUE_BED,
                Material.BROWN_BED,
                Material.CYAN_BED,
                Material.GREEN_BED,
                Material.LIGHT_BLUE_BED,
                Material.LIGHT_GRAY_BED,
                Material.MAGENTA_BED,
                Material.ORANGE_BED,
                Material.LIME_BED,
                Material.PINK_BED,
                Material.PURPLE_BED,
                Material.RED_BED,
                Material.WHITE_BED,
                Material.YELLOW_BED
        );
        return getItemStack(bedList.get(new Random().nextInt(bedList.size())), displayName, lore, customModelData, amount);
    }

    public static ItemStack getPlayerHead(String playerName, String displayName, List<String> lore, Integer customModelData, Integer amount) {
        ItemStack item = getItemStack(Material.PLAYER_HEAD, displayName, lore, customModelData, amount);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (displayName != null) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        }
        item.setItemMeta(meta);
        return item;
    }

    public static YamlConfiguration getMenu(String MenuFile) {
        return YamlConfiguration.loadConfiguration(new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "Menus/" + MenuFile));
    }

    public static String getMenuFromItem(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        return Objects.requireNonNull(nbtItem.getCompound("MHDFTools")).getString("menu");
    }

    public static String getItemNameFromItem(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        return Objects.requireNonNull(nbtItem.getCompound("MHDFTools")).getString("item");
    }

    public static List<String> getCustomMenuList() {
        List<String> MenuList = new ArrayList<>();
        try (Stream<Path> File = Files.walk(new File(PluginLoader.INSTANCE.getPlugin().getDataFolder(), "Menus").toPath())) {
            MenuList = File.filter(Files::isRegularFile).map(Path::toString).map(FileName -> FileName
                    .replaceAll("plugins\\\\MHDF-Tools\\\\Menus\\\\", "")
                    .replaceAll("plugins/MHDF-Tools/Menus/", "")
            ).collect(Collectors.toList());
        } catch (IOException ignored) {
        }
        MenuList.remove("HomeMenu.yml");
        return MenuList;
    }

    public static boolean ifCustomMenu(Player player, String title) {
        for (String MenuFileName : getCustomMenuList()) {
            YamlConfiguration Menu = getMenu(MenuFileName);
            if (Placeholder(player, Menu.getString("menu.Title")).equals(Placeholder(player, title))) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getMenuItem(String menuFileName, String itemID, String type, String displayName, List<String> lore, Integer customModelData, Integer amount) {
        NBTItem nbtItem = new NBTItem(getItemStack(type, displayName, lore, customModelData, amount));
        NBTCompound compound = nbtItem.addCompound("MHDFTools");
        compound.setString("menu", menuFileName);
        compound.setString("item", itemID);
        return nbtItem.getItem();
    }

    public static List<Integer> getSlot(List<String> slotStringList) {
        List<Integer> slotList = new ArrayList<>();

        for (String slot : slotStringList) {
            if (slot.contains("-")) {
                String[] slots = slot.split("-");
                int start = Integer.parseInt(slots[0]);
                int end = Integer.parseInt(slots[1]) + 1;
                for (int i = start; i < end; i++) {
                    slotList.add(i);
                }
            } else {
                slotList.add(Integer.valueOf(slot));
            }
        }
        return slotList;
    }

    public static void setMenuItem(Inventory menu, String menuFileName, String itemID, String type, String displayName, List<String> lore, Integer customModelData, Integer amount, List<String> slotList) {
        ItemStack item = getMenuItem(menuFileName, itemID, type, displayName, lore, customModelData, amount);

        if (!slotList.isEmpty()) {
            for (Integer slot : getSlot(slotList)) {
                menu.setItem(slot, item);
            }
        } else {
            menu.addItem(item);
        }
    }

    public static List<String> ifAllowClick(Player player, String menuFileName, String itemID, boolean shiftClick) {
        String requirmentType = shiftClick ? "ShiftClickRequirements" : "ClickRequirements";

        if (getMenu(menuFileName).getConfigurationSection("menu.ItemList." + itemID + "." + requirmentType) != null) {
            boolean allow = true;
            for (String requirement : Objects.requireNonNull(getMenu(menuFileName).getConfigurationSection("menu.ItemList." + itemID + "." + requirmentType)).getKeys(false)) {
                String type = getMenu(menuFileName).getString("menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Type");
                String input = getMenu(menuFileName).getString("menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Input");
                String output = getMenu(menuFileName).getString("menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Output");
                if (type != null) {
                    switch (type) {
                        case "<": {
                            if (input != null && output != null) {
                                allow = Double.parseDouble(input) < Double.parseDouble(output);
                            }
                            continue;
                        }
                        case "<=": {
                            if (input != null && output != null) {
                                allow = Double.parseDouble(input) <= Double.parseDouble(output);
                            }
                            continue;
                        }
                        case "==": {
                            if (input != null && output != null) {
                                allow = Double.parseDouble(input) == Double.parseDouble(output);
                            }
                            continue;
                        }
                        case ">": {
                            if (input != null && output != null) {
                                allow = Double.parseDouble(input) > Double.parseDouble(output);
                            }
                            continue;
                        }
                        case ">=": {
                            if (input != null && output != null) {
                                allow = Double.parseDouble(input) >= Double.parseDouble(output);
                            }
                            continue;
                        }
                        case "Permission": {
                            if (input != null) {
                                allow = player.hasPermission(input);
                            }
                            continue;
                        }
                        case "HasItem": {
                            String itemType = getMenu(menuFileName).getString("menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Input.Type");
                            String itemDisplayName = getMenu(menuFileName).getString("menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Input.DisplayName");
                            List<String> itemLore = new ArrayList<>();
                            getMenu(menuFileName).getStringList("menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Input.Lore").forEach(s -> {
                                itemLore.add(Placeholder(player, s));
                            });
                            Integer itemCustomModelData = (Integer) getMenu(menuFileName).get("menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Input.CustomModelData");
                            Integer amount = (Integer) getMenu(menuFileName).get("menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Input.Amount");

                            for (ItemStack playerInvItem : player.getInventory().getContents()) {
                                if (playerInvItem != null) {
                                    if (itemType != null) {
                                        allow = playerInvItem.getType() == Material.getMaterial(itemType);
                                    }
                                    if (amount != null) {
                                        allow = playerInvItem.getAmount() >= amount;
                                    }
                                    if (playerInvItem.getItemMeta() != null) {
                                        if (itemDisplayName != null && playerInvItem.getItemMeta().hasDisplayName()) {
                                            allow = playerInvItem.getItemMeta().getDisplayName().equals(Placeholder(player, itemDisplayName));
                                        }
                                        if (!itemLore.isEmpty()) {
                                            allow = playerInvItem.getItemMeta().getLore() == itemLore;
                                        }
                                        if (playerInvItem.getItemMeta().hasCustomModelData()) {
                                            allow = playerInvItem.getItemMeta().getLore() == itemLore;
                                        }
                                        if (itemCustomModelData != null && playerInvItem.getItemMeta().hasCustomModelData()) {
                                            allow = playerInvItem.getItemMeta().getCustomModelData() == itemCustomModelData;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!allow) {
                    return getMenu(menuFileName).getStringList("menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".DenyAction");
                }
            }
        }
        return new ArrayList<>();
    }

    public static void runAction(CommandSender sender, String menuFileName, String[] action) {
        switch (action[0]) {
            case "[console_args]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    MapUtil.getStringHashMap().put(sender.getName() + "_ArgsRunCommand", "consoleMessage|" + menuFileName + "|" + action[1] + "|" + action[2]);
                    player.closeInventory();
                }
                break;
            }
            case "[player_args]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    MapUtil.getStringHashMap().put(player.getName() + "_ArgsRunCommand", "player|" + menuFileName + "|" + action[1] + "|" + action[2]);
                    player.closeInventory();
                }
                break;
            }
            case "[player]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    Bukkit.getRegionScheduler().run(PluginLoader.INSTANCE.getPlugin(), player.getLocation(), task -> player.chat("/" + Placeholder(player, action[1])));
                } else {
                    Bukkit.getScheduler().runTask(PluginLoader.INSTANCE.getPlugin(), () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Placeholder(null, action[1])));
                }
                break;
            }
            case "[consoleMessage]": {
                Bukkit.getScheduler().runTask(PluginLoader.INSTANCE.getPlugin(), () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Placeholder(null, action[1])));
                break;
            }
            case "[playsound]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    try {
                        player.playSound(player, Sound.valueOf(action[1]), Float.parseFloat(action[2]), Float.parseFloat(action[3]));
                    } catch (Exception e) {
                        player.playSound(player, action[1], Float.parseFloat(action[2]), Float.parseFloat(action[3]));
                    }
                    break;
                }
            }
            case "[message]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    sender.sendMessage(Placeholder(player, action[1]).replaceAll(action[0] + "\\|", "").replaceAll("\\|", "\n"));
                } else {
                    sender.sendMessage(Placeholder(null, action[1]).replaceAll(action[0] + "\\|", "").replaceAll("\\|", "\n"));
                }
                break;
            }
            case "[broadcast]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    Bukkit.broadcast(Placeholder(player, action[1]).replaceAll(action[0] + "\\|", "").replaceAll("\\|", "\n"), "MHDFTools.Broadcast");
                } else {
                    Bukkit.broadcast(Placeholder(null, action[1]).replaceAll(action[0] + "\\|", "").replaceAll("\\|", "\n"), "MHDFTools.Broadcast");
                }
                break;
            }
            case "[title]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.sendTitle(Placeholder(player, action[1]), Placeholder(player, action[2]), Integer.parseInt(action[3]), Integer.parseInt(action[4]), Integer.parseInt(action[5]));
                }
                break;
            }
            case "[actionbar]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Placeholder(player, action[1])));
                }
                break;
            }
            case "[close]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.closeInventory();
                }
                break;
            }
            case "[openmenu]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    openMenu(player, menuFileName);
                }
                break;
            }
            case "[teleport]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.teleport(
                            new Location(
                                    Bukkit.getWorld(action[1]),
                                    Double.parseDouble(action[2]),
                                    Double.parseDouble(action[3]),
                                    Double.parseDouble(action[4]),
                                    Float.parseFloat(action[5]),
                                    Float.parseFloat(action[6])
                            )
                    );
                }
            }
            default:
                MessageUtil.colorMessage("&c[MHDF-Tools]不存在" + action[0] + "这个操作");
        }
    }

    public static void runAction(CommandSender player, String menuFileName, List<String> actionList) {
        for (String actions : actionList) {
            runAction(player, menuFileName, actions.split("\\|"));
        }
    }

    public static void openMenu(Player player, String menuFileName) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            String title = Placeholder(player, getMenu(menuFileName).getString("menu.Title"));
            Inventory menu = Bukkit.createInventory(player, getMenu(menuFileName).getInt("menu.Size"), title);

            for (String itemID : Objects.requireNonNull(getMenu(menuFileName).getConfigurationSection("menu.ItemList")).getKeys(false)) {
                String type = getMenu(menuFileName).getString("menu.ItemList." + itemID + ".Type");
                String displayName = getMenu(menuFileName).getString("menu.ItemList." + itemID + ".DisplayName");
                Integer customModelData = getMenu(menuFileName).getObject("menu.ItemList." + itemID + ".CustomModelData", Integer.class);
                Integer amount = getMenu(menuFileName).getObject("menu.ItemList." + itemID + ".Amount", Integer.class);
                List<String> lore = new ArrayList<>();
                getMenu(menuFileName).getStringList("menu.ItemList." + itemID + ".Lore").forEach(s -> lore.add(Placeholder(player, s)));

                List<String> slotList = new ArrayList<>();
                if (getMenu(menuFileName).getString("menu.ItemList." + itemID + ".Slot") != null) {
                    slotList.add(getMenu(menuFileName).getString("menu.ItemList." + itemID + ".Slot"));
                } else {
                    slotList.addAll(getMenu(menuFileName).getStringList("menu.ItemList." + itemID + ".Slots"));
                }

                setMenuItem(menu, menuFileName, itemID, type, displayName, lore, customModelData, amount, slotList);
            }
            Bukkit.getRegionScheduler().run(PluginLoader.INSTANCE.getPlugin(), player.getLocation(), t -> player.openInventory(menu));
        });
    }
}
