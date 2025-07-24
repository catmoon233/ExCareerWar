package net.exmo.excareerwar.init;

import net.exmo.excareerwar.Excareerwar;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CareerWarModAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Excareerwar.MODID);
	public static final RegistryObject<Attribute> DODGE = ATTRIBUTES.register("dodge", () -> (new RangedAttribute("attribute." + Excareerwar.MODID + ".dodge", 0, 0, 100000000)).setSyncable(true));
	public static final RegistryObject<Attribute> FREEZINGPROBABILITY = ATTRIBUTES.register("freezing_probability", () -> (new RangedAttribute("attribute." + Excareerwar.MODID + ".freezing_probability", 0, 0, 100000000)).setSyncable(true));
	public static final RegistryObject<Attribute> JUMPHIGHT = ATTRIBUTES.register("jump_hight", () -> (new RangedAttribute("attribute." + Excareerwar.MODID + ".jump_hight", 1, 0, 1000000)).setSyncable(true));

	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		event.enqueueWork(() -> {
			ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
		});
	}

	@SubscribeEvent
	public static void addAttributes(EntityAttributeModificationEvent event) {
		List<EntityType<? extends LivingEntity>> entityTypes = event.getTypes();
		entityTypes.forEach((e) -> {
			Class<? extends Entity> baseClass = e.getBaseClass();
			if (baseClass.isAssignableFrom(Mob.class)) {
				event.add(e, DODGE.get());
			}
		});
		entityTypes.forEach((e) -> {
			Class<? extends Entity> baseClass = e.getBaseClass();
			if (baseClass.isAssignableFrom(Mob.class)) {
				event.add(e, FREEZINGPROBABILITY.get());
			}
		});
		entityTypes.forEach((e) -> {
			Class<? extends Entity> baseClass = e.getBaseClass();
			if (baseClass.isAssignableFrom(Mob.class)) {
				event.add(e, JUMPHIGHT.get());
			}
		});
	}

	@Mod.EventBusSubscriber
	private class Utils {
		@SubscribeEvent
		public static void persistAttributes(PlayerEvent.Clone event) {
			Player oldP = event.getOriginal();
			Player newP = (Player) event.getEntity();
			newP.getAttribute(DODGE.get()).setBaseValue(oldP.getAttribute(DODGE.get()).getBaseValue());
			newP.getAttribute(FREEZINGPROBABILITY.get()).setBaseValue(oldP.getAttribute(FREEZINGPROBABILITY.get()).getBaseValue());
			newP.getAttribute(JUMPHIGHT.get()).setBaseValue(oldP.getAttribute(JUMPHIGHT.get()).getBaseValue());
		}
	}
}
