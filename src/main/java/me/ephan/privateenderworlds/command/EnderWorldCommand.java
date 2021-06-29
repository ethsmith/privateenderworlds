package me.ephan.privateenderworlds.command;

import me.ephan.privateenderworlds.PrivateEnderWorlds;
import me.ephan.privateenderworlds.world.EnderWorld;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderWorldCommand implements CommandExecutor {

    // The main plugin class
    private final PrivateEnderWorlds plugin;

    public EnderWorldCommand(PrivateEnderWorlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("enderworld"))
            if (sender instanceof Player) {
                Player player = (Player) sender;

                // Open the EnderWorld menu if no args are present
                if (args.length == 0)
                    plugin.getEnderWorldMenu().open(player);
                // If two arguments are present, check if it is the add or remove subcommand
                else if (args.length == 2) {
                    String playerName = args[1];
                    Player member = plugin.getServer().getPlayer(playerName);

                    if (member != null) {
                        // EnderWorld that a player is a member of
                        EnderWorld world = plugin.getCurrentEnderWorld(player);
                        switch (args[0]) {
                            // Add a member to the EnderWorld
                            case "add":
                                if (world != null) {
                                    if (plugin.isOwner(world, player.getUniqueId()))
                                        if (!plugin.isSamePlayer(player.getUniqueId(), member.getUniqueId()))
                                            world.getMembers().add(member.getUniqueId());
                                        else
                                            player.sendMessage(ChatColor.RED + "You can't add or remove yourself!");
                                } else {
                                    player.sendMessage(ChatColor.RED + "You do not have an active ender world!");
                                }
                                break;
                            // Remove a member from the EnderWorld
                            case "remove":
                                if (world != null) {
                                    if (plugin.isOwner(world, player.getUniqueId()))
                                        if (!plugin.isSamePlayer(player.getUniqueId(), member.getUniqueId()))
                                            world.getMembers().remove(member.getUniqueId());
                                        else
                                            player.sendMessage(ChatColor.RED + "You can't add or remove yourself!");
                                } else {
                                    player.sendMessage(ChatColor.RED + "You do not have an active ender world!");
                                }
                                break;
                            // Send the available commands if the subcommand was wrong
                            default:
                                player.sendMessage(ChatColor.RED + "/enderworld or /enderworld [add/remove] [player]");
                                break;
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Player either isn't online or doesn't exist!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "/enderworld or /enderworld [add/remove] [player]");
                }
            }
        return true;
    }
}
