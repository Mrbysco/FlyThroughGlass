package com.mrbysco.flythroughglass;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

public class FlyHelper {

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
					if (state.is(Tags.Blocks.GLASS) || state.is(Tags.Blocks.GLASS_PANES)) {
						entity.level().destroyBlock(pos, false, entity);
						broke = true;
					}
				}
			}
		}
		return broke;
	}
}