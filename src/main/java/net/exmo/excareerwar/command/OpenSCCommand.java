
package net.exmo.excareerwar.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.exmo.excareerwar.content.OpenSClc;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OpenSCCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("opensc").requires(s -> s.hasPermission(4)).then(Commands.argument("name", EntityArgument.player()).executes(arguments -> {
			Level world = arguments.getSource().getUnsidedLevel();
			double x = arguments.getSource().getPosition().x();
			double y = arguments.getSource().getPosition().y();
			double z = arguments.getSource().getPosition().z();
			Entity entity = arguments.getSource().getEntity();
			if (entity == null && world instanceof ServerLevel _servLevel)
				entity = FakePlayerFactory.getMinecraft(_servLevel);
			Direction direction = Direction.DOWN;
			if (entity != null)
				direction = entity.getDirection();

			OpenSClc.execute(world, x, y, z, arguments);
			return 0;
		})));
		event.getDispatcher().register(Commands.literal("setCareer").requires(s -> s.hasPermission(4)).then(Commands.argument("name", EntityArgument.players()).then(Commands.argument("career", StringArgumentType.string()).executes(arguments -> {
			Level world = arguments.getSource().getUnsidedLevel();
			double x = arguments.getSource().getPosition().x();
			double y = arguments.getSource().getPosition().y();
			double z = arguments.getSource().getPosition().z();
			Entity entity = arguments.getSource().getEntity();
			if (entity == null && world instanceof ServerLevel _servLevel)
				entity = FakePlayerFactory.getMinecraft(_servLevel);
			Direction direction = Direction.DOWN;
			if (entity != null)
				direction = entity.getDirection();

			for (ServerPlayer e : EntityArgument.getPlayers(arguments, "name")){
				e.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(playerVariables->{
					playerVariables.career = StringArgumentType.getString(arguments, "career");
					playerVariables.syncPlayerVariables( e);
				});
			}
			return 0;
		}))));
	}
}
