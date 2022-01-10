package codes.ephan.privateend.controllers;

import codes.ephan.privateend.PrivateEndPlugin;
import codes.ephan.privateend.models.EndWorld;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiController {

    private final static PrivateEndPlugin plugin = PrivateEndPlugin.getInstance();

    @Getter
    private static ItemStack disabledIcon;

    private static void init(Inventory inventory) {
        setDisabledItem(null, 17);
        setCreateItem(inventory, "&aCreate World", Material.ANVIL);
        setEnterItem(inventory, "&5Enter World", Material.OAK_DOOR);
        setExitItem(inventory, "&5Exit World", Material.IRON_DOOR);
        setDeleteItem(inventory, "&cDelete World", Material.BLAZE_POWDER);
        setLeaveItem(inventory, "&4Leave World", Material.DARK_OAK_DOOR);
        setCloseItem(inventory, "&eClose Inventory", Material.BARRIER);
        setAddItem(inventory, "&aAdd Members", Material.GREEN_DYE);
        setRemoveItem(inventory, "&cRemove Members", Material.RED_DYE);
    }

    public static void openMenu(Player player) {
        final Inventory endWorldMenu = plugin.getServer().createInventory(null, 18, ChatColor.DARK_PURPLE + "End World Menu");

        init(endWorldMenu);

        final EndWorld endWorld = WorldController.getWorldByPlayer(player);

        if (endWorld == null) {
            setDisabledItem(endWorldMenu, 2);
            setDisabledItem(endWorldMenu, 4);
            setDisabledItem(endWorldMenu, 6);
            setDisabledItem(endWorldMenu, 11);
            setDisabledItem(endWorldMenu, 13);
        }

        else setDisabledItem(endWorldMenu, 0);

        if (WorldController.isWorldOwner(player)) setDisabledItem(endWorldMenu, 6);

        else setDisabledItem(endWorldMenu, 4);

        player.openInventory(endWorldMenu);
    }

    public static void setCreateItem(Inventory inventory, String name, Material icon) {
        if (!isValidIcon(name, icon)) return;

        inventory.setItem(0, createIcon(name, icon));
    }

    public static void setEnterItem(Inventory inventory, String name, Material icon) {
        if (!isValidIcon(name, icon)) return;

        inventory.setItem(2, createIcon(name, icon));
    }

    public static void setDeleteItem(Inventory inventory, String name, Material icon) {
        if (!isValidIcon(name, icon)) return;

        inventory.setItem(4, createIcon(name, icon));
    }

    public static void setDisabledItem(Inventory inventory, int slot) {
        if (!isValidIcon("&7Disabled!", Material.GRAY_DYE)) return;

        disabledIcon = createIcon("&7Disabled!", Material.GRAY_DYE);

        if (inventory != null) inventory.setItem(slot, disabledIcon);
    }

    public static void setLeaveItem(Inventory inventory, String name, Material icon) {
        if (!isValidIcon(name, icon)) return;

        inventory.setItem(6, createIcon(name, icon));
    }

    public static void setExitItem(Inventory inventory, String name, Material icon) {
        if (!isValidIcon(name, icon)) return;

        inventory.setItem(8, createIcon(name, icon));
    }

    public static void setCloseItem(Inventory inventory, String name, Material icon) {
        if (!isValidIcon(name, icon)) return;

        inventory.setItem(15, createIcon(name, icon));
    }

    public static void setAddItem(Inventory inventory, String name, Material icon) {
        if (!isValidIcon(name, icon)) return;

        inventory.setItem(11, createIcon(name, icon));
    }

    public static void setRemoveItem(Inventory inventory, String name, Material icon) {
        if (!isValidIcon(name, icon)) return;

        inventory.setItem(13, createIcon(name, icon));
    }

    public static ItemStack createIcon(String name, Material icon) {
        ItemStack iconItem = new ItemStack(icon);
        ItemMeta iconItemMeta = iconItem.getItemMeta();

        if (iconItemMeta == null) return null;

        iconItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        iconItem.setItemMeta(iconItemMeta);

        return iconItem;
    }

    private static boolean isValidIcon(String name, Material icon) {
        return createIcon(name, icon) != null;
    }
}
