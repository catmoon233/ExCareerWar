package net.exmo.excareerwar.mixin;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.exmo.excareerwar.entity.WolfProjectile;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Nullable public LocalPlayer player;

    /**
     * Necessary make entities appear glowing on our client while we have the echolocation effect
     */
    @Inject(method = "shouldEntityAppearGlowing", at = @At(value = "HEAD"), cancellable = true)
    public void changeGlowOutline(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {

        if (pEntity instanceof ArmorStand) cir.setReturnValue(false);
        LocalPlayer player = Minecraft.getInstance().player;
        if (pEntity  instanceof Player player1) {
            if (player1.getTeam()!=null && player1.getTeam().isAlliedTo(player1.getTeam())){
                cir.setReturnValue(true);
            }
        }
        if (pEntity instanceof WolfProjectile wolfProjectile){
            if (wolfProjectile.getOwner() == player){
                cir.setReturnValue(true);
            }
        }
        if (player != null && player.hasEffect(CareerWarModMobEffects.HeavenlyEye.get()) && pEntity instanceof LivingEntity) {
            LazyOptional<CareerWarModVariables.PlayerVariables> capability = player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null);
            String heavenlyEyeUUID = capability.orElseGet(null).HeavenlyEyeUUID;
            if (heavenlyEyeUUID.length()<30)return;
            if (pEntity.position().distanceTo(player.position())<20) {
                if (pEntity.getUUID().toString().equals(heavenlyEyeUUID) && Mth.abs((float) (pEntity.getY() - player.getY())) < 21) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
