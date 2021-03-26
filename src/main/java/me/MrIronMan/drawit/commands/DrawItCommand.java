package me.MrIronMan.drawit.commands;

import me.MrIronMan.drawit.commands.subCommands.game.*;
import me.MrIronMan.drawit.commands.subCommands.setup.*;
import me.MrIronMan.drawit.commands.subCommands.util.AddPointsCommand;
import me.MrIronMan.drawit.commands.subCommands.util.AddTokensCommand;
import me.MrIronMan.drawit.commands.subCommands.util.SetMainLobbyCommand;
import me.MrIronMan.drawit.data.MessagesUtils;
import me.MrIronMan.drawit.utility.PermissionsUtil;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DrawItCommand extends BukkitCommand {

    public DrawItCommand(String cmd) {
        super(cmd);
        List<String> aliases = new ArrayList<>();
        aliases.add("di");
        this.setAliases(aliases);
        this.setDescription("DrawIt main command to see all list of commands.");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Sorry, but you cant use this command from console!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (player.hasPermission(PermissionsUtil.COMMAND_MAIN)) {
                MessagesUtils.sendMessage(player, MessagesUtils.DRAWIT_COMMANDS);
            }else {
                for (String msg : MessagesUtils.DRAWIT_COMMANDS_PLAYER) {
                    player.sendMessage(TextUtil.colorize(msg));
                }
            }
            return true;
        }

        String arg = args[0];

        List<String> oldArgs = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (i != 0) {
                oldArgs.add(args[i]);
            }
        }

        String[] newArgs = oldArgs.toArray(new String[0]);

        if (arg.equalsIgnoreCase("SetMainLobby")) {
            new SetMainLobbyCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("Join")) {
            new JoinCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("Leave")) {
            new LeaveGameCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("Start") || arg.equalsIgnoreCase("ForceStart")) {
            new ForceStartCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("Gui")) {
            new GuiCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("Setup")) {
            new SetupCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("Delete")) {
            new DeleteCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("SetBoard")) {
            new SetBoardCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("SetLobby")) {
            new SetLobbyCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("SetDrawer")) {
            new SetDrawerCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("Save")) {
            new SaveCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("AddPoints")) {
            new AddPointsCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("AddTokens")) {
            new AddTokensCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("Skip")) {
            new SkipGameCommand().execute(sender, newArgs);
        }
        else {
            player.sendMessage(TextUtil.colorize(MessagesUtils.SUBCOMMAND_NOT_FOUND));
        }
        return true;
    }
}
