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
    public AddPointsCommand() {
        super("selection");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length != 2) {
            player.sendMessage(TextUtil.colorize(PluginMessages.USAGE_COMMAND_ADD_POINTS));
            return;
        }

        int amount = Math.max(1, Integer.parseInt(args[1]));
        if (amount < 0) {
            player.sendMessage(TextUtil.colorize(PluginMessages.POINTS_NOT_POSITIVE));
        }

        String target = args[0];

        if (!OtherUtils.isOnline(target)) {
            player.sendMessage(TextUtil.colorize(PluginMessages.PLAYER_NOT_FOUND));
            return;
        }

        Player playerToAdd = Bukkit.getPlayer(target);
        PlayerData playerData = DrawIt.getPlayerData(playerToAdd);
        playerData.addData(PlayerDataType.POINTS, amount);
        DrawIt.getInstance().updateSidebar(player);
        player.sendMessage(TextUtil.colorize(PluginMessages.POINTS_ADDED.replace("{player}", target).replace("{points}", String.valueOf(amount))));

    }

}
