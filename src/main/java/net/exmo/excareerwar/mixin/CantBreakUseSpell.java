package net.exmo.excareerwar.mixin;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.exmo.excareerwar.content.careers.Garen;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSpell.class)
public abstract class CantBreakUseSpell {
    @Inject(method = "canBeInterrupted", at = @At("HEAD"), cancellable = true,remap = false)
    public void canBeInterrupted$ex(Player player, CallbackInfoReturnable<Boolean> cir){
        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            String career = capability.career;
            if (career.equals("ThunderCloud") || career.equals("Wind_Ranger") || career.equals("Garen") || career.equals("ErlangShen")) {
                cir.setReturnValue(false);
                return;
            }
            });
    }
}
