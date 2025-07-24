
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.exmo.excareerwar.init;

import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowProjectile;
import net.exmo.excareerwar.Excareerwar;

import net.exmo.excareerwar.entity.FenrirArrowEntity;
import net.exmo.excareerwar.entity.MistArrowEntity;
import net.exmo.excareerwar.item.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.UUID;

@Mod.EventBusSubscriber
public class CareerWarModItems {
	static final AttributeModifier attrModifier = new AttributeModifier( UUID.fromString("05d0f7f4-c5c5-4c5c-b5b5-5c5c5c5c5c5c"),"Mouse_Chui",1, AttributeModifier.Operation.ADDITION);
	static final AttributeModifier attrModifier1 = new AttributeModifier( UUID.fromString("05d0f7f4-c5c5-4c5c-b5b5-5c5c5c2c5c5c"),"Mouse_Chui",-4, AttributeModifier.Operation.ADDITION);
	@SubscribeEvent
	public static void AttrModifier(ItemAttributeModifierEvent event){
		if (event.getSlotType() != EquipmentSlot.MAINHAND)return;
		if (event.getItemStack().getItem() == Mouse_Chui.get()){
			event.addModifier(ForgeMod.ENTITY_REACH.get(),(attrModifier));
			event.addModifier(Attributes.ATTACK_SPEED,(attrModifier1));
		}
	}
	public static class EnderArrow extends ArrowItem {
		public EnderArrow(Properties p_40512_) {
			super(p_40512_);
		}
		@Override
		public void inventoryTick(ItemStack stack, Level p_41405_, Entity p_41406_, int slot, boolean p_41408_) {
			super.inventoryTick(stack, p_41405_, p_41406_, slot, p_41408_);
			if (slot == 8 || slot == 7 || slot == 6){

			}else {
				stack.shrink(1);
			}

		}

		public Projectile createArrow_(Level level, ItemStack stack, LivingEntity entity) {
			MagicArrowProjectile magicArrow = new MagicArrowProjectile(level, entity);
			magicArrow.setPos(entity.position().add(0, entity.getEyeHeight() - magicArrow.getBoundingBox().getYsize() * .5f, 0).add(entity.getForward()));
			magicArrow.shoot(entity.getLookAngle());
			magicArrow.setDamage(6.5Ff);
			magicArrow.getPersistentData().putBoolean("Plus", true);
			return magicArrow;
		}
	}
	public static class FenrirArrow extends ArrowItem {
		public FenrirArrow(Properties p_40512_) {
			super(p_40512_);
		}


		@Override
		public void inventoryTick(ItemStack stack, Level p_41405_, Entity p_41406_, int slot, boolean p_41408_) {
			super.inventoryTick(stack, p_41405_, p_41406_, slot, p_41408_);
			if (slot == 8 || slot == 7 || slot == 6){

			}else {
				stack.shrink(1);
			}

		}

		public Projectile createArrow_(Level level, ItemStack stack, LivingEntity player) {
			Vec3 direction = player.getLookAngle();
			FenrirArrowEntity arrow = new FenrirArrowEntity(player.level(), player);
			arrow.setPos(player.getX() + direction.x * 2, player.getY() + 1.5, player.getZ() + direction.z * 2);
			arrow.shoot(direction.x, direction.y, direction.z, 5f, 0);
			arrow.setPierceLevel((byte) 2);
			arrow.setBaseDamage(5.0f); // 基础伤害


			return arrow;
		}
	}
	public static class MistArrow extends ArrowItem {
        public MistArrow(Properties p_40512_) {
            super(p_40512_);
        }

        @Override
        public void inventoryTick(ItemStack stack, Level p_41405_, Entity p_41406_, int slot, boolean p_41408_) {
            super.inventoryTick(stack, p_41405_, p_41406_, slot, p_41408_);
            if (slot == 8 || slot == 7 || slot == 6) {
                
            } else {
                stack.shrink(1);
            }
        }

        public Projectile createArrow_(Level level, ItemStack stack, LivingEntity player) {
            Vec3 direction = player.getLookAngle();
            MistArrowEntity arrow = new MistArrowEntity(player.level(), player);
            arrow.setPos(player.getX() + direction.x * 2, player.getY() + 1.5, player.getZ() + direction.z * 2);
            arrow.shoot(direction.x, direction.y, direction.z, 5f, 0);
            return arrow;
        }
    }
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Excareerwar.MODID);
	public static final RegistryObject<Item> VERTICAL_STRING_A = REGISTRY.register("vertical_string_a", () -> new VerticalStringAItem());
	public static final RegistryObject<Item> WOOD_STAFF = REGISTRY.register("wood_staff", () -> new WoodStaffItem());
	public static final RegistryObject<Item> TIAN_JI = REGISTRY.register("tianji", () -> new TianJi(Tiers.GOLD, 10,1, new Item.Properties().rarity(Rarity.EPIC).defaultDurability(1000)));
	public static final RegistryObject<Item> EnderArrow = REGISTRY.register("ender_arrow", () -> new EnderArrow(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> FenrirArrow = REGISTRY.register("fenrir_arrow", () -> new FenrirArrow(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> MistArrow =
        REGISTRY.register("mist_arrow", () -> new MistArrow(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> MAGIC_BOOK = REGISTRY.register("magic_book", MagicBookItem::new);
	public static final RegistryObject<Item> Mouse_Chui = REGISTRY.register("mouse_chui", () -> new SwordItem(Tiers.WOOD, 1, 4, new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> CrossbowPlus = REGISTRY.register("crossbow_plus", net.exmo.excareerwar.item.CrossbowPlus::new);
	// Start of user code block custom items
	// End of user code block custom items
}
