package me.MrIronMan.drawit.menu.menus;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.menu.UniqueMenu;
import me.MrIronMan.drawit.menu.PlayerMenuUtility;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WordChooseMenu extends UniqueMenu {

    public boolean isChooses = false;
    private Game game;
    private List<String> words;
    private HashMap<Integer, String> wordMap;
    private Integer[] slots = DrawIt.getConfigData().getIntegerList(ConfigData.SELECT_WORD_MENU_SETTINGS_SLOTS).toArray(new Integer[0]);

    public WordChooseMenu(PlayerMenuUtility playerMenuUtility, Game game) {
        super(playerMenuUtility);
        this.game = game;
        this.words = game.getGameManager().getWordsForPlayer();
        this.wordMap = new HashMap<>();
    }

    @Override
    public String getMenuName() {
        return DrawIt.getMessagesData().getString(MessagesData.WORD_CHOOSE_MENU_SETTINGS_TITLE);
    }

    @Override
    public int getSlots() {
        return DrawIt.getConfigData().getInt(ConfigData.SELECT_WORD_MENU_SETTINGS_SIZE);
    }

    @Override
    public void handleClose(InventoryCloseEvent e) {
        if (!isChooses) {
            Bukkit.getServer().getScheduler().runTaskLater(DrawIt.getInstance(), this::open, 10);
        }
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!wordMap.containsKey(e.getSlot())) return;
        isChooses = true;
        player.closeInventory();
        game.getGameManager().setWord(wordMap.get(e.getSlot()));
    }

    @Override
    public void setMenuItems() {
        for (int i = 0; i < slots.length; i++) {
            inventory.setItem(slots[i], getItem(words.get(i)));
            wordMap.put(slots[i], words.get(i));
        }
    }

    public void chooseRandomWord() {
        isChooses = true;
        playerMenuUtility.getPlayer().closeInventory();
        game.getGameManager().setWord(words.get(new Random().nextInt(words.size())));
    }

    public ItemStack getItem(String word) {
        ItemStack item = DrawIt.getConfigData().getItem(ConfigData.SELECT_WORD_MENU_WORD_ITEM);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(TextUtil.colorize(itemMeta.getDisplayName().replace("{word}", word)));
        List<String> newLore = new ArrayList<>();
        itemMeta.getLore().forEach(s -> newLore.add(TextUtil.colorize(s.replace("{word}", word))));
        itemMeta.setLore(newLore);
        item.setItemMeta(itemMeta);
        return item;
    }

}
