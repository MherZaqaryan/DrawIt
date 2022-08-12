package club.mher.drawit.commands;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.subCommands.game.*;
import club.mher.drawit.commands.subCommands.setup.*;
import club.mher.drawit.commands.subCommands.util.*;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.data.PluginMessages;
import club.mher.drawit.utility.PermissionsUtil;
import club.mher.drawit.utility.TextUtil;
import com.cryptomorin.xseries.XSound;
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
            sender.sendMessage("You cant run this command from console!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (player.hasPermission(PermissionsUtil.COMMAND_MAIN)) {
                PluginMessages.sendMessage(player, PluginMessages.DRAWIT_COMMANDS);
                XSound.play(player, "CLICK,1,2");
            }else {
                for (String msg : DrawIt.getMessagesData().getStringList(MessagesData.DRAWIT_COMMANDS_PLAYER)) {
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
        else if (arg.equalsIgnoreCase("Start")) {
            new ForceStartCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("QuickJoin")) {
            new QuickJoinCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("Menu")) {
            new MenuCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("Reload")) {
            new ReloadCommand().execute(sender, newArgs);
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
        else if (arg.equalsIgnoreCase("Skip")) {
            new SkipGameCommand().execute(sender, newArgs);
        }
        else if (arg.equalsIgnoreCase("BuildMode")) {
            new BuildModeCommand().execute(sender, newArgs);
        }
        else {
            player.sendMessage(TextUtil.colorize(PluginMessages.SUBCOMMAND_NOT_FOUND));
        }
        return true;
    }
}
