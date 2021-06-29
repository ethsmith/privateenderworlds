package me.ephan.privateenderworlds.world;

import me.ephan.privateenderworlds.PrivateEnderWorlds;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EnderWorld {

    // Instance of the main plugin class, transient means it won't be serialized by gson
    private final transient PrivateEnderWorlds plugin = PrivateEnderWorlds.getInstance();

    // UUID of the EnderWorld owner
    private final UUID owner;

    // UUID/name of the world
    private final UUID worldId;

    // World connected to the EnderWorld data, transient means it won't be serialized by gson
    private transient World world;

    // Collection of all players that have access to the EnderWorld
    private final Set<UUID> members;

    public EnderWorld(UUID owner, UUID worldId, Set<UUID> members) {
        this.owner = owner;
        this.worldId = worldId;
        this.members = members;

        create(plugin.getConfig().getBoolean("copy-enabled"));

        if (world == null)
            plugin.getLogger().severe(ChatColor.RED + "New ender world '" + worldId + "' was not created correctly!");
        else
            plugin.getLogger().info(ChatColor.GREEN + "New ender world '" + worldId + "' was created correctly!");
    }

    public EnderWorld(UUID owner) {
        this.owner = owner;
        this.worldId = UUID.randomUUID();
        this.members = new HashSet<>();

        create(plugin.getConfig().getBoolean("copy-enabled"));

        if (world == null)
            plugin.getLogger().severe(ChatColor.RED + "New ender world '" + worldId + "' was not created correctly!");
        else
            plugin.getLogger().info(ChatColor.GREEN + "New ender world '" + worldId + "' was created correctly!");
    }

    /**
     * Returns the UUID of the owner of the EnderWorld
     * @return UUID of the EnderWorld owner
     */
    public UUID getOwner() {
        return owner;
    }

    /**
     * Returns the unique id/name of the world
     * @return uuid/name of the world
     */
    public UUID getWorldId() {
        return worldId;
    }

    /**
     * Returns the world connected to the EnderWorld data
     * @return the world connected to the EnderWorld data
     */
    public World getWorld() {
        return world;
    }

    /**
     * Returns a collection of players that have access to the EnderWorld
     * @return a collection of players that have access to the EnderWorld
     */
    public Set<UUID> getMembers() {
        return members;
    }

    /**
     * Teleports the player to the spawn location of the world connected to the EnderWorld data
     * @param player - The player to teleport
     */
    public void teleport(Player player) {
        player.teleport(world.getSpawnLocation());
    }

    /**
     * Creates an end world to connect to the EnderWorld data
     * @param copy - Whether or not to use the config to copy a base world
     */
    public void create(boolean copy) {
        if (copy) {
            World baseWorld = plugin.getServer().getWorld(plugin.getConfig().getString("base-world"));
            if (baseWorld != null)
                this.world = new WorldCreator(worldId.toString()).copy(baseWorld).createWorld();
            else
                plugin.getLogger().severe(ChatColor.RED + "Base world does not exist for copying!");
        } else {
            this.world = new WorldCreator(worldId.toString()).environment(World.Environment.THE_END).createWorld();
        }
    }
}
