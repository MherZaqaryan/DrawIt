package me.MrIronMan.drawit.support;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.database.PlayerData;
import me.MrIronMan.drawit.database.PlayerDataType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "drawit";
    }

    @Override
    public @NotNull String getAuthor() {
        return "MrIronMan";
    }

    @Override
    public @NotNull String getVersion() {
        return DrawIt.getInstance().getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier){
        PlayerData pd = DrawIt.getPlayerData((Player) player);
        switch (identifier) {
            case "points":
                return String.valueOf(pd.getData(PlayerDataType.POINTS));
            case "games_played":
                return String.valueOf(pd.getData(PlayerDataType.GAMES_PLAYED));
            case "victories":
                return String.valueOf(pd.getData(PlayerDataType.VICTORIES));
            case "correct_guesses":
                return String.valueOf(pd.getData(PlayerDataType.CORRECT_GUESSES));
            case "incorrect_guesses":
                return String.valueOf(pd.getData(PlayerDataType.INCORRECT_GUESSES));
            case "skips":
                return String.valueOf(pd.getData(PlayerDataType.SKIPS));
            case "rank_format":
                return DrawIt.getConfigData().getPointFormat((Player) player);
        }
        return null;
    }

}
