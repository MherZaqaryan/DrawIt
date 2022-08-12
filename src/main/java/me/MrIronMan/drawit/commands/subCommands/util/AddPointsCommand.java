package me.MrIronMan.drawit.commands.subCommands.util;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.sql.PlayerData;
import me.MrIronMan.drawit.sql.PlayerDataType;
import me.MrIronMan.drawit.utility.OtherUtils;
import me.MrIronMan.drawit.utility.PermissionsUtil;
import me.MrIronMan.drawit.utility.TextUtil;
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
