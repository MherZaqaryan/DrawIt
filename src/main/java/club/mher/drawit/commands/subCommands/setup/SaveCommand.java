package club.mher.drawit.commands.subCommands.setup;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.data.PluginMessages;
import club.mher.drawit.menu.menus.SaveGameMenu;
import club.mher.drawit.utility.PermissionsUtil;
import club.mher.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;
        if (player.hasPermission(PermissionsUtil.COMMAND_SETUP)) {
            if (args.length == 0) {
                if (DrawIt.getInstance().isInSetup(player)) {
                    new SaveGameMenu(DrawIt.getPlayerMenuUtility(player)).open();
                } else {
                    player.sendMessage(TextUtil.colorize(PluginMessages.NOT_IN_SETUP));
                }
            }
        }else {
            player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NO_PERMS)));
        }
        return false;
    }

}
