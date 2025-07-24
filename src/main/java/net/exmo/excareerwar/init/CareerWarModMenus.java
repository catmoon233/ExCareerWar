
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.exmo.excareerwar.init;


import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.inventory.SelectedCareerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CareerWarModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Excareerwar.MODID);
	public static final RegistryObject<MenuType<SelectedCareerMenu>> SELECTED_CAREER = REGISTRY.register("selected_career", () -> IForgeMenuType.create(SelectedCareerMenu::new));
}
