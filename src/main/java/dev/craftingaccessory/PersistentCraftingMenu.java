package dev.craftingaccessory;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;

/**
 * CraftingMenu subclass that stays open without a real crafting table block.
 * Vanilla's stillValid() checks proximity to a placed block; we re-check the
 * accessory instead, closing the menu if the table is unequipped.
 */
public class PersistentCraftingMenu extends CraftingMenu {

    public PersistentCraftingMenu(int containerId, Inventory playerInventory,
            ContainerLevelAccess access) {
        super(containerId, playerInventory, access);
    }

    @Override
    public boolean stillValid(Player player) {
        return CraftingAccessoryMod.hasCraftingTableEquipped(player);
    }
}
