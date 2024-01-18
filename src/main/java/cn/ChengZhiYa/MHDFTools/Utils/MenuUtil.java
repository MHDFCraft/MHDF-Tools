package cn.ChengZhiYa.MHDFTools.Utils;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static cn.ChengZhiYa.MHDFTools.MHDFTools.dataSource;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.ChatColor;

public final class MenuUtil {
    static final Map<Object, List<String>> MenuHashMap = new HashMap<>();
    static final Map<Object, String> MenuItemLangHashMap = new HashMap<>();
    public static YamlConfiguration HomeMenu;

    public static Map<Object, String> getMenuItemLangHashMap() {
        return MenuItemLangHashMap;
    }

    public static Map<Object, List<String>> getMenuHashMap() {
        return MenuHashMap;
    }

    public static ItemStack getItemStack(Material material, String DisplayName, List<String> Lores) {
        ItemStack Item = new ItemStack(material);
        ItemMeta Meta = Item.getItemMeta();
        Objects.requireNonNull(Meta).setDisplayName(ChatColor(DisplayName));
        ArrayList<String> Lore = new ArrayList<>();
        for (String lore : Lores) {
            Lore.add(ChatColor(lore));
        }
        Meta.setLore(Lore);
        Item.setItemMeta(Meta);
        return Item;
    }

    public static ItemStack getItemStack(String material, String DisplayName, List<String> Lore) {
        if (material.startsWith("{RandomBed}")) {
            return getRandomBed(DisplayName, Lore);
        }
        if (material.startsWith("PlayerHead-")) {
            return getPlayerHead(material.replaceAll("PlayerHead-", ""), DisplayName, Lore);
        }
        return getItemStack(Material.getMaterial(material), DisplayName, Lore);

    }

    public static ItemStack getRandomBed(String DisplayName, List<String> Lores) {
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
        Item.setItemMeta(ItemMeta);
        return Item;
    }

    public static ItemStack getPlayerHead(String PlayerName, String DisplayName, List<String> Lores) {
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
        Item.setItemMeta(SkullMeta);
        return Item;
    }

    public static YamlConfiguration getHomeMenu() {
        if (HomeMenu == null) {
            File LangFile = new File(MHDFTools.instance.getDataFolder(), "HomeMenu.yml");
            HomeMenu = YamlConfiguration.loadConfiguration(LangFile);
        }
        return HomeMenu;
    }

    public static String getPlaceholder(String Message, String LangMessage, String Placeholder) {
        String[] OtherMessage = ChatColor(LangMessage).split(Placeholder.replaceAll("\\{", "\\\\{"));
        for (String s : OtherMessage) {
            Message = Message.replaceAll(ChatColor(s), "");
        }
        return Message;
    }

    public static void OpenHomeMenu(Player player, int Page) {
        Bukkit.getScheduler().runTaskAsynchronously(MHDFTools.instance, () -> {
            Inventory Menu = Bukkit.createInventory(player, getHomeMenu().getInt("Menu.Size"), ChatColor(Objects.requireNonNull(getHomeMenu().getString("Menu.Title")).replaceAll("\\{Page\\}", String.valueOf(Page))));
            int HomeSize = getHomeMenu().getInt("Menu.HomeSize");

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
            } catch (SQLException e) {
                e.printStackTrace();
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
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (String Item : Objects.requireNonNull(getHomeMenu().getConfigurationSection("Menu.ItemList")).getKeys(false)) {
                String ItemType = getHomeMenu().getString("Menu.ItemList." + Item + ".ItemType");
                String Type = Objects.requireNonNull(getHomeMenu().getString("Menu.ItemList." + Item + ".Type"));
                String DisplayName = Objects.requireNonNull(getHomeMenu().getString("Menu.ItemList." + Item + ".DisplayName"));
                List<String> Lore = getHomeMenu().getStringList("Menu.ItemList." + Item + ".Lore");
                if (ItemType != null) {
                    if (ItemType.equals("GoToHome")) {
                        if (!PlayerHomeList.isEmpty()) {
                            for (String HomeName : PlayerHomeList) {
                                ItemStack ItemStack = getItemStack(
                                        Type.replaceAll("\\{HomeName\\}", HomeName),
                                        DisplayName.replaceAll("\\{HomeName\\}", HomeName),
                                        Lore
                                );
                                getMenuHashMap().put(ItemStack.getType() + "|" + ItemStack.getItemMeta().getDisplayName(), getHomeMenu().getStringList("Menu.ItemList." + Item + ".ClickAction"));
                                getMenuItemLangHashMap().put(ItemStack.getItemMeta().getDisplayName(), DisplayName);
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
                        Lore
                );
                getMenuHashMap().put(ItemStack.getType() + "|" + ItemStack.getItemMeta().getDisplayName(), getHomeMenu().getStringList("Menu.ItemList." + Item + ".ClickAction"));
                getMenuItemLangHashMap().put(ItemStack.getItemMeta().getDisplayName(), DisplayName);

                String[] ItemSlot = Objects.requireNonNull(getHomeMenu().getString("Menu.ItemList." + Item + ".Slot")).split("-");
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
}
