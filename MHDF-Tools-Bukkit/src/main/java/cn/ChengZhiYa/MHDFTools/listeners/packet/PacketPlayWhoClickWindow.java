package cn.ChengZhiYa.MHDFTools.listeners.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;

/**
 * Patched paper1.16+ window click
 * Stupid PaperMC team make crash server by invalid window click
 */
public class PacketPlayWhoClickWindow extends PacketListenerAbstract {

    public PacketPlayWhoClickWindow() {
        super(PacketListenerPriority.LOW);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            WrapperPlayClientClickWindow click = new WrapperPlayClientClickWindow(event);
            int clickType = click.getWindowClickType().ordinal();
            int button = click.getButton();
            int windowId = click.getWindowId();
            int slot = click.getSlot();

            if ((clickType == 1 || clickType == 2) && windowId >= 0 && button < 0) {
                event.setCancelled(true);
            } else if (windowId >= 0 && clickType == 2 && slot < 0) {
                event.setCancelled(true);
            }
        }
    }
}