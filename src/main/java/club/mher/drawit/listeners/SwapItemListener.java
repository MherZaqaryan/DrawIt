package club.mher.drawit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class SwapItemListener implements Listener {

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent e) {
        if (e.isCancelled()) return;
        e.setCancelled(true);
    }

}
