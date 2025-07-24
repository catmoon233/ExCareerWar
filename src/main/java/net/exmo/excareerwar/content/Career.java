
package net.exmo.excareerwar.content;

import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public abstract   class Career {
  public String LocalName ;
  public Item itemIcon;
  public List<MobEffectInstance> Effects = new ArrayList<>();
  public List<CareerSkill> Skills = new ArrayList<>();
  public boolean bingItems = false;
  public boolean IsAlwaysHas = false;
  public Career(String LocalName){
    super();
    this.LocalName = LocalName;
    CareerHandle.registerCareer(this);

  }
  public CareerSkill getSkill(String name){
    return SkillHandle.getSkill(name);
  }
  public void OnHas(String zhiye, Player player) {
    {
      if (!Objects.equals(zhiye, "null")) {
        Career career = CareerHandle.getCareer(zhiye);
        if (career != null) {

          AtomicReference<IItemHandler> _iitemhandlerref = new AtomicReference<>();
          player.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(_iitemhandlerref::set);
          if (_iitemhandlerref.get() != null) {
            for (int _idx = 0; _idx < _iitemhandlerref.get().getSlots(); _idx++) {
              ItemStack itemstackiterator = _iitemhandlerref.get().getStackInSlot(_idx).copy();
              if (!itemstackiterator.isEmpty()) {

                if (itemstackiterator.hasTag() && itemstackiterator.getTag().getBoolean("CareerItems")) {
                  player.getInventory().setItem(_idx, ItemStack.EMPTY);
                }



              }
            }
          }
        }
      }
    }
    player.removeAllEffects();
    if (!Effects.isEmpty()) {
      for (MobEffectInstance mobEffectInstance : Effects) {
        player.addEffect(mobEffectInstance);
      }
    }
    for (ItemKit itemKit : ItemKits) {
      if (itemKit.firstHas) {

        ItemStack itemStack = itemKit.itemStack.copy();
        itemStack.getOrCreateTag();
        if (bingItems) {


        }
        itemStack.getTag().putBoolean("Bind", true);
        itemStack.getTag().putBoolean("CareerItems", true);
        if (itemKit.equipmentSlot != null) {

          player.setItemSlot(itemKit.equipmentSlot, itemStack.copy());
        } else {
          player.getInventory().setItem(itemKit.slot, itemStack.copy());
        }
      }
    }
    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 10));

  }

  public String LocalDescription;
  public List<ItemKit> ItemKits = new ArrayList<>();
}
