package club.mher.drawit.utility;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public interface ItemUtil {

    default ItemStack makeItem(Material material, int amount) {
        return makeItem(material, amount, 0, "");
    }

    default ItemStack makeItem(ItemStack itemStack, String displayName, String... lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(TextUtil.colorize(displayName));
        itemMeta.setLore(TextUtil.colorize(Arrays.asList(lore)));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    default ItemStack makeItem(Material material, String displayName, String... lore) {
        return makeItem(material, 1,0, displayName, lore);
    }


    default ItemStack makeItem(Material material, int amount, int data, String displayName, String... lore) {
        ItemStack item = new ItemStack(material, amount, (short) data);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(TextUtil.colorize(displayName));
        itemMeta.setLore(TextUtil.colorize(Arrays.asList(lore)));

        item.setItemMeta(itemMeta);

        return item;
    }

    default ItemStack makeHead(String playerName, String displayName, String... lore) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1 , (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(playerName);
        meta.setDisplayName(TextUtil.colorize(displayName));
        meta.setLore(TextUtil.colorize(Arrays.asList(lore.clone())));
        item.setItemMeta(meta);
        return item;
    }


}
