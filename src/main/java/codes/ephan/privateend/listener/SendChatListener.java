package codes.ephan.privateend.listener;

import codes.ephan.privateend.PrivateEndPlugin;
import codes.ephan.privateend.controllers.SettingsController;
import codes.ephan.privateend.controllers.WorldController;
import codes.ephan.privateend.models.EndWorld;
import codes.ephan.privateend.util.MessageFormatUtil;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class SendChatListener implements Listener {

    private final static PrivateEndPlugin plugin = PrivateEndPlugin.getInstance();

    @Getter
    private static final Set<Player> addingMembers = new HashSet<>();

    @Getter
    private static final Set<Player> removingMembers = new HashSet<>();

    @EventHandler
    public void onSendChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!addingMembers.contains(player) && !removingMembers.contains(player)) return;

        event.setCancelled(true);

        EndWorld endWorld = WorldController.getWorldByPlayer(player);
        String[] playerNames = event.getMessage().replace(" ", "").split(",");

        List<String> validNames = removeInvalidNames(playerNames);

        if (addingMembers.contains(player)) {
            if (validNames.isEmpty()) return;

            validNames.forEach(playerName -> endWorld.getMembers().add(plugin.getServer().getPlayer(playerName).getUniqueId()));
            player.sendMessage(MessageFormatUtil.formatMessage(SettingsController.Messages.WORLD_MEMBER_ADDED));
        } else {
            if (validNames.isEmpty()) return;

            validNames.forEach(playerName -> endWorld.getMembers().remove(plugin.getServer().getPlayer(playerName).getUniqueId()));
            player.sendMessage(MessageFormatUtil.formatMessage(SettingsController.Messages.WORLD_MEMBER_REMOVED));
        }
    }

    private List<String> removeInvalidNames(String[] playerNames) {
        List<String> validPlayerNames = new LinkedList<>(Arrays.asList(playerNames));

        for (String playerName : playerNames) {
            Player member = plugin.getServer().getPlayer(playerName);

            if (member == null) validPlayerNames.remove(playerName);
        }

        return validPlayerNames;
    }
}
