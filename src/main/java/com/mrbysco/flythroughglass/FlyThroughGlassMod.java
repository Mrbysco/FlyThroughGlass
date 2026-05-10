package com.mrbysco.flythroughglass;

import com.mojang.logging.LogUtils;
import com.mrbysco.flythroughglass.config.config.FlyConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(FlyThroughGlassMod.MOD_ID)
public class FlyThroughGlassMod {
	public static final String MOD_ID = "flythroughglass";
	public static final Logger LOGGER = LogUtils.getLogger();

	public FlyThroughGlassMod(ModContainer container, Dist dist) {
		container.registerConfig(ModConfig.Type.COMMON, FlyConfig.commonSpec);

		if (dist.isClient()) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}
}
