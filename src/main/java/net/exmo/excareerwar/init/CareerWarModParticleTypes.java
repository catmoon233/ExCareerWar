
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.exmo.excareerwar.init;

import net.exmo.excareerwar.Excareerwar;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CareerWarModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Excareerwar.MODID);
	public static final RegistryObject<SimpleParticleType> LINES = REGISTRY.register("lines", () -> new SimpleParticleType(false));
}
