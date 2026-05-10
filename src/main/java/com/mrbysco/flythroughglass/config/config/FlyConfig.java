package com.mrbysco.flythroughglass.config.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class FlyConfig {
	public static class Common {
		public final ForgeConfigSpec.BooleanValue runThroughBlocks;
		public final ForgeConfigSpec.DoubleValue runMinSpeed;

		Common(ForgeConfigSpec.Builder builder) {
			//General settings
			builder.comment("General settings")
					.push("general");

			runThroughBlocks = builder
					.comment("When enabled allows running through blocks [Default: false]")
					.define("runThroughBlocks", false);

			runMinSpeed = builder
					.comment("The minimum speed a player must have to run through blocks [Default: 0.18]")
					.defineInRange("runMinSpeed", 0.18, 0, 1024);

			builder.pop();
		}
	}


	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

}
