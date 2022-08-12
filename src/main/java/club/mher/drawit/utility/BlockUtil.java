package club.mher.drawit.utility;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BlockUtil {

    public static void setBlock(Block block, ItemStack itemStack) {
        if (ReflectionUtils.isLegacy()) {
            block.setTypeIdAndData(itemStack.getType().getId(), (byte) itemStack.getDurability(), true);
        }else {
            block.setType(itemStack.getType());
        }
    }

    // Legacy
    public static void setBlock(Block block, Material material, int data) {
        if (ReflectionUtils.isLegacy()) {
            setBlock(block, new ItemStack(material, 1, (byte) data));
        }else {
            setBlock(block, new ItemStack(material));
        }
    }

    public static void setBlock(Block block, Material material) {
        if (!ReflectionUtils.isLegacy()) {
            setBlock(block, new ItemStack(material));
        }
    }

    public static boolean isSame(Block block, Block block2) {
        if (ReflectionUtils.isLegacy()) {
            return block.getType().equals(block2.getType()) && block.getData() == block2.getData();
        }else {
            return block.getType().equals(block2.getType());
        }
    }

    public static boolean isAxsisZ(Location loc) {
        return loc.clone().add(1, 0, 0).getBlock().getType().equals(Material.AIR) ||
                (loc.clone().subtract(1, 0, 0).getBlock().getType().equals(Material.AIR));
    }

}
