package net.thenameislinus.tungstenmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.thenameislinus.tungstenmod.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(
            method = "onLandedUpon",
            at = @At("HEAD")
    )
    private void tungstenBootsStomp(
            World world,
            Entity entity,
            float fallDistance,
            CallbackInfo ci
    ) {
        // entity = the thing that landed
        Entity self = (Entity) (Object) this;

        // only living things matter
        if (!(entity instanceof LivingEntity attacker)) return;
        if (!(self instanceof LivingEntity target)) return;

        // check boots
        ItemStack boots = attacker.getEquippedStack(EquipmentSlot.FEET);
        if (!boots.isOf(ModItems.TUNGSTEN_BOOTS)) return;

        // ignore tiny falls
        if (fallDistance < 3.0f) return;

        // calculate damage (balanced, not braindead)
        float damage = Math.min(fallDistance * 1.5f, 20.0f);

        target.damage(
                world.getDamageSources().fall(),
                damage
        );
    }
}
