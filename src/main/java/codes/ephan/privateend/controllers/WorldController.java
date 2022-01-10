package codes.ephan.privateend.controllers;

import codes.ephan.privateend.PrivateEndPlugin;
import codes.ephan.privateend.models.EndWorld;
import com.google.gson.Gson;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WorldController {

    private final static PrivateEndPlugin plugin = PrivateEndPlugin.getInstance();

    private final static Gson gson = new Gson();

    public final static String BASE_DIR = plugin.getDataFolder().getAbsolutePath() + "/worlds/";

    private final static String BASE_WORLD = SettingsController.General.BASE_WORLD;
    @Getter
    private static final Set<EndWorld> worldCache = new HashSet<>();

    public static boolean cacheHas(UUID worldId) {
        for (EndWorld world : worldCache) {
            if (world.getId() == worldId) return true;
        }

        return false;
    }

    public static boolean databaseHas(UUID worldId) {
        File worldFolder = new File(BASE_DIR);

        if (!worldFolder.exists()) return false;

        File worldFile = new File(BASE_DIR + worldId.toString() + ".json");

        if (!worldFile.exists()) return false;

        return true;
    }

    public static EndWorld getFromCache(UUID worldId) {
        for (EndWorld world : worldCache) { if (world.getId() == worldId) return world; }

        return null;
    }

    public static EndWorld loadWorld(UUID owner, UUID worldId) {
        if (cacheHas(worldId)) return getFromCache(worldId);

        String worldName = worldId.toString();
        if (databaseHas(worldId)) {
            try {
                EndWorld world = gson.fromJson(new FileReader(BASE_DIR + worldName + ".json"), EndWorld.class);

                worldCache.add(world);

                return world;
            }

            catch (FileNotFoundException e) { e.printStackTrace(); }
        }

        return owner != null ? createWorld(owner, worldId) : null;
    }

    public static EndWorld createWorld(UUID owner, UUID worldId) {
        if (cacheHas(worldId)) return getFromCache(worldId);

        if (databaseHas(worldId)) return loadWorld(null, worldId);

        World world = null;

        if (plugin.getServer().getWorld(BASE_WORLD) == null) world = new WorldCreator(worldId.toString()).environment(World.Environment.THE_END).createWorld();
        else world = copyBaseWorld(worldId);

        EndWorld endWorld = new EndWorld(owner, worldId, new HashSet<>());
        endWorld.setWorld(world);

        worldCache.add(endWorld);

        return endWorld;
    }

    public static void deleteWorld(UUID worldId) {
        plugin.getServer().unloadWorld(worldId.toString(), false);

        File endWorldFolder = new File(worldId.toString());
        endWorldFolder.delete();

        File endWorldData = new File(BASE_DIR + worldId.toString() + ".json");
        endWorldData.delete();
    }

    public static void enterWorld(Player player) {
        EndWorld endWorld = getWorldByPlayer(player);

        if (endWorld == null) return;

        if (endWorld.getWorld() == null) endWorld.setWorld(new WorldCreator(endWorld.getId().toString()).environment(World.Environment.THE_END).createWorld());

        player.teleport(endWorld.getWorld().getSpawnLocation());
    }

    public static void exitWorld(Player player) {
        EndWorld endWorld = getWorldByPlayer(player);

        if (endWorld == null) return;

        if (endWorld.getWorld() == null) return;

        if (endWorld.getWorld() != player.getWorld()) return;

        player.teleport(plugin.getServer().getWorld("world").getSpawnLocation());
    }

    public static void leaveWorld(Player player) {
        EndWorld endWorld = getWorldByPlayer(player);

        if (endWorld == null) return;

        if (player.getWorld().equals(endWorld.getWorld())) player.teleport(plugin.getServer().getWorld("world").getSpawnLocation());

        endWorld.getMembers().remove(player.getUniqueId());
    }

    public static EndWorld getWorldByPlayer(Player player) {
        for (EndWorld endWorld : worldCache) {
            if (endWorld.getOwner().equals(player.getUniqueId())) return endWorld;
            if (endWorld.getMembers().contains(player.getUniqueId())) return endWorld;
        }

        return null;
    }

    public static boolean isWorldOwner(Player player) {
        for (EndWorld endWorld : worldCache) {
            if (player.getUniqueId().equals(player.getUniqueId())) return true;
        }

        return false;
    }

    private static World copyBaseWorld(UUID worldId) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            File src = new File(BASE_WORLD);
            File dest = new File(worldId.toString());

            try {
                FileUtils.copyDirectory(src, dest);
                File duplicateUid = new File(worldId.toString() + "/uid.dat");
                duplicateUid.delete();
            }

            catch (IOException e) { e.printStackTrace(); }
        });

        return new WorldCreator(worldId.toString()).environment(World.Environment.THE_END).createWorld();
    }
}
