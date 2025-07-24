
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.exmo.excareerwar.init;

import net.exmo.excareerwar.client.gui.SelectedCareerScreen;
import net.exmo.excareerwar.inventory.SelectedCareerMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CareerWarModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(CareerWarModMenus.SELECTED_CAREER.get(), SelectedCareerScreen::new);
		});
	}
}
