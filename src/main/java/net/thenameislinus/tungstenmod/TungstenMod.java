package net.thenameislinus.tungstenmod;

import net.fabricmc.api.ModInitializer;

import net.thenameislinus.tungstenmod.block.ModBlocks;
import net.thenameislinus.tungstenmod.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TungstenMod implements ModInitializer {
	public static final String MOD_ID = "tungstenmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
	}
}