package me.MrIronMan.drawit.game.utility;

import com.cryptomorin.xseries.XMaterial;
import me.MrIronMan.drawit.data.ConfigUtils;
import me.MrIronMan.drawit.data.MessagesUtils;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class DrawTools {

    public static ItemStack THIN_BRUSH = makeItem(XMaterial.matchXMaterial(ConfigUtils.THIN_BRUSH_MAT).get().parseItem(), MessagesUtils.THIN_BRUSH_NAME, MessagesUtils.THIN_BRUSH_LORE);
    public static ItemStack THICK_BRUSH = makeItem(XMaterial.matchXMaterial(ConfigUtils.THICK_BRUSH_MAT).get().parseItem(), MessagesUtils.THICK_BRUSH_NAME, MessagesUtils.THICK_BRUSH_LORE);
    public static ItemStack SPRAY_CAN = makeItem(XMaterial.matchXMaterial(ConfigUtils.SPRAY_CAN_MAT).get().parseItem(), MessagesUtils.SPRAY_CAN_NAME, MessagesUtils.SPRAY_CAN_LORE);
    public static ItemStack FILL_CAN = makeItem(XMaterial.matchXMaterial(ConfigUtils.FILL_CAN_MAT).get().parseItem(), MessagesUtils.FILL_CAN_NAME, MessagesUtils.FILL_CAN_LORE);
    public static ItemStack BURN_CANVAS = makeItem(XMaterial.matchXMaterial(ConfigUtils.BURN_CANVAS_MAT).get().parseItem(), MessagesUtils.BURN_CANVAS_NAME, MessagesUtils.BURN_CANVAS_LORE);

    public static ItemStack makeItem(ItemStack item, String displayName, List<String> lore) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(TextUtil.colorize(displayName));
        itemMeta.setLore(TextUtil.colorize(lore));
        item.setItemMeta(itemMeta);
        return item;
    }

}
