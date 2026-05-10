package com.mrbysco.flythroughglass.datagen;

import com.mrbysco.flythroughglass.FlyThroughGlassMod;
import com.mrbysco.flythroughglass.util.FlyHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class FlyDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new FlyLanguageProvider(packOutput));
		generator.addProvider(true, new FlyBlockTagProvider(packOutput, lookupProvider));
	}

	public static class FlyLanguageProvider extends LanguageProvider {


		public FlyLanguageProvider(PackOutput output) {
			super(output, FlyThroughGlassMod.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			addConfig("general", "General", "General Settings");
			addConfig("runThroughBlocks", "Run Through Blocks", "When enabled allows running through blocks [Default: false]");
			addConfig("runMinSpeed", "Run Min Speed", "The minimum speed a player must have to run through blocks [Default: 0.18]");
		}

		/**
		 * Add the translation for a config entry
		 *
		 * @param path        The path of the config entry
		 * @param name        The name of the config entry
		 * @param description The description of the config entry
		 */
		private void addConfig(String path, String name, String description) {
			this.add(FlyThroughGlassMod.MOD_ID + ".configuration." + path, name);
			this.add(FlyThroughGlassMod.MOD_ID + ".configuration." + path + ".tooltip", description);
		}
	}

	public static class FlyBlockTagProvider extends BlockTagsProvider {

		public FlyBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(output, lookupProvider, FlyThroughGlassMod.MOD_ID);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			this.tag(FlyHelper.BLOCKS_TO_BREAK).addTags(
					Tags.Blocks.GLASS_BLOCKS, Tags.Blocks.GLASS_PANES
			);
		}
	}
}
