
package net.exmo.excareerwar.content.skills.FireLord;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@AutoInit
@Mod.EventBusSubscriber
public class FlameCharge extends CareerSkill {
    public FlameCharge() {
        super("Flame_Charge");
        this.CoolDown = (int) (40*4.5);
        this.Icon = Items.SHIELD;
        this.LocalDescription = "Flame_Charge_d";
    }
    @Override
    public void use(Player player) {
        if (!setCoolDown(player))return;
        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
//        Map<String, String> a = capability.playercooldown;
//        if (a.containsKey("Flame_Charge1")) a.replace("Flame_Charge1", "0");
//        a.put("Flame_Charge1", "60");
//        capability.playercooldown = a;
//        capability.syncPlayerVariables(player);
        });
        if (player.level().isClientSide) return;
        SpellRegistry.BURNING_DASH_SPELL.get().attemptInitiateCast(ItemStack.EMPTY, 1, player.level(), (ServerPlayer) player, CastSource.COMMAND, false, "command");
         player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 80, 1, false, false, false));
    }
    @SubscribeEvent
    public static void atHit(LivingHurtEvent event){

        if (event.getSource().getEntity() instanceof Player player) {
            LivingEntity entityiterator =  event.getEntity();
            if (isSameTeam(entityiterator, player))return;
            player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                String career = capability.career;
                if (career == null)return;
                if (career.equals("fire_lord")) {

                    if (player.hasEffect(MobEffectRegistry.BURNING_DASH.get())) {
                        if (SpellRegistry.BURNING_DASH_SPELL.get().getDamageSource(player).get().type().equals(event.getSource().type())) {
                            Map<String, Integer> a = capability.playercooldown;

                            entityiterator.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 4, (false), (false)));
											entityiterator.setSecondsOnFire(5);
											entityiterator.invulnerableTime = 0;
											entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 2f);
											entityiterator.setDeltaMovement(new Vec3(0, 1, 0));
                                            entityiterator.setSecondsOnFire(4);
                            a.replace("Flame_Charge", Math.max(((capability.playercooldown.get("Flame_Charge")) - 70), 0));
                            capability.playercooldown = a;
                            capability.syncPlayerVariables(player);
                        }
                        ;
                    }
                }

            });
        }
    }
}
