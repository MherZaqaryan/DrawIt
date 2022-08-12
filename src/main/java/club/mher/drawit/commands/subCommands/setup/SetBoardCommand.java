package club.mher.drawit.commands.subCommands.setup;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.data.PluginMessages;
import club.mher.drawit.game.SetupGame;
import club.mher.drawit.utility.OtherUtils;
import club.mher.drawit.utility.PermissionsUtil;
import club.mher.drawit.utility.TextUtil;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetBoardCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission(PermissionsUtil.COMMAND_SETUP)) {
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
                        } else {
                            setupGame.setBoardPos2(b.getLocation());
                            player.sendMessage(setupGame.customize("{game} &aYou have successfully set board position 2"));
                        }
                        PluginMessages.sendMessage(player, setupGame.getCurrentMessage());
                    }
                } else {
                    player.sendMessage(TextUtil.colorize(PluginMessages.NOT_IN_SETUP));
                }
            } else {
                player.sendMessage(TextUtil.colorize(PluginMessages.USAGE_COMMAND_SETBOARD));
            }
        }else {
            player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NO_PERMS)));
        }
        return true;
    }

}
