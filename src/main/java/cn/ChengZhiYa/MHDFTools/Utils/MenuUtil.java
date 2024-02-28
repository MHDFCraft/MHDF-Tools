package cn.ChengZhiYa.MHDFTools.Utils;

import cn.ChengZhiYa.MHDFTools.HashMap.StringHasMap;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.ChengZhiYa.MHDFTools.MHDFTools.dataSource;
import static cn.ChengZhiYa.MHDFTools.Utils.BCUtil.TpPlayerHome;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.PAPIChatColor;
import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;
import static cn.chengzhiya.mhdfpluginapi.Util.ColorLog;

public final class MenuUtil {
    static final Map<Object, String> MenuHashMap = new HashMap<>();
    static final Map<Object, String> MenuItemHashMap = new HashMap<>();

    public static Map<Object, String> getMenuHashMap() {
        return MenuHashMap;
    }

    public static Map<Object, String> getMenuItemHashMap() {
        return MenuItemHashMap;
    }

    public static ItemStack getItemStack(Material material, String DisplayName, List<String> Lores, int CustomModelData, int Amount) {
        ItemStack Item = new ItemStack(material);
        ItemMeta Meta = Item.getItemMeta();
        Objects.requireNonNull(Meta).setDisplayName(ChatColor(DisplayName));
        ArrayList<String> Lore = new ArrayList<>();
        for (String lore : Lores) {
            Lore.add(ChatColor(lore));
        }
        Meta.setLore(Lore);
        Meta.setCustomModelData(CustomModelData);
        Item.setItemMeta(Meta);
        Item.setAmount(Amount);
        return Item;
    }

    public static ItemStack getItemStack(String material, String DisplayName, List<String> Lore, Integer CustomModelData, Integer Amount) {
        if (material.startsWith("{RandomBed}")) {
            return getRandomBed(DisplayName, Lore, CustomModelData, Amount);
        }
        if (material.startsWith("PlayerHead-")) {
            return getPlayerHead(material.replaceAll("PlayerHead-", ""), DisplayName, Lore, CustomModelData, Amount);
        }
        return getItemStack(Material.getMaterial(material), DisplayName, Lore, CustomModelData, Amount);
    }

    public static ItemStack getRandomBed(String DisplayName, List<String> Lores, int CustomModelData, int Amount) {
        List<Material> BedList = new ArrayList<>();
        BedList.add(Material.BLACK_BED);
        BedList.add(Material.BLUE_BED);
        BedList.add(Material.BROWN_BED);
        BedList.add(Material.CYAN_BED);
        BedList.add(Material.GREEN_BED);
        BedList.add(Material.LIGHT_BLUE_BED);
        BedList.add(Material.LIGHT_GRAY_BED);
        BedList.add(Material.MAGENTA_BED);
        BedList.add(Material.ORANGE_BED);
        BedList.add(Material.LIME_BED);
        BedList.add(Material.PINK_BED);
        BedList.add(Material.PURPLE_BED);
        BedList.add(Material.RED_BED);
        BedList.add(Material.WHITE_BED);
        BedList.add(Material.YELLOW_BED);
        ItemStack Item = new ItemStack(BedList.get(new Random().nextInt(BedList.size())));
        ItemMeta ItemMeta = Item.getItemMeta();
        Objects.requireNonNull(ItemMeta).setDisplayName(ChatColor(DisplayName));
        ArrayList<String> Lore = new ArrayList<>();
        for (String lore : Lores) {
            Lore.add(ChatColor(lore));
        }
        ItemMeta.setLore(Lore);
        ItemMeta.setCustomModelData(CustomModelData);
        Item.setItemMeta(ItemMeta);
        Item.setAmount(Amount);
        return Item;
    }

