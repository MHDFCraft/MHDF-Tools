package cn.ChengZhiYa.MHDFTools.utils.menu;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil.tpPlayerHome;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.Placeholder;
import static cn.ChengZhiYa.MHDFTools.utils.database.DatabaseUtil.dataSource;
import static cn.ChengZhiYa.MHDFTools.utils.database.HomeUtil.*;
import static cn.ChengZhiYa.MHDFTools.utils.menu.MenuUtil.*;

public final class HomeMenuUtil {
    public final static String homeMenuFile = "HomeMenu.yml";

    public static String getPlaceholder(String Message, String LangMessage, String Placeholder) {
        String[] OtherMessage = MessageUtil.colorMessage(LangMessage).split(
                Placeholder.replaceAll("\\{", "\\\\{")
        );
        for (String s : OtherMessage) {
            Message = Message.replaceAll(MessageUtil.colorMessage(s), "");
        }
        return Message;
    }

    public static List<String> getPlayerHomeList(String playerName, int size, int offset) {
        List<String> playerHomeList = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdftools_home WHERE Owner = ? LIMIT " + size + " OFFSET ?");
            ps.setString(1, playerName);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                playerHomeList.add(rs.getString("Home"));
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException ignored) {
        }
        return playerHomeList;
    }

    public static void openHomeMenu(Player player, int page) {
        Bukkit.getAsyncScheduler().runNow(PluginLoader.INSTANCE.getPlugin(), task -> {
            String title = Placeholder(player, getMenu(homeMenuFile).getString("menu.Title")).replaceAll("\\{Page}", String.valueOf(page));
            Inventory menu = Bukkit.createInventory(player, getMenu(homeMenuFile).getInt("menu.Size"), title);

            int HomeSize = getMenu(homeMenuFile).getInt("menu.HomeSize");

            List<String> playerHomeList = getPlayerHomeList(player.getName(), HomeSize, HomeSize * (page - 1));
            List<String> nextPagePlayerHomeList = getPlayerHomeList(player.getName(), HomeSize, HomeSize * (page));

            for (String itemID : Objects.requireNonNull(getMenu(homeMenuFile).getConfigurationSection("menu.ItemList")).getKeys(false)) {
                String itemType = getMenu(homeMenuFile).getString("menu.ItemList." + itemID + ".ItemType");
                String type = getMenu(homeMenuFile).getString("menu.ItemList." + itemID + ".Type");
                String displayName = getMenu(homeMenuFile).getString("menu.ItemList." + itemID + ".DisplayName");
                List<String> lore = new ArrayList<>();
                Integer customModelData = getMenu(homeMenuFile).getObject("menu.ItemList." + itemID + ".CustomModelData", Integer.class);
                Integer amount = getMenu(homeMenuFile).getObject("menu.ItemList." + itemID + ".Amount", Integer.class);

                List<String> slotList = new ArrayList<>();
                if (getMenu("HomeMenu.yml").getString("menu.ItemList." + itemID + ".Slot") != null) {
                    slotList.add(getMenu("HomeMenu.yml").getString("menu.ItemList." + itemID + ".Slot"));
                } else {
                    slotList.addAll(getMenu("HomeMenu.yml").getStringList("menu.ItemList." + itemID + ".Slots"));
                }

                if (itemType != null) {
                    switch (itemType) {
                        case "Home": {
                            int i = 0;
                            if (!playerHomeList.isEmpty()) {
                                for (String home : playerHomeList) {
                                    String homeDisplayName = null;
                                    List<String> homeLore = new ArrayList<>();
                                    SuperLocation homeLocation = getHomeLocation(player.getName(), home);

                                    if (displayName != null) {
                                        homeDisplayName = displayName
                                                .replaceAll("\\{HomeName}", home)
                                                .replaceAll("\\{Server}", getHomeServer(player.getName(), home))
                                                .replaceAll("\\{World}", Objects.requireNonNull(homeLocation).getWorldName())
                                                .replaceAll("\\{X}", String.valueOf(homeLocation.getX()))
                                                .replaceAll("\\{Y}", String.valueOf(homeLocation.getBlockY()))
                                                .replaceAll("\\{Z}", String.valueOf(homeLocation.getBlockZ()));
                                    }

                                    getMenu(homeMenuFile).getStringList("menu.ItemList." + itemID + ".Lore").forEach(s ->
                                            homeLore.add(
                                                    Placeholder(player, s)
                                                            .replaceAll("\\{HomeName}", home)
                                                            .replaceAll("\\{Server}", getHomeServer(player.getName(), home))
                                                            .replaceAll("\\{World}", Objects.requireNonNull(homeLocation).getWorldName())
                                                            .replaceAll("\\{X}", String.valueOf(homeLocation.getBlockX()))
                                                            .replaceAll("\\{Y}", String.valueOf(homeLocation.getBlockY()))
                                                            .replaceAll("\\{Z}", String.valueOf(homeLocation.getBlockZ()))
                                            )
                                    );

                                    ItemStack item = getMenuItem(homeMenuFile, itemID, type, homeDisplayName, homeLore, customModelData, amount);

                                    if (!slotList.isEmpty()) {
                                        menu.setItem(getSlot(slotList).get(i), item);
                                    } else {
                                        menu.addItem(item);
                                    }
                                    i++;
                                }
                            }
                            continue;
                        }
                        case "PageUp": {
                            if (page <= 1) {
                                continue;
                            }
                        }
                        case "PageNext": {
                            if (nextPagePlayerHomeList.isEmpty()) {
                                continue;
                            }
                        }
                    }
                }

                if (displayName != null) {
                    displayName = displayName.replaceAll("\\{Page}", String.valueOf(page));
                }
                getMenu(homeMenuFile).getStringList("menu.ItemList." + itemID + ".Lore").forEach(s ->
                        lore.add(Placeholder(player, s).replaceAll("\\{Page}", String.valueOf(page)))
                );

                getMenu(homeMenuFile).getStringList("menu.ItemList." + itemID + ".Lore").forEach(s -> lore.add(Placeholder(player, s)));

                setMenuItem(menu, homeMenuFile, itemID, type, displayName, lore, customModelData, amount, slotList);
            }
            Bukkit.getRegionScheduler().run(PluginLoader.INSTANCE.getPlugin(), player.getLocation(), t -> player.openInventory(menu));
        });
    }

    public static void runAction(Player player, String menuFileName, int page, ItemStack clickItem, List<String> actionList) {
        for (String action : actionList) {
            switch (action) {
                case "[PageUp]":
                    openHomeMenu(player, page - 1);
                    break;
                case "[PageNext]":
                    openHomeMenu(player, page + 1);
                    break;
                case "[Home]":
                    handleHomeAction(player, clickItem);
                    break;
                case "[DelHome]":
                    handleDelHomeAction(player, clickItem);
                    break;
                default:
                    MenuUtil.runAction(player, menuFileName, action.split("\\|"));
                    break;
            }
        }
    }

    private static void handleHomeAction(Player player, ItemStack clickItem) {
        String homeName = getHomeName(clickItem);
        if (homeName != null) {
            tpPlayerHome(player.getName(), homeName);
        }
    }

    private static void handleDelHomeAction(Player player, ItemStack clickItem) {
        String homeName = getHomeName(clickItem);
        if (homeName != null) {
            removeHome(player.getName(), homeName);
        }
    }

    private static String getHomeName(ItemStack clickItem) {
        try {
            String displayName = ChatColor.stripColor(clickItem.getItemMeta().getDisplayName());
            String menuDisplayName = ChatColor.stripColor(MessageUtil.colorMessage(getMenu(homeMenuFile).getString("menu.ItemList.Home.DisplayName")));
            return getPlaceholder(displayName, menuDisplayName, "{HomeName}");
        } catch (NullPointerException e) {
            return null;
        }
    }
}