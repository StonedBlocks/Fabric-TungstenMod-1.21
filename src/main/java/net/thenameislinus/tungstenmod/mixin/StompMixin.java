package net.thenameislinus.tungstenmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.thenameislinus.tungstenmod.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class StompMixin {

    @Unique private boolean tungstenHasStomped = false;
    @Unique private int tungstenFreezeTicks = 0;

    /* ---------------- MID-AIR IMPACT ---------------- */

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void tungstenMidAirStomp(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        if (player.getWorld().isClient) return;
        if (player.isOnGround()) return;
        if (tungstenHasStomped) return;

        ItemStack boots = player.getEquippedStack(EquipmentSlot.FEET);
        if (!boots.isOf(ModItems.TUNGSTEN_BOOTS)) return;

        if (player.getVelocity().y > -0.25) return; // must be falling

        Box hitBox = player.getBoundingBox()
                .offset(0.0, -0.6, 0.0)
                .expand(0.4, 0.2, 0.4);

        List<Entity> targets = player.getWorld().getOtherEntities(
                player,
                hitBox,
                e -> e instanceof LivingEntity && e != player
        );

        if (targets.isEmpty()) return;

        float velocity = (float)Math.abs(player.getVelocity().y);
        float damage = velocity * 8.0f;

        for (Entity e : targets) {
            e.damage(
                    player.getDamageSources().playerAttack(player),
                    damage
            );
        }

        // sound (mace)
        player.getWorld().playSound(
                null,
                player.getBlockPos(),
                SoundEvents.ITEM_MACE_SMASH_GROUND,
                SoundCategory.PLAYERS,
                1.0f,
                1.0f
        );

        // PARTICLES (real mace-style block dust shooting UP)
        ServerWorld world = (ServerWorld) player.getWorld();
        world.spawnParticles(
                new BlockStateParticleEffect(
                        ParticleTypes.BLOCK,
                        world.getBlockState(player.getBlockPos().down())
                ),
                player.getX(),
                player.getY() - 0.2,
                player.getZ(),
                35,            // count
                0.4, 0.1, 0.4, // spread
                0.6            // upward speed (this is the key)
        );

        // freeze (impact pause)
        player.setNoGravity(true);
        player.setVelocity(0.0, 0.0, 0.0);
        player.velocityModified = true;

        tungstenFreezeTicks = 3;
        tungstenHasStomped = true;

        // durability loss
        boots.damage(
                1 + (int)(velocity * 2),
                player,
                EquipmentSlot.FEET
        );
    }

    /* ---------------- FREEZE TIMER ---------------- */

    @Inject(method = "tick", at = @At("HEAD"))
    private void tungstenFreezeTick(CallbackInfo ci) {
        if (tungstenFreezeTicks > 0) {
            PlayerEntity player = (PlayerEntity)(Object)this;

            player.setVelocity(0.0, 0.0, 0.0);
            player.velocityModified = true;

            tungstenFreezeTicks--;

            if (tungstenFreezeTicks == 0) {
                player.setNoGravity(false);
            }
        }
    }

    /* ---------------- FALL DAMAGE CANCEL ---------------- */

    @Inject(
            method = "handleFallDamage(FFLnet/minecraft/entity/damage/DamageSource;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tungstenCancelFallDamage(
            float fallDistance,
            float damageMultiplier,
            DamageSource source,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (tungstenHasStomped) {
            tungstenHasStomped = false;
            cir.setReturnValue(false);
        }
    }
}
