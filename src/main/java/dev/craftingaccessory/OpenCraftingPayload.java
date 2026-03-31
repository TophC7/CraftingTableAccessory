package dev.craftingaccessory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Empty C2S packet: client asks the server to open the crafting grid.
 * Server re-validates that the player actually has a crafting table equipped
 * before opening the menu (prevents packet spoofing).
 */
public record OpenCraftingPayload() implements CustomPacketPayload {

    public static final Type<OpenCraftingPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    CraftingAccessoryMod.MOD_ID, "open_crafting"));

    public static final StreamCodec<FriendlyByteBuf, OpenCraftingPayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public OpenCraftingPayload decode(FriendlyByteBuf buf) {
                    return new OpenCraftingPayload();
                }

                @Override
                public void encode(FriendlyByteBuf buf, OpenCraftingPayload payload) {
                    // empty payload
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenCraftingPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer sp) {
                CraftingAccessoryMod.openCrafting(sp);
            }
        });
    }
}
