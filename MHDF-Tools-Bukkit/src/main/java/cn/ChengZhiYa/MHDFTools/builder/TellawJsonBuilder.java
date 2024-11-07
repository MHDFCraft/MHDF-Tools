package cn.ChengZhiYa.MHDFTools.builder;

import cn.ChengZhiYa.MHDFTools.util.message.LogUtil;
import de.tr7zw.changeme.nbtapi.NBT;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public final class TellawJsonBuilder {
    public BaseComponent components = new TextComponent();

    public String toString() {
        return components.toString();
    }

    public TellawJsonBuilder append(String message) {
        Arrays.stream(TextComponent.fromLegacyText(message)).forEach(components::addExtra);
        return this;
    }

    public TellawJsonBuilder append(TellawJsonBuilder tellrawJson) {
        components.addExtra(tellrawJson.components);
        return this;
    }

    public TellawJsonBuilder hoverText(String message) {
        components.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(message))
        );
        return this;
    }

    public TellawJsonBuilder hoverItem(ItemStack item) {
        components.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_ITEM, new Item(
                        item.getType().getKey().toString(),
                        1,
                        ItemTag.ofNbt(NBT.readNbt(item).toString()))
                )
        );
        return this;
    }

    public TellawJsonBuilder openURL(String url) {
        components.setClickEvent(
                new ClickEvent(ClickEvent.Action.OPEN_URL, url)
        );
        return this;
    }

    public TellawJsonBuilder runCommand(String command) {
        components.setClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
        );
        return this;
    }

    public TellawJsonBuilder changeInput(String input) {
        components.setClickEvent(
                new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, input)
        );
        return this;
    }

    public TellawJsonBuilder copyText(String text) {
        components.setClickEvent(
                new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text)
        );
        return this;
    }

    public void sendTo(CommandSender sender) {
        if (sender instanceof Player player) {
            player.spigot().sendMessage(components);
        } else {
            LogUtil.log(toString());
        }
    }

    public void broadcast() {
        Bukkit.getOnlinePlayers().forEach(this::sendTo);
        LogUtil.log(toString());
    }
}
