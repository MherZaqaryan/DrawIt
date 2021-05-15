package me.MrIronMan.drawit.data;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.api.data.DataManager;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.api.game.GameState;
import me.MrIronMan.drawit.api.game.DrawerTool;
import me.MrIronMan.drawit.database.PlayerData;
import me.MrIronMan.drawit.database.PlayerDataType;
import me.MrIronMan.drawit.utility.MaterialUtil;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ConfigData extends DataManager {

    public ConfigData(Plugin plugin, String dir, String name) {
        super(plugin, dir, name);
        YamlConfiguration config = getConfig();

        config.options().header("DrawIt Mini-game by MrIronMan (Spigot: Mher) Version: " + DrawIt.getInstance().getDescription().getVersion());
        config.addDefault("lobby-server", "lobby");
        config.addDefault("mysql.enabled", false);
        config.addDefault("mysql.host", "localhost");
        config.addDefault("mysql.port", "3306");
        config.addDefault("mysql.database", "drawit");
        config.addDefault("mysql.username", "root");
        config.addDefault("mysql.password", "pass");

        config.addDefault(CHAT_FORMAT, true);

        config.addDefault(COUNTDOWN_STARTING, 20);
        config.addDefault(COUNTDOWN_WORD_CHOOSE, 10);
        config.addDefault(COUNTDOWN_PER_ROUND, 70);
        config.addDefault(COUNTDOWN_AFTER_ROUND, 3);
        config.addDefault(COUNTDOWN_RESTART, 10);

        config.addDefault(SOUND_UNDER_5, "CLICK,1,1");
        config.addDefault(SOUND_DRAWER_WORD_CHOOSE, "SUCCESSFUL_HIT,1,0");
        config.addDefault(SOUND_SPRAY_CANVAS, "DIG_GRASS,0.5,0.5");
        config.addDefault(SOUND_COLOR_PICK, "SUCCESSFUL_HIT,1,1");
        config.addDefault(SOUND_LETTER_EXPLAIN, "CHICKEN_EGG_POP,1,2");
        config.addDefault(SOUND_LESS_TIME, "BLOCK_NOTE_BASS,1,1");
        config.addDefault(SOUND_WORD_GUESS, "ANVIL_LAND,1,1");
        config.addDefault(SOUND_GAME_OVER, "WITHER_DEATH,1,1");

        config.addDefault(LOBBY_ITEMS, new String[]{});

        if (isFirstTime()) {
            saveItem(LOBBY_ITEMS+".game-selector", MaterialUtil.getMaterial(XMaterial.CHEST), false, 0, "drawit menu games");
            saveItem(LOBBY_ITEMS+".return-to-lobby", MaterialUtil.getMaterial(XMaterial.SLIME_BALL), false, 8, "drawit leave");
        }

        config.addDefault(WAITING_ITEMS, new String[]{});

        if (isFirstTime()) {
            saveItem(WAITING_ITEMS+".leave", MaterialUtil.getMaterial(XMaterial.SLIME_BALL), false, 8, "drawit leave");
        }

        config.addDefault(SPECTATE_ITEMS, new String[]{});

        if (isFirstTime()) {
            saveItem(SPECTATE_ITEMS+".teleporter", MaterialUtil.getMaterial(XMaterial.COMPASS), false, 0, "di menu teleporter");
            saveItem(SPECTATE_ITEMS+".leave", MaterialUtil.getMaterial(XMaterial.SLIME_BALL), false, 8, "di leave");
        }

        config.addDefault(GAMES_MENU_SETTINGS_SIZE, 45);
        config.addDefault(GAMES_MENU_SETTINGS_SLOTS, new Integer[]{28, 29, 30, 31, 32, 33, 34});
        config.addDefault(GAMES_MENU_SETTINGS_SHOW_PLAYINGS, false);

        saveItem(GAMES_MENU_SETTINGS_WAITING, MaterialUtil.getMaterial(XMaterial.LIME_TERRACOTTA), false);
        saveItem(GAMES_MENU_SETTINGS_STARTING, MaterialUtil.getMaterial(XMaterial.YELLOW_TERRACOTTA), false);
        saveItem(GAMES_MENU_SETTINGS_PLAYING, MaterialUtil.getMaterial(XMaterial.RED_TERRACOTTA), false);
        saveItem(GAMES_MENU_SETTINGS_WAITING_SLOTS, MaterialUtil.getMaterial(XMaterial.GRAY_STAINED_GLASS_PANE), false);

        config.addDefault(GAMES_MENU_ITEMS, new String[]{});

        if (isFirstTime()) {
            saveItem(GAMES_MENU_ITEMS+".spectate-game", MaterialUtil.getMaterial(XMaterial.MAGMA_CREAM), false, 12, "drawit menu spectate");
            saveItem(GAMES_MENU_ITEMS+".quick-join", MaterialUtil.getMaterial(XMaterial.MINECART), false, 14, "drawit quickjoin");
        }

        config.addDefault(SELECT_WORD_MENU_SETTINGS_SIZE, 27);
        config.addDefault(SELECT_WORD_MENU_SETTINGS_SLOTS, new Integer[]{11, 13, 15});
        saveItem(SELECT_WORD_MENU_WORD_ITEM, MaterialUtil.getMaterial(XMaterial.PAPER), false);

        config.addDefault(SPECTATE_MENU_SIZE, 45);
        config.addDefault(SPECTATE_MENU_SLOTS, new Integer[]{10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34});
        saveItem(SPECTATE_MENU_GAME, MaterialUtil.getMaterial(XMaterial.MAGMA_CREAM), false);

        config.addDefault(SPECTATE_MENU_ITEMS, new String[]{});

        if (isFirstTime()) {
            saveItem(SPECTATE_MENU_ITEMS+".back", MaterialUtil.getMaterial(XMaterial.ARROW), false, 36, "drawit menu games");
        }

        saveItem("drawer-tools.thin-brush", MaterialUtil.getMaterial(XMaterial.WOODEN_SWORD), false, 0);
        saveItem("drawer-tools.thick-brush", MaterialUtil.getMaterial(XMaterial.DIAMOND_SWORD), false, 1);
        saveItem("drawer-tools.spray-canvas", MaterialUtil.getMaterial(XMaterial.SHEARS), false, 2);
        saveItem("drawer-tools.fill-can", MaterialUtil.getMaterial(XMaterial.BUCKET), false, 3);
        saveItem("drawer-tools.burn-canvas", MaterialUtil.getMaterial(XMaterial.BLAZE_POWDER), false, 8);

        config.addDefault(COLOR_PICKER+".settings.size", 54);
        config.addDefault(COLOR_PICKER+".items", new String[]{"" +
                "0; &7Orange; " + MaterialUtil.getMaterial(XMaterial.ORANGE_WOOL),
                "1; &7Magenta; " + MaterialUtil.getMaterial(XMaterial.MAGENTA_WOOL),
                "2; &7Light Blue; " + MaterialUtil.getMaterial(XMaterial.LIGHT_BLUE_WOOL),
                "3; &7Yellow; " + MaterialUtil.getMaterial(XMaterial.YELLOW_WOOL),
                "4; &7Lime; " + MaterialUtil.getMaterial(XMaterial.LIME_WOOL),
                "5; &7Cyan; " + MaterialUtil.getMaterial(XMaterial.CYAN_WOOL),
                "6; &7Purple; " + MaterialUtil.getMaterial(XMaterial.PURPLE_WOOL),
                "7; &7Blue; " + MaterialUtil.getMaterial(XMaterial.BLUE_WOOL),
                "8; &7Green; " + MaterialUtil.getMaterial(XMaterial.GREEN_WOOL),
                "9; &7Red; " + MaterialUtil.getMaterial(XMaterial.RED_WOOL),
                "10; &7Pastel Orange; " + MaterialUtil.getMaterial(XMaterial.ORANGE_TERRACOTTA),
                "11; &7Pastel Magenta; "+ MaterialUtil.getMaterial(XMaterial.MAGENTA_TERRACOTTA),
                "12; &7Pastel Light Blue; "+ MaterialUtil.getMaterial(XMaterial.LIGHT_BLUE_TERRACOTTA),
                "13; &7Pastel Yellow; "+ MaterialUtil.getMaterial(XMaterial.YELLOW_TERRACOTTA),
                "14; &7Pastel Lime; "+ MaterialUtil.getMaterial(XMaterial.LIME_TERRACOTTA),
                "15; &7Pastel Cyan; "+ MaterialUtil.getMaterial(XMaterial.CYAN_TERRACOTTA),
                "16; &7Pastel Purple; "+ MaterialUtil.getMaterial(XMaterial.PURPLE_TERRACOTTA),
                "17; &7Pastel Blue; "+ MaterialUtil.getMaterial(XMaterial.BLUE_TERRACOTTA),
                "18; &7Pastel Green; "+ MaterialUtil.getMaterial(XMaterial.GREEN_TERRACOTTA),
                "19; &7Pastel Red; "+ MaterialUtil.getMaterial(XMaterial.RED_TERRACOTTA),
                "20; &7Gold; "+MaterialUtil.getMaterial(XMaterial.GOLD_BLOCK),
                "21; &7Diamond; "+MaterialUtil.getMaterial(XMaterial.DIAMOND_BLOCK),
                "22; &7Redstone; "+MaterialUtil.getMaterial(XMaterial.REDSTONE_BLOCK),
                "23; &7Sponge; "+MaterialUtil.getMaterial(XMaterial.SPONGE),
                "27; &7Pink; "+MaterialUtil.getMaterial(XMaterial.PINK_WOOL),
                "29; &7Emerald; "+MaterialUtil.getMaterial(XMaterial.EMERALD_BLOCK),
                "31; &7Black; "+MaterialUtil.getMaterial(XMaterial.BLACK_WOOL),
                "32; &7Brown; "+MaterialUtil.getMaterial(XMaterial.BROWN_WOOL),
                "33; &7Gray; "+MaterialUtil.getMaterial(XMaterial.GRAY_WOOL),
                "34; &7Light Gray; "+MaterialUtil.getMaterial(XMaterial.LIGHT_GRAY_WOOL),
                "35; &7White; "+MaterialUtil.getMaterial(XMaterial.WHITE_WOOL),
                "36; &7Pastel Pink; "+ MaterialUtil.getMaterial(XMaterial.PINK_TERRACOTTA),
                "38; &7Iron; "+MaterialUtil.getMaterial(XMaterial.IRON_BLOCK),
                "40; &7Pastel Black; "+ MaterialUtil.getMaterial(XMaterial.BLACK_TERRACOTTA),
                "41; &7Pastel Brown; "+ MaterialUtil.getMaterial(XMaterial.BROWN_TERRACOTTA),
                "42; &7Pastel Gray; "+ MaterialUtil.getMaterial(XMaterial.GRAY_TERRACOTTA),
                "43; &7Pastel Light Gray; "+ MaterialUtil.getMaterial(XMaterial.LIGHT_GRAY_TERRACOTTA),
                "44; &7Pastel White; "+ MaterialUtil.getMaterial(XMaterial.WHITE_TERRACOTTA)
        });

        config.addDefault(STATS_POINTS_DRAWER_PER_RIGHT_WORD, 2);

        if (isFirstTime()) {
            config.addDefault(STATS_POINTS_GUESSER+".1", 10);
            config.addDefault(STATS_POINTS_GUESSER+".2", 8);
            config.addDefault(STATS_POINTS_GUESSER+".3", 6);
            config.addDefault(STATS_POINTS_GUESSER+".4", 4);
        }

        config.addDefault(STATS_POINTS_GUESSER_OTHER, 2);

        config.addDefault(PLAYER_RANKING +".0-250.format", "&7Finger Painter");
        config.addDefault(PLAYER_RANKING +".250-1000.format", "&6Crayon Set");
        config.addDefault(PLAYER_RANKING +".1000-2500.format", "&dPaint Brush");
        config.addDefault(PLAYER_RANKING +".2500-5000.format", "&bRaphael");
        config.addDefault(PLAYER_RANKING +".5000-7500.format", "&5Kandinsky");
        config.addDefault(PLAYER_RANKING +".7500-12500.format", "&aRembrandt");
        config.addDefault(PLAYER_RANKING +".12500-20000.format", "&cManet");
        config.addDefault(PLAYER_RANKING +".20000-40000.format", "&9Warhol");
        config.addDefault(PLAYER_RANKING +".40000-70000.format", "&5Dali");
        config.addDefault(PLAYER_RANKING +".70000-100000.format", "&6&lMonet");
        config.addDefault(PLAYER_RANKING +".100000-150000.format", "&b&lMondrian");
        config.addDefault(PLAYER_RANKING +".150000-200000.format", "&3&lLichenstein");
        config.addDefault(PLAYER_RANKING +".200000-250000.format", "&e&lMichelangelo");
        config.addDefault(PLAYER_RANKING +".250000-300000.format", "&e&lLeonardo da Vinci");
        config.addDefault(PLAYER_RANKING +".other.format", "&a&lVincent van Gogh");

        config.options().copyDefaults(true);
        save();
    }

    public void saveItem(String path, String material, boolean enchanted, int slot, String cmd) {
        saveItem(path, material, enchanted, slot);
        getConfig().addDefault(path+".command", cmd);
    }

    public void saveItem(String path, String material, boolean enchanted, int slot) {
        saveItem(path, material, enchanted);
        getConfig().addDefault(path+".slot", slot);
    }

    public void saveItem(String path, String material, boolean enchanted) {
        getConfig().addDefault(path+".material", material);
        getConfig().addDefault(path+".enchanted", enchanted);
    }

    public ItemStack getItem(String path) {
        path = path+".";
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(XMaterial.matchXMaterial(getString(path + "material")).get().parseItem()));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if (getBoolean(path+"enchanted")) {
            itemMeta.addEnchant(Enchantment.LUCK, 0, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (DrawIt.getMessagesData().getString(path+"display-name") == null) {
            DrawIt.getMessagesData().set(path+"display-name", "&7Display name not set");
        }
        if (DrawIt.getMessagesData().getStringList(path+"lore").isEmpty()) {
            DrawIt.getMessagesData().set(path+"lore", new String[]{"","&7Lore not set"});
        }
        itemMeta.setDisplayName(TextUtil.colorize(DrawIt.getMessagesData().getString(path+"display-name")));
        itemMeta.setLore(TextUtil.colorize(DrawIt.getMessagesData().getStringList(path+"lore")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getLobbyItem(String subPath) {
        String path = "lobby-items."+subPath;
        ItemStack itemStack = getItem(path);
        NBTItem nbti = new NBTItem(itemStack);
        nbti.setString("name", "lobby-item");
        nbti.setInteger("slot", getInt(path+".slot"));
        nbti.setString("command", getString(path+".command"));
        return nbti.getItem();
    }

    public ItemStack getDrawerTool(DrawerTool drawerTool) {
        String path = "drawer-tools."+drawerTool.getPath();
        ItemStack itemStack = getItem(path);
        NBTItem nbti = new NBTItem(itemStack);
        nbti.setString("name", "drawer-tool");
        nbti.setString("type", drawerTool.getPath());
        nbti.setBoolean("isPickable", !drawerTool.equals(DrawerTool.BURN_CANVAS));
        nbti.setInteger("slot", getInt(path+".slot"));
        return nbti.getItem();
    }

    public List<ItemStack> getGameMenuItems() {
        List<ItemStack> items = new ArrayList<>();
        if (getConfig().getConfigurationSection(GAMES_MENU_ITEMS).getKeys(false).isEmpty()) return Collections.emptyList();
        for (String s : getConfig().getConfigurationSection(GAMES_MENU_ITEMS).getKeys(false)) {
            ItemStack itemStack = getItem(GAMES_MENU_ITEMS+"."+s);
            NBTItem nbti = new NBTItem(itemStack);
            nbti.setString("name", "game-menu");
            nbti.setInteger("slot", getInt(GAMES_MENU_ITEMS+"."+s+".slot"));
            nbti.setString("command", getString(GAMES_MENU_ITEMS+"."+s+".command"));
            items.add(nbti.getItem());
        }
        return items;
    }

    public List<ItemStack> getSpectatorItems() {
        List<ItemStack> items = new ArrayList<>();
        if (getConfig().getConfigurationSection(SPECTATE_ITEMS).getKeys(false).isEmpty()) return Collections.emptyList();
        for (String s : getConfig().getConfigurationSection(SPECTATE_ITEMS).getKeys(false)) {
            ItemStack itemStack = getItem(SPECTATE_ITEMS+"."+s);
            NBTItem nbti = new NBTItem(itemStack);
            nbti.setString("name", "spectate-item");
            nbti.setInteger("slot", getInt(SPECTATE_ITEMS+"."+s+".slot"));
            nbti.setString("command", getString(SPECTATE_ITEMS+"."+s+".command"));
            items.add(nbti.getItem());
        }
        return items;
    }

    public List<ItemStack> getWaitingItems() {
        List<ItemStack> items = new ArrayList<>();
        if (getConfig().getConfigurationSection(WAITING_ITEMS).getKeys(false).isEmpty()) return Collections.emptyList();
        for (String s : getConfig().getConfigurationSection(WAITING_ITEMS).getKeys(false)) {
            ItemStack itemStack = getItem(WAITING_ITEMS+"."+s);
            NBTItem nbti = new NBTItem(itemStack);
            nbti.setString("name", "waiting-item");
            nbti.setInteger("slot", getInt(WAITING_ITEMS+"."+s+".slot"));
            nbti.setString("command", getString(WAITING_ITEMS+"."+s+".command"));
            items.add(nbti.getItem());
        }
        return items;
    }

    public ItemStack getItem(String path, Game game) {
        ItemStack item = DrawIt.getConfigData().getItem(path);
        ItemMeta itemMeta = item.getItemMeta();
        if (game != null) {
            List<String> newLore = new ArrayList<>();
            for (String s : itemMeta.getLore()) {
                newLore.add(s
                        .replace("{in}", String.valueOf(game.getUuids().size()))
                        .replace("{max}", String.valueOf(game.getMaxPlayers()))
                        .replace("{state}", GameState.getName(game.getGameState())));
            }
            itemMeta.setDisplayName(itemMeta.getDisplayName().replace("{game}", game.getDisplayName()));
            itemMeta.setLore(TextUtil.colorize(newLore));
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    public List<ItemStack> getSpectateMenuItems() {
        List<ItemStack> items = new ArrayList<>();
        for (String s : getConfig().getConfigurationSection(SPECTATE_MENU_ITEMS).getKeys(false)) {
            ItemStack itemStack = getItem(SPECTATE_MENU_ITEMS+"."+s);
            NBTItem nbti = new NBTItem(itemStack);
            nbti.setString("name", "spectate-menu");
            nbti.setInteger("slot", getInt(SPECTATE_MENU_ITEMS+"."+s+".slot"));
            nbti.setString("command", getString(SPECTATE_MENU_ITEMS+"."+s+".command"));
            items.add(nbti.getItem());
        }
        return items;
    }

    public List<ItemStack> getLobbyItems() {
        List<ItemStack> items = new ArrayList<>();
        for (String path : getConfig().getConfigurationSection("lobby-items").getKeys(false)) {
            items.add(getLobbyItem(path));
        }
        return items;
    }

    public List<String> getMySqlInfo() {
        List<String> mysqlInfo = new ArrayList<>();
        mysqlInfo.add(getString("mysql.host"));
        mysqlInfo.add(getString("mysql.port"));
        mysqlInfo.add(getString("mysql.database"));
        mysqlInfo.add(getString("mysql.username"));
        mysqlInfo.add(getString("mysql.password"));
        return mysqlInfo;
    }

    public boolean isMySql() {
        return getBoolean("mysql.enabled");
    }

    public String getPointFormat(Player player) {
        PlayerData playerData = DrawIt.getPlayerData(player);
        int points = playerData.getData(PlayerDataType.POINTS);
        for (String st : getConfig().getConfigurationSection(PLAYER_RANKING).getKeys(false)) {
            if (st.split("-").length == 2) {
                int min = Integer.parseInt(st.split("-")[0]);
                int max = Integer.parseInt(st.split("-")[1]);
                if (points >= min && points <= max) {
                    return TextUtil.colorize(getConfig().getString(PLAYER_RANKING +"."+st+".format"));
                }
            }else if (st.equals("other")) {
                return TextUtil.colorize(getConfig().getString(PLAYER_RANKING +".other.format"));
            }
        }
        return null;
    }

    public int getGameWordsCount() {
        return getIntegerList(ConfigData.SELECT_WORD_MENU_SETTINGS_SLOTS).size();
    }

    public static final String CHAT_FORMAT = "use-chat-format",

     GAMES_MENU_SETTINGS_SIZE = "games-menu.settings.size",
     GAMES_MENU_SETTINGS_SLOTS = "games-menu.settings.slots",
     GAMES_MENU_SETTINGS_WAITING = "games-menu.settings.waiting",
     GAMES_MENU_SETTINGS_STARTING = "games-menu.settings.starting",
     GAMES_MENU_SETTINGS_PLAYING = "games-menu.settings.playing",
     GAMES_MENU_SETTINGS_WAITING_SLOTS = "games-menu.settings.waiting-slots",
     GAMES_MENU_SETTINGS_SHOW_PLAYINGS = "games-menu.settings.show-playings",
     GAMES_MENU_ITEMS = "games-menu.items",

     SELECT_WORD_MENU_SETTINGS_SIZE = "select-word-menu.settings.size",
     SELECT_WORD_MENU_SETTINGS_SLOTS = "select-word-menu.settings.slots",
     SELECT_WORD_MENU_WORD_ITEM = "select-word-menu.word-item",

     SPECTATE_MENU_SIZE = "spectate-menu.settings.size",
     SPECTATE_MENU_SLOTS = "spectate-menu.settings.slots",
     SPECTATE_MENU_GAME = "spectate-menu.settings.game",
     SPECTATE_MENU_ITEMS = "spectate-menu.items",

     LOBBY_ITEMS = "lobby-items",
     WAITING_ITEMS = "waiting-items",
     SPECTATE_ITEMS = "spectate-items",

     COLOR_PICKER = "color-picker",
     LOBBY_LOCATION = "lobby-location",
     LOBBY_SERVER = "lobby-server",

     COUNTDOWN_STARTING = "countdowns.starting",
     COUNTDOWN_WORD_CHOOSE = "countdowns.word-choose",
     COUNTDOWN_PER_ROUND = "countdowns.per-round",
     COUNTDOWN_AFTER_ROUND = "countdowns.after-round",
     COUNTDOWN_RESTART = "countdowns.restart",

     SOUND_UNDER_5 = "sounds.starting-under-5",
     SOUND_DRAWER_WORD_CHOOSE = "sounds.word-choose",
     SOUND_SPRAY_CANVAS = "sounds.spray-canvas",
     SOUND_LETTER_EXPLAIN = "sounds.letter-explain",
     SOUND_WORD_GUESS = "sounds.word-guess",
     SOUND_GAME_OVER = "sounds.game-over",
     SOUND_COLOR_PICK = "sounds.color-pick",
     SOUND_LESS_TIME = "sounds.less-time",

     STATS_POINTS_DRAWER_PER_RIGHT_WORD = "stats.points.drawer.per-right-word",
     STATS_POINTS_GUESSER = "stats.points.guesser",
     STATS_POINTS_GUESSER_OTHER = "stats.points.guesser.other",

     PLAYER_RANKING = "player-ranking";

}
