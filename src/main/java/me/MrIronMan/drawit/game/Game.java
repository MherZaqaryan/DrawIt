package me.MrIronMan.drawit.game;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.game.utility.Cuboid;
import me.MrIronMan.drawit.utility.OtherUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class Game {

    private GameManager gameManager;

    private YamlConfiguration gameFile;

    private String name;
    private String displayName;
    private World world;
    private int minPlayers;
    private int maxPlayers;
    private Location lobbyLocation;
    private Location drawerLocation;
    private Cuboid board;
    private int round;

    private boolean enabled;

    private List<UUID> players;
    private List<UUID> spectators;
    private List<String> words;
    private HashMap<UUID, ItemStack> playerColorMap;

    private List<BukkitRunnable> tasks;
    private GameState gameState = GameState.WAITING;

    public Game(String name) {
        this.name = name;
        this.gameFile = YamlConfiguration.loadConfiguration(new File(DrawIt.getInstance().getDataFolder() + File.separator + "Games", name+".yml"));
        this.world = Bukkit.createWorld(new WorldCreator(name));
        this.displayName = gameFile.getString("display-name");
        this.enabled = gameFile.getBoolean("enabled");
        this.minPlayers = gameFile.getInt("min-players");
        this.maxPlayers = gameFile.getInt("max-players");
        this.words = OtherUtils.getWords(maxPlayers*3);
        this.gameManager = new GameManager(this);
        this.lobbyLocation = readLocation(gameFile.getString("locations.lobby"));
        this.drawerLocation = readLocation(gameFile.getString("locations.drawer"));
        this.board = new Cuboid(readLocation(gameFile.getString("locations.board-pos1")),
               readLocation(gameFile.getString("locations.board-pos2")));
        this.round = 0;
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.playerColorMap = new HashMap<>();
        this.tasks = new ArrayList<>();
        this.board.burn();
        DrawIt.getInstance().getLogger().info("Loaded Game: " + name);
    }


    public GameManager getGameManager() {
        return gameManager;
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
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

    public List<UUID> getPlayers() {
        return players;
    }

    public boolean isGameState(GameState gameState){
        return this.gameState == gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Cuboid getBoard() {
        return board;
    }

    public List<BukkitRunnable> getTasks() {
        return tasks;
    }

    public ItemStack getPlayerColor(UUID uuid) {
        if (playerColorMap.containsKey(uuid)) {
            return playerColorMap.get(uuid);
        }else {
            return new ItemStack(Material.WOOL, 1, (byte) 15);
        }
    }

    public List<String> getWords() {
        return words;
    }

    public void setPlayerColor(UUID uuid, ItemStack itemStack) {
        playerColorMap.put(uuid, itemStack);
    }

    public YamlConfiguration getGameFile() {
        return gameFile;
    }

    public void addRound() {
        ++this.round;
    }

    public int getRound() {
        return round;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setSpectator(UUID uuid) {
        if (!spectators.contains(uuid)) this.spectators.add(uuid);
    }

    public boolean isSpectator(UUID uuid) {
        return this.spectators.contains(uuid);
    }

    public void removeSpectator(UUID uuid) {
        if (!spectators.contains(uuid)) return;
        spectators.remove(uuid);
    }

    // Util

    public Location readLocation(String loc) {
        String[] args = loc.split(", ");
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double z = Double.parseDouble(args[2]);
        if (args.length == 3) {
            return new Location(this.getWorld(), x, y, z);
        } else if (args.length == 5) {
            float yaw = Float.parseFloat(args[3]);
            float pitch = Float.parseFloat(args[4]);
            return new Location(this.getWorld(), x, y, z, yaw, pitch);
        } else {
            return null;
        }
    }

}
