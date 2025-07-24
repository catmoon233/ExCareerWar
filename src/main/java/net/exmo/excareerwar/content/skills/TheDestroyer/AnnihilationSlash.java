package net.exmo.excareerwar.content.skills.TheDestroyer;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashProjectile;
import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

@AutoInit
public class AnnihilationSlash extends CareerSkill {
    public AnnihilationSlash() {
        super("AnnihilationSlash");
        this.CoolDown = (int) (40 * 5);
        this.IconTexture = SpellRegistry.BLOOD_SLASH_SPELL.get().getSpellIconResource();
        this.LocalDescription = "AnnihilationSlash_d";
    }

    @Override
    public void use(Player player) {

        if (isInCooldown(player)) return;
        if (player.level().isClientSide){
            ClientSpellCastHelper.handleClientBoundOnCastStarted(player.getUUID(),"irons_spellbooks:blood_slash",5);
        }
        player.swing(player.getUsedItemHand());
        Level world = player.level();
        for (int index = -1; index < 2; index++) {
            int finalIndex = index;
            Excareerwar.queueServerWork(index + 2, () -> {
                double x = player.getX() + (Math.cos(Math.toRadians(player.getYRot())) * 2) * finalIndex * 1;
                double z = player.getZ() + (Math.sin(Math.toRadians(player.getYRot())) * 2) * finalIndex * 1;
                BloodSlashProjectile bloodSlash = new BloodSlashProjectile(world, player);
                bloodSlash.setPos(x, player.getEyePosition().y(), z);
                bloodSlash.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, 2, 0);
                bloodSlash.getPersistentData().putBoolean("AnnihilationSlash", true);
                bloodSlash.setDamage(3.7f);
                world.addFreshEntity(bloodSlash);
            });
            super.use(player);
        }
    }
}
