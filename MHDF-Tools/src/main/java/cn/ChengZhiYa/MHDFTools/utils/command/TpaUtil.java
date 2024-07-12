package cn.ChengZhiYa.MHDFTools.utils.command;

import cn.ChengZhiYa.MHDFTools.entity.TpaData;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.HashMap;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class TpaUtil {
    @Getter
    public static HashMap<String, TpaData> tpaHashMap = new HashMap<>();

    public static TextComponent getTpaRequestMessage(String playerName) {
        TextComponent message = new TextComponent();
        for (String messages : i18n("Tpa.Message").split("\\?")) {
            if (messages.equals("Accept")) {
                TextComponent messageButton = new TextComponent(MessageUtil.colorMessage(i18n("Tpa.AcceptMessage")));
                messageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accept " + playerName));
                messageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(MessageUtil.colorMessage("&a接受" + playerName + "的传送请求"))));
                message.addExtra(messageButton);
            } else {
                if (messages.equals("Defuse")) {
                    TextComponent messageButton = new TextComponent(MessageUtil.colorMessage(i18n("Tpa.DefuseMessage")));
                    messageButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa defuse " + playerName));
                    messageButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(MessageUtil.colorMessage("&c拒绝" + playerName + "的传送请求"))));
                    message.addExtra(messageButton);
                } else {
                    message.addExtra(new TextComponent(MessageUtil.colorMessage(messages.replaceAll("%1", playerName))));
                }
            }
        }
        return message;
    }
}
