
package net.exmo.excareerwar.content.careers;

import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@AutoInit
@Mod.EventBusSubscriber
public class WindRanger extends Career {
	public WindRanger() throws Exception {
		super("Wind_Ranger");
		this.Skills.add(SkillHandle.getSkill("wind_run"));
		this.Skills.add(SkillHandle.getSkill("arrow_lead"));
		this.ItemKits.add(new ItemKit(0, "minecraft:bow{FX:true,Unbreakable:true,Enchantments:[{id:\"power\",lvl:1s},{id:\"knockback\",lvl:1s},{id:\"knockback\",lvl:2s}],AttributeModifiers:[{AttributeName:\"generic.attack_damage\",Name:\"generic.attack_damage\",Slot:\"mainhand\",Operation:0,Amount:2d,UUID:[I;-578286535,1016743318,-1156934988,-576272163]},{AttributeName:\"generic.knockback_resistance\",Name:\"generic.knockback_resistance\",Slot:\"mainhand\",Operation:0,Amount:2.5d,UUID:[I;1534268828,1514359241,-2030784481,-1928449843]}],display:{Name:'{\"text\":\"\\\\u00a7a\\\\u00a7l风行\"}'}}",1));
		this.ItemKits.add(new ItemKit(1, "minecraft:arrow{}",1));
		this.ItemKits.add(new ItemKit(EquipmentSlot.HEAD, "minecraft:leather_helmet{Unbreakable:true,Enchantments:[{id:\"blast_protection\",lvl:1s},{id:\"fire_protection\",lvl:1s},{id:\"binding_curse\",lvl:1s},{id:\"depth_strider\",lvl:3s},{id:\"feather_falling\",lvl:5s},{id:\"protection\",lvl:1s},{id:\"projectile_protection\",lvl:3s}],AttributeModifiers:[{AttributeName:\"generic.movement_speed\",Name:\"generic.movement_speed\",Slot:\"head\",Operation:1,Amount:0.2d,UUID:[I;2032902480,-213824230,-1679967255,-977241219]},{AttributeName:\"generic.knockback_resistance\",Name:\"generic.knockback_resistance\",Slot:\"head\",Operation:0,Amount:10.0d,UUID:[I;-175730613,-1799009612,-1666383447,-806773003]},{AttributeName:\"generic.max_health\",Name:\"generic.max_health\",Slot:\"head\",Operation:0,Amount:-1.0d,UUID:[I;1357404337,1220299345,-1241379974,1274085135]},{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"head\",Operation:0,Amount:3.0d,UUID:[I;-509474114,-944028974,-1674738383,-270132766]},{AttributeName:\"generic.attack_speed\",Name:\"generic.attack_speed\",Slot:\"head\",Operation:1,Amount:-0.4d,UUID:[I;-1104091537,-1289860570,-1547355458,1118852393]}],display:{color:65298,Name:'{\"text\":\"\\\\u00a7l\\\\u00a7a精灵绿帽\"}'}}",1));
		this.IsAlwaysHas = true;
		this.bingItems = true;
		this.itemIcon = Items.FEATHER;
		this.LocalDescription = "Wind_Ranger_d";


	}
	@SubscribeEvent
	public static void atHurt(LivingHurtEvent event) {
		if (!(event.getSource().getEntity() instanceof Player player)) return;
		if (player.getMainHandItem().isEmpty()) return;
		if (!player.getMainHandItem().hasTag()) return;
		if (!player.getMainHandItem().getTag().contains("FX")) return;
		if ((player.getMainHandItem().getTag()).getBoolean("FX")) {
			if (event.getSource().getDirectEntity() instanceof Arrow) {
				event.getEntity().invulnerableTime = 0;
				{
					Entity _ent = player;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "clear @s arrow");
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "give @s minecraft:arrow{Bind:true} 1");

					}
				}


				//((Entity)player).setDeltaMovement(new Vec3(0,1,0));
				Excareerwar.queueServerWork(8, () -> {

					{
						SkillHandle.sendDashMessage2(player, 2);
						player.getPersistentData().putInt("careerwar:fx", 5);
						player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							Map<String, Integer> s = capability.playercooldown;
							s.entrySet().removeIf(entry -> entry.getKey().equals("fx"));
							s.put("fx", 1);
							capability.playercooldown = s;
							capability.syncPlayerVariables(player);
						});
					}
				});
			}




			player.heal(1);

		}

	}
}
