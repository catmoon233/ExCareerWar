package net.exmo.excareerwar.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Arrow.class)
public class CantGetTippedArrow
{
    @Shadow @Final private Set<MobEffectInstance> effects;

    @Shadow private Potion potion;

    @Inject(at = @At("HEAD"), method = "getPickupItem", cancellable = true)
    public void getPickupItem(CallbackInfoReturnable<ItemStack> cir)
    {
        if (!this.effects.isEmpty() || this.potion!=Potions.EMPTY){
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}
