package net.exmo.excareerwar.network;


import net.exmo.excareerwar.client.gui.SelectedCareerScreen;
import net.exmo.exmodifier.content.modifier.menu.RefreshMenuScreenPlus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PlayerRefreshScreenOverMessageMessage(ItemStack item, Component content) {
    public static void encode(PlayerRefreshScreenOverMessageMessage msg, FriendlyByteBuf buffer) {
        buffer.writeItem(msg.item);
        buffer.writeComponent(msg.content);

    }
    public static PlayerRefreshScreenOverMessageMessage decode(FriendlyByteBuf buffer) {
        return new PlayerRefreshScreenOverMessageMessage(
                buffer.readItem(), buffer.readComponent()
        );
    }

    public static void handle(PlayerRefreshScreenOverMessageMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            SelectedCareerScreen.addOverlay(System.currentTimeMillis(), msg.content, msg.item);
        });
        ctx.get().setPacketHandled(true);
    }
}
