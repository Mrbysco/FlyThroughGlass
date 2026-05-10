package com.mrbysco.flythroughglass.mixin;

import com.mrbysco.flythroughglass.util.FlyHelper;
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

	@Inject(method = "travelFallFlying(Lnet/minecraft/world/phys/Vec3;)V", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
			shift = At.Shift.BEFORE, ordinal = 0))
	private void flythroughglass$preMoveElytra(Vec3 input, CallbackInfo ci) {
		flythroughglass$brokeGlass = false;
		if (!level().isClientSide()) {
			flythroughglass$preVelocity = getDeltaMovement();
			flythroughglass$brokeGlass = FlyHelper.breakGlassAhead((LivingEntity) (Object) this, flythroughglass$preVelocity);
		}
	}

	@Inject(method = "travelFallFlying(Lnet/minecraft/world/phys/Vec3;)V", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
			shift = At.Shift.BEFORE, ordinal = 0), cancellable = true)
	private void flythroughglass$postMoveElytra(Vec3 input, CallbackInfo ci) {
		if (flythroughglass$brokeGlass && !level().isClientSide()) {
			setDeltaMovement(flythroughglass$preVelocity.x, getDeltaMovement().y, flythroughglass$preVelocity.z);
			horizontalCollision = false;
			ci.cancel();
		}
	}

	@Inject(method = "travel", at = @At("TAIL"))
	private void flythroughglass$afterTravel(Vec3 travelVector, CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		if (FlyHelper.shouldBreakGlass(livingEntity)) {
			FlyHelper.breakGlassAhead(livingEntity, getLookAngle());
		}
	}
}