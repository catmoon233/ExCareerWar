package net.exmo.excareerwar.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.signature.qual.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryMenu.class)
public class CantMoveBindItemMixin {
    @Inject(at = @At("HEAD"),method = "quickMoveStack",cancellable = true)
    public void quickMoveStack(Player p_39723_, int p_39724_, CallbackInfoReturnable<ItemStack> cir)
    {
        InventoryMenu inventoryMenu = (InventoryMenu) (Object) this;
        ItemStack itemStack = inventoryMenu.slots.get(p_39724_).getItem();
        if (itemStack.getTag()!=null && itemStack.getTag().contains("Bind")){
            cir.cancel();
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }




    @Inject(at = @At("HEAD"),method = "shouldMoveToInventory",cancellable = true)
    public void shouldMoveToInventory( int p_39724_, CallbackInfoReturnable<Boolean> cir)
    {
        InventoryMenu inventoryMenu = (InventoryMenu) (Object) this;
        ItemStack itemStack = inventoryMenu.slots.get(p_39724_).getItem();
        if (itemStack.getTag()!=null && itemStack.getTag().contains("Bind")){
            cir.cancel();
            cir.setReturnValue(false);
        }
    }

}
