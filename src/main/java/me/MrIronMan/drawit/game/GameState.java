package me.MrIronMan.drawit.game;

public enum GameState {

    WAITING,
    STARTING,
    ACTIVE,
    ENDING;

    public static String getName(GameState gs) {
        switch (gs) {
            case WAITING:
                return "Waiting...";
            case STARTING:
                return "Starting...";
            case ACTIVE:
                return "Active";
            case ENDING:
                return "Ending...";
        }
        return null;
    }

}
