package club.mher.drawit.game;

public enum GameState {

    WAITING,
    STARTING,
    PLAYING,
    RESTARTING;

    public static String getName(GameState gs) {
        switch (gs) {
            case WAITING:
                return "Waiting...";
            case STARTING:
                return "Starting";
            case PLAYING:
                return "Playing";
            case RESTARTING:
                return "Restarting";
        }
        return null;
    }

}
