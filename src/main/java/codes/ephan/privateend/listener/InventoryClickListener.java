package codes.ephan.privateend.listener;

import codes.ephan.privateend.PrivateEndPlugin;
import codes.ephan.privateend.controllers.GuiController;
import codes.ephan.privateend.controllers.SettingsController;
import codes.ephan.privateend.controllers.WorldController;
import codes.ephan.privateend.models.EndWorld;
import codes.ephan.privateend.util.MessageFormatUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.File;
import java.util.UUID;

public class InventoryClickListener implements Listener {

    private static final PrivateEndPlugin plugin = PrivateEndPlugin.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!ChatColor.stripColor(event.getView().getTitle()).equals("End World Menu")) return;

        if (event.getCurrentItem() == null) return;

        event.setCancelled(true);

        if (event.getCurrentItem().isSimilar(GuiController.getDisabledIcon())) return;

        Player player = (Player) event.getWhoClicked();
        EndWorld endWorld = WorldController.getWorldByPlayer(player);

        switch (event.getSlot()) {
            // Create
            case 0:
                WorldController.createWorld(player.getUniqueId(), UUID.randomUUID());

                player.closeInventory();

                player.sendMessage(MessageFormatUtil.formatMessage(SettingsController.Messages.WORLD_CREATED));

                break;

            // Enter
            case 2:
                WorldController.enterWorld(player);

                player.closeInventory();

                player.sendMessage(MessageFormatUtil.formatMessage(SettingsController.Messages.WORLD_ENTERED));

                break;

            // Delete
            case 4:
                if (endWorld == null) return;

                WorldController.deleteWorld(endWorld.getId());
                WorldController.getWorldCache().remove(endWorld);

                player.closeInventory();

                player.sendMessage(MessageFormatUtil.formatMessage(SettingsController.Messages.WORLD_DELETED));

                break;

            // Leave
            case 6:
                if (WorldController.isWorldOwner(player)) return;

                if (endWorld == null) return;

                WorldController.leaveWorld(player);

                player.closeInventory();

                player.sendMessage(MessageFormatUtil.formatMessage(SettingsController.Messages.WORLD_LEFT));

                break;

            // Exit
            case 8:
                if (endWorld == null) return;

                WorldController.exitWorld(player);

                player.closeInventory();

                player.sendMessage(MessageFormatUtil.formatMessage(SettingsController.Messages.WORLD_EXITED));

            // Close
            case 15:
                player.closeInventory();

                player.sendMessage(MessageFormatUtil.formatMessage(SettingsController.Messages.WORLD_MENU_CLOSED));

                break;

            // Add
            case 11:
                SendChatListener.getAddingMembers().add(player);

                player.closeInventory();

                sendChatInstructions(player);

                break;

            // Remove
            case 13:
                SendChatListener.getRemovingMembers().add(player);

                player.closeInventory();

                sendChatInstructions(player);

                break;
        }
    }

    private void sendChatInstructions(Player player) {
        player.sendMessage(MessageFormatUtil.formatMessage("&5Type the names of a member (or members) you want to add/remove."));
        player.sendMessage(MessageFormatUtil.formatMessage("&5Names are separated by commas. Example: playerOne,playerTwo or playerOne, playerTwo"));
    }
}
