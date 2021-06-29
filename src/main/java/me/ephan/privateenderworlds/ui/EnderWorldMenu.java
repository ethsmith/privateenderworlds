package me.ephan.privateenderworlds.ui;

import me.ephan.privateenderworlds.PrivateEnderWorlds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnderWorldMenu {

    // The main plugin class
    private final PrivateEnderWorlds plugin;

    // The item that creates the end world
    private ItemStack createItem;

    // The item to enter the end world
    private ItemStack enterItem;

    // The item to delete the end world
    private ItemStack deleteItem;

    // The item that represents an unavailable option
    private ItemStack disabledItem;

    // THe item to leave a players ender world
    private ItemStack leaveItem;

    // The item to close the EnderWorld menu
    private ItemStack closeItem;

    public EnderWorldMenu(PrivateEnderWorlds plugin) {
        this.plugin = plugin;

        setCreateItem("&aCreate World", Material.ANVIL);
        setEnterItem("&5Enter World", Material.OAK_DOOR);
        setDeleteItem("&cDelete World", Material.BLAZE_POWDER);
        setDisabledItem("&7Disabled!", Material.GRAY_DYE);
        setLeaveItem("&4Leave World", Material.DARK_OAK_DOOR);
        setCloseItem("&eClose Inventory", Material.BARRIER);
    }

    /**
     * Open the EnderWorld menu for a specific player
     * @param player - The player that will see the menu
     */
    public void open(Player player) {
        Inventory inventory = plugin.getServer().createInventory(player, 9, ChatColor.DARK_PURPLE + "Ender World Menu");

        if (plugin.getCurrentEnderWorld(player) == null) {
            inventory.setItem(0, createItem);
            inventory.setItem(2, disabledItem);
            inventory.setItem(4, disabledItem);
            inventory.setItem(6, disabledItem);
            inventory.setItem(8, closeItem);
        } else {
            inventory.setItem(0, disabledItem);
            inventory.setItem(2, deleteItem);
            inventory.setItem(4, enterItem);
            inventory.setItem(6, leaveItem);
            inventory.setItem(8, closeItem);
        }

        player.openInventory(inventory);
    }

    /**
     * Returns the create world item
     * @return - The item that creates the world
     */
    public ItemStack getCreateItem() {
        return createItem;
    }

    /**
     * Returns the enter world item
     * @return - The item that lets a player enter the end world
     */
    public ItemStack getEnterItem() {
        return enterItem;
    }

    /**
     * Returns the delete world item
     * @return - The item that deletes a players end world
     */
    public ItemStack getDeleteItem() {
        return deleteItem;
    }

    /**
     * Returns the disabled option item
     * @return - The item that represents a disabled option
     */
    public ItemStack getDisabledItem() {
        return disabledItem;
    }

    /**
     * Returns the leave world item
     * @return - The item that a player uses to leave another players end world
     */
    public ItemStack getLeaveItem() {
        return leaveItem;
    }

    /**
     * Returns the close inventory item
     * @return - The item that closes the EnderWorld menu inventory
     */
    public ItemStack getCloseItem() {
        return closeItem;
    }

    // Sets the name and material of the create item
    public void setCreateItem(String name, Material material) {
        createItem = setItem(name, material);
    }

    // Sets the name and material of the enter item
    public void setEnterItem(String name, Material material) {
        enterItem = setItem(name, material);
    }

    // Sets the name and material of the delete item
    public void setDeleteItem(String name, Material material) {
        deleteItem = setItem(name, material);
    }

    // Sets the name and material of the disabled item
    public void setDisabledItem(String name, Material material) {
        disabledItem = setItem(name, material);
    }

    // Sets the name and material of the leave item
    public void setLeaveItem(String name, Material material) {
        leaveItem = setItem(name, material);
    }

    // Sets the name and material of the close item
    public void setCloseItem(String name, Material material) {
        closeItem = setItem(name, material);
    }

    // Sets the name and material of an ItemStack and returns it
    private ItemStack setItem(String name, Material material) {
        ItemStack item = new ItemStack(material);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        item.setItemMeta(itemMeta);

        return item;
    }
}
