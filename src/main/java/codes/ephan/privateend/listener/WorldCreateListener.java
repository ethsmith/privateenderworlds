package codes.ephan.privateend.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public class WorldCreateListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldCreate(WorldInitEvent event) {
        event.getWorld().setKeepSpawnInMemory(false);
    }
}
