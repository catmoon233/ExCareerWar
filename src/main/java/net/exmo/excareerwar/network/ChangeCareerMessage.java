

package net.exmo.excareerwar.network;

import io.netty.buffer.Unpooled;
import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangeCareerMessage {
	int type, pressedms;

	public ChangeCareerMessage(int type, int pressedms) {
		this.type = type;
		this.pressedms = pressedms;
	}

	public ChangeCareerMessage(FriendlyByteBuf buffer) {
		this.type = buffer.readInt();
		this.pressedms = buffer.readInt();
	}

	public static void buffer(ChangeCareerMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.type);
		buffer.writeInt(message.pressedms);
	}

	public static void handler(ChangeCareerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			pressAction(context.getSender(), message.type, message.pressedms);
		});
		context.setPacketHandled(true);
	}

	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(entity.blockPosition()))
			return;
		if (type == 0) {
			if (!(entity instanceof ServerPlayer serverPlayer))return;
			if (entity.hasEffect(CareerWarModMobEffects.CAN_CHANGE_CAREER_EFFECT.get())){
				NetworkHooks.openScreen((ServerPlayer) entity, new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.literal("SelectedCareer");
					}

					@Override
					public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
						return new net.exmo.excareerwar.inventory.SelectedCareerMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(entity.blockPosition()));
					}
				}, entity.blockPosition());
			}
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		Excareerwar.addNetworkMessage(ChangeCareerMessage.class, ChangeCareerMessage::buffer, ChangeCareerMessage::new, ChangeCareerMessage::handler);
	}
}
