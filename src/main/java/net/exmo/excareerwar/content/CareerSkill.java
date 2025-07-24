
package net.exmo.excareerwar.content;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.network.UseSkillMessage;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;
import org.openjdk.nashorn.internal.ir.CallNode;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public  class CareerSkill {
	public static final int sendCondTick = 40;
	public CareerSkill(String Name){
		super();
		this.LocalName = Name;
		this.LocalDescription =getLocalName()+"_d";
		SkillHandle.RegisterSkill(this);

	}
	public static void playSound(Player player, SoundEvent soundEvent){
		if (player.level() instanceof ServerLevel serverLevel){
			serverLevel.players().forEach(e->{
				serverLevel.playSeededSound(player, e.getX(), e.getY(), e.getZ(), soundEvent, e.getSoundSource(), 1.0F, 1.0F, 0L);
			});
		}
	}
	public static boolean isSameTeam(Entity entity1, Entity entity2){
		if (entity1==null)return false;
		if (entity2==null)return false;
		if (entity1 instanceof IronGolem)return true;
		if (entity1 instanceof ArmorStand)return true;

		if (entity1.getTeam()!=null && entity2.getTeam()!=null) {
            return entity1.getTeam() == entity2.getTeam();
        }
		return false;
	}
	public static List<Entity> getEntities(Level level, Vec3 pos, int radius,Entity entity){
		return level.getEntities(entity, AABB.ofSize(pos, radius * 2, radius, radius * 2));
	}
	public String getLocalName(){
		return this.LocalName;
	}
	public DamageSource getDamageSoure(Player player, ResourceKey<DamageType> type){
		return  new DamageSource(player.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(type), player);
	}
	public Runnable getDamageSoureNOI(Player player, ResourceKey<DamageType> type, Consumer<DamageSource> consumer){
		Holder.Reference<DamageType> holderOrThrow = player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type);

		// 保存原始标签
		List<TagKey<DamageType>> originalTags = holderOrThrow.tags().toList();

		// 添加新的标签
		holderOrThrow.bindTags(List.of(
				DamageTypeTags.BYPASSES_COOLDOWN,
				DamageTypeTags.BYPASSES_INVULNERABILITY
		));
		Objects.requireNonNull(consumer);
		consumer.accept(new DamageSource(holderOrThrow, player));
	return () -> holderOrThrow.bindTags(originalTags);

	}

	public float getAttackDamage(Player player){
		return   (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
	}
	public CareerSkill(AbstractSpell spell,int level){
		 super();
		 this.LocalName = spell.getSpellName();
		 this.IconTexture =  new ResourceLocation(spell.getSpellResource().getNamespace(), "textures/gui/spell_icons/" + spell.getSpellName() + ".png");
		 this.CoolDown =(CareerHandle.random.nextInt(10)+1 )*30;
		 this.SkillLevel = level;
		 this.spell = spell;
		 this.LocalDescription = spell.getSpellName()+"_d";

		 SkillHandle.RegisterSkill(this);
	}
 	 public String LocalName;
	 public AbstractSpell spell;
	 public int firstCooldown = 0;
	 public int SkillLevel;
	 public Item Icon;
	 public boolean isSelfCooldown;
	 public ResourceLocation IconTexture = null;
	 public boolean isPassive = false;
	 public String LocalDescription;
	 public int CoolDown;
	 public boolean setCoolDown(Player player){
		 MinecraftForge.EVENT_BUS.post(new UseSkillEvent(player,this.LocalName,this.CoolDown));
		 CareerWarModVariables.PlayerVariables v = player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new CareerWarModVariables.PlayerVariables());
		 if (v.playercooldown.containsKey(this.LocalName) && Integer.valueOf(v.playercooldown.get(this.LocalName)) !=0) return false;
		 if (player.level().isClientSide)return true ;

		 player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

			 UseSkillMessage message = new UseSkillMessage(getLocalName());
			 Excareerwar.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
			 Map<String, Integer> map = capability.playercooldown;
			 if (map.containsKey(this.LocalName))map.remove(this.LocalName);
			 map.put(this.LocalName, (this.CoolDown));
			 capability.playercooldown = map;
			 capability.syncPlayerVariables(player);
		 });
		return true;
	 }
	 public boolean isInCooldown(Player player){
		 if (isPassive)return false;
		 CareerWarModVariables.PlayerVariables v = player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new CareerWarModVariables.PlayerVariables());
         return v.playercooldown.containsKey(this.LocalName) && Integer.valueOf(v.playercooldown.get(this.LocalName)) != 0;
     }
	 public void castIronsSpell(Player player ,AbstractSpell spell1,int skillLevel1){
		 if (player instanceof ServerPlayer serverPlayer) {
			 spell1.attemptInitiateCast(ItemStack.EMPTY, skillLevel1, player.level(), serverPlayer, CastSource.COMMAND, false, "command");
		 }
	 }
	 public void use(Player player){
		 if (!setCoolDown(player))return;
		 if (this.spell!=null){
			castIronsSpell(player,this.spell,this.SkillLevel);

		 }

	 }
}
