
package net.exmo.excareerwar.client.renderer;


import net.exmo.excareerwar.entity.PuppetmasterontologyEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class PuppetmasterontologyRenderer extends HumanoidMobRenderer<net.exmo.excareerwar.entity.PuppetmasterontologyEntity, HumanoidModel<PuppetmasterontologyEntity>> {
	public PuppetmasterontologyRenderer(EntityRendererProvider.Context context) {
		super(context, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
		this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(net.exmo.excareerwar.entity.PuppetmasterontologyEntity entity) {
		return new ResourceLocation("career_war:textures/entities/2941358395984227493.png");
	}
}
