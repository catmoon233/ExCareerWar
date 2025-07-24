package net.exmo.excareerwar.content.spell;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireField;
import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.List;

@Mod.EventBusSubscriber
@AutoSpellConfig
public class FlameFieldSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(IronsSpellbooks.MODID, "flame_field");

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SchoolRegistry.FIRE_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(10)
            .build();

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }


    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

//            entity.getPersistentData().putInt("flame_field1", (int) (40*(Math.pow(1.25,spellLevel))));
                    if (!level.isClientSide) {
            FireField fire = new FireField(level);
            fire.setOwner(entity);
            fire.setDuration((int) (100*(Math.pow(1.25,spellLevel))));
            fire.setDamage(0.75f*spellLevel);
            fire.setRadius(spellLevel*3);

            fire.setCircular();
            fire.moveTo(entity.position());
            level.addFreshEntity(fire);
                        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
        }


    }
    @SubscribeEvent
    public static void effect(TickEvent.PlayerTickEvent event){
    LivingEntity player = event.player;
        LazyOptional<CareerWarModVariables.PlayerVariables> capability1 = player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null);
        if (capability1==null)return;
        capability1.ifPresent(capability -> {
            if (capability==null)return;
            if (capability.career==null)return;
                if (capability.career.equals("fire_lord")) {
						{
							final Vec3 _center = new Vec3(player.getX(), player.getY(), player.getZ());
							List<Entity> _entfound = player.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
							for (Entity entityiterator : _entfound) {
								if (entityiterator != player) {
									if (entityiterator instanceof FireField) {
									    if (((FireField) entityiterator).getOwner() == player)
                                        {
                                            entityiterator.setPos(player.position());
                                        }
									}
								}
							}
						}
                }
            });
            }
}
