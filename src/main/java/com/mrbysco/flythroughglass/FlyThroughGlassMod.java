package com.mrbysco.flythroughglass;

import com.mojang.logging.LogUtils;
import com.mrbysco.flythroughglass.config.config.FlyConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(FlyThroughGlassMod.MOD_ID)
public class FlyThroughGlassMod {
	public static final String MOD_ID = "flythroughglass";
	public static final Logger LOGGER = LogUtils.getLogger();

	public FlyThroughGlassMod() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FlyConfig.commonSpec);
	}
}
