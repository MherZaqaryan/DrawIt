package me.MrIronMan.drawit.menuSystem;

import org.bukkit.event.inventory.InventoryCloseEvent;

public abstract class UniqueMenu extends Menu{

    public UniqueMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public abstract void handleClose(InventoryCloseEvent e);

}
