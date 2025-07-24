
package net.exmo.excareerwar.content.careers;

import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
@AutoInit
public class Yi extends Career {
	public Yi() throws Exception {
		super("Yi");
		this.IsAlwaysHas = true;
		this.LocalDescription = "Yi_d";
		this.bingItems = true;
		this.Skills.add(SkillHandle.getSkill("AccumulatePowerChop"));
		this.Skills.add(SkillHandle.getSkill("Omnipotent_Explosion"));
		this.Skills.add(SkillHandle.getSkill("SensitiveWaterWhirl"));
		this.Effects.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1000000, 1, false, false, false));
		this.Effects.add(new MobEffectInstance(MobEffects.REGENERATION, 1000000, 0, false, false, false));
		this.Effects.add(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1000000, 0, false, false, false));
		this.itemIcon = Items.DIAMOND_SWORD;
		this.ItemKits.add(new ItemKit(1,"minecraft:diamond_sword{Unbreakable:true,Enchantments:[{id:\"sharpness\",lvl:2s},{id:\"fire_aspect\",lvl:2s},{id:\"sweeping\",lvl:2s}],AttributeModifiers:[{AttributeName:\"generic.attack_damage\",Name:\"generic.attack_damage\",Slot:\"mainhand\",Operation:0,Amount:6.0d,UUID:[I;2039994944,-1946795013,-1905110765,1520241297]},{AttributeName:\"generic.attack_speed\",Name:\"generic.attack_speed\",Slot:\"mainhand\",Operation:0,Amount:-2.6d,UUID:[I;1702580593,-1874310168,-1085528097,865807257]},{AttributeName:\"generic.max_health\",Name:\"generic.max_health\",Slot:\"mainhand\",Operation:0,Amount:1d,UUID:[I;1619971229,-1635761370,-1730110859,-47454858]}],display:{Name:'{\"text\":\"\\\\u00a7l\\\\u00a74极\"}'}}",1,false));
		this.ItemKits.add(new ItemKit(0,"minecraft:stone_sword{Unbreakable:true,Enchantments:[{id:\"fire_aspect\",lvl:2s},{id:\"sweeping\",lvl:2s}],AttributeModifiers:[{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"mainhand\",Operation:0,Amount:6.0d,UUID:[I;1672534749,265437927,-1635972972,913088645]},{AttributeName:\"generic.attack_damage\",Name:\"generic.attack_damage\",Slot:\"mainhand\",Operation:0,Amount:5.0d,UUID:[I;161552313,-1020181367,-1704501186,-1320158221]},{AttributeName:\"generic.attack_speed\",Name:\"generic.attack_speed\",Slot:\"mainhand\",Operation:0,Amount:-2.9d,UUID:[I;-1113777948,-1485027559,-2086690721,-1287413420]}],display:{Name:'{\"text\":\"\\\\u00a7l\\\\u00a77简\"}'}}",1,true));
		this.ItemKits.add(new ItemKit(EquipmentSlot.CHEST,"minecraft:leather_chestplate{Unbreakable:true,Enchantments:[{id:\"protection\",lvl:1s},{id:\"feather_falling\",lvl:2s},{id:\"fire_protection\",lvl:2s},{id:\"binding_curse\",lvl:1s}],AttributeModifiers:[{AttributeName:\"generic.max_health\",Name:\"generic.max_health\",Slot:\"chest\",Operation:0,Amount:6.0d,UUID:[I;-1519584637,-480162630,-1450770654,-1882169739]},{AttributeName:\"generic.attack_damage\",Name:\"generic.attack_damage\",Slot:\"chest\",Operation:1,Amount:-0.4d,UUID:[I;-911762671,1953318090,-1295307296,-666395435]},{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"chest\",Operation:1,Amount:1.0d,UUID:[I;150462662,-445364920,-1257012112,-917960538]},{AttributeName:\"generic.attack_speed\",Name:\"generic.attack_speed\",Slot:\"chest\",Operation:1,Amount:-0.3d,UUID:[I;-1725978898,1091783597,-1162804194,1895685675]}],display:{color:16711680,Name:'{\"text\":\"\\\\u00a7l\\\\u00a74封\"}'},HideFlags:120}",1));
		this.ItemKits.add(new ItemKit(EquipmentSlot.CHEST,"minecraft:diamond_chestplate{Unbreakable:true,Enchantments:[{id:\"binding_curse\",lvl:1s},{id:\"protection\",lvl:1s}],AttributeModifiers:[{AttributeName:\"generic.max_health\",Name:\"generic.max_health\",Slot:\"chest\",Operation:0,Amount:-6.0d,UUID:[I;-781552440,-730316441,-1597350454,-1262542642]},{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"chest\",Operation:0,Amount:-6.0d,UUID:[I;-2083473879,2010072498,-1897673506,1377547607]},{AttributeName:\"generic.attack_damage\",Name:\"generic.attack_damage\",Slot:\"chest\",Operation:1,Amount:0.5d,UUID:[I;1583381643,1880836855,-1380322837,1211072599]},{AttributeName:\"generic.attack_speed\",Name:\"generic.attack_speed\",Slot:\"chest\",Operation:1,Amount:0.1d,UUID:[I;1052393500,-1171700997,-2046696806,1997806481]},{AttributeName:\"generic.movement_speed\",Name:\"generic.movement_speed\",Slot:\"chest\",Operation:1,Amount:0.4d,UUID:[I;1406632242,-262061888,-1980573547,424756267]}],display:{color:16711680,Name:'{\"text\":\"\\\\u00a7l\\\\u00a74解\"}'},HideFlags:120}",1,false));
	}

}
