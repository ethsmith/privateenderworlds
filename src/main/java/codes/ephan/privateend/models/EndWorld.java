package codes.ephan.privateend.models;

import lombok.Data;
import org.bukkit.World;

import java.util.Set;
import java.util.UUID;

@Data
public class EndWorld {

    // UUID of the EnderWorld owner
    private final UUID owner;

    // UUID/name of the world
    private final UUID id;

    // World connected to the EnderWorld data, transient means it won't be serialized by gson
    private transient World world;

    // Collection of all players that have access to the EnderWorld
    private final Set<UUID> members;
}
