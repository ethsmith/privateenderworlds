package codes.ephan.privateend;

import codes.ephan.privateend.commands.EndWorldCommand;
import codes.ephan.privateend.controllers.SettingsController;
import codes.ephan.privateend.controllers.WorldController;
import codes.ephan.privateend.listener.InventoryClickListener;
import codes.ephan.privateend.listener.PlayerLeaveListener;
import codes.ephan.privateend.listener.SendChatListener;
import codes.ephan.privateend.listener.WorldCreateListener;
import codes.ephan.privateend.models.EndWorld;
import codes.ephan.privateend.util.FileListUtil;
import com.google.gson.Gson;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class PrivateEndPlugin extends JavaPlugin {

    @Getter
    private static PrivateEndPlugin instance;

    private static final Gson gson = new Gson();

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) WorldController.exitWorld(player);

        File file = new File(WorldController.BASE_DIR);

        if (!file.exists()) file.mkdirs();

        for (EndWorld endWorld : WorldController.getWorldCache()) {
            try {
                String fileName = WorldController.BASE_DIR + endWorld.getId().toString() + ".json";

                file = new File(fileName);
                file.createNewFile();

                FileWriter fileWriter = new FileWriter(fileName);

                gson.toJson(endWorld, fileWriter);

                fileWriter.close();
            }

            catch (IOException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void onEnable() {
        instance = this;

        SettingsController.init();

        Set<String> serializedEndWorlds = FileListUtil.listFilesInSingleLayer(WorldController.BASE_DIR);

        if (serializedEndWorlds != null) {
            for (String worldName : serializedEndWorlds) {
                UUID id = UUID.fromString(worldName.replace(".json", ""));
                WorldController.loadWorld(null, id);
            }
        }

        getCommand("endworld").setExecutor(new EndWorldCommand());

        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new SendChatListener(), this);
        getServer().getPluginManager().registerEvents(new WorldCreateListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
    }
}
