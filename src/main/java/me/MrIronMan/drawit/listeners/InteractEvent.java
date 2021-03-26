package me.MrIronMan.drawit.listeners;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigUtils;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.menuSystem.menus.ColorPickerMenu;
import me.MrIronMan.drawit.game.utility.DrawTools;
import me.MrIronMan.drawit.utility.DrawingUtils;
import me.MrIronMan.drawit.utility.OtherUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;


public class InteractEvent implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand() == null) return;
        ItemStack itemStack = player.getItemInHand();

        if (DrawIt.getInstance().isInGame(player)) {
            Game game = DrawIt.getInstance().getGame(player);
            if (game.getGameManager().isDrawer(player)) {

                if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    if (itemStack.equals(DrawTools.THIN_BRUSH) || itemStack.equals(DrawTools.THICK_BRUSH) || itemStack.equals(DrawTools.SPRAY_CAN) || itemStack.equals(DrawTools.FILL_CAN)) {
                        new ColorPickerMenu(DrawIt.getPlayerMenuUtility(player)).open();
                    }
                }

                if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                    DrawingUtils drawingUtils = new DrawingUtils(game.getBoard(), game, player);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!player.isBlocking()) cancel();
                            Block boardBlock = OtherUtils.getTargetBlock(player, 100);
                            if (game.getBoard().isIn(boardBlock)) {
                                if (itemStack.equals(DrawTools.THIN_BRUSH)) {
                                    drawingUtils.thinBrush(boardBlock);
                                } else if ((itemStack.equals(DrawTools.THICK_BRUSH))) {
                                    drawingUtils.thickBrush(boardBlock);
                                } else if (itemStack.equals(DrawTools.SPRAY_CAN)) {
                                    drawingUtils.sprayCan(boardBlock);
                                } else if (itemStack.equals(DrawTools.FILL_CAN)) {
                                    drawingUtils.fillCan(boardBlock);
                                } else if (itemStack.equals(DrawTools.BURN_CANVAS)) {
                                    drawingUtils.burnCanvas();
                                }
                            }
                        }
                    }.runTaskTimer(DrawIt.getInstance(), 0L, 1L);
                }
            }
        }else {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                for (Map.Entry<ItemStack, String> item : ConfigUtils.getLobbyItemsPath()) {
                    if (player.getItemInHand().equals(item.getKey())) {
                        Bukkit.dispatchCommand(player, ConfigUtils.getLobbyItemCommand(item.getValue()));
                    }
                }
            }
        }

    }

}
