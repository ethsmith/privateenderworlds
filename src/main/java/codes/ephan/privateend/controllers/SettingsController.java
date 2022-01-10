package codes.ephan.privateend.controllers;

import codes.ephan.privateend.PrivateEndPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class SettingsController {

    private final static PrivateEndPlugin plugin = PrivateEndPlugin.getInstance();

    private final static FileConfiguration settings = plugin.getConfig();

    public static void init() {
        settings.options().copyDefaults(true);
        plugin.saveConfig();
        General.init();
        Messages.init();
    }

    public static class General {
        public static String BASE_WORLD;

        public static int MAX_MEMBERS;

        public static void init() {
            final String PATH_PREFIX = "General";

            BASE_WORLD = settings.getString(PATH_PREFIX + ".BASE_WORLD");
            MAX_MEMBERS = settings.getInt(PATH_PREFIX + ".MAX_MEMBERS");
        }
    }

    public static class Messages {
        public static String WORLD_CREATED;

        public static String WORLD_ENTERED;

        public static String WORLD_EXITED;

        public static String WORLD_DELETED;

        public static String WORLD_LEFT;

        public static String WORLD_MENU_CLOSED;

        public static String WORLD_MEMBER_ADDED;

        public static String WORLD_MEMBER_REMOVED;

        public static String MEMBER_NOT_OWNER;

        public static String WORLD_DOESNT_EXIST;

        public static void init() {
            final String PATH_PREFIX = "Messages";

            WORLD_CREATED = settings.getString(PATH_PREFIX + ".WORLD_CREATED");
            WORLD_ENTERED = settings.getString(PATH_PREFIX + ".WORLD_ENTERED");
            WORLD_EXITED = settings.getString(PATH_PREFIX + ".WORLD_EXITED");
            WORLD_DELETED = settings.getString(PATH_PREFIX + ".WORLD_DELETED");
            WORLD_LEFT = settings.getString(PATH_PREFIX + ".WORLD_LEFT");
            WORLD_MENU_CLOSED = settings.getString(PATH_PREFIX + ".WORLD_MENU_CLOSED");
            WORLD_MEMBER_ADDED = settings.getString(PATH_PREFIX + ".WORLD_MEMBER_ADDED");
            WORLD_MEMBER_REMOVED = settings.getString(PATH_PREFIX + ".WORLD_MEMBER_REMOVED");
            MEMBER_NOT_OWNER = settings.getString(PATH_PREFIX + ".MEMBER_NOT_OWNER");
            WORLD_DOESNT_EXIST = settings.getString(PATH_PREFIX + ".WORLD_DOESNT_EXIST");
        }
    }
}
