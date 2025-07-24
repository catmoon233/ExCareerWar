package net.exmo.excareerwar.mixin;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerMenu.class)
public class ContainerMixin {



    @Inject(at = @At("HEAD"), method = "moveItemStackTo", cancellable = true)
    private void moveItemStackTo(ItemStack p_38904_, int p_38905_, int p_38906_, boolean p_38907_, CallbackInfoReturnable<Boolean> cir){
        CompoundTag tag = p_38904_.getTag();
        if (tag != null && !tag.isEmpty()) {
            if (tag.contains("Bind") && tag.getBoolean("Bind")){
                cir.cancel();
            }
        }
    }
}
