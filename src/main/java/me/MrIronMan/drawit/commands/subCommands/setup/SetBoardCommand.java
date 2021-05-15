package me.MrIronMan.drawit.commands.subCommands.setup;

import com.cryptomorin.xseries.XMaterial;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.api.commands.SubCommand;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.game.SetupGame;
import me.MrIronMan.drawit.utility.OtherUtils;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetBoardCommand extends SubCommand {

    public SetBoardCommand() {
        super("setboard");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(TextUtil.colorize(PluginMessages.USAGE_COMMAND_SETBOARD));
            return;
        }

        if (!DrawIt.getInstance().isInSetup(player)) {
            player.sendMessage(TextUtil.colorize(PluginMessages.NOT_IN_SETUP));
            return;
        }

        SetupGame setupGame = DrawIt.getInstance().getSetupGame(player);
        Block b = OtherUtils.getTargetBlock(player, 5);
        if (b.getType().equals(XMaterial.AIR.parseMaterial())) {
            player.sendMessage(setupGame.customize("{game} &cThe block you are looking at is air."));
            return;
        }

        if (args[0].equalsIgnoreCase("Pos1")) {
            setupGame.setBoardPos1(b.getLocation());
            player.sendMessage(setupGame.customize("{game} &aYou have successfully set board position 1"));
        }

        if (args[0].equalsIgnoreCase("Pos2")) {
            setupGame.setBoardPos2(b.getLocation());
            player.sendMessage(setupGame.customize("{game} &aYou have successfully set board position 2"));
        }

        PluginMessages.sendMessage(player, setupGame.getCurrentMessage());
    }

}
