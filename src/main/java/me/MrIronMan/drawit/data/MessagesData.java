package me.MrIronMan.drawit.data;

import me.MrIronMan.drawit.api.data.DataManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class MessagesData extends DataManager {

    public MessagesData(Plugin plugin, String dir, String name) {
        super(plugin, dir, name);
        YamlConfiguration msg = getConfig();

        msg.addDefault(PREFIX, "&8▍ &b&lDraw&a&lIt &8▏&r");

        msg.addDefault(DRAWIT_COMMANDS_PLAYER, new String[]{
                "&b&lDraw&a&lIt &c&lCommands",
                "&7/DrawIt Join (Game) &8- Join the specified game.",
                "&7/DrawIt Quickjoin &8- Quick join game.",
                "&7/DrawIt Skip &8- Skip word when drawer.",
                "&7/DrawIt Leave &8-Leave from current game."
        });

        msg.addDefault(PLAYER_JOIN, "{prefix} &7{player} &ajoined the game.");
        msg.addDefault(PLAYER_QUIT, "{prefix} &7{player} &cquit the game.");
        msg.addDefault(FULL_GAME, "{prefix} &c&lThis game is full.");
        msg.addDefault(IN_GAME, "{prefix} &c&lYou can't do this while in game.");
        msg.addDefault(NOT_IN_GAME, "{prefix} &c&lYou are not in game.");
        msg.addDefault(NOT_SPECTATING, "{prefix} &cYou are not spectating game.");
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
        msg.addDefault(QUICK_JOIN_GAME_NOT_FOUND, "{prefix} &cCurrently there is no game available to join.");

        msg.addDefault(COLOR_PICKER_TITLE, "&5&lColor Picker");
        msg.addDefault(START_COUNTDOWN, "&aStarting game in &a&l{time}.");
        msg.addDefault(START_COUNTDOWN_UNDER_5, "&aStarting game in &c&l{time}.");
        msg.addDefault(DRAWER, "&e&lYou must draw: &f&l{word}&e&l!");
        msg.addDefault(PICK_WORD, "&e&lWaiting for the drawer to pick a word.");
        msg.addDefault(START_DRAW, "&e&lWaiting for the drawer to start drawing.");
        msg.addDefault(GUESSERS, "&e&l{word}");
        msg.addDefault(HIDING_CHARACTER, "_");

        msg.addDefault(LEADER_FORMAT, "&f{point} &8- &7{player}");

        msg.addDefault(GAME_END_MESSAGE, new String[]{
                "",
                "       &c&lGame Over! &9{winner_1} &7won the game!",
                "",
                "            &2Place 1 &8- &9{winner_1}",
                "            &ePlace 2 &8- &9{winner_2}",
                "            &6Place 3 &8- &9{winner_3}",
                "",
                "",
                "&cGame will restart in 10 seconds."
        });

        msg.addDefault(ConfigData.LOBBY_ITEMS, new String[]{});

        if (isFirstTime()) {
            saveItem(ConfigData.LOBBY_ITEMS+".game-selector", "&aGame Selector &7[Right-click]", "","&7Click to open game selector");
            saveItem(ConfigData.LOBBY_ITEMS+".return-to-lobby", "&cReturn to lobby &7[Right-click]", "","&7Click to return to lobby.");
        }

        msg.addDefault(GAME_MENU_SETTINGS_TITLE, "&7Game Selector");

        saveItem(ConfigData.GAMES_MENU_SETTINGS_WAITING, "&a{game}", "","&7Players: &f{in}/{max}","&7State: &f{state}","","&bClick to join");
        saveItem(ConfigData.GAMES_MENU_SETTINGS_STARTING, "&a{game}", "","&7Players: &f{in}/{max}","&7State: &f{state}","","&bClick to join");
        saveItem(ConfigData.GAMES_MENU_SETTINGS_PLAYING, "&a{game}", "","&7Players: &f{in}/{max}","&7State: &f{state}","","&bClick to join");
        saveItem(ConfigData.GAMES_MENU_SETTINGS_WAITING_SLOTS, "&7Waiting...", "","&bWaiting for instance...");

        if (isFirstTime()) {
            saveItem(ConfigData.GAMES_MENU_ITEMS+".spectate-game", "&c&lSpectate Game", "","&7Allows you to browse","&7playing games to spectate","","&bClick to view games");
            saveItem(ConfigData.GAMES_MENU_ITEMS+".quick-join", "&a&lQuick Join", "","&7Adds you to join the","&7quick join queue to find","&7you a game!","","&bClick to join queue");
        }

        msg.addDefault(WORD_CHOOSE_MENU_SETTINGS_TITLE, "&8Select a Word");
        saveItem(ConfigData.WORD_CHOOSE_MENU_WORD_ITEM, "&e&l{word}", "","&7Click to select: {word}","&7as the word!","","&bClick to select");

        msg.addDefault(SPECTATE_MENU_SETTINGS_TITLE, "&7Spectate Game");
        saveItem(ConfigData.SPECTATE_MENU_GAME, "&e{game}", "","&7Players: &f{in}/{max}","&7State: &f{state}","","&bClick to join");


        if (isFirstTime()) {
            saveItem(ConfigData.SPECTATE_MENU_ITEMS+".back", "&c&lBack", "","&bClick here to go back");
        }

        msg.addDefault(ConfigData.SPECTATE_ITEMS, new String[]{});

        if (isFirstTime()) {
            saveItem(ConfigData.SPECTATE_ITEMS+".teleporter", "&aTeleporter &7[Right-click]", "","&bClick to open teleporter menu.");
            saveItem(ConfigData.SPECTATE_ITEMS+".leave", "&cLeave &7[Right-click]", "","&bClick to leave.");
        }

        msg.addDefault(TELEPORTER_MENU_SETTINGS_TITLE, "&7Teleporter");
        saveItem(TELEPORTER_MENU_PLAYER_HEAD, "&9{player}", "","&bClick to teleport.");

        saveItem("drawer-tools.thin-brush", "&6&lThin Brush", "","&7Paints a one pixel line.");
        saveItem("drawer-tools.thick-brush", "&e&lThick Brush", "","&7Paints a three pixel dot.");
        saveItem("drawer-tools.spray-canvas", "&b&lSpray Canvas", "","&7Splatters the canvas.");
        saveItem("drawer-tools.fill-can", "&d&lFill Can", "","&7Fills are with selected color.");
        saveItem("drawer-tools.burn-canvas", "&c&lBurn Canvas", "","&7Clears the canvas.");

        msg.addDefault(BOARD_LOBBY_TITLE, "&b&lDraw&a&lIt");
        msg.addDefault(BOARD_LOBBY_LINES, new String[]{
                "",
                "&a&lPlayer",
                "&7{player}",
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

        msg.addDefault(CHAT_FORMAT+".lobby", "&e{points} &8▍ {pointFormat} &7{player} &8» &7{message}");
        msg.addDefault(CHAT_FORMAT+".waiting", "&e{points} &8▍ {pointFormat} &7{player} &8» &7{message}");
        msg.addDefault(CHAT_FORMAT+".game", "{pointFormat} &7{player} &8» &7{message}");
        msg.addDefault(CHAT_FORMAT+".spectator", "&e{points} &8▍ &4SPEC &8▏ {pointFormat} &7{player} &8» &7{message}");

        save();
    }

    public void saveItem(String path, String displayName, String... lore) {
        getConfig().addDefault(path+".display-name", displayName);
        getConfig().addDefault(path+".lore", lore);
    }

    public static String PREFIX = "prefix";

    public static String DRAWIT_COMMANDS_PLAYER = "player-commands";

    public static String PLAYER_JOIN = "player-join";
    public static String PLAYER_QUIT = "player-quit";
    public static String FULL_GAME = "full-game";
    public static String IN_GAME = "in-game";
    public static String NOT_IN_GAME = "not-in-game";
    public static String NOT_SPECTATING = "not-spectating";
    public static String GAME_NOT_FOUND = "game-not-found";
    public static String GAME_NOT_ENABLED = "game-not-enabled";
    public static String NEXT_ROUND = "next-round";
    public static String QUIT_MID_GAME = "quit-mid-game";
    public static String WORD_ANNOUNCE = "word-announce";
    public static String EVERYONE_GOT_THE_WORD = "everyone-got-word";
    public static String PLAYER_GUESSED = "player-guessed";
    public static String DRAWER_CHAT_LOCK = "drawer-chat-lock";
    public static String QUICK_JOIN_GAME_NOT_FOUND = "quick-join-game-not-found";

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

    public static String GAME_MENU_SETTINGS_TITLE = "games-menu.settings.title";
    public static String SPECTATE_MENU_SETTINGS_TITLE = "spectate-menu.settings.title";

    public static String WORD_CHOOSE_MENU_SETTINGS_TITLE = "word-choose-menu.settings.title";

    public static String TELEPORTER_MENU_SETTINGS_TITLE = "teleporter.settings.title";
    public static String TELEPORTER_MENU_PLAYER_HEAD = "teleporter.player-head";

    public static String BOARD_LOBBY_TITLE = "scoreboards.lobby.title";
    public static String BOARD_LOBBY_LINES = "scoreboards.lobby.lines";
    public static String BOARD_GAME_TITLE = "scoreboards.game.title";
    public static String BOARD_GAME_LINES = "scoreboards.game.lines";

    public static String CHAT_FORMAT = "chat-format";

}
