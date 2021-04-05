package me.MrIronMan.drawit.menuSystem.menus;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.menuSystem.UniqueMenu;
import me.MrIronMan.drawit.menuSystem.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WordChooseMenu extends UniqueMenu {

    public boolean isChooses = false;
    private Game game;
    private List<String> words;
    private HashMap<Integer, String> wordMap;
    private Integer[] slots = {11, 13, 15};

    public WordChooseMenu(PlayerMenuUtility playerMenuUtility, Game game) {
        super(playerMenuUtility);
        this.game = game;
        this.words = game.getGameManager().getWordsForPlayer();
        this.wordMap = new HashMap<>();
    }

    @Override
    public String getMenuName() {
        return "&dChoose The Word";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleClose(InventoryCloseEvent e) {
        if (!isChooses) {
            Bukkit.getScheduler().runTask(DrawIt.getInstance(), this::open);
        }
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (wordMap.containsKey(e.getSlot())) {
            isChooses = true;
            player.closeInventory();
            game.getGameManager().setWord(wordMap.get(e.getSlot()));
        }
    }

    @Override
    public void setMenuItems() {
        for (int i = 0; i < slots.length; i++) {
            inventory.setItem(slots[i], makeItem(Material.PAPER, "&a"+words.get(i)));
            wordMap.put(slots[i], words.get(i));
        }
    }

    public void chooseRandomWord() {
        isChooses = true;
        playerMenuUtility.getPlayer().closeInventory();
        game.getGameManager().setWord(words.get(new Random().nextInt(words.size())));
    }

}