    public static ItemStack getPlayerHead(String PlayerName, String DisplayName, List<String> Lores, int CustomModelData, int Amount) {
        ItemStack Item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta ItemMeta = Item.getItemMeta();
        Objects.requireNonNull(ItemMeta).setDisplayName(ChatColor(DisplayName));
        ArrayList<String> Lore = new ArrayList<>();
        for (String lore : Lores) {
            Lore.add(ChatColor(lore));
        }
        ItemMeta.setLore(Lore);
        SkullMeta SkullMeta = (SkullMeta) ItemMeta;
        SkullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(PlayerName));
        SkullMeta.setCustomModelData(CustomModelData);
        Item.setItemMeta(SkullMeta);
        Item.setAmount(Amount);
        return Item;
    }

    public static YamlConfiguration getMenu(String MenuFile) {
        File LangFile = new File(MHDFTools.instance.getDataFolder(), "Menus/" + MenuFile);
        return YamlConfiguration.loadConfiguration(LangFile);
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

    public static String getPlaceholder(String Message, String LangMessage, String Placeholder) {
        String[] OtherMessage = ChatColor(LangMessage).split(Placeholder.replaceAll("\\{", "\\\\{"));
        for (String s : OtherMessage) {
            Message = Message.replaceAll(ChatColor(s), "");
        }
        return Message;
    }

    @SuppressWarnings("ExtractMethodRecommender")
    public static void OpenHomeMenu(Player player, int Page) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            String MenuTitle = ChatColor(Objects.requireNonNull(getMenu("HomeMenu.yml").getString("Menu.Title")).replaceAll("\\{Page\\}", String.valueOf(Page)));
            getMenuHashMap().put(MenuTitle, "HomeMenu.yml");
            Inventory Menu = Bukkit.createInventory(player, getMenu("HomeMenu.yml").getInt("Menu.Size"), MenuTitle);
            int HomeSize = getMenu("HomeMenu.yml").getInt("Menu.HomeSize");

            List<String> PlayerHomeList = new ArrayList<>();
            try {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM MHDFTools_Home WHERE Owner = ? LIMIT " + HomeSize + " OFFSET ?");
                ps.setString(1, player.getName());
                ps.setInt(2, HomeSize * (Page - 1));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    PlayerHomeList.add(rs.getString("Home"));
                }
                rs.close();
                ps.close();
                connection.close();
            } catch (SQLException ignored) {
            }

            List<String> NextPlayerHomeList = new ArrayList<>();
            try {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM MHDFTools_Home WHERE Owner = ? LIMIT " + HomeSize + " OFFSET ?");
                ps.setString(1, player.getName());
                ps.setInt(2, HomeSize * (Page));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    NextPlayerHomeList.add(rs.getString("Home"));
                }
                rs.close();
                ps.close();
                connection.close();
            } catch (SQLException ignored) {
            }

            for (String Item : Objects.requireNonNull(getMenu("HomeMenu.yml").getConfigurationSection("Menu.ItemList")).getKeys(false)) {
                String ItemType = getMenu("HomeMenu.yml").getString("Menu.ItemList." + Item + ".ItemType");
                String Type = Objects.requireNonNull(getMenu("HomeMenu.yml").getString("Menu.ItemList." + Item + ".Type"));
                String DisplayName = Objects.requireNonNull(getMenu("HomeMenu.yml").getString("Menu.ItemList." + Item + ".DisplayName"));
                int CustomModelData = getMenu("HomeMenu.yml").getInt("Menu.ItemList." + Item + ".Model");
                int Amount = getMenu("HomeMenu.yml").getInt("Menu.ItemList." + Item + ".Amount");
                if (Amount == 0) {
                    Amount = 1;
                }
                List<String> Lore = getMenu("HomeMenu.yml").getStringList("Menu.ItemList." + Item + ".Lore");
                if (ItemType != null) {
                    if (ItemType.equals("GoToHome")) {
                        if (!PlayerHomeList.isEmpty()) {
                            for (String HomeName : PlayerHomeList) {
                                ItemStack ItemStack = getItemStack(
                                        Type.replaceAll("\\{HomeName\\}", HomeName),
                                        DisplayName.replaceAll("\\{HomeName\\}", HomeName),
                                        Lore,
                                        CustomModelData,
                                        Amount
                                );
                                getMenuItemHashMap().put(MenuTitle + "|" + ItemStack.toString(), Item);
                                Menu.addItem(ItemStack);
                            }
                        }
                        continue;
                    }
                    if (ItemType.equals("PageUp")) {
                        if (Page <= 1) {
                            continue;
                        }
                    }
                    if (ItemType.equals("PageNext")) {
                        if (NextPlayerHomeList.isEmpty()) {
                            continue;
                        }
                    }
                }
                ItemStack ItemStack = getItemStack(
                        Type,
                        DisplayName.replaceAll("\\{Page\\}", String.valueOf(Page)),
                        Lore,
                        CustomModelData,
                        Amount
                );
                getMenuItemHashMap().put(MenuTitle + "|" + ItemStack.toString(), Item);

                String[] ItemSlot = Objects.requireNonNull(getMenu("HomeMenu.yml").getString("Menu.ItemList." + Item + ".Slot")).split("-");
                if (ItemSlot.length == 2) {
                    int Start = Integer.parseInt(ItemSlot[0]);
                    int End = Integer.parseInt(ItemSlot[1]) + 1;
                    for (int i = Start; i < End; i++) {
                        Menu.setItem(i, ItemStack);
                    }
                } else {
                    Menu.setItem(Integer.parseInt(ItemSlot[0]), ItemStack);
                }
            }

            Bukkit.getScheduler().runTask(MHDFTools.instance, () -> player.openInventory(Menu));
        });
    }

    public static void OpenMenu(Player player, String MenuFile) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            String MenuTitle = ChatColor(Objects.requireNonNull(getMenu(MenuFile).getString("Menu.Title")));
            getMenuHashMap().put(MenuTitle, MenuFile);
            Inventory Menu = Bukkit.createInventory(player, getMenu(MenuFile).getInt("Menu.Size"), MenuTitle);

            for (String Item : Objects.requireNonNull(getMenu(MenuFile).getConfigurationSection("Menu.ItemList")).getKeys(false)) {
                String Type = Objects.requireNonNull(getMenu(MenuFile).getString("Menu.ItemList." + Item + ".Type"));
                String DisplayName = Objects.requireNonNull(getMenu(MenuFile).getString("Menu.ItemList." + Item + ".DisplayName"));
                int CustomModelData = getMenu(MenuFile).getInt("Menu.ItemList." + Item + ".Model");
                int Amount = getMenu(MenuFile).getInt("Menu.ItemList." + Item + ".Amount");
                if (Amount == 0) {
                    Amount = 1;
                }
                List<String> Lore = getMenu(MenuFile).getStringList("Menu.ItemList." + Item + ".Lore");
                ItemStack ItemStack = getItemStack(
                        Type,
                        DisplayName,
                        Lore,
                        CustomModelData,
                        Amount
                );
                getMenuItemHashMap().put(MenuTitle + "|" + ItemStack.toString(), Item);

                String[] ItemSlot = Objects.requireNonNull(getMenu(MenuFile).getString("Menu.ItemList." + Item + ".Slot")).split("-");
                if (ItemSlot.length == 2) {
                    int Start = Integer.parseInt(ItemSlot[0]);
                    int End = Integer.parseInt(ItemSlot[1]) + 1;
                    for (int i = Start; i < End; i++) {
                        Menu.setItem(i, ItemStack);
                    }
                } else {
                    Menu.setItem(Integer.parseInt(ItemSlot[0]), ItemStack);
                }
            }

            Bukkit.getScheduler().runTask(MHDFTools.instance, () -> player.openInventory(Menu));
        });
    }

    public static List<String> AllowClickAction(Player player, YamlConfiguration Menu, String Item) {
        if (Menu.getConfigurationSection("Menu.ItemList." + Item + ".ClickRequirements") == null) {
            return new ArrayList<>();
        }
        List<String> DenyActionList = new ArrayList<>();
        boolean Allow = true;
        for (String Requirements : Objects.requireNonNull(Menu.getConfigurationSection("Menu.ItemList." + Item + ".ClickRequirements")).getKeys(false)) {
            String Type = Menu.getString("Menu.ItemList." + Item + ".ClickRequirements." + Requirements + ".Type");
            String Input = Menu.getString("Menu.ItemList." + Item + ".ClickRequirements." + Requirements + ".Input");
            String Output = Menu.getString("Menu.ItemList." + Item + ".ClickRequirements." + Requirements + ".Output");
            if (Type != null && Input != null && Output != null && Type.equals(">")) {
                Allow = Double.parseDouble(Input) > Double.parseDouble(Output);
            }
            if (Type != null && Input != null && Output != null && Type.equals(">=")) {
                Allow = Double.parseDouble(Input) >= Double.parseDouble(Output);
            }
            if (Type != null && Input != null && Output != null && Type.equals("==")) {
                Allow = Double.parseDouble(Input) == Double.parseDouble(Output);
            }
            if (Type != null && Input != null && Output != null && Type.equals("<")) {
                Allow = Double.parseDouble(Input) < Double.parseDouble(Output);
            }
            if (Type != null && Input != null && Output != null && Type.equals("<=")) {
                Allow = Double.parseDouble(Input) <= Double.parseDouble(Output);
            }
            if (Type != null && Input != null && Type.equals("Permission")) {
                Allow = player.hasPermission(Input);
            }
            if (Type != null && Input != null && Type.equals("HasItem")) {
                if (Material.getMaterial(Input) != null) {
                    Allow = player.getInventory().contains(Objects.requireNonNull(Material.getMaterial(Input)));
                } else {
                    Allow = false;
                    ColorLog("&c[MHDF-Tools]不存在" + Input + "这个空间ID的物品");
                }
            }
            if (!Allow) {
                DenyActionList = Menu.getStringList("Menu.ItemList." + Item + ".ClickRequirements." + Requirements + ".DenyAction");
                break;
            }
        }
        return DenyActionList;
    }

    public static void RunAction(String Menu, Player player, List<String> ActionList, Integer Page, ItemStack ClickItem) {
        for (String Actions : ActionList) {
            String[] Action = Actions.split("\\|");
            if (Menu.equals("HomeMenu.yml") && Page != null && ClickItem != null) {
                if (Action[0].equals("[PageUp]")) {
                    OpenHomeMenu(player, Page - 1);
                    continue;
                }
                if (Action[0].equals("[PageNext]")) {
                    OpenHomeMenu(player, Page + 1);
                    continue;
                }
                if (Action[0].equals("[Home]")) {
                    String DisplayName = ClickItem.getItemMeta().getDisplayName();
                    String HomeName = getPlaceholder(DisplayName, getMenuItemHashMap().get(DisplayName), "{HomeName}");
                    TpPlayerHome(player.getName(), HomeName);
                    continue;
                }
            }
            if (Action[0].equals("[player]")) {
                Bukkit.getScheduler().runTask(MHDFTools.instance, () -> player.chat("/" + PlaceholderAPI.setPlaceholders(player, Action[1])));
                continue;
            }
            if (Action[0].equals("[console]")) {
                StringHasMap.getHasMap().put(player.getName() + "_ArgsRunCommmand", "console|" + Menu + "|" + Action[1] + "|" + Action[2]);
                player.closeInventory();
                continue;
            }
            if (Action[0].equals("[player_args]")) {
                StringHasMap.getHasMap().put(player.getName() + "_ArgsRunCommmand", "player|" + Menu + "|" + Action[1] + "|" + Action[2]);
                player.closeInventory();
                continue;
            }
            if (Action[0].equals("[console_args]")) {
                Bukkit.getScheduler().runTask(MHDFTools.instance, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, Action[1])));
                continue;
            }
            if (Action[0].equals("[playsound]")) {
                try {
                    player.playSound(player, Sound.valueOf(Action[1]), Float.parseFloat(Action[2]), Float.parseFloat(Action[3]));
                } catch (Exception e) {
                    ColorLog("&c[MHDF-Tools]不存在" + Action[1] + "这个音频");
                }
                continue;
            }
            if (Action[0].equals("[playsound_pack]")) {
                try {
                    player.playSound(player, Action[1], Float.parseFloat(Action[2]), Float.parseFloat(Action[3]));
                } catch (Exception e) {
                    ColorLog("&c[MHDF-Tools]不存在" + Action[1] + "这个音频");
                }
                continue;
            }
            if (Action[0].equals("[message]")) {
                player.sendMessage(PAPIChatColor(player, Action[1]).replaceAll(Action[0] + "\\|", "").replaceAll("\\|", "\n"));
                continue;
            }
            if (Action[0].equals("[title]")) {
                player.sendTitle(PAPIChatColor(player, Action[1]), PAPIChatColor(player, Action[2]), Integer.parseInt(Action[3]), Integer.parseInt(Action[4]), Integer.parseInt(Action[5]));
                continue;
            }
            if (Action[0].equals("[actionbar]")) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(PAPIChatColor(player, Action[1])));
                continue;
            }
            if (Action[0].equals("[close]")) {
                player.closeInventory();
                continue;
            }
            ColorLog("&c[MHDF-Tools]不存在" + Action[0] + "这个操作");
        }
    }
}
