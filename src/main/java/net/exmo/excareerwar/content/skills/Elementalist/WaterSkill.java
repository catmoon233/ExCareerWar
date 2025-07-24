package net.exmo.excareerwar.content.skills.Elementalist;


import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.content.careers.Elementalist;
import net.exmo.excareerwar.entity.WatersEntity;
import net.exmo.excareerwar.init.CareerWarModEntities;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
@AutoInit
public class WaterSkill extends CareerSkill {
	public WaterSkill() {
		super("water");
		this.Icon = Items.WATER_BUCKET;
		this.CoolDown = 4*40;
		this.LocalDescription = "water_skill_d";
	}
	public void use(Player player) {
		if (!this.setCoolDown(player))return;
	//	Elementalist.skillCooldownin(player);
		if (player.hasEffect(CareerWarModMobEffects.SPELL_BURST_E.get())){
			SkillHandle.ChangeSkillV(player,"water",30);
		}
		Elementalist.magicSet(player,"water");
		player.swing(player.getUsedItemHand());
		for (int index = -1; index < 2; index++) {
			double x = player.getX() + (Math.cos(Math.toRadians(player.getYRot())) * 2) * index*0.5;
			double z = player.getZ() + (Math.sin(Math.toRadians(player.getYRot())) * 2) * index*0.5;


			Entity _shootFrom = player;
			Level projectileLevel = _shootFrom.level();
				if (!projectileLevel.isClientSide()) {
					Projectile _entityToSpawn = new Object() {
						public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
							AbstractArrow entityToSpawn = new WatersEntity(CareerWarModEntities.WATERS.get(), level);
							entityToSpawn.setOwner(shooter);
							entityToSpawn.setBaseDamage(damage);
							entityToSpawn.setKnockback(knockback);
							return entityToSpawn;
						}
					}.getArrow(projectileLevel, player, 0.75f, 1);
					_entityToSpawn.setPos(x, _shootFrom.getEyeY() - 0.1, z);
					_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, 5, 0);
					projectileLevel.addFreshEntity(_entityToSpawn);
				}

		}
	}
}
