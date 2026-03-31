package dev.craftingaccessory.client.mixin;

import dev.craftingaccessory.OpenCraftingPayload;
import dev.craftingaccessory.client.CraftingButton;
import dev.craftingaccessory.client.ClientSetup;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects the crafting button into the vanilla inventory screen.
 * Position/visibility updates are handled via events.
 */
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin
        extends AbstractContainerScreen<InventoryMenu> {

    private InventoryScreenMixin() {
        super(null, null, null);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void craftingAccessory$addButton(CallbackInfo ci) {
        var button = new CraftingButton(
                this.getGuiLeft() + ClientSetup.BUTTON_X,
                this.getGuiTop() + ClientSetup.BUTTON_Y,
                () -> PacketDistributor.sendToServer(new OpenCraftingPayload()));
        this.addRenderableWidget(button);
        ClientSetup.setCraftingButton(button);
    }
}
