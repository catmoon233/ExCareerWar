package net.exmo.excareerwar.content.careers;

import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
@AutoInit
public class GodLeader extends Career {
	public GodLeader() throws Exception {
		super("god_leader");
		this.Skills.add(SkillHandle.getSkill("retreat"));
		this.Skills.add(SkillHandle.getSkill("AntiConflict"));
		this.ItemKits.add(new ItemKit(0, "minecraft:bow{Unbreakable:true,Enchantments:[{id:\"infinity\",lvl:1s},{id:\"power\",lvl:3s},{id:\"punch\",lvl:3s},{id:\"knockback\",lvl:2s}],AttributeModifiers:[{AttributeName:\"generic.attack_damage\",Name:\"generic.attack_damage\",Slot:\"mainhand\",Operation:0,Amount:2.0d,UUID:[I;-1462963777,1210863499,-1394164920,4857675]}],display:{Name:'{\"text\":\"\\\\u00a76\\\\u00a7l神领者\"}'}}",1));
		this.ItemKits.add(new ItemKit(1, "minecraft:arrow{}",1));
		this.ItemKits.add(new ItemKit(EquipmentSlot.CHEST, "minecraft:leather_chestplate{Unbreakable:true,Enchantments:[{id:\"protection\",lvl:2s},{id:\"projectile_protection\",lvl:4s},{id:\"feather_falling\",lvl:10s},{id:\"blast_protection\",lvl:1s},{id:\"fire_protection\",lvl:1s},{id:\"binding_curse\",lvl:1s}],AttributeModifiers:[{AttributeName:\"generic.max_health\",Name:\"generic.max_health\",Slot:\"chest\",Operation:0,Amount:10.0d,UUID:[I;-362269272,-1914023976,-1712375799,1856394825]},{AttributeName:\"generic.knockback_resistance\",Name:\"generic.knockback_resistance\",Slot:\"chest\",Operation:0,Amount:0.5d,UUID:[I;-2059123838,-837596811,-1919169844,-1116210276]},{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"chest\",Operation:0,Amount:4.0d,UUID:[I;-1715849181,-362329445,-1234192605,-118596695]}],display:{color:16768512,Name:'{\"text\":\"\\\\u00a7l\\\\u00a76神使之袍\"}'}}",1));
		this.IsAlwaysHas = true;
		this.Effects.add(new MobEffectInstance(CareerWarModMobEffects.FastCastEMobEffect.get(), 40, 3));
		this.bingItems = true;
		this.itemIcon = Items.BOW;
		this.LocalDescription = "god_leader_d";

	}

}


