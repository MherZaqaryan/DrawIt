package club.mher.drawit.commands;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    public abstract boolean execute(CommandSender sender, String[] args);

}
