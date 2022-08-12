package club.mher.drawit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class SkipCommand extends BukkitCommand {

    public SkipCommand(String cmd) {
        super(cmd);
        this.setDescription("DrawIt command to skip the game");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        Bukkit.dispatchCommand(player, "DrawIt Skip");
        return true;
    }

}
