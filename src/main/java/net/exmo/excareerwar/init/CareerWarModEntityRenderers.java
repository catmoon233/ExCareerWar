
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.exmo.excareerwar.init;

import net.exmo.excareerwar.client.renderer.PuppetmasterontologyRenderer;
import net.exmo.excareerwar.client.renderer.WolfProjectileRender;
import net.exmo.excareerwar.entity.FenrirArrowEntity;
import net.exmo.excareerwar.entity.MistArrowEffectEntity;
import net.exmo.excareerwar.entity.MistArrowEntity;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CareerWarModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CareerWarModEntities.PUPPETMASTERONTOLOGY.get(), PuppetmasterontologyRenderer::new);
		event.registerEntityRenderer(CareerWarModEntities.VERTICAL_STRING.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(CareerWarModEntities.WATERS.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(CareerWarModEntities.WolfProjectile.get(), WolfProjectileRender::new);
		event.registerEntityRenderer(CareerWarModEntities.ICE_BALL.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(CareerWarModEntities.FenrirArrow.get(), FenrirArrowRender::new);
		event.registerEntityRenderer(CareerWarModEntities.MistArrow.get(), MistArrowRenderer::new);
		event.registerEntityRenderer(CareerWarModEntities.MistArrowEffect.get(), WolfProjectileRender::new);
	}
	public static class FenrirArrowRender extends ArrowRenderer<FenrirArrowEntity> {
		public FenrirArrowRender(EntityRendererProvider.Context context) {
			super(context);
		}

		@Override
		public ResourceLocation getTextureLocation(FenrirArrowEntity p_114482_) {
			return ResourceLocation.tryBuild("excareerwar", "textures/entities/fenrir_arrow.png");
		}
	}

	public static class MistArrowRenderer extends ArrowRenderer<MistArrowEntity> {
		public MistArrowRenderer(EntityRendererProvider.Context context) {
			super(context);
		}

		@Override
		public ResourceLocation getTextureLocation(MistArrowEntity p_114482_) {
			return ResourceLocation.tryBuild("excareerwar", "textures/entities/mist_arrow.png");
		}
	}
}
