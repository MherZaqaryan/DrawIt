package me.MrIronMan.drawit.menuSystem.menus;

import com.cryptomorin.xseries.XMaterial;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.game.SetupGame;
import me.MrIronMan.drawit.menuSystem.Menu;
import me.MrIronMan.drawit.menuSystem.PlayerMenuUtility;
import me.MrIronMan.drawit.utility.OtherUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SaveGameMenu extends Menu {

    private SetupGame setupGame;

    private boolean enabled;

    private int minPlayers;
    private int maxPlayers;

    public SaveGameMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        this.setupGame = DrawIt.getInstance().getSetupGame(playerMenuUtility.getPlayer());
        this.minPlayers = setupGame.getMinPlayers();
        this.maxPlayers = setupGame.getMaxPlayers();
        this.enabled = setupGame.isEnabled();
    }

    @Override
    public String getMenuName() {
        return "&cSaving:&e "+setupGame.getName();
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getSlot() == 15) {
            if (e.getClick().equals(ClickType.LEFT)) {
                if (setupGame.getMinPlayers() < setupGame.getMaxPlayers()) {
                    minPlayers = minPlayers+1;
                }
            }else if (e.getClick().equals(ClickType.RIGHT)){
                if (setupGame.getMinPlayers() > 1) {
                    minPlayers = minPlayers-1;
                }
            }
            DrawIt.getInstance().playSound(player, Sound.CLICK, 1.0F, 1.0F);
            setMenuItems();
        }else if (e.getSlot() == 16) {
            if (e.getClick().equals(ClickType.LEFT)) {
                maxPlayers = maxPlayers+1;
            }else if (e.getClick().equals(ClickType.RIGHT)){
                if (setupGame.getMaxPlayers() > setupGame.getMinPlayers()) {
                    maxPlayers = maxPlayers-1;
                }
            }
            DrawIt.getInstance().playSound(player, Sound.CLICK, 1.0F, 1.0F);
            setMenuItems();
        }else if (e.getSlot() == 28) {
            player.closeInventory();
            DrawIt.getInstance().playSound(player, Sound.CLICK, 1.0F, 1.0F);
        }
        else if (e.getSlot() == 31) {
            enabled = !enabled;
            DrawIt.getInstance().playSound(player, Sound.CLICK, 1.0F, 1.0F);
            setMenuItems();
        }
        else if (e.getSlot() == 34) {
            DrawIt.getInstance().playSound(player, Sound.CLICK, 1.0F, 1.0F);
            player.closeInventory();
            setupGame.setMaxPlayers(maxPlayers);
            setupGame.setMinPlayers(minPlayers);
            setupGame.setEnabled(enabled);
            setupGame.save(player);
        }
    }

    @Override
    public void setMenuItems() {
        setFillerGlass();

        String[] minMaxLore = {
            "",
            "&7Right-click to add.",
            "&7Left-click to subtract."
        };

        inventory.setItem(10, makeItem(XMaterial.ACACIA_SIGN.parseMaterial(), "&aGeneral Information",
                "",
                "&eWorld Name: &6" + setupGame.getName(),
                "&eLobby Location: &6" + OtherUtils.writeLocation(setupGame.getLobbyLocation(), true),
                "&eDrawer Location: &6"+ OtherUtils.writeLocation(setupGame.getDrawerLocation(), true),
                "&eBoard Pos1: &6" + OtherUtils.writeLocation(setupGame.getBoardPos1(), false),
                "&eBoard Pos2: &6" + OtherUtils.writeLocation(setupGame.getBoardPos2(), false)));

        inventory.setItem(15, makeItem(XMaterial.REDSTONE_TORCH.parseMaterial(), "&cMin Players: " + minPlayers, minMaxLore));
        inventory.setItem(16, makeItem(XMaterial.LEVER.parseMaterial(), "&cMax Players: " + maxPlayers, minMaxLore));

        inventory.setItem(28, makeItem(XMaterial.BARRIER.parseMaterial(), "&cCancel"));
        inventory.setItem(31, enabled ? makeItem(XMaterial.LIME_DYE.parseItem(), "&aEnabled", "","&7Click to disable.") : makeItem(XMaterial.RED_DYE.parseItem(), "&cDisabled", "","&7Click to enable."));
        inventory.setItem(34, makeItem(XMaterial.BEACON.parseMaterial(), "&3Save & Close"));

    }

}
