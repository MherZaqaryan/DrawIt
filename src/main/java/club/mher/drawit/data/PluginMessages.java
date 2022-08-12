package club.mher.drawit.data;

import club.mher.drawit.utility.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class PluginMessages {

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

    public static TextComponent[] DRAWIT_COMMANDS = {
            simpleText("&3&l&m----------------------------"),
            simpleText(""),
            simpleHover("  &b&lDraw&a&lIt &e&lCommands", "&aAuthor: &7Mher Zaqaryan"),
            simpleText(""),
            commandRun("  &c• &7/DrawIt SetMainLobby", "&7Set main lobby location.", "/DrawIt SetMainLobby"),
            commandSuggest("  &c• &7/DrawIt Setup (World|Exit)", "&7Start setup a new or edit game or exit from current.", "/DrawIt Setup "),
            commandSuggest("  &c• &7/DrawIt Delete (Game)", "&7Delete specified game.", "/DrawIt Delete "),
            commandSuggest("  &c• &7/DrawIt Join (Game)", "&7Join a specified game.", "/DrawIt Join "),
            commandSuggest("  &c• &7/DrawIt Leave", "&7Leave from the current game (Connect to lobby server).", "/DrawIt Leave"),
            commandSuggest("  &c• &7/DrawIt Menu (Games|Spectate|Teleporter)", "&7Open the games, spectate games or teleporter menu.", "/DrawIt Menu "),
            commandRun("  &c• &7/DrawIt Start", "&7Force start the game when there is not enough players.", "/DrawIt Start"),
            commandRun("  &c• &7/DrawIt Reload", "&7Reload all configuration files (Config.yml, Messages.yml, Words.yml).", "/DrawIt Reload"),
            commandRun("  &c• &7/DrawIt BuildMode", "&7Enable/disable build mode.", "/DrawIt BuildMode"),
            commandSuggest("  &c• &7/DrawIt AddPoints (Player) (Amount)", "&7Add player points.", "/DrawIt AddPoints <Player> <Amount>"),
            simpleText(""),
            simpleText("&3&l&m----------------------------"),
    };

    public static String USAGE_COMMAND_MENU = "{prefix} &cUsage: &7/DrawIt Menu (Games|Spectate|Teleporter)";
    public static String USAGE_COMMAND_SETUP = "{prefix} &cUsage: &7/DrawIt Setup (World|Exit)";
    public static String USAGE_COMMAND_SETBOARD = "{prefix} &cUsage: &7/DrawIt SetBoard (Pos1|Pos2)";
    public static String USAGE_COMMAND_DELETE = "{prefix} &cUsage: &7/DrawIt Remove (Game)";
    public static String USAGE_COMMAND_JOIN = "{prefix} &cUsage: &7/DrawIt Join (Game)";
    public static String USAGE_COMMAND_ADD_POINTS = "{prefix} &cUsage: &7&7/DrawIt AddPoints (Player) (Amount)";

    public static String BUILDMODE_ENABLED = "{prefix} &aBuild mode has been enabled.";
    public static String BUILDMODE_DISABLED = "{prefix} &cBuild mode has been disabled.";
    public static String NOT_IN_SETUP = "{prefix} &cYou are not setting game.";
    public static String ALREADY_IN_SETUP = "{prefix} &cPlease finish your setup first.";
    public static String EXIT_SETUP = "{prefix} &cYou have left from setup.";
    public static String SETUP_LOBBY_NOT_SET = "{prefix} &cPlease set main lobby location first.";
    public static String LOBBY_NOT_SET = "{prefix} &cMain lobby location not set.";
    public static String GAME_REMOVED = "{prefix} &e%game% &aSuccessfully removed.";
    public static String DISABLE_GAME = "{prefix} &cPlease disable this game first.";
    public static String SUBCOMMAND_NOT_FOUND = "{prefix} &cSub-command not found, type /drawit for all list of commands.";
    public static String CONFIG_RELOADED = "{prefix} &aConfiguration files has been successfully reloaded.";

    public static String POINTS_NOT_NUMBER = "{prefix} &cAmount must be number.";
    public static String POINTS_NOT_POSITIVE = "{prefix} &cAmount must be positive";
    public static String POINTS_ADDED = "{prefix} &a{player} &ePoints has been added by &a{points}.";

    public static String PLAYER_NOT_FOUND = "{prefix} &cPlayer not found.";
    public static String SET_LOBBY_IN_GAME = "{prefix} &cYou cant set main lobby location while in game.";


}
