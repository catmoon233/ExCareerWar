
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.exmo.excareerwar.init;


import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.entity.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CareerWarModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Excareerwar.MODID);
	public static final RegistryObject<EntityType<PuppetmasterontologyEntity>> PUPPETMASTERONTOLOGY = register("puppetmasterontology",
			EntityType.Builder.<PuppetmasterontologyEntity>of(PuppetmasterontologyEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(PuppetmasterontologyEntity::new)

					.sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<VerticalStringEntity>> VERTICAL_STRING = register("vertical_string",
			EntityType.Builder.<VerticalStringEntity>of(VerticalStringEntity::new, MobCategory.MISC).setCustomClientFactory(VerticalStringEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));

	public static final RegistryObject<EntityType<FenrirArrowEntity>> FenrirArrow = register("fenrir_arrow_enttity",
			EntityType.Builder.<FenrirArrowEntity>of(FenrirArrowEntity::new, MobCategory.MISC).setCustomClientFactory(FenrirArrowEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));

	public static final RegistryObject<EntityType<WatersEntity>> WATERS = register("waters",
			EntityType.Builder.<WatersEntity>of(WatersEntity::new, MobCategory.MISC).setCustomClientFactory(WatersEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<IceBallEntity>> ICE_BALL = register("ice_ball",
			EntityType.Builder.<IceBallEntity>of(IceBallEntity::new, MobCategory.MISC).setCustomClientFactory(IceBallEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<WolfProjectile>> WolfProjectile = register("projectile_wolf", EntityType.Builder.<WolfProjectile>of(WolfProjectile::new, MobCategory.MISC)
			.setCustomClientFactory(WolfProjectile::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));

    public static final RegistryObject<EntityType<MistArrowEntity>> MistArrow = register("mist_arrow",
            EntityType.Builder.<MistArrowEntity>of(MistArrowEntity::new, MobCategory.MISC)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(4)
                    .setUpdateInterval(20)
                    .sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<MistArrowEffectEntity>> MistArrowEffect = register("mist_arrow_effect",
            EntityType.Builder.<MistArrowEffectEntity>of(MistArrowEffectEntity::new, MobCategory.MISC)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(4)
                    .setUpdateInterval(20)
                    .sized(0.1f, 0.1f));


	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			PuppetmasterontologyEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(PUPPETMASTERONTOLOGY.get(), PuppetmasterontologyEntity.createAttributes().build());
	}
}
