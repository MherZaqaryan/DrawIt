package me.MrIronMan.drawit.commands;

import com.cryptomorin.xseries.XSound;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.subCommands.game.*;
import me.MrIronMan.drawit.commands.subCommands.setup.*;
import me.MrIronMan.drawit.commands.subCommands.util.*;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.utility.PermissionsUtil;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.IntStream;

import static me.MrIronMan.drawit.data.PluginMessages.DRAWIT_COMMANDS;

public class DrawItCommand extends BukkitCommand {
    public static final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public DrawItCommand(String cmd) {
        super(cmd);
        this.setAliases(new ArrayList<>(Collections.singleton("di")));
        this.setDescription("DrawIt main command to see all list of commands.");

    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("drawit." + getName())) {
            sender.sendMessage("You don't have permission to execute this command!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("You cant run this command from console!");
            return true;
        }

        Player player = (Player) sender;
        if (player.hasPermission(PermissionsUtil.COMMAND_MAIN) && args.length <= 0) {
            PluginMessages.sendMessage(player, DRAWIT_COMMANDS);
            XSound.play(player, "CLICK,1,2");
            return true;
        }

        String[] arguments = IntStream.range(0, args.length).filter(i -> i != 0).mapToObj(i -> args[i]).toArray(String[]::new);
        DrawIt.getMessagesData().getStringList(MessagesData.DRAWIT_COMMANDS_PLAYER).stream().map(TextUtil::colorize).forEach(player::sendMessage);
        if (subCommands.stream().noneMatch(subCommand -> subCommand.getName().equalsIgnoreCase(args[0]))) return true;
        subCommands.stream().filter(subCommand -> subCommand.getName().equalsIgnoreCase(args[0])).findFirst().get().execute(sender, arguments);
        return true;
    }
}
