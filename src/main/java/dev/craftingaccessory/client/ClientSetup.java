package dev.craftingaccessory.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.craftingaccessory.CraftingAccessoryMod;
import dev.craftingaccessory.OpenCraftingPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Client-side event handlers: button lifecycle, visibility, and keybind.
 * All client-only references are isolated in this class.
 */
public class ClientSetup {

    // Unbound by default per user request
    public static final KeyMapping OPEN_CRAFTING_KEY = new KeyMapping(
            "key.crafting_accessory.open_crafting",
            InputConstants.UNKNOWN.getValue(),
            "key.categories.crafting_accessory");

    public static final int BUTTON_X = 130;
    public static final int BUTTON_Y = 61;

    static CraftingButton craftingButton;

    public static void setCraftingButton(CraftingButton button) {
        craftingButton = button;
    }

    /** Game-bus events: button visibility, position, cleanup, keybind. */
    @EventBusSubscriber(modid = CraftingAccessoryMod.MOD_ID, value = Dist.CLIENT)
    public static class GameEvents {

        /** Update button position and visibility before each frame. */
        @SubscribeEvent
        public static void onScreenRender(ScreenEvent.Render.Pre event) {
            if (!(event.getScreen() instanceof InventoryScreen screen)) return;
            if (craftingButton == null) return;

            var cs = (AbstractContainerScreen<?>) screen;
            craftingButton.setPosition(
                    cs.getGuiLeft() + BUTTON_X, cs.getGuiTop() + BUTTON_Y);

            var mc = Minecraft.getInstance();
            craftingButton.visible = mc.player != null
                    && CraftingAccessoryMod.hasCraftingTableEquipped(mc.player);
        }

        /** Clear stale button reference when inventory screen closes. */
        @SubscribeEvent
        public static void onScreenClose(ScreenEvent.Closing event) {
            if (event.getScreen() instanceof InventoryScreen) {
                craftingButton = null;
            }
        }

        /** Handle the configurable keybind. */
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            var mc = Minecraft.getInstance();
            while (OPEN_CRAFTING_KEY.consumeClick()) {
                if (mc.player != null
                        && CraftingAccessoryMod.hasCraftingTableEquipped(mc.player)) {
                    PacketDistributor.sendToServer(new OpenCraftingPayload());
                }
            }
        }
    }

    /** Mod-bus events: keybind registration. */
    @EventBusSubscriber(modid = CraftingAccessoryMod.MOD_ID, value = Dist.CLIENT,
            bus = EventBusSubscriber.Bus.MOD)
    public static class ModEvents {

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(OPEN_CRAFTING_KEY);
        }
    }
}
