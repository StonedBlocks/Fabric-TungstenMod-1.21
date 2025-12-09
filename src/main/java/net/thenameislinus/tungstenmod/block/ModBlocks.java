package net.thenameislinus.tungstenmod.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.thenameislinus.tungstenmod.TungstenMod;


public class ModBlocks {

    public static final Block TUNSGTEN_BLOCK = registerBlock("tungsten_block",
            new Block(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).strength(6f)
                    .requiresTool().sounds(BlockSoundGroup.NETHERITE)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(TungstenMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(TungstenMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        TungstenMod.LOGGER.info("Registering Mod Blocks for " + TungstenMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.addAfter(Blocks.IRON_BLOCK, ModBlocks.TUNSGTEN_BLOCK);
        });
    }

}
