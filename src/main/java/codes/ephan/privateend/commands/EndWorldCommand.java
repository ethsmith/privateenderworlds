package codes.ephan.privateend.commands;

import codes.ephan.privateend.controllers.GuiController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndWorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("endworld")) return true;

        if (!(sender instanceof Player)) return true;

        if (!(args.length == 0)) return true;

        Player player = (Player) sender;

        GuiController.openMenu(player);

        return true;
    }
}
