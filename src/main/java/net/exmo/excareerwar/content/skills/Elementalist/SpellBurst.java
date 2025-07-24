
package net.exmo.excareerwar.content.skills.Elementalist;

import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;
@AutoInit
public class SpellBurst  extends CareerSkill {
	public SpellBurst() {
		super("SpellBurst");
		this.CoolDown = 40*40;
		this.Icon = new ItemStack(net.exmo.excareerwar.init.CareerWarModItems.MAGIC_BOOK.get()).getItem();
		this.LocalDescription = "SpellBurst_d";
		firstCooldown=30*40;

	}
	public void use(Player player) {
		if (!this.setCoolDown(player)) return;
		player.addEffect(new MobEffectInstance(CareerWarModMobEffects.SPELL_BURST_E.get(), 320, 1));
		player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 320, 2));
		player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 320, 1));
		player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 320, 1));
		if (player.getInventory().countItem(CareerWarModItems.MAGIC_BOOK.get())+3<=6){
		// 创建一个包含所有可能 case 值的列表
		List<String> magicNames = List.of(
				"LightingHit",
				"iceBall",
				"FireBall",
				"NatureShield",
				"FireField",
				"steam",
				"tornado",
				"earthquake",
				"teleporting",
				"Black_Hold"
		);

		// 使用 Random 类生成一个随机索引
		for (int i = 0; i < 3; i++) {
			Random random = new Random();
			int randomIndex = random.nextInt(magicNames.size());
			ItemStack is = new ItemStack(CareerWarModItems.MAGIC_BOOK.get());
			// 从列表中获取随机选择的 case 值
			String selectedMagicName = magicNames.get(randomIndex);
			is.setHoverName(Component.translatable(selectedMagicName));
			is.getOrCreateTag().putString("MagicName", selectedMagicName);
			player.addItem(is);

		}
		}
	}

}
