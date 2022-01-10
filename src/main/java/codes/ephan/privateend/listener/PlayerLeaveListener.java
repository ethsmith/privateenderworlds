package codes.ephan.privateend.listener;

import codes.ephan.privateend.controllers.WorldController;
import codes.ephan.privateend.models.EndWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        WorldController.exitWorld(player);
    }
}
