
package net.exmo.excareerwar.content.skills.Yi;

import net.exmo.excareerwar.content.CareerHandle;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@AutoInit
@Mod.EventBusSubscriber
public class OmnipotentExplosion extends CareerSkill {

	public OmnipotentExplosion() {
		super("Omnipotent_Explosion");
		this.CoolDown = 800;
		this.LocalDescription = "Omnipotent_Explosion_d";
		this.Icon= Items.DIAMOND_CHESTPLATE;
	}
	public static void Wear(Player player){
		player.setItemSlot(EquipmentSlot.CHEST, CareerHandle.getCareer("Yi").ItemKits.get(3).itemStack);
		player.getInventory().items.stream().filter(itemStack -> itemStack.getItem()==Items.STONE_SWORD).forEach(
				itemStack -> player.getInventory().removeItem(itemStack)
		);
		player.setItemSlot(EquipmentSlot.MAINHAND,CareerHandle.getCareer("Yi").ItemKits.get(0).itemStack.copy());

	}
	public static void Take(Player player){
		player.setItemSlot(EquipmentSlot.CHEST,CareerHandle.getCareer("Yi").ItemKits.get(2).itemStack.copy());

		player.getInventory().items.stream().filter(itemStack -> itemStack.getItem()==Items.DIAMOND_SWORD).forEach(
				itemStack -> player.getInventory().removeItem(itemStack)
		);
		player.setItemSlot(EquipmentSlot.MAINHAND,CareerHandle.getCareer("Yi").ItemKits.get(1).itemStack.copy());
		}

	@SubscribeEvent
	public  static void Living(TickEvent.PlayerTickEvent event){
		Player player = event.player;
		if (Integer.valueOf(SkillHandle.getSkillV(player,"Omnipotent_Explosion1"))==1) {
			Take(player);
		}
		if (Integer.valueOf(SkillHandle.getSkillV(player,"Omnipotent_Explosion1"))>0) {
			if (player.level() instanceof ServerLevel level){
				level.sendParticles(ParticleTypes.GLOW,player.getX(),player.getY()+1,player.getZ(),10,0.3,0.3,0.3,0.2);
				player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 3));
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0));
				player.addEffect(new MobEffectInstance(CareerWarModMobEffects.VULNERABILITY.get(), 60, 1));

			}
		}
	}
	@SubscribeEvent
	public  static void AtDeath(LivingDeathEvent e){
		if (e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			if (Integer.valueOf(SkillHandle.getSkillV(player,"Omnipotent_Explosion1"))>0){
				Take(player);
			}
		}
	}

	public void use(Player player) {
		if (!this.setCoolDown(player))return;
		Wear(player);
		if (player.level() instanceof ServerLevel level) {
			level.sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY(), player.getZ(), 30, 1.5, 1.5, 1.5, 0.3);
		}
		SkillHandle.ChangeSkillV(player,"Omnipotent_Explosion1",300);

	}
}
