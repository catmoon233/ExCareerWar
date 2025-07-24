package net.exmo.excareerwar;

import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.item.CrossbowPlus;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
    public static void setup(final FMLClientSetupEvent event)
    {
        //ItemProperties
        event.enqueueWork(() -> {
            var item = CareerWarModItems.CrossbowPlus;
            Item p174571 = item.get();
            ItemProperties.register(p174571, new ResourceLocation("pull"), (ItemStack, ClientLevel, LivingEntity, p_174623_) -> {
                if (LivingEntity == null) {
                    return 0.0F;
                } else {
                    return CrossbowPlus.isCharged(ItemStack) ? 0.0F : (float)(ItemStack.getUseDuration() - LivingEntity.getUseItemRemainingTicks()) / (float)CrossbowPlus.getChargeDuration(ItemStack);
                }
            });
            ItemProperties.register(p174571, new ResourceLocation("pulling"), (ItemStack, ClientLevel, LivingEntity, p_174618_) -> {
                return LivingEntity != null && LivingEntity.isUsingItem() && LivingEntity.getUseItem() == ItemStack && !CrossbowPlus.isCharged(ItemStack) ? 1.0F : 0.0F;
            });
            ItemProperties.register(p174571, new ResourceLocation("charged"), (ItemStack, ClientLevel, LivingEntity, p_174613_) -> {
                return LivingEntity != null && CrossbowPlus.isCharged(ItemStack) ? 1.0F : 0.0F;
            });
            ItemProperties.register(p174571, new ResourceLocation("firework"), (ItemStack, ClientLevel, LivingEntity, p_174608_) -> {
                return LivingEntity != null && CrossbowPlus.isCharged(ItemStack) && CrossbowPlus.containsChargedProjectile(ItemStack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
            });
            ItemProperties.register(p174571, new ResourceLocation("end"), (ItemStack, ClientLevel, LivingEntity, p_174608_) -> {
                return LivingEntity != null && CrossbowPlus.isCharged(ItemStack) && CrossbowPlus.containsChargedProjectile(ItemStack, CareerWarModItems.EnderArrow.get()) ? 1.0F : 0.0F;
            });
            ItemProperties.register(p174571, new ResourceLocation("fen"), (ItemStack, ClientLevel, LivingEntity, p_174608_) -> {
                return LivingEntity != null && CrossbowPlus.isCharged(ItemStack) && CrossbowPlus.containsChargedProjectile(ItemStack, CareerWarModItems.FenrirArrow.get()) ? 1.0F : 0.0F;
            });
            ItemProperties.register(p174571, new ResourceLocation("mis"), (ItemStack, ClientLevel, LivingEntity, p_174608_) -> {
                return LivingEntity != null && CrossbowPlus.isCharged(ItemStack) && CrossbowPlus.containsChargedProjectile(ItemStack, CareerWarModItems.MistArrow.get()) ? 1.0F : 0.0F;
            });

        });
    }
}
