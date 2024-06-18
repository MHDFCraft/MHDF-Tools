package cn.ChengZhiYa.MHDFTools.util.menu;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.hashmap.StringHasMap;
import cn.ChengZhiYa.MHDFTools.util.message.LogUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
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

import static cn.ChengZhiYa.MHDFTools.util.Util.PAPI;

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
            meta.setDisplayName(LogUtil.ChatColor(displayName));
        }
        if (lore != null && !lore.isEmpty()) {
            List<String> lores = new ArrayList<>();
            lore.forEach(s -> lores.add(LogUtil.ChatColor(s)));
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
        return YamlConfiguration.loadConfiguration(new File(MHDFTools.instance.getDataFolder(), "Menus/" + MenuFile));
    }

    public static String getMenuFromItem(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        return Objects.requireNonNull(nbtItem.getCompound("MHDFTools")).getString("Menu");
    }

    public static String getItemNameFromItem(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        return Objects.requireNonNull(nbtItem.getCompound("MHDFTools")).getString("Item");
    }

    public static List<String> getCustomMenuList() {
        List<String> MenuList = new ArrayList<>();
        try (Stream<Path> File = Files.walk(new File(MHDFTools.instance.getDataFolder(), "Menus").toPath())) {
            MenuList = File.filter(Files::isRegularFile).map(Path::toString).map(FileName -> FileName.replaceAll("plugins\\\\MHDF-Tools\\\\Menus\\\\", "")).collect(Collectors.toList());
        } catch (IOException ignored) {
        }
        MenuList.remove("HomeMenu.yml");
        return MenuList;
    }

    public static boolean ifCustomMenu(Player player, String title) {
        for (String MenuFileName : getCustomMenuList()) {
            YamlConfiguration Menu = getMenu(MenuFileName);
            if (PAPI(player, Menu.getString("Menu.Title")).equals(PAPI(player, title))) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getMenuItem(String menuFileName, String itemID, String type, String displayName, List<String> lore, Integer customModelData, Integer amount) {
        NBTItem nbtItem = new NBTItem(getItemStack(type, displayName, lore, customModelData, amount));
        NBTCompound compound = nbtItem.addCompound("MHDFTools");
        compound.setString("Menu", menuFileName);
        compound.setString("Item", itemID);
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

        if (getMenu(menuFileName).getConfigurationSection("Menu.ItemList." + itemID + "." + requirmentType) != null) {
            boolean allow = true;
            for (String requirement : Objects.requireNonNull(getMenu(menuFileName).getConfigurationSection("Menu.ItemList." + itemID + "." + requirmentType)).getKeys(false)) {
                String type = getMenu(menuFileName).getString("Menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Type");
                String input = getMenu(menuFileName).getString("Menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Input");
                String output = getMenu(menuFileName).getString("Menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Output");
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
                            String itemType = getMenu(menuFileName).getString("Menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Input.Type");
                            String itemDisplayName = getMenu(menuFileName).getString("Menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Input.DisplayName");
                            List<String> itemLore = new ArrayList<>();
                            getMenu(menuFileName).getStringList("Menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Input.Lore").forEach(s -> {
                                itemLore.add(PAPI(player, s));
                            });
                            Integer itemCustomModelData = (Integer) getMenu(menuFileName).get("Menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Input.CustomModelData");
                            Integer amount = (Integer) getMenu(menuFileName).get("Menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".Input.Amount");

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
                                            allow = playerInvItem.getItemMeta().getDisplayName().equals(PAPI(player, itemDisplayName));
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
                    return getMenu(menuFileName).getStringList("Menu.ItemList." + itemID + "." + requirmentType + "." + requirement + ".DenyAction");
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
                    StringHasMap.getHasMap().put(sender.getName() + "_ArgsRunCommmand", "console|" + menuFileName + "|" + action[1] + "|" + action[2]);
                    player.closeInventory();
                }
                break;
            }
            case "[player_args]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    StringHasMap.getHasMap().put(sender.getName() + "_ArgsRunCommmand", "player|" + menuFileName + "|" + action[1] + "|" + action[2]);
                    player.closeInventory();
                }
                break;
            }
            case "[player]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    Bukkit.getScheduler().runTask(MHDFTools.instance, () -> player.chat("/" + PAPI(player, action[1])));
                } else {
                    Bukkit.getScheduler().runTask(MHDFTools.instance, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPI(null, action[1])));
                }
                break;
            }
            case "[console]": {
                Bukkit.getScheduler().runTask(MHDFTools.instance, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPI(null, action[1])));
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
                    sender.sendMessage(PAPI(player, action[1]).replaceAll(action[0] + "\\|", "").replaceAll("\\|", "\n"));
                } else {
                    sender.sendMessage(PAPI(null, action[1]).replaceAll(action[0] + "\\|", "").replaceAll("\\|", "\n"));
                }
                break;
            }
            case "[title]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.sendTitle(PAPI(player, action[1]), PAPI(player, action[2]), Integer.parseInt(action[3]), Integer.parseInt(action[4]), Integer.parseInt(action[5]));
                }
                break;
            }
            case "[actionbar]": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(PAPI(player, action[1])));
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
            default:
                LogUtil.ChatColor("&c[MHDF-Tools]不存在" + action[0] + "这个操作");
        }
    }

    public static void runAction(CommandSender player, String menuFileName, List<String> actionList) {
        for (String actions : actionList) {
            runAction(player, menuFileName, actions.split("\\|"));
        }
    }

    public static void openMenu(Player player, String menuFileName) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            String title = PAPI(player, getMenu(menuFileName).getString("Menu.Title"));
            Inventory menu = Bukkit.createInventory(player, getMenu(menuFileName).getInt("Menu.Size"), title);

            for (String itemID : Objects.requireNonNull(getMenu(menuFileName).getConfigurationSection("Menu.ItemList")).getKeys(false)) {
                String type = getMenu(menuFileName).getString("Menu.ItemList." + itemID + ".Type");
                String displayName = getMenu(menuFileName).getString("Menu.ItemList." + itemID + ".DisplayName");
                Integer customModelData = getMenu(menuFileName).getObject("Menu.ItemList." + itemID + ".CustomModelData", Integer.class);
                Integer amount = getMenu(menuFileName).getObject("Menu.ItemList." + itemID + ".Amount", Integer.class);
                List<String> lore = new ArrayList<>();
                getMenu(menuFileName).getStringList("Menu.ItemList." + itemID + ".Lore").forEach(s -> lore.add(PAPI(player, s)));

                List<String> slotList = new ArrayList<>();
                if (getMenu(menuFileName).getString("Menu.ItemList." + itemID + ".Slot") != null) {
                    slotList.add(getMenu(menuFileName).getString("Menu.ItemList." + itemID + ".Slot"));
                } else {
                    slotList.addAll(getMenu(menuFileName).getStringList("Menu.ItemList." + itemID + ".Slot"));
                }

                setMenuItem(menu, menuFileName, itemID, type, displayName, lore, customModelData, amount, slotList);
            }
            Bukkit.getScheduler().runTask(MHDFTools.instance, () -> player.openInventory(menu));
        });
    }
}
