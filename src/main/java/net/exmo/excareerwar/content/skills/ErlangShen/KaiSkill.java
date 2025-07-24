package net.exmo.excareerwar.content.skills.ErlangShen;

import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.entity.VerticalStringEntity;
import net.exmo.excareerwar.entity.WolfProjectile;
import net.exmo.excareerwar.init.CareerWarModEntities;
import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
@AutoInit
public class KaiSkill extends CareerSkill {
    public KaiSkill() {
        super("KaiSkill");
        this.CoolDown = 40*9;
        this.Icon = CareerWarModItems.TIAN_JI.get();
        this.LocalDescription = "KaiSkill_d";
    }

    @Override
    public void use(Player player) {
        if (isInCooldown(player))return;
        {
            player.swing(player.getUsedItemHand());
            Entity _shootFrom = player;
            Level projectileLevel = _shootFrom.level();
            if (!projectileLevel.isClientSide()) {
                Projectile _entityToSpawn = new Object() {
                    public Projectile getArrow(Level level, Entity shooter, float damage, int knockback, byte piercing) {
                        AbstractArrow entityToSpawn = new WolfProjectile(CareerWarModEntities.WolfProjectile.get(), level);
                        entityToSpawn.setOwner(shooter);
                        entityToSpawn.setBaseDamage(damage);
                        entityToSpawn.setKnockback(knockback);
                        entityToSpawn.setSilent(true);
                        entityToSpawn.setPierceLevel(piercing);
                        entityToSpawn.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        return entityToSpawn;
                    }
                }.getArrow(projectileLevel, player, 3f, 1, (byte) 0);
                _entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
                _entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, 2.6f, 0);
                projectileLevel.addFreshEntity(_entityToSpawn);
            }
        }
        super.use(player);
    }
}
