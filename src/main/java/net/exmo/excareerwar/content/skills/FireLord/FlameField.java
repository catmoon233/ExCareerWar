
package net.exmo.excareerwar.content.skills.FireLord;

import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
@AutoInit
@Mod.EventBusSubscriber
public class FlameField extends CareerSkill {
    public FlameField() {
        super("flame_field");
        this.CoolDown = 400;
        this.Icon = Items.FIRE_CORAL;
        this.LocalDescription = "flame_field_d";
    }
    @Override
    public void use(Player player) {
        if (!setCoolDown(player)) return;
        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
        Map<String, Integer> a = capability.playercooldown;

        playSound(player, SoundEvents.FLINTANDSTEEL_USE);
        if (a.containsKey("Flame_Charge1")) a.replace("Flame_Charge1", 0);
        a.put("Flame_Charge1", 15);
            player.getPersistentData().putInt("flame_field", 120);
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 2, (false), (false)));
        capability.playercooldown = a;
        capability.syncPlayerVariables(player);
        });
//        if (player instanceof ServerPlayer serverPlayer) {
//            SpellRegistry.FLAME_FIELD.get().attemptInitiateCast(ItemStack.EMPTY, SkillLevel, player.level(), serverPlayer, CastSource.COMMAND, false, "command");
//        }

        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 2, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 80, 1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 1, false, false, false));
        }
    }


