

package net.exmo.excareerwar.content.careers;

import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

import java.awt.*;

@AutoInit
public class ZhiMingDuShi extends Career {
	public ZhiMingDuShi() throws Exception {
		super("ZhiMingDuShi");
		this.IsAlwaysHas = true;
		this.LocalDescription = "ZhiMingDuShi_d";
		this.bingItems = true;


		this.itemIcon = Items.SPLASH_POTION;
		this.ItemKits.add(new ItemKit(EquipmentSlot.CHEST,"minecraft:leather_chestplate{Unbreakable:true,display:{color:" +
				Color.magenta.getRGB() +
				",Name:'{\"text\":\"\\\\u00a7l\\\\u00a75药袍\"}'},HideFlags:120}",1));
		this.ItemKits.add(new ItemKit(EquipmentSlot.LEGS,"minecraft:leather_leggings{Unbreakable:true,display:{color:" +
				Color.magenta.getRGB() +
				",Name:'{\"text\":\"\\\\u00a7l\\\\u00a75药袍\"}'},HideFlags:120}",1));
		this.ItemKits.add(new ItemKit(EquipmentSlot.FEET,"minecraft:leather_boots{Unbreakable:true,display:{color:" +
				Color.magenta.getRGB() +
				",Name:'{\"text\":\"\\\\u00a7l\\\\u00a75药袍\"}'},HideFlags:120}",1));
		this.ItemKits.add(new ItemKit(EquipmentSlot.HEAD,"minecraft:leather_helmet{Unbreakable:true,display:{color:" +
				Color.magenta.getRGB() +
				",Name:'{\"text\":\"\\\\u00a7l\\\\u00a75药袍\"}'},HideFlags:120}",1));
	}

}
