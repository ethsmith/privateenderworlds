package me.ephan.privateenderworlds.listener;

import me.ephan.privateenderworlds.PrivateEnderWorlds;
import me.ephan.privateenderworlds.ui.EnderWorldMenu;
import me.ephan.privateenderworlds.world.EnderWorld;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class InventoryClickListener implements Listener {

    // The main plugin class
    private final PrivateEnderWorlds plugin;

    public InventoryClickListener(PrivateEnderWorlds plugin) {
        this.plugin = plugin;
    }

    // Manage the creation, deletion, entering, leaving of the world and closing of the menu based on the item
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (ChatColor.stripColor(event.getView().getTitle()).equals("Ender World Menu")) {
            EnderWorldMenu menu = plugin.getEnderWorldMenu();
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);

            if (event.getCurrentItem() != null) {
                // Check if the option is not disabled
                if (!event.getCurrentItem().isSimilar(menu.getDisabledItem())) {
                    switch (event.getSlot()) {
                        // Create item
                        case 0:
                            if (event.getCurrentItem().isSimilar(menu.getCreateItem())) {
                                EnderWorld world = new EnderWorld(player.getUniqueId());
                                world.getMembers().add(player.getUniqueId());
                                plugin.getEnderWorlds().add(world);
                                world.teleport(player);
                            }
                            break;
                        // Enter item
                        case 4:
                            if (event.getCurrentItem().isSimilar(menu.getEnterItem())) {
                                EnderWorld world = plugin.getCurrentEnderWorld(player);
                                if (world != null)
                                    world.teleport(player);
                            }
                            break;
                        // Delete item
                        case 2:
                            if (event.getCurrentItem().isSimilar(menu.getDeleteItem())) {
                                EnderWorld world = plugin.getCurrentEnderWorld(player);

                                if (player.getUniqueId().equals(world.getOwner())) {
                                    for (UUID uuid : world.getMembers()) {
                                        Player p = plugin.getServer().getPlayer(uuid);
                                        p.performCommand("spawn");
                                    }

                                    plugin.getServer().unloadWorld(world.getWorld(), false);
                                    plugin.getEnderWorlds().remove(world);
                                    world.getWorld().getWorldFolder().delete();
                                    player.sendMessage(ChatColor.GREEN + "You have deleted your current ender world!");
                                    player.closeInventory();
                                    plugin.getEnderWorldMenu().open(player);
                                }
                            }
                            break;
                        // Leave item
                        case 6:
                            if (event.getCurrentItem().isSimilar(menu.getLeaveItem())) {
                                EnderWorld world = plugin.getCurrentEnderWorld(player);
                                if (world != null) {
                                    if (!plugin.isOwner(world, player.getUniqueId())) {
                                        world.getMembers().remove(player.getUniqueId());
                                        player.sendMessage(ChatColor.GREEN + "You have left your current ender world!");
                                        player.closeInventory();
                                        plugin.getEnderWorldMenu().open(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + "You cannot leave your own ender world, delete it instead!");
                                    }
                                }
                            }
                            break;
                        // Close item
                        case 8:
                            if (event.getCurrentItem().isSimilar(menu.getCloseItem()))
                                player.closeInventory();
                            break;
                    }
                }
            }
        }
    }
}
