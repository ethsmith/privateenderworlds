package codes.ephan.privateend.util;

import codes.ephan.privateend.models.EndWorld;
import org.bukkit.ChatColor;

public class MessageFormatUtil {

    public static String formatMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
