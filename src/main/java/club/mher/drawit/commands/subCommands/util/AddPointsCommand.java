package club.mher.drawit.commands.subCommands.util;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.data.PluginMessages;
import club.mher.drawit.sql.PlayerData;
import club.mher.drawit.sql.PlayerDataType;
import club.mher.drawit.utility.OtherUtils;
import club.mher.drawit.utility.PermissionsUtil;
import club.mher.drawit.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddPointsCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission(PermissionsUtil.COMMAND_ADDPOINTS)) {
            if (args.length == 2) {
                String playerName = args[0];
                int amount;
                try {
                    amount = Integer.parseInt(args[1]);
                    if (amount < 0) {
                        player.sendMessage(TextUtil.colorize(PluginMessages.POINTS_NOT_POSITIVE));
                    }
                    if (OtherUtils.isOnline(playerName)) {
                        Player playerToAdd = Bukkit.getPlayer(playerName);
                        PlayerData playerData = DrawIt.getPlayerData(playerToAdd);
                        playerData.addData(PlayerDataType.POINTS, amount);
                        DrawIt.getInstance().updateSidebar(player);
                        player.sendMessage(TextUtil.colorize(PluginMessages.POINTS_ADDED.replace("{player}", playerName).replace("{points}", String.valueOf(amount))));
                    } else {
                        player.sendMessage(TextUtil.colorize(PluginMessages.PLAYER_NOT_FOUND));
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(TextUtil.colorize(PluginMessages.POINTS_NOT_NUMBER));
                }
            } else {
                player.sendMessage(TextUtil.colorize(PluginMessages.USAGE_COMMAND_ADD_POINTS));
            }
        }else {
            player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NO_PERMS)));
        }
        return true;
    }

}
