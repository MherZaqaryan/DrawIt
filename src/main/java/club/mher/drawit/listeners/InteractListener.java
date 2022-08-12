package club.mher.drawit.listeners;

import club.mher.drawit.DrawIt;
import club.mher.drawit.game.Game;
import club.mher.drawit.game.GameState;
import club.mher.drawit.game.utility.DrawerTool;
import club.mher.drawit.menu.menus.ColorPickerMenu;
import club.mher.drawit.utility.DrawingUtils;
import club.mher.drawit.utility.OtherUtils;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;


public class InteractListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR)) return;
        ItemStack itemStack = player.getItemInHand();
        NBTItem nbti = new NBTItem(itemStack);
        if (DrawIt.getInstance().isInGame(player)) {
            Game game = DrawIt.getInstance().getGame(player);
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (game.isGameState(GameState.WAITING) || game.isGameState(GameState.STARTING)) {
                    if (nbti.hasKey("name")) {
                        if (nbti.getString("name").equals("waiting-item")) {
                            if (!nbti.getString("command").equals("none")) {
                                Bukkit.dispatchCommand(player, nbti.getString("command"));
                            }
                        }
                    }
                }else if (game.isGameState(GameState.PLAYING)) {
                    if (game.isSpectator(uuid)) {
                        if (nbti.hasKey("name")) {
                            if (nbti.getString("name").equals("spectate-item")) {
                                Bukkit.dispatchCommand(player, nbti.getString("command"));
                            }
                        }
                    } else if (game.getGameManager().isDrawer(player)) {
                        DrawingUtils drawingUtils = new DrawingUtils(game, player);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!player.isBlocking()) cancel();
                                Block boardBlock = OtherUtils.getTargetBlock(player, 100);
                                if (nbti.hasKey("name") && nbti.getString("name").equals("drawer-tool")) {
                                    if (nbti.hasKey("type")) {
                                        if (game.getBoard().isIn(boardBlock)) {
                                            String type = nbti.getString("type");
                                            if (type.equals(DrawerTool.THIN_BRUSH.getPath())) {
                                                drawingUtils.thinBrush(boardBlock);
                                            } else if (type.equals(DrawerTool.THICK_BRUSH.getPath())) {
                                                drawingUtils.thickBrush(boardBlock);
                                            } else if (type.equals(DrawerTool.SPRAY_CANVAS.getPath())) {
                                                drawingUtils.sprayCan(boardBlock);
                                            } else if (type.equals(DrawerTool.FILL_CAN.getPath())) {
                                                drawingUtils.fillCan(boardBlock);
                                            } else if (type.equals(DrawerTool.BURN_CANVAS.getPath())) {
                                                drawingUtils.burnCanvas();
                                            }
                                        }
                                    }
                                }
                            }
                        }.runTaskTimer(DrawIt.getInstance(), 0L, 1L);
                    }
                }
            }
            else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {
                 if (game.getGameManager().isDrawer(player)) {
                     if (nbti.hasKey("isPickable") && nbti.getBoolean("isPickable")) {
                         new ColorPickerMenu(DrawIt.getPlayerMenuUtility(player)).open();
                     }
                 }
            }
        }else {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (nbti.hasKey("name")) {
                    if (nbti.getString("name").equals("lobby-item")) {
                        if (!nbti.getString("command").equals("none")) {
                            Bukkit.dispatchCommand(player, nbti.getString("command"));
                        }
                    }
                }
            }
        }

    }

}
