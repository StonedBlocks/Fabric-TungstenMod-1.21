package net.thenameislinus.tungstenmod.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.thenameislinus.tungstenmod.TungstenMod;

public class ModItems {

    public static final Item TUNGSTEN_INGOT = registerItem("tungsten_ingot", new Item(new Item.Settings()));
    public static final Item RAW_TUNGSTEN = registerItem("raw_tungsten", new Item(new Item.Settings()));

    public static final Item TUNGSTEN_BOOTS = registerItem("tungsten_boots",
            new ArmorItem(ModArmorMaterials.TUNGSTEN_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(TungstenMod.MOD_ID, name), item);
    }


    public static void registerModItems() {
        TungstenMod.LOGGER.info("Registering Mod Items for " + TungstenMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.addAfter(Items.IRON_INGOT, ModItems.TUNGSTEN_INGOT);
            fabricItemGroupEntries.addAfter(Items.RAW_IRON, ModItems.RAW_TUNGSTEN);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.addAfter(Items.NETHERITE_BOOTS, ModItems.TUNGSTEN_BOOTS);
        });



    }
}
