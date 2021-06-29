package me.ephan.privateenderworlds;

import com.google.gson.Gson;
import me.ephan.privateenderworlds.command.EnderWorldCommand;
import me.ephan.privateenderworlds.listener.InventoryClickListener;
import me.ephan.privateenderworlds.ui.EnderWorldMenu;
import me.ephan.privateenderworlds.world.EnderWorld;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class PrivateEnderWorlds extends JavaPlugin {

    // Instance to access the rest of the plugin
    private static PrivateEnderWorlds instance;
    // Gson is used to write to and read from Json files for EnderWorld data
    private final Gson gson = new Gson();
    // Collection of all EnderWorlds currently created
    private final Set<EnderWorld> enderWorlds = new HashSet<>();
    // The menu used to access each player or groups EnderWorld
    private final EnderWorldMenu enderWorldMenu = new EnderWorldMenu(this);
    // Config file
    private File configFile;
    // Configuration data access
    private FileConfiguration config;

    /**
     * Used to access plugin data where necessary
     * @return Plugin instance
     */
    public static PrivateEnderWorlds getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        // initialize the instance
        instance = this;
        // Create the original config file
        createConfig();
        // Save the config file
        saveConfig();
        // Load all EnderWorld data from json files
        loadWorlds();
        // Register the command that opens the EnderWorld gui/menu
        getCommand("enderworld").setExecutor(new EnderWorldCommand(this));
        // Register the event that manages players interaction with the EnderWorld gui/menu
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // Save all active EnderWorlds to json files for persistence across restarts
        saveWorlds();
    }

    /**
     * Used to access/write configuration data in the config.yml
     * @return The configuration file (config.yml)
     */
    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Used to access the EnderWorlds currently loaded in memory
     * @return EnderWorld collection (Set)
     */
    public Set<EnderWorld> getEnderWorlds() {
        return enderWorlds;
    }

    /**
     * Used to access the EnderWorld gui/menu
     * @return EnderWorld gui/menu
     */
    public EnderWorldMenu getEnderWorldMenu() {
        return enderWorldMenu;
    }

    /**
     * Used to retrieve the EnderWorld that a player belongs to
     * @param player - Player that has an EnderWorld
     * @return EnderWorld of specified player if it exists, else null
     */
    public EnderWorld getCurrentEnderWorld(Player player) {
        for (EnderWorld world : getEnderWorlds()) {
            for (UUID memberId : world.getMembers()) {
                Player member = getServer().getPlayer(memberId);
                if (member == player)
                    return world;
            }
        }
        return null;
    }

    /**
     * Check whether or not a specific player's uuid matches the EnderWorld owner's uuid
     * @param world - EnderWorld to check
     * @param uniqueId - The uuid of the player that needs checked
     * @return True if the player is the owner, else false
     */
    public boolean isOwner(EnderWorld world, UUID uniqueId) {
        return world.getOwner().equals(uniqueId);
    }

    /**
     * Check whether or not two uuid's are the same
     * @param ownerId - First uuid
     * @param memberId - Second uuid
     * @return True if the uuids are the same, else false
     */
    public boolean isSamePlayer(UUID ownerId, UUID memberId) {
        return ownerId.equals(memberId);
    }

    /**
     * Create the config file
     */
    private void createConfig() {
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save all EnderWorld data to json files
     */
    private void saveWorlds() {
        for (EnderWorld world : getEnderWorlds()) {
            try {
                FileWriter fileWriter = new FileWriter(getDataFolder().getAbsolutePath()  + "/worlds/" + world.getWorldId().toString() + ".json");
                gson.toJson(world, fileWriter);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create EnderWorld objects from json data and add it to the EnderWorld collection
     */
    private void loadWorlds() {
        try {
            if (Paths.get(getDataFolder().getPath() + "/worlds").toFile().exists()) {
                List<File> files = Files.list(Paths.get(getDataFolder().getPath() + "/worlds"))
                        .map(Path::toFile)
                        .collect(Collectors.toList());

                if (!files.isEmpty())
                    for (File file : files) {
                        if (!file.getName().endsWith(".json"))
                            continue;

                        EnderWorld world = gson.fromJson(new FileReader(file), EnderWorld.class);
                        if (world.getWorld() == null)
                            world.create(getConfig().getBoolean("copy-enabled"));
                        getEnderWorlds().add(world);

                        file.delete();
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
