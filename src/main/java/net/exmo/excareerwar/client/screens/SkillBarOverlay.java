
package net.exmo.excareerwar.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.CareerHandle;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.init.CareerWarModKeyMappings;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.UiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class SkillBarOverlay {
	static int screenHeight;
    static int screenWidth;
    private static final Map<String, Long> skillAnimationMap = new HashMap<>();
    private static final Map<Integer, String> keyMap = new HashMap<>();
    static final float ICON_SCALE = 1.1f; // 图标放大比例
    static final int ICON_SIZE = 20; // 原始图标大小
    static final int SCALED_SIZE = (int) (ICON_SIZE * ICON_SCALE); // 放大后的大小
    static final int SPACING = 6; // 技能间间隔

    // 初始化按键映射
    static {
        keyMap.put(0, "1");
        keyMap.put(1, "2");
        keyMap.put(2, "3");
        keyMap.put(3, "4");
    }

    // 触发技能动画
    public static void triggerSkillAnimation(String skillName) {
        skillAnimationMap.put(skillName, System.currentTimeMillis());
    }
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getWindow().getGuiScaledWidth();
		int h = event.getWindow().getGuiScaledHeight();
		Level world = null;
		double x = 0;
		double y = 0;
		double z = 0;
		Player entity = Minecraft.getInstance().player;
		if (entity != null) {
			world = entity.level();
			x = entity.getX();
			y = entity.getY();
			z = entity.getZ();
		}
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.setShaderColor(1, 1, 1, 1);
        screenWidth = w;
        screenHeight = h;
        int	centerX = screenWidth / 2 - Math.max(110, screenWidth / 4);
        int centerY = screenHeight - Math.max(55, screenHeight / 8);
        CareerWarModVariables.PlayerVariables v = entity.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new CareerWarModVariables.PlayerVariables());
        Career career = CareerHandle.getCareer(v.career);

        if(career == null)return;
        GuiGraphics gg = event.getGuiGraphics();
        // 计算技能栏位置（居中）
        int totalWidth = career.Skills.size() * (SCALED_SIZE + SPACING) - SPACING;
        int startX = (screenWidth - totalWidth) / 2;
        int startY = screenHeight - 60; // 离底部60像素
        //	CareerWarMod.LOGGER.debug(career.LocalName + " rendered" + career.Skills.size());
        var map = new HashMap<Integer,String>();
        if (career==null)return;

        for (int i = 0; i < career.Skills.size(); i++) {
            CareerSkill careerSkill = career.Skills.get(i);
            if (careerSkill == null) break;

            // 计算技能位置（考虑间隔）
            int xPos = startX + i * (SCALED_SIZE + SPACING);
            int yPos = startY;

            // 检查是否在动画中
            boolean isAnimating = false;
            float animationProgress = 0f;
            if (skillAnimationMap.containsKey(careerSkill.LocalName)) {
                long startTime = skillAnimationMap.get(careerSkill.LocalName);
                long elapsed = System.currentTimeMillis() - startTime;
                animationProgress = Math.min(elapsed / 300f, 1f); // 300ms动画

                if (animationProgress >= 1f) {
                    skillAnimationMap.remove(careerSkill.LocalName);
                } else {
                    isAnimating = true;
                }
            }

            // 应用缩放变换
            gg.pose().pushPose();
            gg.pose().translate(xPos, yPos, 0);

            // 动画效果：使用技能时图标放大
            if (isAnimating) {
                float scaleFactor = 1 + 0.3f * (1 - animationProgress);
                gg.pose().scale(scaleFactor, scaleFactor, 1f);
                gg.pose().translate(
                        -((scaleFactor - 1) * ICON_SIZE / 2),
                        -((scaleFactor - 1) * ICON_SIZE / 2),
                        0
                );
            }

            // 绘制技能背景（使用新纹理）
            ResourceLocation skillBg = new ResourceLocation("excareerwar:textures/screens/skill_bg.png");
            gg.blit(skillBg, 0, 0, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

            // 绘制技能图标
            if (careerSkill.IconTexture != null) {
                // 应用图标放大
                gg.pose().pushPose();
                gg.pose().scale(ICON_SCALE, ICON_SCALE, 1f);
                // 修改图标绘制位置为居中
                gg.blit(careerSkill.IconTexture, 
                       (ICON_SIZE/2 - 9),
                       (ICON_SIZE/2 - 9),
                       0, 0, 16, 16, 16, 16);
                gg.pose().popPose();
            } else if (careerSkill.Icon != null) {
                gg.pose().pushPose();
                gg.pose().scale(ICON_SCALE, ICON_SCALE, 1f);
                gg.renderItem(careerSkill.Icon.getDefaultInstance(), 
                             (ICON_SIZE/2 - 9),
                             (ICON_SIZE/2 - 9));
                gg.pose().popPose();
            }

            // 绘制冷却效果
            if (v.playercooldown.containsKey(careerSkill.LocalName) &&
                    v.playercooldown.get(careerSkill.LocalName) > 0) {

                float cooldownPercent =( v.playercooldown.get(careerSkill.LocalName) / (float) careerSkill.CoolDown);
                int cooldownHeight = (int) (19 * cooldownPercent);

                // 修改冷却层为顶部填充
                gg.setColor(0, 0, 0, 0.7f);
                gg.fill(1, 1, ICON_SIZE-1, ICON_SIZE-1, 0x80000000);
                gg.setColor(1, 1, 1, 1);

                // 冷却进度条改为顶部开始
                gg.setColor(0.2f, 0.2f, 0.8f, 0.6f);
                gg.fill(1, 1, ICON_SIZE-1, cooldownHeight, Color.darkGray.getRGB());
                gg.setColor(1, 1, 1, 1);

                // 冷却时间显示位置调整
                float secondsLeft = v.playercooldown.get(careerSkill.LocalName) / 40f;
                String formattedSeconds = String.format("%.1f", secondsLeft);
                if (secondsLeft > 0) {
                    String cooldownText = formattedSeconds + "s";
                    int textWidth = Minecraft.getInstance().font.width(cooldownText);
                    UiUtil.renderLineStr(gg, List.of(cooldownText), Minecraft.getInstance().font,
                            0     + textWidth / 2,
                            ICON_SIZE/2  - 4,
                            0xFFFFFF, 0x80000000, UiUtil.displaystyle.center);
                }
            }

            gg.pose().popPose(); // 结束变换

            // 绘制按键映射（只显示前4个技能的按键）
            if (i < 4 && keyMap.containsKey(i)) {
                String keyText = keyMap.get(i);
                Integer i1 = Integer.valueOf(keyText);
                if (i1 ==1){
                    keyText = CareerWarModKeyMappings.SKILL_1.getTranslatedKeyMessage().getString();
                }
                if (i1 ==2){
                    keyText = CareerWarModKeyMappings.SKILL_2.getTranslatedKeyMessage().getString();
                }
                if (i1 ==3){
                    keyText = CareerWarModKeyMappings.SKILL_3.getTranslatedKeyMessage().getString();
                }
                if (i1 ==4){
                    keyText = CareerWarModKeyMappings.SKILL_4.getTranslatedKeyMessage().getString();
                }

                int textWidth = Minecraft.getInstance().font.width(keyText);
                int keyX = xPos + (ICON_SIZE - textWidth) / 2;
                int keyY = yPos + ICON_SIZE + 2;

                // 按键背景
                gg.fill(keyX - 2, keyY - 2, keyX + textWidth + 2, keyY + 10, 0x80000000);

                // 按键文字
                gg.drawString(Minecraft.getInstance().font, keyText, keyX, keyY, 0xFFD700, false);
            }
        }


        RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
}
