package club.mher.drawit.game;

import club.mher.drawit.DrawIt;
import club.mher.drawit.data.PluginMessages;
import club.mher.drawit.utility.TextUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class SetupGame {

    private World world;
    private String name;
    private String displayName;

    private boolean enabled;

    private int minPlayers;
    private int maxPlayers;
    private Location lobbyLocation;
    private Location drawerLocation;
    private Location boardPos1;
    private Location boardPos2;

    private File gamesFolder;
    private File file;

    private Game game = null;

    public SetupGame(String name) {
        this.name = name;
        this.world = Bukkit.getWorld(name);
        this.displayName = name;
        this.enabled = false;
        this.minPlayers = 6;
        this.maxPlayers = 10;
        loadFiles();
    }

    public SetupGame(Game game) {
        this.game = game;
        this.name = game.getName();
        this.displayName = game.getDisplayName();
        this.enabled = game.isEnabled();
        this.minPlayers = game.getMinPlayers();
        this.maxPlayers = game.getMaxPlayers();
        this.lobbyLocation = game.getLobbyLocation();
        this.drawerLocation = game.getDrawerLocation();
        this.boardPos1 = game.getBoard().getLoc1();
        this.boardPos2 = game.getBoard().getLoc2();
        loadFiles();
    }

    public void loadFiles() {
        this.gamesFolder = new File(DrawIt.getInstance().getDataFolder() + File.separator + "Games");
        this.file = new File(gamesFolder, name+".yml");
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    public void setDrawerLocation(Location drawerLocation) {
        this.drawerLocation = drawerLocation;
    }

    public void setBoardPos1(Location boardPos1) {
        this.boardPos1 = boardPos1;
    }

    public void setBoardPos2(Location boardPos2) {
        this.boardPos2 = boardPos2;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public Location getDrawerLocation() {
        return drawerLocation;
    }

    public Location getBoardPos1() {
        return boardPos1;
    }

    public Location getBoardPos2() {
        return boardPos2;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public World getWorld() {
        return world;
    }

    public void save(Player player) {
        if (this.lobbyLocation == null) {
            player.sendMessage(customize("{game} &cLobby location not set."));
        }else if (this.drawerLocation == null) {
            player.sendMessage(customize("{game} &cDrawer location not set."));
        }else if (this.boardPos1 == null) {
            player.sendMessage(customize("{game} &cBoard position 1 not set."));
        }else if (this.boardPos2 == null) {
            player.sendMessage(customize("{game} &cBoard position 2 not set."));
        }else {
            player.sendMessage(customize("{game} &aSaving game, please wait..."));
            if (createGameFile()) {
                YamlConfiguration gameFile = YamlConfiguration.loadConfiguration(file);
                gameFile.set("display-name", displayName);
                gameFile.set("enabled", enabled);
                gameFile.set("min-players", minPlayers);
                gameFile.set("max-players", maxPlayers);
                gameFile.set("locations.lobby", writeLocation(lobbyLocation, true));
                gameFile.set("locations.drawer", writeLocation(drawerLocation, true));
                gameFile.set("locations.board-pos1", writeLocation(boardPos1, false));
                gameFile.set("locations.board-pos2", writeLocation(boardPos2, false));
                gameFile.set("advanced-settings.board-color", "WOOL:0");
                try {
                    gameFile.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (DrawIt.getInstance().getGames().contains(DrawIt.getInstance().getGame(name))) {
                    DrawIt.getInstance().restartGame(name);
                }else {
                    DrawIt.getInstance().registerGame(name);
                }
                player.sendMessage(customize("{game} &eSuccessfully saved."));
                DrawIt.getInstance().exitSetupMode(player);
            }else {
                player.sendMessage(customize("{game} &cSomething went wrong please check the console."));
            }
        }
    }

    public void remove() {
        DrawIt.getInstance().getGames().remove(this.game);
        file.delete();
    }

    public boolean createGameFile() {
        if (!gamesFolder.exists()) {
            gamesFolder.mkdir();
        }
        try {
            file.createNewFile();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String customize(String s) {
        return TextUtil.colorize(s.replace("{game}", "&a["+name+"&a]"));
    }

    public String writeLocation(Location loc, boolean advanced) {
        if (loc == null) return "N/A";
        String regex = ", ";
        DecimalFormat df = new DecimalFormat("##.#");
        String x = df.format(loc.getX());
        String y = df.format(loc.getY());
        String z = df.format(loc.getZ());
        String yaw = df.format(loc.getYaw());
        String pitch = df.format(loc.getPitch());
        return advanced ? x + regex + y + regex + z + regex + yaw + regex + pitch : x + regex + y + regex + z;
    }

    public TextComponent[] getCurrentMessage() {
        return new TextComponent[] {
                PluginMessages.simpleText("&3&l&m----------------------------"),
                PluginMessages.simpleText(""),
                PluginMessages.simpleHover("  &b&lDraw&a&lIt &e&lSetup", "&aAuthor: &7Mher Zaqaryan"),
                PluginMessages.simpleText(""),
                PluginMessages.commandRun("  &c• &7/DrawIt SetLobby " + (lobbyLocation == null ? "" : "&8(Set)"), "&7Set game lobby location.", "/DrawIt SetLobby"),
                PluginMessages.commandRun("  &c• &7/DrawIt SetDrawer " + (drawerLocation == null ? "" : "&8(Set)"), "&7Set game drawer location.", "/DrawIt SetDrawer"),
                PluginMessages.commandSuggest("  &c• &7/DrawIt SetBoard <Pos1|Pos2> " + (boardPos1 != null && boardPos2 != null ? "&8(Set)" : ""), "&7Set game board positions.", "/DrawIt SetBoard Pos"),
                PluginMessages.commandSuggest("  &c• &7/DrawIt Setup Exit", "&7Exit from current setup.", "/DrawIt Setup Exit"),
                PluginMessages.commandRun("  &c• &7/DrawIt Save", "&7Save current game.", "/DrawIt Save"),
                PluginMessages.simpleText(""),
                PluginMessages.simpleText("&3&l&m----------------------------"),
        };
    }

}
