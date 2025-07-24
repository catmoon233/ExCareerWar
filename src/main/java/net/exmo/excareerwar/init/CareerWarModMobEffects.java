
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.exmo.excareerwar.init;


import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.potion.FastCastEMobEffect;
import net.exmo.excareerwar.potion.SpellBurstEMobEffect;
import net.exmo.excareerwar.potion.SpellDamageBoostEMobEffect;
import net.exmo.excareerwar.potion.VulnerabilityMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CareerWarModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Excareerwar.MODID);
	public static final RegistryObject<MobEffect> VULNERABILITY = REGISTRY.register("vulnerability", VulnerabilityMobEffect::new);
	public static final RegistryObject<MobEffect> SPELL_BURST_E = REGISTRY.register("spell_burst_effect", SpellBurstEMobEffect::new);
	public static final RegistryObject<MobEffect> SPELL_DAMAGE_BOOST_EFFECT = REGISTRY.register("spell_damage_boost_effect", SpellDamageBoostEMobEffect::new);
	public static final RegistryObject<MobEffect> FastCastEMobEffect = REGISTRY.register("fast_cast_effect", net.exmo.excareerwar.potion.FastCastEMobEffect::new);
	public static final RegistryObject<MobEffect> HeavenlyEye = REGISTRY.register("heavenly_eye", net.exmo.excareerwar.potion.HeavenlyEye::new);
	public static final RegistryObject<MobEffect> ImprisonMobEffect = REGISTRY.register("imprison_effect", net.exmo.excareerwar.potion.ImprisonMobEffect::new);
	public static final RegistryObject<MobEffect> ReallyDamageEffect = REGISTRY.register("really_damage_effect", net.exmo.excareerwar.potion.ReallyDamageEffect::new);
	public static final RegistryObject<MobEffect> CAN_CHANGE_CAREER_EFFECT = REGISTRY.register("can_change_career_effect", net.exmo.excareerwar.potion.CanChangeCareerMobEffect::new);
}
