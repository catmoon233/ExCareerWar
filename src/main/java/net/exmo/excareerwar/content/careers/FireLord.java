
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
public class FireLord extends Career {
	public FireLord() throws Exception {
		super("fire_lord");
		this.itemIcon = Items.BLAZE_POWDER	;
		this.Skills.add(SkillHandle.getSkill("flame_field"));
		this.Skills.add(SkillHandle.getSkill("Flame_Charge"));
		this.bingItems= true;
		this.LocalDescription = "fire_lord_d";
		this.IsAlwaysHas= true;
		this.ItemKits.add(new ItemKit(0, "minecraft:stone_sword{Unbreakable:true,Enchantments:[{id:\"fire_aspect\",lvl:2s},{id:\"sweeping\",lvl:2s}],AttributeModifiers:[{AttributeName:\"generic.attack_damage\",Name:\"generic.attack_damage\",Slot:\"mainhand\",Operation:0,Amount:4.0d,UUID:[I;-850288116,-2145367561,-1074312044,-1384491735]},{AttributeName:\"generic.attack_speed\",Name:\"generic.attack_speed\",Slot:\"mainhand\",Operation:0,Amount:-2.9d,UUID:[I;1428451962,-1297462222,-1642829968,1568748030]},{AttributeName:\"generic.knockback_resistance\",Name:\"generic.knockback_resistance\",Slot:\"mainhand\",Operation:0,Amount:2.0d,UUID:[I;-1798717754,-325499177,-2122330652,934621485]}],display:{Name:'{\"text\":\"\\\\u00a74\\\\u00a7l灼焰\"}'}}", 1));
		this.ItemKits.add(new ItemKit(EquipmentSlot.CHEST, "minecraft:leather_chestplate{Unbreakable:true,Enchantments:[{id:\"protection\",lvl:1s},{id:\"fire_protection\",lvl:10s},{id:\"feather_falling\",lvl:1s},{id:\"projectile_protection\",lvl:2s},{id:\"binding_curse\",lvl:1s},{id:\"blast_protection\",lvl:3s}],AttributeModifiers:[{AttributeName:\"generic.max_health\",Name:\"generic.max_health\",Slot:\"chest\",Operation:0,Amount:15.0d,UUID:[I;515557183,38553655,-1693062785,2975891]},{AttributeName:\"generic.movement_speed\",Name:\"generic.movement_speed\",Slot:\"chest\",Operation:1,Amount:-0.3d,UUID:[I;-383185298,-1543156197,-1119638655,-1235512117]},{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"chest\",Operation:0,Amount:5.5d,UUID:[I;476750780,595018704,-1272047914,-999761766]}],display:{color:16711680,Name:'{\"text\":\"\\\\u00a74\\\\u00a7l灼焰魔甲\"}'}}", 1));
		this.Effects.add(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 99999999,0));
	}

}
