package me.MrIronMan.drawit.game.utility;

import me.MrIronMan.drawit.utility.BlockUtil;
import me.MrIronMan.drawit.utility.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class FloodFill {

    private final Set<Block> blockList;

    private final Material material;
    private final int data;
    private final Cuboid board;
    private final Block block;

    public FloodFill(Cuboid board, Block block, Material material, int data) {
        this.blockList = new HashSet<>();
        this.material = material;
        this.data = data;
        this.board = board;
        this.block = block;
        if (board.isIn(block)) {
            fillGrid(block);
        }
    }

    public void fillGrid(Block b) {
        if (blockList.contains(b) || !board.isIn(b)) return;
        if (BlockUtil.isSame(b, block)) {
            Location loc = b.getLocation();
            blockList.add(b);
            if (BlockUtil.isAxsisZ(loc)) {
                fillGrid(loc.clone().add(0, 0, 1).getBlock());
                fillGrid(loc.clone().subtract(0, 0, 1).getBlock());
            } else {
                fillGrid(loc.clone().add(1, 0, 0).getBlock());
                fillGrid(loc.clone().subtract(1, 0, 0).getBlock());
            }
            fillGrid(loc.clone().add(0, 1, 0).getBlock());
            fillGrid(loc.clone().subtract(0, 1, 0).getBlock());
        }
    }

    public void setBlocks() {
        for (Block b : blockList) {
            if (ReflectionUtils.isLegacy()) {
                BlockUtil.setBlock(b, material, data);
            } else {
                BlockUtil.setBlock(b, material);
            }
        }
    }

}