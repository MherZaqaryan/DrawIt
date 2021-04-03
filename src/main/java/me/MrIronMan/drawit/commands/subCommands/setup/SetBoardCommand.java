package me.MrIronMan.drawit.commands.subCommands.setup;

import com.cryptomorin.xseries.XMaterial;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.game.SetupGame;
import me.MrIronMan.drawit.utility.OtherUtils;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetBoardCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

            if (args.length == 1 && (args[0].equalsIgnoreCase("Pos1") || args[0].equalsIgnoreCase("Pos2"))) {
                if (DrawIt.getInstance().isInSetup(player)) {
                    SetupGame setupGame = DrawIt.getInstance().getSetupGame(player);
                    Block b = OtherUtils.getTargetBlock(player, 5);
                    if (b.getType().equals(XMaterial.AIR.parseMaterial())) {
                        player.sendMessage(setupGame.customize("{game} &cThe block you are looking at is air."));
                    } else {
                        if (args[0].equalsIgnoreCase("Pos1")) {
                            setupGame.setBoardPos1(b.getLocation());
                            player.sendMessage(setupGame.customize("{game} &aYou have successfully set board position 1"));
                        }else {
                            setupGame.setBoardPos2(b.getLocation());
                            player.sendMessage(setupGame.customize("{game} &aYou have successfully set board position 2"));
                        }
                        PluginMessages.sendMessage(player, setupGame.getCurrentMessage());
                    }
                }
                else {
                    player.sendMessage(TextUtil.colorize(PluginMessages.NOT_IN_SETUP));
                }
            }
            else {
                player.sendMessage(TextUtil.colorize(PluginMessages.USAGE_COMMAND_SETBOARD));
            }
        return true;
    }

}
