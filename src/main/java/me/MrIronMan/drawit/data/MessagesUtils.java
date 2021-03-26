package me.MrIronMan.drawit.data;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.utility.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MessagesUtils {

    private static FileConfiguration getData() {
        return DrawIt.getInstance().getMessagesFile().getConfig();
    }

    public static TextComponent commandSuggest(String name, String hoverText, String cmd) {
        TextComponent text = new TextComponent(TextUtil.colorize(name));
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TextUtil.colorize(hoverText)).create()));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
        return text;
    }

    public static TextComponent commandRun(String name, String hoverText, String cmd) {
        TextComponent text = new TextComponent(TextUtil.colorize(name));
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TextUtil.colorize(hoverText)).create()));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
        return text;
    }

    public static TextComponent simpleHover(String name, String hoverText) {
        TextComponent text = new TextComponent(TextUtil.colorize(name));
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TextUtil.colorize(hoverText)).create()));
        return text;
    }

    public static void sendMessage(Player player, TextComponent[] tc) {
        for (TextComponent t : tc) {
            player.spigot().sendMessage(t);
        }
    }

    public static TextComponent simpleText(String name) {
        return new TextComponent(TextUtil.colorize(name));
    }

    // General Messages

    public static String PREFIX = getData().getString("prefix");

    public static String PLAYER_JOIN = getData().getString("player-join");
    public static String PLAYER_QUIT = getData().getString("player-quit");
    public static String FULL_GAME = getData().getString("full-game");
    public static String IN_GAME = getData().getString("in-game");
    public static String NOT_IN_GAME = getData().getString("not-in-game");
    public static String GAME_NOT_FOUND = getData().getString("game-not-found");
    public static String GAME_NOT_ENABLED = getData().getString("game-not-enabled");
    public static String NEXT_DRAWER = getData().getString("next-drawer");
    public static String QUIT_MID_GAME = getData().getString("quit-mid-game");
    public static String WORD_ANNOUNCE = getData().getString("word-announce");
    public static String EVERYONE_GOT_THE_WORD = getData().getString("everyone-got-word");
    public static String PLAYER_GUESSED = getData().getString("player-guessed");
    public static String CHAT_WHILE_DRAWER = getData().getString("drawer-chat-message");

    public static String START_COUNTDOWN = getData().getString("action-bar-messages.start-countdown");
    public static String START_COUNTDOWN_UNDER_5 = getData().getString("action-bar-messages.start-countdown-under-5");
    public static String DRAWER = getData().getString("action-bar-messages.drawer");
    public static String PICK_WORD = getData().getString("action-bar-messages.pick-word");
    public static String START_DRAW = getData().getString("action-bar-messages.start-draw");
    public static String GUESSERS = getData().getString("action-bar-messages.guessers");
    public static String HIDING_CHARACTER = getData().getString("action-bar-messages.hiding-character");

    public static String NO_PERMS = getData().getString("no-permission");

    public static String GAME_SKIPPED = getData().getString("game-skipped");
    public static String NOT_DRAWER_TO_SKIP = getData().getString("not-drawer-to-skip");
    public static String NO_PERM_SKIP = getData().getString("skip-no-permission");

    public static String LEADER_FORMAT = getData().getString("scoreboard-leader-format");
    public static List<String> GAME_END = getData().getStringList("game-end-message");

    public static List<String> DRAWIT_COMMANDS_PLAYER = getData().getStringList("player-commands");

    // Lobby Items

    public static String getLobbyItemName(String subPath) {
        String path = "lobby-items." + subPath + ".name";
        if (getData().getString(path) == null) {
            getData().set(path, path + " Not Set");
            DrawIt.getInstance().getMessagesFile().saveConfig();
        }
        return getData().getString(path);
    }

    public static List<String> getLobbyItemLore(String subPath) {
        String path = "lobby-items." + subPath + ".lore";
        if (getData().getStringList(path) == null) {
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(path + " Not Set");
            getData().set(path, lore);
            DrawIt.getInstance().getMessagesFile().saveConfig();
        }
        return getData().getStringList(path);
    }

    // Drawer Tools

    public static String COLOR_PICKER_NAME = getData().getString("color-picker-title");

    public static String THIN_BRUSH_NAME = getData().getString("drawer-tools.thin-brush.name");
    public static List<String> THIN_BRUSH_LORE = getData().getStringList("drawer-tools.thin-brush.lore");

    public static String THICK_BRUSH_NAME = getData().getString("drawer-tools.thick-brush.name");
    public static List<String> THICK_BRUSH_LORE = getData().getStringList("drawer-tools.thick-brush.lore");

    public static String SPRAY_CAN_NAME = getData().getString("drawer-tools.spray-canvas.name");
    public static List<String> SPRAY_CAN_LORE = getData().getStringList("drawer-tools.spray-canvas.lore");

    public static String FILL_CAN_NAME = getData().getString("drawer-tools.fill-can.name");
    public static List<String> FILL_CAN_LORE = getData().getStringList("drawer-tools.fill-can.lore");

    public static String BURN_CANVAS_NAME = getData().getString("drawer-tools.burn-canvas.name");
    public static List<String> BURN_CANVAS_LORE = getData().getStringList("drawer-tools.burn-canvas.lore");

    // Scoreboards

    public static String SCOREBOARD_LOBBY_TITLE = getData().getString("scoreboards.lobby.title");
    public static List<String> SCOREBOARD_LOBBY_LINES = getData().getStringList("scoreboards.lobby.lines");

    public static String SCOREBOARD_GAME_TITLE = getData().getString("scoreboards.game.title");
    public static List<String> SCOREBOARD_GAME_LINES = getData().getStringList("scoreboards.game.lines");

    // Plugin Messages

    public static String USAGE_COMMAND_SETUP = "{prefix} &cUsage: &7/DrawIt Setup <WorldName>";
    public static String USAGE_COMMAND_SETBOARD = "{prefix} &cUsage: &7/DrawIt SetBoard <Pos1|Pos2>";
    public static String USAGE_COMMAND_DELETE = "{prefix} &cUsage: &7/DrawIt Remove <Game>";

    public static String NOT_IN_SETUP = "{prefix} &cYou are not setting game.";
    public static String ALREADY_IN_SETUP = "{prefix} &cPlease finish your setup first.";
    public static String EXIT_SETUP = "{prefix} &cYou have left from setup.";
    public static String SETUP_LOBBY_NOT_SET = "{prefix} &cPlease set main lobby location first.";
    public static String LOBBY_NOT_SET = "{prefix} &cMain lobby location not set.";
    public static String GAME_REMOVED = "{prefix} &e%game% &aSuccessfully removed.";
    public static String DISABLE_GAME = "{prefix} &cPlease disable this game first.";
    public static String SUBCOMMAND_NOT_FOUND = "{prefix} &cSub-command not found, type /drawit for all list of commands.";
    // Plugin Messages > Clickable Messages

    public static TextComponent[] DRAWIT_COMMANDS = {
            simpleText("&3&l&m----------------------------"),
            simpleText(""),
            simpleHover("  &b&lDraw&a&lIt &e&lCommands", "&aAuthor: &7MrIronMan (Spigot: Mher)"),
            simpleText(""),
            commandRun("  &c• &7/DrawIt SetMainLobby", "&7Click to set main lobby.", "/DrawIt SetMainLobby"),
            commandSuggest("  &c• &7/DrawIt Setup <WorldName>", "&7Click to setup game.", "/DrawIt Setup "),
            commandSuggest("  &c• &7/DrawIt Delete <Game>", "&7Click to delete game.", "/DrawIt Remove "),
            commandSuggest("  &c• &7/DrawIt Join <Game|Random>", "&7Click to join game.", "/DrawIt Join "),
            commandSuggest("  &c• &7/DrawIt Gui <Main|Spectate>", "&7Click to open gui.", "/DrawIt Gui "),
            commandSuggest("  &c• &7/DrawIt AddPoint <Player> <Amount>", "&7Click to add points for player.", "/DrawIt AddPoints <Player> <Amount>"),
            commandSuggest("  &c• &7/DrawIt AddTokens <Player> <Amount>", "&7Click to add tokens for player.", "/DrawIt AddTokens <Player> <Amount>"),
            simpleText(""),
            simpleText("&3&l&m----------------------------"),
    };



}
