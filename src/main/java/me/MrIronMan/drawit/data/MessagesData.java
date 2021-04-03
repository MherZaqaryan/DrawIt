package me.MrIronMan.drawit.data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class MessagesData extends DataManager{

    public MessagesData(Plugin plugin, String dir, String name) {
        super(plugin, dir, name);
        YamlConfiguration msg = getConfig();

        msg.addDefault(PREFIX, "&b&lDraw&a&lIt &8►&r");

        msg.addDefault(DRAWIT_COMMANDS_PLAYER, new String[]{
                "&b&lDraw&a&lIt &c&lCommands",
                "&7/DrawIt Join <Quick|Game> &8- Run to join game.",
                "&7/DrawIt Skip &8- Run to skip your round.",
                "&7/DrawIt Leave &8- Run to leave from game."
        });

        msg.addDefault(PLAYER_JOIN, "{prefix} &7{player} &ajoined the game.");
        msg.addDefault(PLAYER_QUIT, "{prefix} &7{player} &cquit the game.");
        msg.addDefault(FULL_GAME, "{prefix} &c&lThis game is full.");
        msg.addDefault(IN_GAME, "{prefix} &c&lYou can't do this while in game.");
        msg.addDefault(NOT_IN_GAME, "{prefix} &c&lYou are not in game.");
        msg.addDefault(GAME_NOT_FOUND, "{prefix} &cThat game not found.");
        msg.addDefault(GAME_NOT_ENABLED, "{prefix} &cThis game not enabled.");
        msg.addDefault(NEXT_ROUND, "{prefix} &9{drawer} &7is now drawing! &8[Round {round}].");
        msg.addDefault(QUIT_MID_GAME, "{prefix} &b&lThe Drawer &c&lquit mid-drawing. Round ended.");
        msg.addDefault(WORD_ANNOUNCE, "{prefix} &7&lThe word was: &3&l{word}.");
        msg.addDefault(EVERYONE_GOT_THE_WORD, "{prefix} &c&lRound over! &a&lEveryone got the word.");
        msg.addDefault(PLAYER_GUESSED, "{prefix} &e&l{guesser} &a&lguessed the word for &3&l{points} points.");
        msg.addDefault(DRAWER_CHAT_LOCK, "{prefix} &c&lYou cannot chat while you are drawer.");
        msg.addDefault(NO_PERMS, "{prefix} &cCommand dose not exists or you don't have permission.");
        msg.addDefault(NOT_DRAWER_TO_SKIP, "{prefix} &cYou need to be drawer to skip.");
        msg.addDefault(NO_PERM_SKIP, "{prefix} &cYou cant skip the game, you need to be at least VIP.");
        msg.addDefault(GAME_SKIPPED, "{prefix} &c&l{drawer} decided to skip the word.&a&l Next round will start shortly.");
        msg.addDefault(COLOR_PICKER_TITLE, "&5&lColor Picker");

        msg.addDefault(START_COUNTDOWN, "&aStarting game in &a&l{time}.");
        msg.addDefault(START_COUNTDOWN_UNDER_5, "&aStarting game in &c&l{time}.");
        msg.addDefault(DRAWER, "&e&lYou must draw: &f&l{word}&e&l!");
        msg.addDefault(PICK_WORD, "&e&lWaiting for the drawer to pick a word.");
        msg.addDefault(START_DRAW, "&e&lWaiting for the drawer to start drawing.");
        msg.addDefault(GUESSERS, "&e&l{word}");
        msg.addDefault(HIDING_CHARACTER, "_");

        msg.addDefault(GAME_END_MESSAGE, new String[]{
                "",
                "       &c&lGame Over! &3{winner_1} &7won the game!",
                "",
                "            &2Place 1 &8- &9{winner_1}",
                "            &ePlace 2 &8- &9{winner_2}",
                "            &6Place 3 &8- &9{winner_3}",
                "",
                "           &7&lYou got &a&l{points} points!",
                "",
                "&cGame will restart in 10 seconds."
        });

        if (isFirstTime()) {

            msg.addDefault("lobby-items.game-selector.display-name", "&aGame Selector &7[Right-click]");
            msg.addDefault("lobby-items.game-selector.lore", new String[]{
                    "",
                    "&7Click to open game selector"
            });

            msg.addDefault("lobby-items.return-to-lobby.display-name", "&cReturn to lobby &7[Right-click]");
            msg.addDefault("lobby-items.return-to-lobby.lore", new String[]{
                    "",
                    "&7Click to return to lobby."
            });

        }

        msg.addDefault("drawer-tools.thin-brush.display-name", "&6&lThin Brush");
        msg.addDefault("drawer-tools.thin-brush.lore", new String[]{
                "",
                "&7Paints a one pixel line."
        });

        msg.addDefault("drawer-tools.thick-brush.display-name", "&e&lThick Brush");
        msg.addDefault("drawer-tools.thick-brush.lore", new String[]{
                "",
                "&7Paints a three pixel dot."
        });

        msg.addDefault("drawer-tools.spray-canvas.display-name", "&b&lSpray Canvas");
        msg.addDefault("drawer-tools.spray-canvas.lore", new String[]{
                "",
                "&7Splatters the canvas."
        });

        msg.addDefault("drawer-tools.fill-can.display-name", "&d&lFill Can");
        msg.addDefault("drawer-tools.fill-can.lore", new String[]{
                "",
                "&7Fills are with selected color."
        });

        msg.addDefault("drawer-tools.burn-canvas.display-name", "&c&lBurn Canvas");
        msg.addDefault("drawer-tools.burn-canvas.lore", new String[]{
                "",
                "&7Clears the canvas."
        });

        msg.addDefault(BOARD_LOBBY_TITLE, "&b&lDraw&a&lIt");
        msg.addDefault(BOARD_LOBBY_LINES, new String[]{
                "",
                "&a&lTokens",
                "&7{tokens}",
                "",
                "&e&lYour Stats",
                "&3Points: &b{points}",
                "&3Games Played: &b{games_played}",
                "&3Victories: &b{victories}",
                "&3Correct Guesses: &b{correct_guesses}",
                "&3Incorrect Guesses: &b{incorrect_guesses}",
                "&3Skips: &b{skips}",
                "",
                "&8&m--------",
                "&emc.server.net",
        });
        msg.addDefault(BOARD_GAME_TITLE, "&b&lDraw&a&lIt");
        msg.addDefault(BOARD_GAME_LINES, new String[]{
                "",
                "&e&lRound Time",
                "{time}",
                "",
                "&b&lRound Info",
                "&7Rounds Left: {rounds_left}",
                "&7Drawer: {drawer}",
                "",
                "&c&lLeaders",
                "{leader_1}",
                "{leader_2}",
                "{leader_3}",
                "",
                "&8&m--------",
                "&emc.server.net"
        });

        save();
    }


    // Messages Path

    public static String PREFIX = "prefix";

    public static String DRAWIT_COMMANDS_PLAYER = "player-commands";

    public static String PLAYER_JOIN = "player-join";
    public static String PLAYER_QUIT = "player-quit";
    public static String FULL_GAME = "full-game";
    public static String IN_GAME = "in-game";
    public static String NOT_IN_GAME = "not-in-game";
    public static String GAME_NOT_FOUND = "game-not-found";
    public static String GAME_NOT_ENABLED = "game-not-enabled";
    public static String NEXT_ROUND = "next-round";
    public static String QUIT_MID_GAME = "quit-mid-game";
    public static String WORD_ANNOUNCE = "word-announce";
    public static String EVERYONE_GOT_THE_WORD = "everyone-got-word";
    public static String PLAYER_GUESSED = "player-guessed";
    public static String DRAWER_CHAT_LOCK = "drawer-chat-lock";

    public static String START_COUNTDOWN = "action-bar-messages.start-countdown";
    public static String START_COUNTDOWN_UNDER_5 = "action-bar-messages.start-countdown-under-5";
    public static String DRAWER = "action-bar-messages.drawer";
    public static String PICK_WORD = "action-bar-messages.pick-word";
    public static String START_DRAW = "action-bar-messages.start-draw";
    public static String GUESSERS = "action-bar-messages.guessers";
    public static String HIDING_CHARACTER = "action-bar-messages.hiding-character";

    public static String NO_PERMS = "no-permission";
    public static String GAME_SKIPPED = "game-skipped";
    public static String NOT_DRAWER_TO_SKIP = "not-drawer-to-skip";
    public static String NO_PERM_SKIP = "skip-no-permission";
    public static String COLOR_PICKER_TITLE = "color-picker-title";
    public static String LEADER_FORMAT = "scoreboard-leader-format";
    public static String GAME_END_MESSAGE = "game-end-message";

    public static String BOARD_LOBBY_TITLE = "scoreboards.lobby.title";
    public static String BOARD_LOBBY_LINES = "scoreboards.lobby.lines";
    public static String BOARD_GAME_TITLE = "scoreboards.game.title";
    public static String BOARD_GAME_LINES = "scoreboards.game.lines";

}
