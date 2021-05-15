package me.MrIronMan.drawit.utility;

import com.cryptomorin.xseries.XMaterial;

public class MaterialUtil {

    public static String getMaterial(XMaterial material) {
        if (material == null || material.parseMaterial() == null) return "";
        if (!ReflectionUtils.isLegacy()) return material.parseMaterial().toString();
        if (material.getData() == 0) return material.parseMaterial().toString();
        return material.parseMaterial().toString() + ":" + material.getData();
    }

}
