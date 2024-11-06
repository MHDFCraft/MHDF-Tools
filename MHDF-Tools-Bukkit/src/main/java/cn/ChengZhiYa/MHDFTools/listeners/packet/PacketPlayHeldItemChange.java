package cn.ChengZhiYa.MHDFTools.listeners.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import org.bukkit.entity.Player;

/**
 * patched paper1.20.3 slot crash
 * The Paper core of the foolish PaperMC team crashes when players send slot packets less than 0
 */
public class PacketPlayHeldItemChange extends PacketListenerAbstract {

    public PacketPlayHeldItemChange() {
        super(PacketListenerPriority.LOW);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {
            WrapperPlayClientHeldItemChange slot = new WrapperPlayClientHeldItemChange(event);

            int slots = slot.getSlot();

            Player player = event.getPlayer();

            if (slots < 0) { //Impossible Slot
                event.setCancelled(true);
                player.kickPlayer("Invalid Slot");
            }
        }
    }
}