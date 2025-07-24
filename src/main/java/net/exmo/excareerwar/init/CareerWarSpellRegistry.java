package net.exmo.excareerwar.init;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.content.spell.FlameFieldSpell;
import net.exmo.excareerwar.content.spell.FlameSlashSpell;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


public class CareerWarSpellRegistry
{    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    public static final ResourceKey<Registry<AbstractSpell>> SPELL_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Excareerwar.MODID, "spells"));

        private static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, Excareerwar.MODID);
    public static final Supplier<IForgeRegistry<AbstractSpell>> REGISTRY = SPELLS.makeRegistry(() -> new RegistryBuilder<AbstractSpell>().disableSaving().disableOverrides());


        private static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }
public static final RegistryObject<AbstractSpell> FLAME_FIELD = registerSpell(new FlameFieldSpell());
public static final RegistryObject<AbstractSpell> FLAM_SLASH = registerSpell(new FlameSlashSpell());
}
