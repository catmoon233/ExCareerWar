package net.exmo.excareerwar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.exmo.excareerwar.client.model.WolfModel;
import net.exmo.excareerwar.entity.WolfProjectile;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.Projectile;

public class WolfProjectileRender extends EntityRenderer<Projectile> {
	private static final ResourceLocation texture = new ResourceLocation("minecraft:textures/entity/wolf/wolf_tame.png");
	private final WolfModel model;

	public WolfProjectileRender(EntityRendererProvider.Context context) {
		super(context);
		model = new WolfModel<>(context.bakeLayer(WolfModel.LAYER_LOCATION));
	}

	@Override
	public void render(Projectile entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		if (entityIn.isInvisible())return;
		VertexConsumer vb = bufferIn.getBuffer(RenderType.entityCutout(this.getTextureLocation(entityIn)));
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 180));
		poseStack.mulPose(Axis.ZP.rotationDegrees(180 + Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
		model.renderToBuffer(poseStack, vb, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.0625f);
		poseStack.popPose();
		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(Projectile wolfProjectile) {
		return texture;
	}

}
