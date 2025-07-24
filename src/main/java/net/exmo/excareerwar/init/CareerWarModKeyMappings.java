
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.exmo.excareerwar.init;

import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.Excareerwar;

import net.exmo.excareerwar.network.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class CareerWarModKeyMappings {
	public static final KeyMapping SKILL_1 = new KeyMapping("key.career_war.skill_1", GLFW.GLFW_KEY_UNKNOWN, "key.categories.career_war") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				Excareerwar.PACKET_HANDLER.sendToServer(new Skill1Message(0, 0));
				Skill1Message.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping SKILL_2 = new KeyMapping("key.career_war.skill_2", GLFW.GLFW_KEY_UNKNOWN, "key.categories.career_war") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				Excareerwar.PACKET_HANDLER.sendToServer(new Skill2Message(0, 0));
				Skill2Message.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping SKILL_3 = new KeyMapping("key.career_war.skill_3", GLFW.GLFW_KEY_UNKNOWN, "key.categories.career_war") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				Excareerwar.PACKET_HANDLER.sendToServer(new Skill3Message(0, 0));
				Skill3Message.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping SKILL_4 = new KeyMapping("key.career_war.skill_4", GLFW.GLFW_KEY_UNKNOWN, "key.categories.career_war") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				Excareerwar.PACKET_HANDLER.sendToServer(new Skill4Message(0, 0));
				Skill4Message.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping ChangeCareer = new KeyMapping("key.career_war.change_career", GLFW.GLFW_KEY_H, "key.categories.career_war") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				Excareerwar.PACKET_HANDLER.sendToServer(new ChangeCareerMessage(0, 0));
				ChangeCareerMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(SKILL_1);
		event.register(SKILL_2);
		event.register(SKILL_3);
		event.register(SKILL_4);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				SKILL_1.consumeClick();
				SKILL_2.consumeClick();
				SKILL_3.consumeClick();
				SKILL_4.consumeClick();
			}
		}
	}
}
