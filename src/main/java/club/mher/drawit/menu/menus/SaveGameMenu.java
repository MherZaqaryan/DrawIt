package club.mher.drawit.menu.menus;

import club.mher.drawit.DrawIt;
import club.mher.drawit.game.SetupGame;
import club.mher.drawit.menu.Menu;
import club.mher.drawit.menu.PlayerMenuUtility;
import club.mher.drawit.utility.OtherUtils;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

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
                if (minPlayers < maxPlayers) {
                    minPlayers = minPlayers+1;
                }
            }else if (e.getClick().equals(ClickType.RIGHT)){
                if (minPlayers > 1) {
                    minPlayers = minPlayers-1;
                }
            }
            XSound.play(player, "CLICK,1,1");
            setMenuItems();
        }else if (e.getSlot() == 16) {
            if (e.getClick().equals(ClickType.LEFT)) {
                if (DrawIt.getConfigData().getGameWordsCount()*(maxPlayers+1) <= OtherUtils.getWordsCount()) {
                    maxPlayers = maxPlayers+1;
                }
            }else if (e.getClick().equals(ClickType.RIGHT)){
                if (maxPlayers > minPlayers) {
                    maxPlayers = maxPlayers-1;
                }
            }
            XSound.play(player, "CLICK,1,1");
            setMenuItems();
        }else if (e.getSlot() == 28) {
            player.closeInventory();
            XSound.play(player, "CLICK,1,1");
        }
        else if (e.getSlot() == 31) {
            enabled = !enabled;
            XSound.play(player, "CLICK,1,1");
            setMenuItems();
        }
        else if (e.getSlot() == 34) {
            XSound.play(player, "CLICK,1,1");
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
        inventory.setItem(31, enabled ? makeItem(Objects.requireNonNull(XMaterial.LIME_DYE.parseItem()), "&aEnabled", "","&7Click to disable.") : makeItem(Objects.requireNonNull(XMaterial.RED_DYE.parseItem()), "&cDisabled", "","&7Click to enable."));
        inventory.setItem(34, makeItem(XMaterial.BEACON.parseMaterial(), "&3Save & Close"));

    }

}
