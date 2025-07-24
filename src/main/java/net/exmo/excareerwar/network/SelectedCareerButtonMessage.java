
package net.exmo.excareerwar.network;


import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.CareerHandle;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.inventory.SelectedCareerMenu;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SelectedCareerButtonMessage {
	private final int buttonID, x, y, z;
	private HashMap<String, String> textstate;

	public SelectedCareerButtonMessage(FriendlyByteBuf buffer) {
		this.buttonID = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.textstate = readTextState(buffer);
	}

	public SelectedCareerButtonMessage(int buttonID, int x, int y, int z, HashMap<String, String> textstate) {
		this.buttonID = buttonID;
		this.x = x;
		this.y = y;
		this.z = z;
		this.textstate = textstate;

	}

	public static void buffer(SelectedCareerButtonMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
		writeTextState(message.textstate, buffer);
	}

	public static void handler(SelectedCareerButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			Player entity = context.getSender();
			int buttonID = message.buttonID;
			int x = message.x;
			int y = message.y;
			int z = message.z;
			HashMap<String, String> textstate = message.textstate;
			handleButtonAction(entity, buttonID, x, y, z, textstate);
		});
		context.setPacketHandled(true);
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z, HashMap<String, String> textstate) {
		Level world = entity.level();
		HashMap guistate = SelectedCareerMenu.guistate;
		for (Map.Entry<String, String> entry : textstate.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			guistate.put(key, value);
		}
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z))) {
            return;
        }
		if (buttonID == 0) {
			if (entity instanceof  ServerPlayer serverPlayer) {
                handleSelectedCareerButtonMessage(serverPlayer, textstate);
            }
		}

	}
	public static void ChangeCareer(ServerPlayer player, String career){
		Team team = player.getTeam();
		AtomicBoolean hasSameCareer = new AtomicBoolean(false);
		if (team!=null){
			team.getPlayers().forEach(p -> {
				player.serverLevel().players().stream().filter(e->e.getScoreboardName().equals( p)).findAny().ifPresent(e->{
					e.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(c->{
						if (c.career.equals(career)){
							hasSameCareer.set(true);
						}
					});
				});
			});
		}
		if (hasSameCareer.get()){
			PlayerRefreshScreenOverMessageMessage playerRefreshScreenOverMessageMessage = new PlayerRefreshScreenOverMessageMessage(ItemStack.EMPTY, Component.literal("\u00a4该角色已被选择"));
			Excareerwar.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), playerRefreshScreenOverMessageMessage);
			return;
		}
		clearMagicBook(player);
		setPuppetsDie(player, true);
		updatePlayerCareer(player, career);
		handleCareer(player, career);

	}
	public static void handleSelectedCareerButtonMessage(ServerPlayer player, Map<String, String> textstate) {
		String career = textstate.get("career");
		Team team = player.getTeam();
		AtomicBoolean hasSameCareer = new AtomicBoolean(false);
		if (team!=null){
			team.getPlayers().forEach(p -> {
				player.serverLevel().players().stream().filter(e->e.getScoreboardName().equals( p)).findAny().ifPresent(e->{
					e.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(c->{
						if (c.career.equals(career)){
							hasSameCareer.set(true);
						}
					});
				});
			});
		}
		if (hasSameCareer.get()){
			PlayerRefreshScreenOverMessageMessage playerRefreshScreenOverMessageMessage = new PlayerRefreshScreenOverMessageMessage(ItemStack.EMPTY, Component.literal("\u00a4该角色已被选择"));
			Excareerwar.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), playerRefreshScreenOverMessageMessage);

			return;
		}
		clearMagicBook(player);
		setPuppetsDie(player, true);

		updatePlayerCareer(player, career);
		handleCareer(player, career);

	}

	private static void clearMagicBook(Player entity) {
		if (!entity.level().isClientSide() && entity.getServer() != null) {
			entity.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(),
							entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
							entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity),
					"clear @s excareerwar:magic_book"
			);
		}
	}

	private static void setPuppetsDie(Player entity, boolean value) {
		CareerHandle.ForgeBusEvents.dispp(entity);
		entity.getPersistentData().putBoolean("Puppetsdie", value);
	}

	private static void handleCareer(Player entity, String careerName) {
		entity.closeContainer();
		Career career = CareerHandle.getCareer(careerName);
		if (career == null) {
			// Handle the case where the career does not exist
			// For example, show an error message to the player
			return;
		}

		CareerWarModVariables.PlayerVariables playerVariables = entity.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null)
				.orElse(new CareerWarModVariables.PlayerVariables());

		if (playerVariables.career == null) {
			career.OnHas("null", entity);
		} else {
			career.OnHas(playerVariables.career, entity);
		}
	}

	private static void updatePlayerCareer(Player entity, String careerName) {
		entity.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null)
				.ifPresent(capability -> {
					capability.career = careerName;
					Career career = CareerHandle.getCareer(careerName);
					if (career!=null){
						career.Skills.forEach(
								skill -> {
									if (skill.firstCooldown !=0){
										{

											Integer skillV = SkillHandle.getSkillV(entity, skill.LocalName);
											SkillHandle.ChangeSkillV(entity, skill.LocalName, skill.firstCooldown);
										}
									}
								}
						);
					}
					capability.syncPlayerVariables(entity);
				});
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		Excareerwar.addNetworkMessage(SelectedCareerButtonMessage.class, SelectedCareerButtonMessage::buffer, SelectedCareerButtonMessage::new, SelectedCareerButtonMessage::handler);
	}

	public static void writeTextState(HashMap<String, String> map, FriendlyByteBuf buffer) {
		buffer.writeInt(map.size());
		for (Map.Entry<String, String> entry : map.entrySet()) {
			buffer.writeUtf(entry.getKey());
			buffer.writeUtf(entry.getValue());
		}
	}

	public static HashMap<String, String> readTextState(FriendlyByteBuf buffer) {
		int size = buffer.readInt();
		HashMap<String, String> map = new HashMap<>();
		for (int i = 0; i < size; i++) {
			String key = buffer.readUtf();
			String value = buffer.readUtf();
			map.put(key, value);
		}
		return map;
	}
}
