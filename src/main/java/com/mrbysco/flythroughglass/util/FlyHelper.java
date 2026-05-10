package com.mrbysco.flythroughglass.util;

import com.mrbysco.flythroughglass.FlyThroughGlassMod;
import com.mrbysco.flythroughglass.config.config.FlyConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FlyHelper {
	public static final TagKey<Block> BLOCKS_TO_BREAK = BlockTags.create(new ResourceLocation(FlyThroughGlassMod.MOD_ID, "blocks_to_break"));

	/**
	 * Checks for glass blocks in the path of the entity's movement and breaks them if found.
	 * The method calculates the bounding box of the entity and extends it in the direction of movement to check for glass blocks.
	 * If a glass block is found, it is destroyed without dropping items, and the method returns true if any glass was broken.
	 */
	public static boolean breakGlassAhead(Entity entity, Vec3 velocity) {
		AABB aabb = entity.getBoundingBox();
		boolean broke = false;

		double minX = velocity.x < 0 ? aabb.minX + velocity.x - 0.1 : aabb.minX;
		double maxX = velocity.x > 0 ? aabb.maxX + velocity.x + 0.1 : aabb.maxX;
		double minZ = velocity.z < 0 ? aabb.minZ + velocity.z - 0.1 : aabb.minZ;
		double maxZ = velocity.z > 0 ? aabb.maxZ + velocity.z + 0.1 : aabb.maxZ;

		for (int x = (int) Math.floor(minX); x <= (int) Math.floor(maxX); x++) {
			for (int y = (int) Math.floor(aabb.minY); y <= (int) Math.floor(aabb.maxY); y++) {
				for (int z = (int) Math.floor(minZ); z <= (int) Math.floor(maxZ); z++) {
					BlockPos pos = new BlockPos(x, y, z);
					BlockState state = entity.level().getBlockState(pos);
					if (state.is(BLOCKS_TO_BREAK)) {
						entity.level().destroyBlock(pos, false, entity);
						broke = true;
					}
				}
			}
		}
		return broke;
	}

	/**
	 * Checks if running should break glass
	 *
	 * @param entity The entity that is running
	 * @return If the entity should break glass upon running into glass
	 */
	public static boolean shouldBreakGlass(LivingEntity entity) {
		if (!FlyConfig.COMMON.runThroughBlocks.get() || !(entity instanceof Player)) return false;
		if (!entity.level().isClientSide && !entity.isShiftKeyDown()) {
			double moveSpeed = entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
			boolean fastEnough = moveSpeed >= FlyConfig.COMMON.runMinSpeed.get();

			if (fastEnough) {
				Vec3 lookAhead = entity.getLookAngle().scale(1.0);
				FlyHelper.breakGlassAhead(entity, lookAhead);
				return true;
			}
		}
		return false;
	}
}