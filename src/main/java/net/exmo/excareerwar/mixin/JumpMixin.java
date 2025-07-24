package net.exmo.excareerwar.mixin;

import net.exmo.excareerwar.init.CareerWarModAttributes;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class JumpMixin extends Entity implements Attackable, IForgeLivingEntity {
    @Shadow public abstract AttributeMap getAttributes();

    public JumpMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(at = @At("HEAD"),method = "getJumpPower",cancellable = true)
    protected void getJumpPower(CallbackInfoReturnable<Float> cir) {
        if (this.getAttributes().hasAttribute(CareerWarModAttributes.JUMPHIGHT.get())) cir.setReturnValue((float) ((0.42F * this.getBlockJumpFactor() + ((LivingEntity)(Object)this).getJumpBoostPower())*this.getAttributes().getValue(CareerWarModAttributes.JUMPHIGHT.get())));
    }

}
