package net.exmo.excareerwar.content;


import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class UseMagic {
	public static class Ys{
		public int fire;
		public int water;
		public int wood;
	}
	public static boolean isok(int fire, int water, int wood,Ys ys){
		return fire == ys.fire && water == ys.water && wood == ys.wood;
	}
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player player){
		if (player.getInventory().countItem(CareerWarModItems.MAGIC_BOOK.get()) >=3)return;
			player.swing(player.getUsedItemHand());
			player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				Ys ys = new Ys();
					List<String> stringList = List.of(capability.magicGether.split(","));
					if (!stringList.isEmpty()) {
						StringBuilder dis = new StringBuilder();
						for (String s : stringList) {
							if (s.equals("fire"))ys.fire++;
							if (s.equals("water"))ys.water++;
							if (s.equals("wood"))ys.wood++;
						}
					}
				boolean bs = false;
					String MagicName = "Unknown";
				if (isok(2,0,1,ys)){
					MagicName = "FireField";
					bs=true;
				}
				if (isok(1,0,2,ys)){
					MagicName = "tornado";
					bs=true;
				}
				if (isok(2,1,0,ys)){
					MagicName = "steam";
					bs=true;
				}
				if (isok(1,2,0,ys)){
					MagicName = "LightingHit";
					bs=true;
				}
				if (isok(1,1,1,ys)){
					MagicName = "NatureShield";
					bs=true;
				}
					if (isok(3,0,0,ys)){
						MagicName = "FireBall";
						bs=true;
					}

				if (isok(0,3,0,ys)){
					MagicName = "iceBall";
					bs=true;

				}
				if (isok(0,2,1,ys)){
					MagicName = "earthquake";
					bs=true;

				}
				if (isok(0,0,3,ys)){
					MagicName = "teleporting";
					bs=true;

				}
				if (isok(0,1,2,ys)){
					MagicName = "Black_Hold";
					bs=true;

				}


				if (bs) {
					Level _level = (Level) world;
					if (!_level.isClientSide()) {
						_level.playSound(null, player.blockPosition(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.enchantment_table.use")), SoundSource.NEUTRAL, 1, 1);
					} else {
						_level.playLocalSound(player.blockPosition(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.enchantment_table.use")), SoundSource.NEUTRAL, 1, 1, false);
					}
					ItemStack i = CareerWarModItems.MAGIC_BOOK.get().getDefaultInstance();
					i.getOrCreateTag().putString("MagicName", MagicName);
					i.getOrCreateTag().putBoolean("Bind", true);
					i.setHoverName(Component.translatable(MagicName));
					player.getInventory().add(i);
					capability.magicGether = "";
					capability.syncPlayerVariables(player);
				}
				});
		}
	}
}
