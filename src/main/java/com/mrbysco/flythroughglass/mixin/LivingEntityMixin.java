package com.mrbysco.flythroughglass.mixin;

import com.mrbysco.flythroughglass.FlyHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Unique
	private Vec3 flythroughglass$preVelocity = Vec3.ZERO;
	@Unique
	private boolean flythroughglass$brokeGlass = false;

	@Inject(method = "travel", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
			shift = At.Shift.BEFORE, ordinal = 2))
	private void flythroughglass$preMoveElytra(Vec3 travelVector, CallbackInfo ci) {
		flythroughglass$brokeGlass = false;
		if (!level().isClientSide) {
			flythroughglass$preVelocity = getDeltaMovement();
			flythroughglass$brokeGlass = FlyHelper.breakGlassAhead((LivingEntity) (Object) this, flythroughglass$preVelocity);
		}
	}

	@Inject(method = "travel", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
			shift = At.Shift.AFTER, ordinal = 2), cancellable = true)
	private void flythroughglass$postMoveElytra(Vec3 travelVector, CallbackInfo ci) {
		if (flythroughglass$brokeGlass && !level().isClientSide) {
			setDeltaMovement(flythroughglass$preVelocity.x, getDeltaMovement().y, flythroughglass$preVelocity.z);
			horizontalCollision = false;
			ci.cancel();
		}
	}
}