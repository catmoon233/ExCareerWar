
package net.exmo.excareerwar.item;

import net.exmo.excareerwar.content.careers.Elementalist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class MagicBookItem extends Item {
	public MagicBookItem() {
		super(new Properties().stacksTo(1).rarity(Rarity.RARE));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		Player player = (Player) entity;
		Level _level = (Level) world;
		if (!_level.isClientSide()) {
			_level.playSound(null, player.blockPosition(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.enchantment_table.use")), SoundSource.NEUTRAL, 1, 1);
		} else {
			_level.playLocalSound(player.blockPosition(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.enchantment_table.use")), SoundSource.NEUTRAL, 1, 1, false);
		}
		ItemStack object = ar.getObject();
		switch (object.getOrCreateTag().getString("MagicName")){
			case "LightingHit":
				Elementalist.LightingHit(player);
				break;
			case "iceBall":
				Elementalist.iceBall(player);
				break;
			case "FireBall":
				Elementalist.FireBall(player);
				break;
			case  "NatureShield":
				Elementalist.NatureShield(player);
				break;
			case "FireField":
				Elementalist.FireField(player);
				break;
			case "steam":
				Elementalist.steam(player);
				break;
			case "tornado":
				Elementalist.tornado(player);
				break;
			case "earthquake":
				Elementalist.earthquake(player);
				break;
			case "teleporting":
				Elementalist.teleporting(player);
				break;
			case "Black_Hold":
				Elementalist.BlackHole(player);
				break;
		}
		object.shrink(1);
		return ar;
	}
}
