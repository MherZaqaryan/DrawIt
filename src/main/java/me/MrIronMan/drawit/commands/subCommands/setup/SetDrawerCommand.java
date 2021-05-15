package me.MrIronMan.drawit.commands.subCommands.setup;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.api.commands.SubCommand;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.game.SetupGame;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetDrawerCommand extends SubCommand {

    public SetDrawerCommand() {
        super("setdrawer");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (!DrawIt.getInstance().isInSetup(player)) {
            player.sendMessage(TextUtil.colorize(PluginMessages.NOT_IN_SETUP));
            return;
        }

        SetupGame setupGame = DrawIt.getInstance().getSetupGame(player);
        setupGame.setDrawerLocation(player.getLocation());
        player.sendMessage(setupGame.customize("{game} &aYou have successfully set drawer location."));
        PluginMessages.sendMessage(player, setupGame.getCurrentMessage());
    }

}
