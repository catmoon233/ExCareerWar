package net.exmo.excareerwar.mixin;

import net.exmo.excareerwar.content.events.SwingEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class SwingMixin extends Entity implements Attackable, IForgeLivingEntity {

    public SwingMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "swing*", at = @At("HEAD"))
    public void swinga(InteractionHand hand, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new SwingEvent((LivingEntity)(Object)this));
    }

}
