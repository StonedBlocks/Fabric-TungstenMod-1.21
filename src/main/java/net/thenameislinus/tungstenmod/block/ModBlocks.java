package net.thenameislinus.tungstenmod.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.thenameislinus.tungstenmod.TungstenMod;


public class ModBlocks {

    public static final Block TUNSGTEN_BLOCK = registerBlock("tungsten_block",
            new Block(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).strength(6f)
                    .requiresTool().sounds(BlockSoundGroup.NETHERITE)));

    public static final Block TUNGSTEN_ORE = registerBlock(
            "tungsten_ore",
            new ExperienceDroppingBlock(
                    ConstantIntProvider.create(0),
                    AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(4.0F, 3.0F)
            )
    );

    public static final Block DEEPSLATE_TUNGSTEN_ORE = registerBlock(
            "deepslate_tungsten_ore",
            new ExperienceDroppingBlock(
                    ConstantIntProvider.create(0),
                    AbstractBlock.Settings.copyShallow(TUNGSTEN_ORE).mapColor(MapColor.DEEPSLATE_GRAY).strength(5.5F, 3.0F).sounds(BlockSoundGroup.DEEPSLATE)
            )
    );

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

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.addAfter(Blocks.DEEPSLATE_IRON_ORE, ModBlocks.TUNGSTEN_ORE);
            fabricItemGroupEntries.addAfter(ModBlocks.TUNGSTEN_ORE, ModBlocks.DEEPSLATE_TUNGSTEN_ORE);
        });
    }

}
