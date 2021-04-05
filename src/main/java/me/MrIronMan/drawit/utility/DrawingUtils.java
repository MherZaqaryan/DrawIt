package me.MrIronMan.drawit.utility;

import com.cryptomorin.xseries.XSound;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import me.MrIronMan.drawit.game.utility.Cuboid;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.GameManager;
import me.MrIronMan.drawit.game.utility.FloodFill;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


import java.util.Random;
import java.util.UUID;

public class DrawingUtils {

    private Game game;
    private GameManager gameManager;
    private Player player;
    private UUID uuid;
    private Cuboid board;

    public DrawingUtils(Cuboid board, Game game, Player player) {
        this.game = game;
        this.gameManager = game.getGameManager();
        this.player = player;
        this.uuid = player.getUniqueId();
        this.board = board;
    }

    public void thinBrush(Block block) {
        if (board.isIn(block)) {
            block.setTypeIdAndData(game.getPlayerColor(uuid).getTypeId(), (byte) game.getPlayerColor(uuid).getDurability(), true);
        }
    }

    public void thickBrush(Block block) {
        Block[] crossBlocks = {
                block,
                block.getLocation().add(0.0D, 1.0D, 0.0D).getBlock(),
                block.getLocation().add(0.0D, -1.0D, 0.0D).getBlock(),
                block.getLocation().add(0.0D, 0.0D, 1.0D).getBlock(),
                block.getLocation().add(0.0D, 0.0D, -1.0D).getBlock()
        };
        for (Block b : crossBlocks) {
            if (board.isIn(b)) {
                b.setTypeIdAndData(game.getPlayerColor(uuid).getTypeId(), (byte) game.getPlayerColor(uuid).getDurability(), true);
            }
        }
    }

    public void sprayCan(Block block) {
        Location p1 = block.getLocation().add(0.0D, 2.0D, 2.0D);
        Location p2 = block.getLocation().subtract(0.0D, 2.0D, 2.0D);

        for (Block b : OtherUtils.getBlocks(p1, p2)) {
            Random random = new Random();
            if (board.isIn(b)) {
                if (random.nextInt(3) == 1) {
                    b.setTypeIdAndData(game.getPlayerColor(uuid).getTypeId(), (byte) game.getPlayerColor(uuid).getDurability(), true);
                    XSound.play(player, DrawIt.getConfigData().getString(ConfigData.SOUND_SPRAY_CANVAS));
                }
            }
        }
    }

    public void fillCan(Block block) {
        FloodFill floodFill = new FloodFill(board, block.getLocation(), game.getPlayerColor(uuid).getType(), (byte) game.getPlayerColor(uuid).getDurability());
        floodFill.setBlocks();
    }

    public void burnCanvas() {
        for (Block block : board.getBlocks()) {
            block.setType(Material.WOOL);
        }
    }

}
