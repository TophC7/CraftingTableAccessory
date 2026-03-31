package dev.craftingaccessory.client;

import dev.craftingaccessory.CraftingAccessoryMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Button that renders a custom sprite sheet
 * Sprite sheet is 256x256 with two 20x19 frames stacked vertically.
 */
public class CraftingButton extends AbstractButton {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            CraftingAccessoryMod.MOD_ID, "textures/gui/button.png");

    private static final int SPRITE_WIDTH = 20;
    private static final int SPRITE_HEIGHT = 19;
    private static final int TEX_SIZE = 256;

    private final Runnable onPress;

    public CraftingButton(int x, int y, Runnable onPress) {
        super(x, y, SPRITE_WIDTH, SPRITE_HEIGHT,
                Component.translatable("gui.crafting_accessory.open_crafting"));
        this.onPress = onPress;
        setTooltip(Tooltip.create(
                Component.translatable("gui.crafting_accessory.open_crafting")));
    }

    @Override
    public void onPress() {
        onPress.run();
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY,
            float partialTick) {
        // Hover state is the second frame, offset by SPRITE_HEIGHT
        int v = isHovered() ? SPRITE_HEIGHT : 0;
        graphics.blit(TEXTURE, getX(), getY(), 0, v,
                SPRITE_WIDTH, SPRITE_HEIGHT, TEX_SIZE, TEX_SIZE);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}
