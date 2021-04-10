package me.MrIronMan.drawit.sql;

public enum PlayerDataType {

    POINTS,
    GAMES_PLAYED,
    VICTORIES,
    CORRECT_GUESSES,
    INCORRECT_GUESSES,
    SKIPS;

    static String getEnum(PlayerDataType pdt) {
        switch (pdt) {
            case POINTS:
                return "Points";
            case GAMES_PLAYED:
                return "GamesPlayed";
            case VICTORIES:
                return "Victories";
            case CORRECT_GUESSES:
                return "CorrectGuesses";
            case INCORRECT_GUESSES:
                return "IncorrectGuesses";
            case SKIPS:
                return "Skips";
        }
        return null;
    }

}
