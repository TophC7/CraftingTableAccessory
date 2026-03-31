package dev.craftingaccessory;

import io.wispforest.accessories.api.Accessory;
import io.wispforest.accessories.api.AccessoriesAPI;
import io.wispforest.accessories.api.AccessoriesCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Items;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CraftingAccessoryMod.MOD_ID)
public class CraftingAccessoryMod {

    public static final String MOD_ID = "crafting_accessory";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public CraftingAccessoryMod(IEventBus modEventBus) {
        LOGGER.info("Crafting Table Accessory loaded");

        // Register vanilla crafting table as an equippable accessory
        AccessoriesAPI.registerAccessory(Items.CRAFTING_TABLE, new Accessory() {});

        modEventBus.addListener(this::registerPayloads);
    }

    public static boolean hasCraftingTableEquipped(Player player) {
        var cap = AccessoriesCapability.get(player);
        return cap != null && cap.isEquipped(Items.CRAFTING_TABLE);
    }

    /** Server-side: open the portable crafting grid for a player. */
    public static void openCrafting(ServerPlayer player) {
        if (!hasCraftingTableEquipped(player)) return;

        // Skip if already in a portable crafting menu
        if (player.containerMenu instanceof PersistentCraftingMenu) return;

        player.openMenu(new SimpleMenuProvider(
                (containerId, inv, p) -> new PersistentCraftingMenu(
                        containerId, inv,
                        ContainerLevelAccess.create(p.level(), p.blockPosition())),
                Component.translatable("container.crafting")));
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar(MOD_ID)
                .playToServer(
                        OpenCraftingPayload.TYPE,
                        OpenCraftingPayload.STREAM_CODEC,
                        OpenCraftingPayload::handle);
    }
}
