package net.thenameislinus.tungstenmod.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.thenameislinus.tungstenmod.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {


    private static final Item[] HEAVY_ITEMS = {
            ModItems.TUNGSTEN_INGOT,
            ModItems.RAW_TUNGSTEN,
            ModItems.TUNGSTEN_BOOTS
    };

    private boolean isHeavy(ItemStack stack) {
        Item item = stack.getItem();
        for (Item heavy : HEAVY_ITEMS) {
            if (item == heavy) return true;
        }
        return false;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ItemEntity entity = (ItemEntity)(Object)this;
        ItemStack stack = entity.getStack();

        if (!isHeavy(stack)) return; {

            if (entity.isTouchingWater()) {
                entity.addVelocity(0, -0.01, 0);
            } else {
                entity.addVelocity(0, -0.035, 0);
            }

            Vec3d v = entity.getVelocity();
            double drag = entity.isTouchingWater() ? 0.90 : 0.96;
            entity.setVelocity(v.x * drag, v.y, v.z * drag);
        }
    }
}
