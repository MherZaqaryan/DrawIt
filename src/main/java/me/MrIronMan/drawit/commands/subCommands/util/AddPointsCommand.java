package me.MrIronMan.drawit.commands.subCommands.util;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.sql.PlayerData;
import me.MrIronMan.drawit.sql.PlayerDataType;
import me.MrIronMan.drawit.utility.OtherUtils;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddPointsCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 2) {
            String playerName = args[0];
            int amount;
            try {
                amount = Integer.parseInt(args[1]);
                if (amount < 0) {
                    player.sendMessage(TextUtil.colorize("{prefix} &cAmount must be positive."));
                }
                if (OtherUtils.isOnline(playerName)) {
                    Player playerToAdd = Bukkit.getPlayer(playerName);
                    PlayerData playerData = DrawIt.getPlayerData(playerToAdd);
                    playerData.addData(PlayerDataType.POINTS, amount);
                }else {
                    player.sendMessage(TextUtil.colorize("{prefix} &cPlayer not found."));
                }
            }catch (NumberFormatException e) {
                player.sendMessage(TextUtil.colorize("{prefix} &cAmount must be number."));
            }
        }else {
            player.sendMessage(TextUtil.colorize(" S O O N "));
        }

        return true;
    }

}
