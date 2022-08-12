package me.MrIronMan.drawit.utility;

import com.cryptomorin.xseries.XSound;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import me.MrIronMan.drawit.game.utility.Cuboid;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.GameManager;
import me.MrIronMan.drawit.game.utility.FloodFill;
import org.bukkit.Location;
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

    public DrawingUtils(Game game, Player player) {
        this.game = game;
        this.gameManager = game.getGameManager();
        this.player = player;
        this.uuid = player.getUniqueId();
        this.board = game.getBoard();
    }

    public void thinBrush(Block block) {
        if (board.isIn(block)) {
            if (ReflectionUtils.isLegacy()) {
                BlockUtil.setBlock(block, game.getPlayerColor(uuid).getType(), game.getPlayerColor(uuid).getDurability());
            }else {
                BlockUtil.setBlock(block, game.getPlayerColor(uuid).getType());
            }
        }
    }

    public void thickBrush(Block block) {
        Block[] crossBlocks = {
                block,
                block.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock(),
                block.getLocation().clone().subtract(0.0D, 1.0D, 0.0D).getBlock(),
                block.getLocation().clone().add(1.0D, 0.0D, 0.0D).getBlock(),
                block.getLocation().clone().subtract(1.0D, 0.0D, 0.0D).getBlock()
        };
        if (BlockUtil.isAxsisZ(block.getLocation())) {
            crossBlocks[3] = block.getLocation().clone().add(0.0D, 0.0D, 1.0D).getBlock();
            crossBlocks[4] = block.getLocation().clone().subtract(0.0D, 0.0D, 1.0D).getBlock();
        }
        for (Block b : crossBlocks) {
            if (board.isIn(b)) {
                if (ReflectionUtils.isLegacy()) {
                    BlockUtil.setBlock(b, game.getPlayerColor(uuid).getType(), game.getPlayerColor(uuid).getDurability());
                }else {
                    BlockUtil.setBlock(b, game.getPlayerColor(uuid).getType());
                }
            }
        }
    }

    public void sprayCan(Block block) {
        Location p1 = block.getLocation().add(0.0D, 2.0D, 2.0D);
        Location p2;
        if (BlockUtil.isAxsisZ(block.getLocation())) {
             p2 = block.getLocation().subtract(0.0D, 2.0D, 2.0D);
        }else {
            p2 = block.getLocation().subtract(2.0D, 2.0D, 0.0D);
        }

        for (Block b : OtherUtils.getBlocks(p1, p2)) {
            Random random = new Random();
            if (board.isIn(b)) {
                if (random.nextInt(3) == 1) {
                    if (ReflectionUtils.isLegacy()) {
                        BlockUtil.setBlock(b, game.getPlayerColor(uuid).getType(), game.getPlayerColor(uuid).getDurability());
                    }else {
                        BlockUtil.setBlock(b, game.getPlayerColor(uuid).getType());
                    }
                }
            }
        }
        XSound.play(player, DrawIt.getConfigData().getString(ConfigData.SOUND_SPRAY_CANVAS));
    }

    public void fillCan(Block block) {
        FloodFill floodFill = new FloodFill(board, block, game.getPlayerColor(uuid).getType(), (byte) game.getPlayerColor(uuid).getDurability());
        floodFill.setBlocks();
    }

    public void burnCanvas() {
        game.getBoard().burn(game.getBoardColor());
    }

}
